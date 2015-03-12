package com.intuit.elevator.exception;


/**
 * @author indranildey
 * Only happen when elevator is running under full capacity
 * @see com.intuit.elevator.exception.AbstractElevatorException
 */
public class ElevatorFullException extends AbstractElevatorException{
    public ElevatorFullException(final int elevatorId) {
        super(String.format("Elevator %d is full", elevatorId));
    }
}
