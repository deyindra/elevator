package com.intuit.elevator.util;

import com.intuit.elevator.rule.ExceptionLoggingRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class FailurePropertyUtilTest {
    private String filePath;

    public FailurePropertyUtilTest(String filePath) {
        this.filePath = filePath;
    }

    @Rule
    public ExceptionLoggingRule exceptionLoggingRule = new ExceptionLoggingRule();
    @Rule public ExpectedException expectedException = ExpectedException.none();

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"/abc.properties"},
                {"/xyz.properties"},
        });
    }

    @Test
    public void failureTest() {
        expectedException.expect(IllegalArgumentException.class);
        new PropertyUtils(filePath);
    }


}
