package com.intuit.elevator.state;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * A State class which will be either sub classed to {@link com.intuit.elevator.state.person.PersonState}
 * or {@link com.intuit.elevator.state.elevator.ElevatorState}
 * @see com.intuit.elevator.state.person.PersonState
 * @see com.intuit.elevator.state.elevator.ElevatorState
 */
public abstract class State {
    protected int id;
    /**
     * use this to lock for write operations like add/remove
     */
    protected final Lock readLock;
    /**
     * use this to lock for read operations like get/iterator/contains..
     */
    protected final Lock writeLock;

    protected State(int id) {
        this.id = id;
        ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
        readLock = rwLock.readLock();
        writeLock = rwLock.writeLock();
    }

    public int getId() {
        readLock.lock();
        try {
            return id;
        }finally {
            readLock.unlock();
        }
    }

    public void setId(int id) {
        writeLock.lock();
        this.id = id;
        writeLock.unlock();
    }
}
