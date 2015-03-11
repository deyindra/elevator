package com.intuit.elevator.state.person;

import com.intuit.elevator.state.State;

/**
 * @author indranil dey
 * A child class of {@link com.intuit.elevator.state.State} describing person state
 * @see com.intuit.elevator.state.State
 * @see com.intuit.elevator.state.person.PersonState.PersonActivity
 * @see com.intuit.elevator.state.person.CurrentPersonLocation
 * @see com.intuit.elevator.state.person.DestinationPersonLocation
 */
public class PersonState extends State{

    public PersonState(int id) {
        super(id);
    }

    // Person activity
    private PersonActivity activity;
    /**
     * Elevator number only applicable in case of activity
     * is equal to {@link com.intuit.elevator.state.person.PersonState.PersonActivity#RIDING}
     */
    private int elevatorNumber;
    // Current location of the person
    private CurrentPersonLocation currentPersonLocation;
    // Destination location of the person
    private DestinationPersonLocation destinationPersonLocation;



    public synchronized void setActivity(PersonActivity activity) {
        assert (activity!=null);
        this.activity = activity;
    }

    public synchronized void setElevatorNumber(int elevatorNumber) {
        assert (elevatorNumber>0);
        this.elevatorNumber = elevatorNumber;
    }

    public synchronized void setCurrentPersonLocation(CurrentPersonLocation currentPersonLocation) {
        assert (currentPersonLocation!=null);
        this.currentPersonLocation = currentPersonLocation;
    }

    public synchronized void setDestinationPersonLocation(DestinationPersonLocation destinationPersonLocation) {
        assert (destinationPersonLocation!=null);
        this.destinationPersonLocation = destinationPersonLocation;
    }



    public PersonActivity getActivity() {
        return activity;
    }

    public int getElevatorNumber() {
        return elevatorNumber;
    }

    public CurrentPersonLocation getCurrentPersonLocation() {
        return currentPersonLocation;
    }

    public DestinationPersonLocation getDestinationPersonLocation() {
        return destinationPersonLocation;
    }


    public enum PersonActivity{
        /**
         * Person is waiting for elevator
         */
        WAITING("WAITING FOR AN ELEVATOR"),
        /**
         * Person is walking outside
         */
        WALKING_OUTSIDE("WALKING OUTSIDE"),
        /**
         * Person is riding elevator and elevatorNumber is mandatory for the same
         */
        RIDING("RIDING ELEVATOR"),
        /**
         * Person is working in the building
         */
        WORKING("WORKING"),
        /**
         * Person is taking stair
         */
        TAKING_STAIRS("TAKING_STAIRS");

        private String description;

        PersonActivity(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return description;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("");
        builder.append("person id ").append(id);
        if(activity!=null){
            builder.append("activity ").append(activity);
        }
        if(elevatorNumber!=0){
            builder.append("elevator ").append(elevatorNumber);
        }
        if(currentPersonLocation!=null){
            builder.append("current location ").append(currentPersonLocation);
        }
        if(destinationPersonLocation!=null){
            builder.append("destination location ").append(destinationPersonLocation);
        }
        return builder.toString();
    }
}
