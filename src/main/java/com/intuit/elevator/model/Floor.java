package com.intuit.elevator.model;

/**
 * @author indranildey
 * Interface implemented by {@link com.intuit.elevator.model.FloorImpl}
 */
public interface Floor {
    // Flag which will identify if the elevator is commanded to go up immediately when a person call for it.
    boolean isCommandUpImmediately();
    // Flag which will identify if the elevator is commanded to go down immediately when a person call for it.
    boolean isCommandDownImmediately();
    //current floor number
    int getFloorNumber();
    //return number to person waiting to go up from the current floor
    int getNumberWaitingUp();
    //return number to person waiting to go down from the current floor
    int getNumberWaitingDown();
    //command to elevator up immediately
    void commandElevatorUpImmediately(Person person);
    //command to elevator down immediately
    void commandElevatorDownImmediately(Person person);
    //elevator arrive up on the current floor
    void elevatorArrivedUp(Elevator elevator);
    //elevator arrive down on the current floor
    void elevatorArrivedDown(Elevator elevator);
    //remove the person from the list who is stopped waiting on the current floor
    void stopWaiting(Person person);
}
