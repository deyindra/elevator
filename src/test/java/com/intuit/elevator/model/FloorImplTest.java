package com.intuit.elevator.model;

import com.intuit.elevator.AbstractPropertyLoader;
import com.intuit.elevator.rule.ExceptionLoggingRule;
import com.intuit.elevator.state.State;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class FloorImplTest extends AbstractPropertyLoader {

    @Rule
    public ExceptionLoggingRule exceptionLoggingRule = new ExceptionLoggingRule();
    @Rule public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void invalidFloorNumberTest1(){
        expectedException.expect(IllegalArgumentException.class);
        new FloorImpl(new DummyElevatorController(),10000);
    }

    @Test
    public void invalidFloorNumberTest2(){
        expectedException.expect(IllegalArgumentException.class);
        new FloorImpl(new DummyElevatorController(),-1);
    }

    @Test
    public void invalidControllerTest(){
        expectedException.expect(IllegalArgumentException.class);
        new FloorImpl(null,2);
    }

    @Test
    public void validFloorTest(){
        DummyElevatorController controller = new DummyElevatorController();
        Floor f = new FloorImpl(controller,10);
        Elevator e1 = new ElevatorImpl(controller,3,10);
        Elevator e2 = new ElevatorImpl(controller,2,10);
        Assert.assertEquals(false, f.isCommandDownImmediately());
        Assert.assertEquals(false, f.isCommandUpImmediately());
        Person dummyPerson1 = new DummyPerson(1);
        Person dummyPerson2 = new DummyPerson(2);
        Person dummyPerson3 = new DummyPerson(3);
        Person dummyPerson4 = new DummyPerson(4);

        f.commandElevatorUpImmediately(dummyPerson1);
        f.commandElevatorUpImmediately(dummyPerson2);

        f.commandElevatorDownImmediately(dummyPerson3);
        f.commandElevatorDownImmediately(dummyPerson4);

        Assert.assertEquals(true, f.isCommandDownImmediately());
        Assert.assertEquals(true, f.isCommandUpImmediately());

        Assert.assertEquals(2, f.getNumberWaitingUp());
        Assert.assertEquals(2, f.getNumberWaitingDown());

        f.elevatorArrivedUp(e1);
        f.elevatorArrivedDown(e2);

        Assert.assertEquals(false, f.isCommandDownImmediately());
        Assert.assertEquals(false, f.isCommandUpImmediately());

        f.stopWaiting(dummyPerson1);
        f.stopWaiting(dummyPerson3);

        Assert.assertEquals(1, f.getNumberWaitingUp());
        Assert.assertEquals(1, f.getNumberWaitingDown());


    }

    private class DummyPerson implements Person{
        private int personId;

        public DummyPerson(int personId) {
            this.personId = personId;
        }

        @Override
        public int getPersonNumber() {
            return personId;
        }

        @Override
        public void elevatorArrived(Elevator elevator) {

        }

        @Override
        public void attention() {

        }

        @Override
        public String toString() {
            return Integer.toString(personId);
        }
    }

    private class DummyElevatorController implements ElevatorController{
        @Override
        public void commandElevatorToUpImmediately(int floorNumber, Person person) {

        }

        @Override
        public void commandElevatorDownToDownImmediately(int floorNumber, Person person) {

        }

        @Override
        public void startElevators() {

        }

        @Override
        public State getElevatorState(int elevatorNumber) {
            return null;
        }

        @Override
        public int getNumberWaitingUp(int floorNumber) {
            return 0;
        }

        @Override
        public int getNumberWaitingDown(int floorNumber) {
            return 0;
        }

        @Override
        public Floor getFloor(int floorNumber) {
            return null;
        }

        @Override
        public void stopElevators() {

        }

        @Override
        public void elevatorArrived(int floorNumber, Elevator elevator) {

        }
    }


}
