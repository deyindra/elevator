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
    private volatile PersonActivity activity;
    /**
     * Elevator number only applicable in case of activity
     * is equal to {@link com.intuit.elevator.state.person.PersonState.PersonActivity#RIDING}
     */
    private volatile int elevatorNumber;
    // Current location of the person
    private volatile CurrentPersonLocation currentPersonLocation;
    // Destination location of the person
    private volatile DestinationPersonLocation destinationPersonLocation;



    public  void setActivity(PersonActivity activity) {
            writeLock.lock();
            try {
                assert (activity != null);
                this.activity = activity;
            }finally {
                writeLock.unlock();
            }
    }

    public  void setElevatorNumber(int elevatorNumber) {
        writeLock.lock();
        try {
            assert (elevatorNumber > 0);
            this.elevatorNumber = elevatorNumber;
        }finally {
            writeLock.unlock();
        }


    }

    public  void setCurrentPersonLocation(CurrentPersonLocation currentPersonLocation) {
        writeLock.lock();
        try {
            assert (currentPersonLocation != null);
            this.currentPersonLocation = currentPersonLocation;
        }finally {
            writeLock.unlock();
        }
    }

    public  void setDestinationPersonLocation(DestinationPersonLocation destinationPersonLocation) {
        writeLock.lock();
        try {
            assert (destinationPersonLocation != null);
            this.destinationPersonLocation = destinationPersonLocation;
        }finally {
            writeLock.unlock();
        }
    }



    public PersonActivity getActivity() {
        readLock.lock();
        try {
            return activity;
        }finally {
            readLock.unlock();
        }
    }

    public int getElevatorNumber() {
        readLock.lock();
        try {
            return elevatorNumber;
        }finally {
            readLock.unlock();
        }
    }

    public CurrentPersonLocation getCurrentPersonLocation() {
        readLock.lock();
        try {
            return currentPersonLocation;
        }finally {
            readLock.unlock();
        }
    }

    public DestinationPersonLocation getDestinationPersonLocation() {
        readLock.lock();
        try {
            return destinationPersonLocation;
        }finally {
            readLock.unlock();
        }
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
