package com.intuit.elevator.state;


import com.intuit.elevator.state.person.AbstractPersonLocation;
import com.intuit.elevator.state.person.CurrentPersonLocation;
import com.intuit.elevator.state.person.DestinationPersonLocation;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class SuccessPersonLocationTest{
    private int expectedValue;
    private AbstractPersonLocation location;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new CurrentPersonLocation(10), 10},
                {CurrentPersonLocation.IN_ELEVATOR, -2},
                {CurrentPersonLocation.OUTSIDE, -1},
                {new DestinationPersonLocation(5), 5},
                {DestinationPersonLocation.GOING_NOWHERE, 0}
        });
    }


    public SuccessPersonLocationTest(AbstractPersonLocation location, int expectedValue) {
        this.location = location;
        this.expectedValue = expectedValue;
    }

    @Test
    public void test(){
        Assert.assertEquals(location.getFloorNumber(),expectedValue);
    }

}
