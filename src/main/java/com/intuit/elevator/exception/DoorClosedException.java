package com.intuit.elevator.exception;

/**
 * @author indranildey
 * Only happen when elevator door is closed and passenger is trying to leave
 * @see com.intuit.elevator.exception.AbstractElevatorException
 */
public class DoorClosedException extends AbstractElevatorException{
    public DoorClosedException(final int elevatorId) {
        super(String.format("For Elevator %d, door is closed", elevatorId));
    }
}
