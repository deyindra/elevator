package com.intuit.elevator.state;

/**
 * A State class which will be either sub classed to {@link com.intuit.elevator.state.person.PersonState}
 * or {@link com.intuit.elevator.state.elevator.ElevatorState}
 * @see com.intuit.elevator.state.person.PersonState
 * @see com.intuit.elevator.state.elevator.ElevatorState
 */
public abstract class State {
    protected int id;

    protected State(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public synchronized void setId(int id) {
        this.id = id;
    }
}
