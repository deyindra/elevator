package com.intuit.elevator.simulator;

import com.intuit.elevator.constant.ElevatorConstant;
import com.intuit.elevator.model.Building;
import com.intuit.elevator.model.Person;
import com.intuit.elevator.model.PersonImpl;
import com.intuit.elevator.util.ConcurrentList;

import java.util.Random;

/**
 * Main Simulator class
 */
public class Main implements ElevatorConstant{
    private static final int IN_BETWEEN_WAIT_SIMULATION_RUN = 500;
    @SuppressWarnings("AccessStaticViaInstance")
    public static void main(String[] args){
        // Get the singleton instance of the building
        Building b = Building.getInstance();
        // start all elevator
        b.startElevators();
        // Create person list and peron thread and start each person thread
        ConcurrentList<Person> list = new ConcurrentList<>();
        Random r = new Random();
        for(int i=0;i<SIMULATION_MAX_PEOPLE;i++){
            int desitinationFloorNumber = r.nextInt(TOTAL_FLOOR)+1;
            Person p = new PersonImpl(b,i+1,desitinationFloorNumber,true,false,false);
            list.add(p);
            p.start();
        }
        // run simulation
        for(int i=0;i<SIMULATION_MAX_TIME;i++){
            try {
                Thread.currentThread().sleep(IN_BETWEEN_WAIT_SIMULATION_RUN);
            } catch (Exception e) {
               //
            }
        }
        // Stop all person thread
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setStopRunning();
        }
        // stop elevators
        b.stopElevators();
    }
}
