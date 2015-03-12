package com.intuit.elevator.model;

import com.intuit.elevator.constant.ElevatorConstant;
import com.intuit.elevator.util.ConcurrentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Concrete Implementation of Floor
 * @see com.intuit.elevator.model.Floor
 * @see com.intuit.elevator.model.AbstractElevatorControllerHolder
 * @see com.intuit.elevator.constant.ElevatorConstant
 * @see com.intuit.elevator.model.ElevatorController
 */
public class FloorImpl extends AbstractElevatorControllerHolder implements Floor, ElevatorConstant{
    private int floorNumber;
    private volatile boolean commandUp;
    private volatile boolean commandDown;
    //Concurrent List to hold persons going up
    private final ConcurrentList<Person> upWaiting = new ConcurrentList<>();
    //Concurrent List to hold persons going down
    private final ConcurrentList<Person> downWaiting = new ConcurrentList<>();
    private static final Logger LOGGER = LoggerFactory.getLogger(FloorImpl.class);

    public FloorImpl(ElevatorController controller, int floorNumber) {
        super(controller);
        if(floorNumber>=1 && floorNumber<=TOTAL_FLOOR) {
            this.floorNumber = floorNumber;
        }else {
            throw new IllegalArgumentException("Invalid Floor number " + floorNumber);
        }
    }

    @Override
    public boolean isCommandUpImmediately() {
        return commandUp;
    }

    @Override
    public boolean isCommandDownImmediately() {
        return commandDown;
    }

    @Override
    public int getFloorNumber() {
        return floorNumber;
    }

    @Override
    public int getNumberWaitingUp() {
        return upWaiting.size();
    }

    @Override
    public int getNumberWaitingDown() {
        return downWaiting.size();
    }

    @Override
    public void commandElevatorUpImmediately(Person person) {
        LOGGER.info("Command up for person " + person.getPersonNumber());
        upWaiting.add(person);
        if (!commandUp) {//if already command no need to do it again
            LOGGER.info("Light off Command UP for person " + person.getPersonNumber());
            controller.commandElevatorToUpImmediately(floorNumber, person);
            commandUp = true;
        }
    }

    @Override
    public void commandElevatorDownImmediately(Person person) {
        LOGGER.info("Command down for person " + person.getPersonNumber());
        downWaiting.add(person);
        if (!commandDown) { // if already command no need to do it again
            LOGGER.info("Light off Command DOWN for person " + person.getPersonNumber());
            controller.commandElevatorDownToDownImmediately(floorNumber, person);
            commandDown = true;
        }
    }

    @Override
    public void elevatorArrivedUp(Elevator elevator) {
        Person p;
        commandUp = false;
        synchronized (upWaiting) {
            for (int i = 0; i < upWaiting.size(); i++) {
                p = upWaiting.get(i);
                p.elevatorArrived(elevator);
                p.attention();
            }
        }
        LOGGER.info("Elevator " + elevator.getElevatorNumber() + " has arrived UP on floor " + getFloorNumber());
    }

    @Override
    public void elevatorArrivedDown(Elevator elevator) {
        Person p;
        commandDown = false;
        synchronized (downWaiting) {
            for (int i = 0; i < downWaiting.size(); i++) {
                p = downWaiting.get(i);
                p.elevatorArrived(elevator);
                p.attention();
            }
        }
        LOGGER.info("Elevator " + elevator.getElevatorNumber() + " has arrived DOWN on floor " + getFloorNumber());
    }

    @Override
    public void stopWaiting(Person person) {
        LOGGER.info("Person " + person.getPersonNumber() + "  has stopped waiting on floor " + getFloorNumber());
        upWaiting.remove(person);
        downWaiting.remove(person);
    }

    @Override
    public String toString() {
        return "FloorImpl{" +
                "floorNumber=" + floorNumber +
                ", commandUp=" + commandUp +
                ", commandDown=" + commandDown +
                ", upWaiting=" + upWaiting +
                ", downWaiting=" + downWaiting +
                '}';
    }
}
