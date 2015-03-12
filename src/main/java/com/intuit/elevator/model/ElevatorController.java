package com.intuit.elevator.model;

import com.intuit.elevator.state.State;

/**
 * @author indranil dey
 * Interface which is representation of Elevator controller It is managing all elevator thread.
 * @see com.intuit.elevator.model.Elevator
 */
public interface ElevatorController {
    /**
     * command elevator to go up to the floorNumber.
     */
    void commandElevatorToUpImmediately(int floorNumber, Person person);
    /**
     * command elevator to go down to the floorNumber.
     */
    void commandElevatorDownToDownImmediately(int floorNumber, Person person);

    void startElevators();

    State getElevatorState(int elevatorNumber);

    int getNumberWaitingUp(int floorNumber);

    int getNumberWaitingDown(int floorNumber);

    Floor getFloor(int floorNumber);

    void stopElevators();

    void elevatorArrived(int floorNumber, Elevator elevator);
}
