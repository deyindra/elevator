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
    int TOTAL_FLOOR = PropertyUtils.getInstance().getIntPropertyValue("total.floor", DEFAULT_TOTAL_FLOOR);

    /**
     * Total number of Elevator
     */
    String DEFAULT_TOTAL_ELEVATOR = "5";
    int TOTAL_ELEVATOR = PropertyUtils.getInstance().getIntPropertyValue("total.elevator", DEFAULT_TOTAL_ELEVATOR);


    /**
     * Only used for Simulator
     */
    String DEFAULT_SIMULATION_MAX_PEOPLE = "200";
    int SIMULATION_MAX_PEOPLE = PropertyUtils.getInstance().getIntPropertyValue("simulation.max.people", DEFAULT_SIMULATION_MAX_PEOPLE);

    /**
     * Only used for Simulator
     */
    String DEFAULT_SIMULATION_MAX_TIME = "60";
    int SIMULATION_MAX_TIME = PropertyUtils.getInstance().getIntPropertyValue("simulation.max.time", DEFAULT_SIMULATION_MAX_TIME);


    /**
     * wait time on a floor
     */
    String DEFAULT_FLOOR_WAIT_TIME = "1000";
    int FLOOR_WAIT_TIME = PropertyUtils.getInstance().getIntPropertyValue("floor.wait.time", DEFAULT_FLOOR_WAIT_TIME);


    /**
     * time to reach a floor
     */
    String DEFAULT_FLOOR_TRAVEL_TIME = "500";
    int FLOOR_TRAVEL_TIME = PropertyUtils.getInstance().getIntPropertyValue("floor.travel.time", DEFAULT_FLOOR_TRAVEL_TIME);

    /**
     * elevator's inactive time
     */
    String DEFAULT_ELEVATOR_INACTIVE_TIME = "2000";
    int ELEVATOR_INACTIVE_TIME = PropertyUtils.getInstance().getIntPropertyValue("elevator.inactive.time", DEFAULT_ELEVATOR_INACTIVE_TIME);

    /**
     * elevator's occupancy
     */
    String DEFAULT_ELEVATOR_MAX_OCCUPANCY = "20";
    int ELEVATOR_MAX_OCCUPANCY = PropertyUtils.getInstance().getIntPropertyValue("elevator.max.occupancy", DEFAULT_ELEVATOR_MAX_OCCUPANCY);

    /**
     * individual person's wait time
     */
    String DEFAULT_PERSON_WAITING_TIME = "2000";
    int PERSON_WAITING_TIME = PropertyUtils.getInstance().getIntPropertyValue("person.waiting.time", DEFAULT_PERSON_WAITING_TIME);


    /**
     * individual person's working time on floor
     */
    String DEFAULT_PERSON_WORKING_TIME = "4000";
    int PERSON_WORKING_TIME = PropertyUtils.getInstance().getIntPropertyValue("person.working.time", DEFAULT_PERSON_WAITING_TIME);

    /**
     * Elevator's door toggle status time
     *
     */
    String DEFAULT_DOOR_TOGGLE_STATUS = "1000";
    int DOOR_TOGGLE_STATUS = PropertyUtils.getInstance().getIntPropertyValue("elevator.door.time", DEFAULT_DOOR_TOGGLE_STATUS);
}
