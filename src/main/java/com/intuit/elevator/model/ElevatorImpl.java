package com.intuit.elevator.model;

import com.intuit.elevator.constant.ElevatorConstant;
import com.intuit.elevator.exception.DoorClosedException;
import com.intuit.elevator.exception.ElevatorFullException;
import com.intuit.elevator.exception.ElevatorMovingException;
import com.intuit.elevator.state.elevator.ElevatorState;
import com.intuit.elevator.util.ConcurrentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author indranil dey
 * Concrete implementation of {@link com.intuit.elevator.model.Elevator} interafce. This is also extends
 * {@link com.intuit.elevator.model.ElevatorController}.
 * @see com.intuit.elevator.model.AbstractElevatorControllerHolder
 * @see com.intuit.elevator.model.Elevator
 * @see com.intuit.elevator.model.ElevatorController
 * @see com.intuit.elevator.constant.ElevatorConstant
 * @see com.intuit.elevator.state.elevator.ElevatorState
 */
public class ElevatorImpl extends AbstractElevatorControllerHolder implements Elevator, ElevatorConstant, Runnable {
    // Elevator ID which will be always greater than 0
    private int elevatorID;
    // Elevator state
    private volatile ElevatorState elevatorState;
    //Current Elevator Floor Number
    private volatile int currentFloorNumber;
    // Flag to keep track of toggle door's status
    private boolean requestDoorOpen;
    // Passenger list riding the elevator
    private final ConcurrentList<Person> rider = new ConcurrentList<>();
    //Current Elevator thread
    private Thread activeElevator;
    // Flag to run the elevator thread
    private volatile boolean keepRunning;
    //total number of floor in the building
    private final int totalFloor;
    private static final Logger LOGGER = LoggerFactory.getLogger(ElevatorImpl.class);

    /**
     *
     * @param controller ElevatorController object
     * @param elevatorID elevator id
     * @param totalFloors total number of floor
     * @throws  java.lang.IllegalArgumentException in case of elevatorID is less or equal to 0 or greater than total totalFloor
     *
     */
    public ElevatorImpl(ElevatorController controller, int elevatorID,
                        final int totalFloors, final int totalElevators) {
        super(controller);
        if(elevatorID>=1 && elevatorID<=totalElevators) {
            this.elevatorID = elevatorID;

        }else{
            throw new IllegalArgumentException("Invalid Elevator Number "+elevatorID);
        }
        elevatorState = new ElevatorState(elevatorID);
        //Initially elevator moving state is STOPPED
        elevatorState.setElevatorMovingState(ElevatorState.ElevatorMovingState.STOPPED);
        //Initially elevator direction is NO_DIRECTION
        elevatorState.setDirection(ElevatorState.ElevatorMovingDirection.NO_DIRECTION);
        //Initially elevator door is DOOR_CLOSED
        elevatorState.setDoorState(ElevatorState.ElevatorDoorState.DOOR_CLOSED);
        //Assumption all elevator will start with floor number 1
        currentFloorNumber=1;
        elevatorState.setCurrentFloorNumber(currentFloorNumber);
        //Number of rider initially assigned to elevator initially will be 0
        elevatorState.setRiders(0);
        // Assign boolean array for desitination list
        boolean[] desitination = new boolean[totalFloors];
        for (int i = 0; i < desitination.length; i++) {
            desitination[i] = false;
        }
        elevatorState.setDestination(desitination);
        LOGGER.info("TOTAL FLOOR ASSIGNED TO ELEVATOR "+elevatorState.getDestination().length);
        totalFloor = totalFloors;
    }


    /**
     * flag to set the elevator thread to stop being running
     */
    @Override
    public void setStopRunning() {
        keepRunning = false;
    }

    /**
     * Set the destination
     * @param floorNumber floorNumber
     */
    @Override
    public synchronized void setDestination(int floorNumber) {
        // if the number of rider is empty and elevator is stop
        // then just set the destination floor and interrupt the active
        // elevator thread
        if (rider.isEmpty() && elevatorState.getElevatorMovingState()
                == ElevatorState.ElevatorMovingState.STOPPED) {
            elevatorState.setDestinationIndex(floorNumber-1,true);
            activeElevator.interrupt();
        } else {
            // Just set the turn on the elevator desitination flag
            elevatorState.setDestinationIndex(floorNumber-1,true);
        }
    }


    //Return the current elevator thread
    @Override
    public  ElevatorState getElevatorState() {
        return elevatorState;
    }

    /**
     *
     * @param floorNumber floor number where elevator should be moved, called by {@link com.intuit.elevator.model.ElevatorControllerImpl}
     * @throws ElevatorMovingException in case current floor and floor number are same and there are passenger inside the elevator
     */
    @Override
    public synchronized void moveToDestination(int floorNumber) throws ElevatorMovingException {
        if (getCurrentFloorNumber() != floorNumber || rider.isEmpty()) {
            elevatorState.setDestinationIndex(floorNumber-1,true);
            activeElevator.interrupt();
        } else {
            throw new ElevatorMovingException(elevatorID, "current floor and the destination floor are same and elevator has passengers");
        }
    }


    /**
     * Request to open elevator door
     * @throws ElevatorMovingException, in case elevator is moving
     */
    @Override
    public synchronized void requestOpenDoor() throws ElevatorMovingException {
        if (elevatorState.getElevatorMovingState() == ElevatorState.ElevatorMovingState.STOPPED)
            requestDoorOpen = true;
        else
            throw new ElevatorMovingException(elevatorID, "Try to open door when elevator is in motion");
    }

    /**
     *
     * @return currentFloorNumber
     */
    @Override
    public int getCurrentFloorNumber() {
        return currentFloorNumber;
    }

    /**
     *
     * @return elevatorID
     */
    @Override
    public int getElevatorNumber() {
        return elevatorID;
    }


    /**
     * start the activeElevator thread
     */
    @Override
    public void start() {
        LOGGER.info("Elevator ID "+elevatorID);
        if (activeElevator == null || !keepRunning) {
            keepRunning = true;
            activeElevator = new Thread(this);
            activeElevator.setPriority(Thread.NORM_PRIORITY - 1);
            activeElevator.start();
        }
    }

    @Override
    public void run() {
        LOGGER.info("Starting elevator " + elevatorID);
        while (keepRunning) {
            switch (elevatorState.getElevatorMovingState()) {
                // Elevator is stopped
                case STOPPED:
                    LOGGER.info("Elevator " + elevatorID + " is stopped");
                    // in case of no passenger and no destination set the elevator in NO DIRECTION and inactive state
                    if (rider.isEmpty() && !isDestination()) {
                        elevatorState.setDirection(ElevatorState.ElevatorMovingDirection.NO_DIRECTION);
                        LOGGER.info("Elevator " + elevatorID + " is empty and has no destination");
                        action(ELEVATOR_INACTIVE_TIME);
                    //in case elevator reached to destination call openDoor, closingDoor and removeDestination routine
                    } else if (isArrived()) {
                        LOGGER.info("Elevator " + elevatorID + " has arrived on " + currentFloorNumber);
                        openDoor();
                        closingDoor();
                        removeDestination();
                    } else {
                        // Elevator is starting to travel to destination call travel routine
                        LOGGER.info("Elevator " + elevatorID + " is continuing to travel");
                        travel();
                    }
                    break;
                case MOVING:
                    // if elevator reached to reached to destination state call stopElevator
                    if (isArrived()) {
                        stopElevator();
                    } else {
                        //else continue to travel
                        travel();
                    }
                    break;
            }
            // Update the total number of passenger to the elevatorState
            elevatorState.setRiders(rider.size());
            //update the current floor number to the elevatorState
            elevatorState.setCurrentFloorNumber(currentFloorNumber);
            LOGGER.info(elevatorState.toString());
        }
    }


    /**
     * Person leave the elevator
     * @param person Person object
     * @throws DoorClosedException if Elevator Door is closed
     */
    public void leaveElevator(final Person person) throws DoorClosedException {
        if (elevatorState.getDoorState() == ElevatorState.ElevatorDoorState.DOOR_OPEN)
            rider.remove(person);
        else {
            LOGGER.info("Elevator " + elevatorID + " door is closed can not leave.");
            throw new DoorClosedException(elevatorID);
        }
    }


    /**
     *
     * @param person want to enter in the elevator
     * @throws ElevatorFullException in case Elevator is full
     * @throws DoorClosedException in case door is closed
     */
    @Override
    public void enterElevator(final Person person) throws ElevatorFullException, DoorClosedException {
        if (elevatorState.getDoorState() == ElevatorState.ElevatorDoorState.DOOR_OPEN) {
            if (rider.size() < ELEVATOR_MAX_OCCUPANCY) {
                rider.add(person);
            } else {
                LOGGER.info("Elevator " + elevatorID + " is full");
                throw new ElevatorFullException(elevatorID);
            }
        } else {
            LOGGER.info("Elevator " + elevatorID + " door is closed can not enter.");
            throw new DoorClosedException(elevatorID);
        }
    }


    /**
     * Set the elevator in-activity time on a floor
     * @param time time to sleep the elevator thread
     */
    @SuppressWarnings("AccessStaticViaInstance")
    private void action(long time) {
        try {
            activeElevator.sleep(time);
        } catch (InterruptedException ix) {
           // DO NOTHING
        }
    }

    /**
     * Check if the desitination is arrived, mark the elevator moving state is STOPPED
     * @return true is desitination is arrived
     *
     */
    private synchronized boolean isArrived() {
        boolean returnValue = false;
        if (elevatorState.getDestinationIndex(currentFloorNumber - 1)) {
            returnValue = true;
            elevatorState.setElevatorMovingState(ElevatorState.ElevatorMovingState.STOPPED);
        }
        return returnValue;
    }

    /**
     * Command for move the elevator up
     */
    private void moveUp() {
        // check if the destination is above the current floor
        if (isDestinationAbove()) {
            // increment floor number till the time current floor is not top floor
            if (currentFloorNumber != totalFloor) {
                LOGGER.info("Elevator is moving up");
                action(FLOOR_TRAVEL_TIME);
                ++currentFloorNumber;
            }
            // if desitination floor is below the current floor then change desitination
        } else if (isDestinationBelow()) {
           LOGGER.info("Elevator moving up is changing direction");
            elevatorState.setDirection(ElevatorState.ElevatorMovingDirection.MOVING_DOWN); //  someone missed floor change direction
        } else {
            // else stop the elevator
            LOGGER.info("move up is stopping");
            stopElevator(); // only destination is this floor
        }
    }

    /**
     * Command for move the elevator down
     */
    private void moveDown() {
        // check if the destination is below the current floor
        if (isDestinationBelow()) {
            // check if the current floor is not the 1st floor
            if (currentFloorNumber != 1) {
                LOGGER.info("Elevator is move down");
                action(FLOOR_TRAVEL_TIME);
                --currentFloorNumber;
            }
            // if desitination floor is above the current floor then change desitination
        } else if (isDestinationAbove()) {
            LOGGER.info("Elevator move down is changing direction");
            elevatorState.setDirection(ElevatorState.ElevatorMovingDirection.MOVING_UP);   // someone missed floor change direction
        } else {
            // else stop the elevator
            LOGGER.info("move down is stopping");
            stopElevator(); // only destination is this floor
        }
    }

    // open door routine. only happen when elevator is STOPPED and door is closed
    private void openDoor() {
        if (elevatorState.getDoorState() == ElevatorState.ElevatorDoorState.DOOR_CLOSED &&
                elevatorState.getElevatorMovingState() == ElevatorState.ElevatorMovingState.STOPPED) {
            elevatorState.setDoorState(ElevatorState.ElevatorDoorState.DOOR_OPEN);
            notifyRiders();
            notifyController();
            action(FLOOR_WAIT_TIME);
        }
    }

    // close door routine
    private void closingDoor() {
        do {
            resetDoorRequest();
            notifyRiders();
            notifyController();
            action(DOOR_TOGGLE_STATUS);//give people a change to request door open
        } while (isRequestDoorOpen());
        elevatorState.setDoorState(ElevatorState.ElevatorDoorState.DOOR_CLOSED);
    }


    // Reset Door open flag
    private synchronized void resetDoorRequest() {
        requestDoorOpen = false;
    }

    // Check if the resetDoorRequest is true or false
    private synchronized boolean isRequestDoorOpen() {
        return requestDoorOpen;
    }

    // Notify passengers
    private void notifyRiders() {
        synchronized (rider) {
            for (int i = 0; i < rider.size(); i++) {
                rider.get(i).attention();
            }
        }

    }

    //Notify controller to call elevatorArrived routine
    private void notifyController() {
        controller.elevatorArrived(currentFloorNumber, this);
    }

    // elevator travel routine
    private synchronized void travel() {
        // if there is destination
        if (isDestination()) {
            LOGGER.info("Elevator has a destination");
            // set the elevator state to MOVING
            elevatorState.setElevatorMovingState(ElevatorState.ElevatorMovingState.MOVING);
            if (elevatorState.getDirection() == ElevatorState.ElevatorMovingDirection.MOVING_UP) {
                LOGGER.info("Elevator Moving up");
                moveUp();
            } else if (elevatorState.getDirection() == ElevatorState.ElevatorMovingDirection.MOVING_DOWN) {
                LOGGER.info("Elevator Moving Down");
                moveDown();
            } else if (isDestinationAbove()) {
                LOGGER.info("Setting direction up");
                elevatorState.setDirection(ElevatorState.ElevatorMovingDirection.MOVING_UP);
                moveUp();
            } else if (isDestinationBelow()) {
                LOGGER.info("Setting direction down");
                elevatorState.setDirection(ElevatorState.ElevatorMovingDirection.MOVING_DOWN);
                moveDown();
            } else { //someone wants us where we are
                LOGGER.info("someone wants on this floor " + currentFloorNumber);
                stopElevator();
            }
        } else { // no destination don't move;
            LOGGER.info("There is no destination");
            elevatorState.setDirection(ElevatorState.ElevatorMovingDirection.NO_DIRECTION);
            elevatorState.setElevatorMovingState(ElevatorState.ElevatorMovingState.STOPPED);
            action(ELEVATOR_INACTIVE_TIME);
        }
    }



    // After reaching to destination floor reset the desitination flag to false
    private synchronized void removeDestination() {
        elevatorState.setDestinationIndex(currentFloorNumber - 1, false);
    }

    // Set the moving state of the elevator to STOPPED
    private void stopElevator() {
        elevatorState.setElevatorMovingState(ElevatorState.ElevatorMovingState.STOPPED);
    }

    // Return true if the destination is set
    private  synchronized boolean isDestination() {
        boolean returnValue = false;
        for (int i = 0; i < totalFloor; i++) {
            if (elevatorState.getDestinationIndex(i)) {
                returnValue = true;
                break;
            }
        }
        return returnValue;
    }


    // Return true if the destination is above the current floor
    private  synchronized boolean isDestinationAbove() {
        boolean returnValue = false;
        for (int i = getCurrentFloorNumber(); i < totalFloor; i++) {
            if (elevatorState.getDestinationIndex(i)) {
                returnValue = true;
                break;
            }
        }
        return returnValue;
    }

    // Return true if the destination is below the current floor
    private  synchronized boolean isDestinationBelow() {
        boolean returnValue = false;
        for (int i = getCurrentFloorNumber() - 2; i >= 0; i--) {
            if (elevatorState.getDestinationIndex(i)) {
                returnValue = true;
                break;
            }
        }
        return returnValue;
    }
}
