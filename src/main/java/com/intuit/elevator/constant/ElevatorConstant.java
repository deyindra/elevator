package com.intuit.elevator.constant;

import com.intuit.elevator.util.PropertyUtils;

/**
 * @author indranil dey
 * Interface to hold all the property constant after reading from config.properties file
 * @see com.intuit.elevator.util.PropertyUtils
 *
 */
public interface ElevatorConstant {

    /**
     * Total number of Floor
     */
    String DEFAULT_TOTAL_FLOOR = "10";
    long TOTAL_FLOOR = PropertyUtils.getInstance().getLongPropertyValue("total.floor", DEFAULT_TOTAL_FLOOR);

    /**
     * Total number of Elevator
     */
    String DEFAULT_TOTAL_ELEVATOR = "5";
    long TOTAL_ELEVATOR = PropertyUtils.getInstance().getLongPropertyValue("total.elevator", DEFAULT_TOTAL_ELEVATOR);


    /**
     * Only used for Simulator
     */
    String DEFAULT_SIMULATION_MAX_PEOPLE = "200";
    long SIMULATION_MAX_PEOPLE = PropertyUtils.getInstance().getLongPropertyValue("simulation.max.people", DEFAULT_SIMULATION_MAX_PEOPLE);

    /**
     * Only used for Simulator
     */
    String DEFAULT_SIMULATION_MAX_TIME = "60";
    long SIMULATION_MAX_TIME = PropertyUtils.getInstance().getLongPropertyValue("simulation.max.time", DEFAULT_SIMULATION_MAX_TIME);


    /**
     * wait time on a floor
     */
    String DEFAULT_FLOOR_WAIT_TIME = "1000";
    long FLOOR_WAIT_TIME = PropertyUtils.getInstance().getLongPropertyValue("floor.wait.time", DEFAULT_FLOOR_WAIT_TIME);


    /**
     * time to reach a floor
     */
    String DEFAULT_FLOOR_TRAVEL_TIME = "500";
    long FLOOR_TRAVEL_TIME = PropertyUtils.getInstance().getLongPropertyValue("floor.travel.time", DEFAULT_FLOOR_TRAVEL_TIME);

    /**
     * elevator's inactive time
     */
    String DEFAULT_ELEVATOR_INACTIVE_TIME = "2000";
    long ELEVATOR_INACTIVE_TIME = PropertyUtils.getInstance().getLongPropertyValue("elevator.inactive.time", DEFAULT_ELEVATOR_INACTIVE_TIME);

    /**
     * elevator's occupancy
     */
    String DEFAULT_ELEVATOR_MAX_OCCUPANCY = "20";
    long ELEVATOR_MAX_OCCUPANCY = PropertyUtils.getInstance().getLongPropertyValue("elevator.max.occupancy", DEFAULT_ELEVATOR_MAX_OCCUPANCY);

    /**
     * individual person's wait time
     */
    String DEFAULT_PERSON_WAITING_TIME = "2000";
    long PERSON_WAITING_TIME = PropertyUtils.getInstance().getLongPropertyValue("person.waiting.time", DEFAULT_PERSON_WAITING_TIME);


    /**
     * individual person's working time on floor
     */
    String DEFAULT_PERSON_WORKING_TIME = "4000";
    long PERSON_WORKING_TIME = PropertyUtils.getInstance().getLongPropertyValue("person.working.time", DEFAULT_PERSON_WAITING_TIME);

}
