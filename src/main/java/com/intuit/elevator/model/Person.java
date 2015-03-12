package com.intuit.elevator.model;

import com.intuit.elevator.state.person.PersonState;

/**
 * @author indranil dey
 */
public interface Person {
    boolean isWantToEnter();
    void setWantToEnter(boolean wantToEnter);
    boolean isWantToLeave();
    void setWantToLeave(boolean wantToLeave);
    boolean isWantToTakeStair();
    void setWantToTakeStair(boolean wantToTakeStair);
    void setStopRunning();
    boolean getKeepRunning();
    void attention();
    void elevatorArrived(Elevator elevator);
    PersonState getState();
    int getPersonNumber();
    void start();
    void setDestination(int destination);
}
