package com.intuit.elevator.model;

import com.intuit.elevator.constant.ElevatorConstant;
import com.intuit.elevator.exception.DoorClosedException;
import com.intuit.elevator.exception.ElevatorFullException;
import com.intuit.elevator.exception.ElevatorMovingException;
import com.intuit.elevator.state.person.CurrentPersonLocation;
import com.intuit.elevator.state.person.DestinationPersonLocation;
import com.intuit.elevator.state.person.PersonState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * @author  indranil dey
 * Peson class representing the rider of the elevator implements {@link com.intuit.elevator.model.Person}
 * @see com.intuit.elevator.model.Person
 */
public class PersonImpl implements Runnable, ElevatorConstant, Person{
    // Current building structure
    private  Building building;
    // Person Id
    private final int personId;
    // Activity time it can be wait time or working time
    private int activityTime;
    // flag to indicate if the person want to enter
    private volatile boolean wantToEnter;
    // flag to indicate if the person want to exit
    private volatile boolean wantToLeave;
    // flag to indicate if the person want to take stair
    private volatile boolean wantToTakeStair;
    // Elevator class
    private Elevator elevator;
    // Destination floor
    private volatile int destination;
    // Current location it can be floor number or OUTSIDE or IN_ELEVATOR
    private volatile int location;
    // Person activity
    private volatile PersonState.PersonActivity activity;
    // Destination person's state
    private volatile DestinationPersonLocation destinationPersonLocation;
    // Floor object
    private Floor floor;
    private Thread activePerson;
    private volatile boolean keepRunning;

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonImpl.class);


    public PersonImpl(final Building building,
                      final int personId, final int destination, final boolean wantToEnter,
                      final boolean wantToLeave, final boolean wantToTakeStair) {
        if(building==null){
            throw new IllegalArgumentException("Invalid Building");
        }else{
            this.building = building;
        }

        if(personId<=0){
            throw new IllegalArgumentException("Invalid Person id");
        }else{
            this.personId = personId;
        }

        if(destination<1 || destination>building.getTotalFloor()){
            throw new IllegalArgumentException("Invalid destination floor");
        }else{
            this.destination = destination;
        }

        this.destination = destination;
        this.wantToEnter = wantToEnter;
        this.wantToLeave = wantToLeave;
        this.wantToTakeStair = wantToTakeStair;
    }

    // Flag to indicate if person want to enter
    public boolean isWantToEnter() {
        return wantToEnter;
    }
    // set the flag for entering
    public synchronized void setWantToEnter(boolean wantToEnter) {
        this.wantToEnter = wantToEnter;
    }

    // Flag to indicate if person want to leave
    public boolean isWantToLeave() {
        return wantToLeave;
    }

    // set the flag for leaving
    public synchronized void setWantToLeave(boolean wantToLeave) {
        this.wantToLeave = wantToLeave;
    }

    // Flag to indicate if person want to take stair
    public boolean isWantToTakeStair() {
        return wantToTakeStair;
    }
    // flag to indicate person taking stair
    public synchronized void setWantToTakeStair(boolean wantToTakeStair) {
        this.wantToTakeStair = wantToTakeStair;
    }

    // stop running thread flag
    public void setStopRunning() {
        keepRunning = false;
    }

    // flag to check if thread is running
    public boolean getKeepRunning() {
        return keepRunning;
    }

    // Interrupt the thread flag
    public synchronized void attention() {
        if(activePerson!=null) {
            activePerson.interrupt();
        }
    }
    // set Elevator
    public synchronized void elevatorArrived(Elevator elevator) {
        this.elevator = elevator;
    }

    // Return person state
    public PersonState getState() {
        PersonState state = new PersonState(personId);
        state.setActivity(activity);
        state.setDestinationPersonLocation(destinationPersonLocation);

        if(location== CurrentPersonLocation.IN_ELEVATOR.getFloorNumber()){
            state.setCurrentPersonLocation((CurrentPersonLocation)CurrentPersonLocation.IN_ELEVATOR);
        }else if(location == CurrentPersonLocation.OUTSIDE.getFloorNumber()){
            state.setCurrentPersonLocation((CurrentPersonLocation)CurrentPersonLocation.OUTSIDE);
        }else {
            state.setCurrentPersonLocation(new CurrentPersonLocation(location));
        }
        if (elevator != null)
            state.setElevatorNumber(elevator.getElevatorNumber());
        return state;
    }
    // get person id
    public int getPersonNumber() {
        return personId;
    }

    public void start() {
        destinationPersonLocation = (DestinationPersonLocation)DestinationPersonLocation.GOING_NOWHERE;
        activity = PersonState.PersonActivity.WALKING_OUTSIDE;
        if (activePerson == null || !keepRunning) {
            keepRunning = true;
            activePerson = new Thread(this);
            activePerson.setPriority(Thread.NORM_PRIORITY - 2);
            activePerson.start();
        }
    }




    @Override
    public void run() {
        while (keepRunning) {
            switch (activity) {
                case WALKING_OUTSIDE:
                    if (isWantToEnter()) {
                        building.peopleOutside--;
                        floor = building.enterBuilding();
                        location = floor.getFloorNumber();
                        if (destination > location) { //go up
                            activity = PersonState.PersonActivity.WAITING;
                            setWaitTime();
                            floor.commandElevatorUpImmediately(this);
                            action();
                        } else { // work on first floor
                            building.peopleWorking++;
                            activity = PersonState.PersonActivity.WORKING;
                            destinationPersonLocation = (DestinationPersonLocation)DestinationPersonLocation.GOING_NOWHERE;
                            setWorkingTime();
                            action();
                        }
                    } else { // did not enter
                        destinationPersonLocation = (DestinationPersonLocation)DestinationPersonLocation.GOING_NOWHERE;
                        location = CurrentPersonLocation.OUTSIDE.getFloorNumber();
                        activity = PersonState.PersonActivity.WALKING_OUTSIDE;
                        setWorkingTime();
                        action();
                    }
                    break;
                case TAKING_STAIRS:
                    if (location == destination) {
                        building.peopleTakingStairs--;
                        building.peopleWorking++;
                        activity = PersonState.PersonActivity.WORKING;
                        floor = building.getFloor(location);
                        destinationPersonLocation = (DestinationPersonLocation)DestinationPersonLocation.GOING_NOWHERE;
                        setWorkingTime();
                        action();
                    } else if (destination > location) {
                        climbUp();
                    } else {
                        climbDown();
                    }
                    break;
                case WAITING:
                    if (elevator != null) {
                        enterElevator();
                    } else { //elevator not here yet
                        if (isWantToTakeStair()) {
                            floor.stopWaiting(this);
                            building.peopleTakingStairs++;
                            activity = PersonState.PersonActivity.TAKING_STAIRS;
                            if (destination > location) {
                                climbUp();
                            } else {
                                climbDown();
                            }
                        } else {//continue to wait
                            setWaitTime();
                            action();
                        }
                    }
                    break;
                case WORKING:
                    if (location == 1) {
                        if (isWantToLeave()) {
                            building.peopleOutside++;
                            building.peopleWorking--;
                            destinationPersonLocation = (DestinationPersonLocation)DestinationPersonLocation.GOING_NOWHERE;
                            location = CurrentPersonLocation.OUTSIDE.getFloorNumber();
                            activity = PersonState.PersonActivity.WALKING_OUTSIDE;
                            setWorkingTime();
                            action();
                        } else { // stay in the building
                            if (destination > location) { //go up
                                building.peopleWorking--;
                                activity = PersonState.PersonActivity.WAITING;
                                setWaitTime();
                                floor.commandElevatorUpImmediately(this);
                                action();
                            } else { // work on same floor
                                activity = PersonState.PersonActivity.WORKING;
                                destinationPersonLocation = (DestinationPersonLocation)DestinationPersonLocation.GOING_NOWHERE;
                                setWorkingTime();
                                action();
                            }
                        }
                    } else { // not on first floor
                        if (destination > location) { //go up
                            building.peopleWorking--;
                            activity = PersonState.PersonActivity.WAITING;
                            setWaitTime();
                            floor.commandElevatorUpImmediately(this);
                            action();
                        } else if (destination < location) {
                            building.peopleWorking--;
                            activity = PersonState.PersonActivity.WAITING;
                            setWaitTime();
                            floor.commandElevatorDownImmediately(this);
                            action();
                        } else { // work on same floor
                            activity = PersonState.PersonActivity.WORKING;
                            destinationPersonLocation = (DestinationPersonLocation)DestinationPersonLocation.GOING_NOWHERE;
                            setWorkingTime();
                            action();
                        }
                    }
                    break;
                case RIDING:
                    if (elevator.getCurrentFloorNumber() == destination) {
                        leaveElevator();
                    } else
                        setWaitTime();
                    action();
                    break;
            }
            LOGGER.info(getState().toString());
        }
    }


    public void setDestination(int destination) {
        if(!keepRunning)
            this.destination = destination;
    }

    private void setWaitTime() {
        activityTime = PERSON_WAITING_TIME;
        LOGGER.info("Person: " + personId + " Maximum wait: " + activityTime);
    }

    private void setWorkingTime() {
        activityTime = PERSON_WORKING_TIME;
        LOGGER.info("Person: " + personId + " Business: " + activityTime);
    }


    @SuppressWarnings("AccessStaticViaInstance")
    private void action() {
        try {
            activePerson.sleep(activityTime);
        } catch (InterruptedException ix) {
            //intentionally left empty
        }
    }

    private void climbUp() {
        if (location != building.getTotalFloor()) {
            action();
            ++location;
        }
    }

    private void climbDown() {
        if (location != 1) {
            action();
            --location;
        }
    }

    private void enterElevator() {
        try {
            elevator.enterElevator(this);
            elevator.setDestination(destination);
            destinationPersonLocation.setFloorNumber(destination);
            floor.stopWaiting(this);
            floor = null;
            location = CurrentPersonLocation.IN_ELEVATOR.getFloorNumber();
            activity = PersonState.PersonActivity.RIDING;
            activityTime = ElevatorConstant.FLOOR_TRAVEL_TIME * Math.abs(location - destination);
            action();
        } catch (ElevatorFullException fx) {
            resetWaitForElevator();
        } catch (DoorClosedException cx) {
            try {
                elevator.requestOpenDoor();
            } catch (ElevatorMovingException mx) {
                resetWaitForElevator();
            }
        }
    }


    private void leaveElevator() {
        try {
            elevator.leaveElevator(this);
            floor = building.getFloor(destination);
            location = destination;
            destinationPersonLocation = (DestinationPersonLocation)DestinationPersonLocation.GOING_NOWHERE;
            activity = PersonState.PersonActivity.WORKING;
            building.peopleWorking++;
            setWorkingTime();
            action();
        } catch (DoorClosedException dx) {
            try {
                elevator.requestOpenDoor();
            } catch (ElevatorMovingException mx) {
                // missed our floor or not arrived yet either way push the button
                elevator.setDestination(destination);
            }
        }
    }

    private void resetWaitForElevator() {
        LOGGER.info("Person " + personId + " missed elevator " + elevator.getElevatorNumber());
        floor.stopWaiting(this);
        elevator = null;
        setWaitTime();
        action();
        if (destination > location)
            floor.commandElevatorUpImmediately(this);
        else
            floor.commandElevatorDownImmediately(this);
    }


}
