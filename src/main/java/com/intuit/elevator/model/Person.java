package com.intuit.elevator.model;

public interface Person {
    int getPersonNumber();
    void elevatorArrived(Elevator elevator);
    void attention();
}
