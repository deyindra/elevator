package com.intuit.elevator;
import com.intuit.elevator.util.PropertyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractPropertyLoader {
    private static final String FILE_PATH =AbstractPropertyLoader.class.getResource("/config.properties").getPath();
    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractPropertyLoader.class);
    static {
        LOGGER.info("Loading Property file "+FILE_PATH);
        System.setProperty(PropertyUtils.PROPERTY_FILE_LOCATION,FILE_PATH);
    }
}