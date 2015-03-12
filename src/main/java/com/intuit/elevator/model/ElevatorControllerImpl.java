package com.intuit.elevator.model;

import com.intuit.elevator.constant.ElevatorConstant;
import com.intuit.elevator.exception.ElevatorMovingException;
import com.intuit.elevator.state.State;
import com.intuit.elevator.state.elevator.ElevatorState;
import com.intuit.elevator.util.ConcurrentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author indranil dey
 * Controller class to manage all elevators
 * @see com.intuit.elevator.model.ElevatorImpl
 * @see com.intuit.elevator.model.Elevator
 * @see com.intuit.elevator.model.ElevatorController
 */
public class ElevatorControllerImpl implements ElevatorController,ElevatorConstant{
    // total number elevator
    private ConcurrentList<Elevator> elevatorConcurrentList = new ConcurrentList<>();
    // total number of floor assigned to elevator
    private ConcurrentList<Floor> floorConcurrentList = new ConcurrentList<>();
    private final int totalFloor;
    private static final Logger LOGGER = LoggerFactory.getLogger(ElevatorControllerImpl.class);


    public ElevatorControllerImpl(final int totalFloor, final int totalElevator) {
        if(totalElevator>=1){
            for(int i=0;i<totalElevator;i++){
                Elevator e = new ElevatorImpl(this,i+1, totalFloor, totalElevator);
                elevatorConcurrentList.add(e);
            }
        }else{
            throw new IllegalArgumentException("Invalid Total number of elevator "+totalElevator);
        }

        if(totalFloor>1){
            for(int i=0;i<totalFloor;i++){
                Floor f = new FloorImpl(this,i+1, totalFloor);
                floorConcurrentList.add(f);
            }
        }else{
            throw new IllegalArgumentException("Invalid Total number of floor "+totalFloor);
        }
        this.totalFloor = totalFloor;
    }

    /**
     *
     * @param floorNumber floor number
     * @param person person who request the elevator
     */
    @SuppressWarnings("AccessStaticViaInstance")
    @Override
    public void commandElevatorToUpImmediately(int floorNumber, Person person) {
        Elevator e = null;
        int counter = 0;
        while (e == null && person.getKeepRunning()){  // need this to stop processes stuck in this loop when program stops
            LOGGER.info("" + ++counter + " tries to move up elevator to floor " + floorNumber);
            e = getSameFloorElevator(floorNumber);
            if(e != null){
                LOGGER.info("Setting up destination for elevator " + e.getElevatorNumber() + " same floor " + floorNumber);
                try{
                    e.moveToDestination(floorNumber);
                }catch(ElevatorMovingException ex){
                    LOGGER.error(ex.getMessage());
                    LOGGER.info("Moving Exception up: floor " + floorNumber + " on elevator " + e.getElevatorState());
                    e = null;
                    continue;
                }
            }else{
                if(floorNumber > 1){ // there won't be any below floor 1
                    LOGGER.info("looking for one moving up from below to floor " + floorNumber);
                    e = getElevator(floorNumber, ElevatorState.ElevatorMovingDirection.MOVING_UP);
                }
                if(e != null){
                    LOGGER.info("Setting destination for elevator " + e.getElevatorNumber() + " from  below floor " + floorNumber);
                    try{
                        e.moveToDestination(floorNumber);
                    }catch(ElevatorMovingException ex){
                        LOGGER.error(ex.getMessage());
                        LOGGER.info("Moving Exception up: floor " + floorNumber + " on elevator " + e.getElevatorState());
                        e = null;
                        continue;
                    }
                }else{
                    LOGGER.info("Looking for closest stopped elevator for up floor " + floorNumber);
                    e = getElevator(floorNumber, ElevatorState.ElevatorMovingDirection.NO_DIRECTION);
                    if (e != null) {
                        LOGGER.info("Setting destination for stopped elevator " + e.getElevatorNumber() + " for up floor " + floorNumber);
                        try{
                            e.moveToDestination(floorNumber);
                        }catch(ElevatorMovingException ex){
                            LOGGER.error(ex.getMessage());
                            LOGGER.info("Moving Exception up: floor " + floorNumber + " on elevator " + e.getElevatorState());
                            e = null;
                            continue;
                        }
                    }else{
                        LOGGER.info("Looking for elevator coming down " + floorNumber);
                        e = getElevator(floorNumber, ElevatorState.ElevatorMovingDirection.MOVING_DOWN);
                        if(e != null){
                            LOGGER.info("Setting destination for moving down elevator " + e.getElevatorNumber() + " for floor " + floorNumber);
                            try{
                                e.moveToDestination(floorNumber);
                            }catch(ElevatorMovingException ex){
                                LOGGER.error(ex.getMessage());
                                LOGGER.info("Moving Exception up: floor " + floorNumber + " on elevator " + e.getElevatorState());
                                e = null;
                                continue;
                            }
                        }// end null any
                    }// end null nearest stopped
                } // end null nearest moving up
            }// end null same floor
            if(e == null){
                try{
                    Thread.currentThread().sleep(1000); //wait a second and try again
                }catch(InterruptedException ix){
                    //intentionally left empty
                }
            }//end if null for sleep
        }// end while
    }

    /**
     *
     * @param floorNumber floor to go to
     * @param person person who request the elevator
     */
    @SuppressWarnings("AccessStaticViaInstance")
    @Override
    public void commandElevatorDownToDownImmediately(int floorNumber, Person person) {
        Elevator e = null;
        int counter = 0;
        while ( e == null && person.getKeepRunning()){  // need this to stop processes stuck in this loop when program stops
            LOGGER.info("" + ++counter + " tries to move down elevator to floor " + floorNumber);
            e = getSameFloorElevator(floorNumber);
            if(e != null){
                LOGGER.info("Setting down destination for elevator " + e.getElevatorNumber() + " same floor " + floorNumber);
                try{
                    e.moveToDestination(floorNumber);
                }catch(ElevatorMovingException ex){
                    LOGGER.error(ex.getMessage());
                    LOGGER.info("Moving Exception down: floor " + floorNumber + " on elevator " + e.getElevatorState());
                    e = null;
                    continue;
                }
            }else{
                if(floorNumber != totalFloor){  // there won't be any above the top floor
                    LOGGER.info("looking for one moving down from above to floor " + floorNumber);
                    e = getElevator(floorNumber, ElevatorState.ElevatorMovingDirection.MOVING_DOWN);
                }
                if(e != null){
                    LOGGER.info("Setting destination for elevator " + e.getElevatorNumber() + "from  above floor " + floorNumber);
                    try{
                        e.moveToDestination(floorNumber);
                    }catch(ElevatorMovingException ex){
                        LOGGER.error(ex.getMessage());
                        LOGGER.error("Moving Exception down: floor " + floorNumber + " on elevator " + e.getElevatorState());
                        e = null;
                        continue;
                    }
                }else{
                    LOGGER.info("Looking for closest stopped elevator for down floor " + floorNumber);
                    e = getElevator(floorNumber, ElevatorState.ElevatorMovingDirection.NO_DIRECTION);
                    if(e != null){
                        LOGGER.info("Setting destination for stopped elevator " + e.getElevatorNumber() + " for down floor " + floorNumber);
                        try{
                            e.moveToDestination(floorNumber);
                        }catch(ElevatorMovingException ex){
                            LOGGER.error(ex.getMessage());
                            LOGGER.info("Moving Exception down3: floor " + floorNumber + " on elevator " + e.getElevatorState());
                            e = null;
                            continue;
                        }
                    }else{
                        LOGGER.info("Looking for elevator coming up " + floorNumber);
                        e = getElevator(floorNumber, ElevatorState.ElevatorMovingDirection.MOVING_UP);
                        if(e != null){
                            LOGGER.info("Setting destination for moving up elevator " + e.getElevatorNumber() + " for floor " + floorNumber);
                            try{
                                e.moveToDestination(floorNumber);
                            }catch(ElevatorMovingException ex){
                                LOGGER.error(ex.getMessage());
                                LOGGER.info("Moving Exception down: floor " + floorNumber + " on elevator " + e.getElevatorState());
                                e = null;
                                continue;
                            }
                        }// end null any
                    }// end null stopped
                }// end null moving down
            } //end null same floor
            if(e == null){
                try{
                    Thread.currentThread().sleep(1000); //wait a second and try again
                }catch(InterruptedException ix){
                    //intentionally left empty
                }
            } //end if null for sleep
        }// end while
    }

    /**
     * start all elevator thread
     */
    @Override
    public void startElevators() {
        for(int i = 0; i < elevatorConcurrentList.size(); i++){
            elevatorConcurrentList.get(i).start();
        }
    }

    /**
     *
     * @param elevatorNumber elevator id
     * @return return the state of the elevator
     */
    @Override
    public State getElevatorState(int elevatorNumber) {
        return elevatorConcurrentList.get(elevatorNumber-1).getElevatorState();
    }

    /**
     *
     * @param floorNumber floor number
     * @return int number of person want to gp up
     */
    @Override
    public int getNumberWaitingUp(int floorNumber) {
        return getFloor(floorNumber).getNumberWaitingUp();
    }

    /**
     *
     * @param floorNumber floor number
     * @return int number of person want to gp down
     */
    @Override
    public int getNumberWaitingDown(int floorNumber) {
        return getFloor(floorNumber).getNumberWaitingDown();
    }

    @Override
    public Floor getFloor(int floorNumber) {
        return floorConcurrentList.get(floorNumber - 1);
    }

    /**
     * stop all elevator thread
     */
    @Override
    public void stopElevators() {
        for(int i = 0; i < elevatorConcurrentList.size(); i++){
            elevatorConcurrentList.get(i).setStopRunning();
        }
    }

    /**
     * Request the elevator to go to a specific floor
     * @param floorNumber floor number
     * @param elevator elevator
     */
    @Override
    public void elevatorArrived(int floorNumber, Elevator elevator) {
        ElevatorState.ElevatorMovingDirection direction = elevator.getElevatorState().getDirection();
        Floor floor = getFloor(floorNumber);
        if(direction == ElevatorState.ElevatorMovingDirection.MOVING_UP && floor.isCommandUpImmediately()){
            floor.elevatorArrivedUp(elevator);
        }else if(direction == ElevatorState.ElevatorMovingDirection.MOVING_DOWN && floor.isCommandDownImmediately()){
            floor.elevatorArrivedDown(elevator);
        }else if(floor.isCommandUpImmediately()){
            floor.elevatorArrivedUp(elevator);
        }else if(floor.isCommandDownImmediately()){
            floor.elevatorArrivedDown(elevator);
        }else if(floor.getNumberWaitingUp() > floor.getNumberWaitingDown()){ // light is off give up first chance
            floor.elevatorArrivedUp(elevator);
        }else{ //when all else fails notify those going down
            floor.elevatorArrivedDown(elevator);
        }
    }

    /**
     * Return the elevator of the given floor if the elevator's current floor is matched with the given floor and
     * elevator motion state is stopped and there is no rider in the elevator
     * @param floorNumber where user want to go to
     * @return Elevator if the match found, else return null
     */
    private Elevator getSameFloorElevator(final int floorNumber){
        Elevator e = null;
        ElevatorState state;
        for(int i = 0; i < elevatorConcurrentList.size(); i++){
            e = elevatorConcurrentList.get(i);
            state = e.getElevatorState();
            if(e.getCurrentFloorNumber() == floorNumber && state.getElevatorMovingState() == ElevatorState.ElevatorMovingState.STOPPED && state.getRiders() == 0 ){
                LOGGER.info("Called elevator " + e.getElevatorNumber());
                break;
            }else {
                e = null;
            }
        }
        return e;
    }


    /**
     *
     * @param floorNumber floor number rider wants to go to
     * @param direction where is user want to go to it will be either {@link ElevatorState.ElevatorMovingDirection#MOVING_DOWN}
     * or {@link ElevatorState.ElevatorMovingDirection#MOVING_UP} or {@link ElevatorState.ElevatorMovingDirection#NO_DIRECTION}
     * @return elevator if the match found
     */
    private Elevator getElevator(final int floorNumber, final ElevatorState.ElevatorMovingDirection direction){
        Elevator closestElevator = null;
        int closestFloor = 0;
        int highestFloor = floorConcurrentList.size()+1;
        Elevator currentElevator;
        int currentFloorNumber;
        for(int i = 0; i < elevatorConcurrentList.size(); i++){
            currentElevator = elevatorConcurrentList.get(i);
            currentFloorNumber = currentElevator.getCurrentFloorNumber();

            if( direction == ElevatorState.ElevatorMovingDirection.MOVING_UP){ // go up
                if(currentFloorNumber > closestFloor && currentFloorNumber < floorNumber){
                    closestElevator = currentElevator;
                    closestFloor = currentFloorNumber;
                }
            }else if(direction == ElevatorState.ElevatorMovingDirection.MOVING_DOWN){ // go down
                if(currentFloorNumber < highestFloor && currentFloorNumber > floorNumber){
                    closestElevator = currentElevator;
                    highestFloor = currentFloorNumber;
                }
            }else{ //  ElevatorState.ElevatorMovingDirection.NO_DIRECTION
                if(currentFloorNumber != floorNumber && Math.abs(currentFloorNumber - floorNumber) < highestFloor){
                    closestElevator = currentElevator;
                    highestFloor = Math.abs(currentFloorNumber - floorNumber);

                }
            }
        }
        if(closestElevator!=null){
            LOGGER.info("Closest Elevator "+closestElevator.getElevatorNumber()+" is at the floor "+closestElevator.getCurrentFloorNumber());
        }
        return closestElevator;
    }
}

