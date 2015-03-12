package com.intuit.elevator.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author : idey
 * A utility class which will load the configurable property and return the value/default value
 * based on the property key lookup
 */
public class PropertyUtils {
    public static final String PROPERTY_FILE_LOCATION = "property.path";
    private static final Logger LOG = LoggerFactory.getLogger(PropertyUtils.class);
    private Properties properties;

    /**
     * Static singleton instance for the PropertyUtils
     */
    public static volatile PropertyUtils INSTANCE=null;

    /**
     * Private Constructor which will try to load the property file as specified in the
     * property.path environmental variable as specified in {@link #PROPERTY_FILE_LOCATION},
     * in case, it is not supplied it will load the
     * empty properties.
     * @param propertyPath path of the property file fetched from {@link #PROPERTY_FILE_LOCATION}
     * @throws java.lang.IllegalArgumentException in case file not found or error in loading property file
     */
    protected PropertyUtils(String propertyPath) {
        properties=new Properties();
        InputStream inputStream=null;
        try {
            if (propertyPath != null && !("").equals(propertyPath.trim())) {
                propertyPath = propertyPath.trim();
                inputStream = new FileInputStream(propertyPath);
                properties.load(inputStream);
            }else{
                LOG.warn("No property file found this will load the default property");
            }
        }catch(FileNotFoundException ex){
            throw new IllegalArgumentException("Invalid Property Path "+propertyPath);
        }catch(IOException ex){
            throw new IllegalArgumentException(ex);
        }finally {
            if(inputStream!=null){
                try{
                    inputStream.close();
                }catch (IOException ex){
                    LOG.error(ex.toString());
                }
            }
        }
    }

    /**
     *
     * @return static singleton instance @{link PropertyUtils#INSTANCE}
     */
    public static PropertyUtils getInstance(){
        if(INSTANCE==null){
            synchronized (PropertyUtils.class){
                if(INSTANCE==null){
                    INSTANCE = new PropertyUtils(System.getProperty(PROPERTY_FILE_LOCATION));
                }
            }
        }
        return INSTANCE;
    }

    /**
     * @param key  A Property key
     * @param defaultValue A default value which will be returned in case of key not found
     * @return return the value/default value
     */
    public String getStringPropertyValue(String key, String defaultValue){
        return properties.getProperty(key,defaultValue);
    }


    /**
     * @param key  A Property key
     * @param defaultValue A default value which will be returned in case of key not found
     * @return return the value/default value
     * This method internally called {@link #getStringPropertyValue(String, String)}
     * @throws java.lang.NumberFormatException in case value is not int
     */
    public int getIntPropertyValue(String key, String defaultValue){
        return Integer.parseInt(getStringPropertyValue(key, defaultValue));
    }



}