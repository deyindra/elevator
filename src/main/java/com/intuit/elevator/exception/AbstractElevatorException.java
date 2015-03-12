package com.intuit.elevator.exception;

/**
 * @author indranil dey
 * An Abstract exception class for elevator system extends {@link java.lang.Exception}
 * @see com.intuit.elevator.exception.DoorClosedException
 * @see com.intuit.elevator.exception.ElevatorFullException
 * @see com.intuit.elevator.exception.ElevatorMovingException
 */
public abstract class AbstractElevatorException extends Exception{
    protected String message;

    public AbstractElevatorException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
