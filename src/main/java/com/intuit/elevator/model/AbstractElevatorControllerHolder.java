package com.intuit.elevator.model;

/**
 * @author indranildey
 * An Abstract Class which will be holding the same instance of Elevator Controller
 * @see com.intuit.elevator.model.FloorImpl
 * @see com.intuit.elevator.model.ElevatorImpl
 * @see com.intuit.elevator.model.ElevatorController
 */
public abstract class AbstractElevatorControllerHolder {
    /**
     * Controller which will manage all elevator thread
     */
    protected ElevatorController controller;

    protected AbstractElevatorControllerHolder(final ElevatorController controller) {
        if(controller==null){
            throw new IllegalArgumentException("Invalid Controller");
        }
        this.controller = controller;
    }
}
