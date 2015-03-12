package com.intuit.elevator.state.elevator;


import com.intuit.elevator.state.State;


/**
 * @author indranil dey
 * A child class of {@link com.intuit.elevator.state.State} describing elevator state
 * @see com.intuit.elevator.state.State
 * @see com.intuit.elevator.state.elevator.ElevatorState.ElevatorDoorState
 * @see com.intuit.elevator.state.elevator.ElevatorState.ElevatorMovingDirection
 * @see com.intuit.elevator.state.elevator.ElevatorState.ElevatorMovingState
 */
public class ElevatorState extends State{
    private volatile ElevatorMovingDirection direction;
    private volatile ElevatorDoorState doorState;
    private volatile ElevatorMovingState elevatorMovingState;
    private volatile int currentFloorNumber;
    private volatile boolean[] destination;
    private volatile int riders;

    public ElevatorState(int id) {
        super(id);
    }


    public ElevatorMovingDirection getDirection() {
        readLock.lock();
        try {
            return direction;
        }finally {
            readLock.unlock();
        }

    }

    public  void setDirection(ElevatorMovingDirection direction) {
        writeLock.lock();
        this.direction = direction;
        writeLock.unlock();
    }

    public  ElevatorDoorState getDoorState() {
        readLock.lock();
        try {
            return doorState;
        }finally {
            readLock.unlock();
        }
    }

    public  void setDoorState(ElevatorDoorState doorState) {
        writeLock.lock();
        this.doorState = doorState;
        writeLock.unlock();
    }

    public ElevatorMovingState getElevatorMovingState() {
        readLock.lock();
        try {
            return elevatorMovingState;
        }finally {
            readLock.unlock();
        }

    }

    public  void setElevatorMovingState(ElevatorMovingState elevatorMovingState) {
        writeLock.lock();
        this.elevatorMovingState = elevatorMovingState;
        writeLock.unlock();
    }

    public int getCurrentFloorNumber() {
        readLock.lock();
        try {
            return currentFloorNumber;
        }finally {
            readLock.unlock();
        }
    }

    public   void setCurrentFloorNumber(int currentFloorNumber) {
        writeLock.lock();
        this.currentFloorNumber = currentFloorNumber;
        writeLock.unlock();
    }

    public boolean[] getDestination() {
        readLock.lock();
        try {
            return destination;
        }finally {
            readLock.unlock();
        }
    }

    public  void setDestination(final boolean[] destination) {
        writeLock.lock();
        this.destination = destination;
        writeLock.unlock();
    }

    public  boolean getDestinationIndex(int index){
        readLock.lock();
        try {
            if (index >= 0 && index < destination.length) {
                return destination[index];
            } else {
                throw new IllegalArgumentException("Invalid Index..." + index+" total destination length "+destination.length);
            }
        }finally {
            readLock.unlock();
        }
    }

    public  void setDestinationIndex(int index, boolean value){
        writeLock.lock();
        try {
            if (index >= 0 && index < destination.length) {
                destination[index] = value;
            } else {
                throw new IllegalArgumentException("Invalid Index..." + index);
            }
        }finally {
            writeLock.unlock();
        }
    }



    public int getRiders() {
        readLock.lock();
        try {
            return riders;
        }finally {
            readLock.unlock();
        }

    }

    public  void setRiders(int riders) {
        writeLock.lock();
        this.riders = riders;
        writeLock.unlock();
    }

    //enum to describe direction of elevator
    public enum ElevatorMovingDirection {
        //Elevator is moving up
        MOVING_UP,
        //Elevator is moving down
        MOVING_DOWN,
        //Elevator is idle no direction
        NO_DIRECTION
    }

    // enum to describe elevator moving state
    public enum ElevatorMovingState {
        //Elevator is moving
        MOVING,
        //Elevator is stopped
        STOPPED
    }

    // enum to describe elevator door state
    public enum ElevatorDoorState {
        //Elevator's door is opened
        DOOR_OPEN,
        //Elevator's door is closed
        DOOR_CLOSED
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("");
        builder.append(" Elevator Number ").append(id);
        builder.append(" Current Floor ").append(currentFloorNumber);
        if(destination!=null && destination.length>0) {
           StringBuilder destList = new StringBuilder("");
           String separator="";
           for(int i=0;i<destination.length;i++){
               if(destination[i]){
                   destList.append(separator);
                   destList.append(i+1);
                   separator=",";
               }
           }
           String strDestList =  destList.toString();
           if(!strDestList.isEmpty()) {
               builder.append(" Destination List ").append(destList.toString());
           }else{
               builder.append(" Destination List ").append("NONE");
           }
        }
        builder.append(" Rider ").append(riders);
        builder.append(" Moving direction ").append(direction);
        builder.append(" Moving state ").append(elevatorMovingState);
        builder.append(" Elevator's Door status ").append(doorState);
        return builder.toString();
    }
}
