package com.intuit.elevator.constant;

import com.intuit.elevator.AbstractPropertyLoader;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;


@RunWith(Parameterized.class)
public class ElevatorConstantTest extends AbstractPropertyLoader{
    private int inputValue;
    private int expectedValue;

    public ElevatorConstantTest(int inputValue, int expectedValue) {
        this.inputValue = inputValue;
        this.expectedValue = expectedValue;
    }


    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {ElevatorConstant.TOTAL_FLOOR, 10},
                {ElevatorConstant.TOTAL_ELEVATOR, 5},
                {ElevatorConstant.SIMULATION_MAX_PEOPLE, 200},
                {ElevatorConstant.SIMULATION_MAX_TIME, 60},
                {ElevatorConstant.FLOOR_WAIT_TIME, 1000},
                {ElevatorConstant.FLOOR_TRAVEL_TIME, 500},
                {ElevatorConstant.ELEVATOR_INACTIVE_TIME, 2000},
                {ElevatorConstant.ELEVATOR_MAX_OCCUPANCY, 30},
                {ElevatorConstant.PERSON_WAITING_TIME, 2000},
                {ElevatorConstant.PERSON_WORKING_TIME, 4000},
                {ElevatorConstant.DOOR_TOGGLE_STATUS, 1000}

        });
    }
    @Test
    public void test(){
        Assert.assertEquals(inputValue, expectedValue);
    }
}
