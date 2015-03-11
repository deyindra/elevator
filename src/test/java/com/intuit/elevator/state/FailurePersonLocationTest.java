package com.intuit.elevator.state;

import com.intuit.elevator.rule.ExceptionLoggingRule;
import com.intuit.elevator.state.person.CurrentPersonLocation;
import com.intuit.elevator.state.person.DestinationPersonLocation;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class FailurePersonLocationTest{
    private int floorNumber;
    private TestType type;

    public FailurePersonLocationTest(int floorNumber, TestType type) {
        this.floorNumber = floorNumber;
        this.type = type;
    }

    @Rule
    public ExceptionLoggingRule exceptionLoggingRule = new ExceptionLoggingRule();
    @Rule public ExpectedException expectedException = ExpectedException.none();

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {-10, TestType.CURRENT},
                {-11, TestType.DESTINATION},
        });
    }

    @Test
    public void failureTest() {
        expectedException.expect(IllegalArgumentException.class);

        if(type==TestType.CURRENT){
            new CurrentPersonLocation(floorNumber);
        }else{
            new DestinationPersonLocation(floorNumber);
        }
    }

    private enum TestType{
        CURRENT, DESTINATION
    }
}
