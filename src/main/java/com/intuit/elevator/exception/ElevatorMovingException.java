package com.intuit.elevator.exception;

/**
 * @author indranildey
 * Only happen when somebody make door open request while moving elevator
 * @see com.intuit.elevator.exception.AbstractElevatorException
 */
public class ElevatorMovingException extends AbstractElevatorException{
    public ElevatorMovingException(final int elevatorId, final String cause) {
        super(String.format("Elevator %d Error %s", elevatorId, cause));
    }
}
