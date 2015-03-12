package com.intuit.elevator.model;

import com.intuit.elevator.constant.ElevatorConstant;
import com.intuit.elevator.state.State;


/**
 * @author indranildey
 * Class Describe building. It will consist of ElevatorController
 * @see com.intuit.elevator.model.ElevatorController
 */
public class Building implements ElevatorConstant{
    public ElevatorController elevatorController;
    //Number of persons taking stair
    protected volatile long peopleTakingStairs;
    /** Number of persons outside, it is default to {@link com.intuit.elevator.constant.ElevatorConstant#SIMULATION_MAX_PEOPLE} **/
    protected volatile long peopleOutside = SIMULATION_MAX_PEOPLE;
    //Number of persons working
    protected volatile long peopleWorking;
    private final int totalElevator;
    private final int totalFloor;

    private static volatile Building BUILDING = null;

    private Building(final int floors, final int elevators) {
        elevatorController = new ElevatorControllerImpl(floors, elevators);
        elevatorController.startElevators();
        this.totalElevator= elevators;
        this.totalFloor=floors;
    }

    public static Building getInstance(final int floors, final int elevators){
        if(BUILDING==null){
            synchronized (Building.class){
                if(BUILDING==null){
                    BUILDING = new Building(floors,elevators);
                }
            }
        }
        return BUILDING;
    }

    public static Building getInstance(){
        return getInstance(TOTAL_FLOOR, TOTAL_ELEVATOR);
    }

    /**
     * Given a number get the ElevatorState
     * @param elevatorNumber number of the elevator
     * @return State
     */
    public State getElevatorState(int elevatorNumber) {
        return elevatorController.getElevatorState(elevatorNumber);
    }

    /**
     * @param floorNumber floor number
     * @return number of person are waiting to go up
     */
    public int getNumberWaitingUp(int floorNumber) {
        return elevatorController.getNumberWaitingUp(floorNumber);
    }

    /**
     * @param floorNumber floor number
     * @return number of person are waiting to go down
     */
    public int getNumberWaitingDown(int floorNumber) {
        return elevatorController.getNumberWaitingDown(floorNumber);
    }

    /**
     * @param floorNumber floor number
     * @return Floor object
     */
    public Floor getFloor(int floorNumber) {
        return elevatorController.getFloor(floorNumber);
    }


    /**
     * @return 1st floor Object
     * Assumption is person will always enter from 1st floor in the building
     */
    public Floor enterBuilding() {
        return getFloor(1);
    }

    /**
     * stop command to stop all elevators
     */
    public void stopElevators() {
        elevatorController.stopElevators();
    }

    /**
     *
     * @return total number of elevator
     */
    public int getTotalElevator() {
        return totalElevator;
    }

    /**
     *
     * @return total number of floor
     */
    public int getTotalFloor() {
        return totalFloor;
    }
}

