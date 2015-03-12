package com.intuit.elevator.model;

import com.intuit.elevator.exception.DoorClosedException;
import com.intuit.elevator.exception.ElevatorFullException;
import com.intuit.elevator.exception.ElevatorMovingException;
import com.intuit.elevator.state.elevator.ElevatorState;

/**
 * @author indranil dey
 * Interface which will implemented by {@link com.intuit.elevator.model.ElevatorImpl}
 * @see com.intuit.elevator.model.ElevatorImpl
 */
public interface Elevator {
    /**
     *
     * @return elevator Number
     */
    int getElevatorNumber();

    // Return Elevator State
    ElevatorState getElevatorState();

    // Get elevator's current floor number
    int getCurrentFloorNumber();

    // Command to enter to the elevator
    void enterElevator(Person person) throws ElevatorFullException, DoorClosedException;

    // Command to leave the elevator
    void leaveElevator(Person person) throws DoorClosedException;

    //Start the elevator thread
    void start();

    //Request to open elevator Door
    void requestOpenDoor() throws ElevatorMovingException;

    //Move elevator to the destination floor
    void moveToDestination(int floorNumber) throws ElevatorMovingException;

    // notify elevator to stop running
    void setStopRunning();

    //set elevator's destination floor
    void setDestination(int floorNumber);
}
