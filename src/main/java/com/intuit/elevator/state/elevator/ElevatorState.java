package com.intuit.elevator.state.elevator;


import com.intuit.elevator.state.State;
import com.intuit.elevator.util.AtomicBitSet;


/**
 * @author indranil dey
 * A child class of {@link com.intuit.elevator.state.State} describing elevator state
 * @see com.intuit.elevator.state.State
 * @see com.intuit.elevator.state.elevator.ElevatorState.ElevatorDoorState
 * @see com.intuit.elevator.state.elevator.ElevatorState.ElevatorMovingDirection
 * @see com.intuit.elevator.state.elevator.ElevatorState.ElevatorMovingState
 */
public class ElevatorState extends State{
    private ElevatorMovingDirection direction;
    private ElevatorDoorState doorState;
    private ElevatorMovingState elevatorMovingState;
    private int currentFloorNumber;
    private AtomicBitSet destination;
    private int riders;

    public ElevatorState(int id) {
        super(id);
    }


    public ElevatorMovingDirection getDirection() {
        return direction;
    }

    public synchronized void setDirection(ElevatorMovingDirection direction) {
        this.direction = direction;
    }

    public  ElevatorDoorState getDoorState() {
        return doorState;
    }

    public synchronized void setDoorState(ElevatorDoorState doorState) {
        this.doorState = doorState;
    }

    public ElevatorMovingState getElevatorMovingState() {
        return elevatorMovingState;
    }

    public synchronized void setElevatorMovingState(ElevatorMovingState elevatorMovingState) {
        this.elevatorMovingState = elevatorMovingState;
    }

    public int getCurrentFloorNumber() {
        return currentFloorNumber;
    }

    public synchronized  void setCurrentFloorNumber(int currentFloorNumber) {
        this.currentFloorNumber = currentFloorNumber;
    }

    public AtomicBitSet getDestination() {
        return destination;
    }

    public synchronized void setDestination(AtomicBitSet destination) {
        this.destination = destination;
    }

    public int getRiders() {
        return riders;
    }

    public synchronized void setRiders(int riders) {
        this.riders = riders;
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
        String destinationList = destination.toString();
        builder.append("Elevator Number ").append(id);
        builder.append("Current Floor ").append(currentFloorNumber);
        if(destinationList!=null && !("").equals(destinationList)) {
            builder.append("Destination Floor(s) ").append(destinationList);
        }
        builder.append("Rider ").append(riders);
        builder.append("Moving direction ").append(direction);
        builder.append("Moving state ").append(elevatorMovingState);
        builder.append("Elevator's Door status ").append(doorState);
        return builder.toString();
    }
}
