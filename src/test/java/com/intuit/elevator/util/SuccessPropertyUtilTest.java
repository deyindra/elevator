package com.intuit.elevator.util;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class SuccessPropertyUtilTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(SuccessPropertyUtilTest.class);

    private String propertyPath;
    private String key;
    private Object defaultValue;

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"/foo.properties", "abc", "1"},
                {"/foo.properties", "test", "xyz"},
                {"/foo.properties", "test1", "NOT FOUND"},
                {"", "NOT FOUND", "NOT FOUND"},
                {"", "NOT FOUND1", "NOT FOUND1"}
        });
    }

    public SuccessPropertyUtilTest(String propertyPath, String key, Object defaultValue) {
        this.propertyPath = propertyPath;
        this.key = key;
        this.defaultValue = defaultValue;
    }

    @Test
    public void testExistingPropertyValue(){
        String filePath=null;
        if(propertyPath!=null && !("").equals(propertyPath.trim())) {
            filePath = SuccessPropertyUtilTest.class.getResource(propertyPath.trim()).getPath();
            LOGGER.info("File path " + filePath);
        }
        PropertyUtils propertyUtils = new PropertyUtils(filePath);
        Assert.assertEquals(propertyUtils.getStringPropertyValue(key,((String)defaultValue)), defaultValue);
    }
}
