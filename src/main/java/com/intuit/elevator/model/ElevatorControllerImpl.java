package com.intuit.elevator.model;

import com.intuit.elevator.constant.ElevatorConstant;
import com.intuit.elevator.state.State;
import com.intuit.elevator.state.elevator.ElevatorState;
import com.intuit.elevator.util.ConcurrentList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElevatorControllerImpl implements ElevatorController,ElevatorConstant{
    private ConcurrentList<Elevator> elevatorConcurrentList = new ConcurrentList<>();
    private ConcurrentList<Floor> floorConcurrentList = new ConcurrentList<>();

    private static final Logger LOGGER = LoggerFactory.getLogger(ElevatorControllerImpl.class);


    public ElevatorControllerImpl(final int totalElevator, final int totalFloor) {
        if(totalElevator>=0 && totalElevator<=TOTAL_ELEVATOR){
            for(int i=0;i<totalElevator;i++){
                Elevator e = new ElevatorImpl(this,i+1, totalFloor);
                elevatorConcurrentList.add(e);
            }
        }else{
            throw new IllegalArgumentException("Invalid Total number of elevator "+totalElevator);
        }

        if(totalFloor>=0 && totalFloor<=TOTAL_FLOOR){
            for(int i=0;i<totalFloor;i++){
                Floor f = new FloorImpl(this,i+1);
                floorConcurrentList.add(f);
            }
        }else{
            throw new IllegalArgumentException("Invalid Total number of floor "+totalFloor);
        }

    }

    @Override
    public void commandElevatorToUpImmediately(int floorNumber, Person person) {
        //TODO
    }

    @Override
    public void commandElevatorDownToDownImmediately(int floorNumber, Person person) {
        //TODO

    }

    @Override
    public void startElevators() {
        //TODO

    }

    @Override
    public State getElevatorState(int elevatorNumber) {
        return elevatorConcurrentList.get(elevatorNumber-1).getElevatorState();
    }

    @Override
    public int getNumberWaitingUp(int floorNumber) {
        return getFloor(floorNumber).getNumberWaitingUp();
    }

    @Override
    public int getNumberWaitingDown(int floorNumber) {
        return getFloor(floorNumber).getNumberWaitingDown();
    }

    @Override
    public Floor getFloor(int floorNumber) {
        return floorConcurrentList.get(floorNumber - 1);
    }

    @Override
    public void stopElevators() {
        //TODO

    }

    /**
     * Request the elevator to go to a specific floor
     * @param floorNumber floor number
     * @param elevator elevator
     */
    @Override
    public void elevatorArrived(int floorNumber, Elevator elevator) {
        ElevatorState.ElevatorMovingDirection direction = elevator.getElevatorState().getDirection();
        Floor floor = getFloor(floorNumber);
        if(direction == ElevatorState.ElevatorMovingDirection.MOVING_UP && floor.isCommandUpImmediately()){
            floor.elevatorArrivedUp(elevator);
        }else if(direction == ElevatorState.ElevatorMovingDirection.MOVING_DOWN && floor.isCommandDownImmediately()){
            floor.elevatorArrivedDown(elevator);
        }else if(floor.isCommandUpImmediately()){
            floor.elevatorArrivedUp(elevator);
        }else if(floor.isCommandDownImmediately()){
            floor.elevatorArrivedDown(elevator);
        }else if(floor.getNumberWaitingUp() > floor.getNumberWaitingDown()){ // light is off give up first chance
            floor.elevatorArrivedUp(elevator);
        }else{ //when all else fails notify those going down
            floor.elevatorArrivedDown(elevator);
        }
    }

    /**
     * Return the elevator of the given floor if the elevator's current floor is matched with the given floor and
     * elevator motion state is stopped and there is no rider in the elevator
     * @param floorNumber where user want to go to
     * @return Elevator if the match found, else return null
     */
    private Elevator getSameFloorElevator(final int floorNumber){
        Elevator e = null;
        ElevatorState state;
        for(int i = 0; i < elevatorConcurrentList.size(); i++){
            e = elevatorConcurrentList.get(i);
            state = e.getElevatorState();
            if(e.getCurrentFloorNumber() == floorNumber && state.getElevatorMovingState() == ElevatorState.ElevatorMovingState.STOPPED && state.getRiders() == 0 ){
                LOGGER.info("Called elevator " + e.getElevatorNumber());
                break;
            }else {
                e = null;
            }
        }
        return e;
    }


    /**
     *
     * @param floorNumber floor number rider wants to go to
     * @param direction where is user want to go to it will be either {@link ElevatorState.ElevatorMovingDirection#MOVING_DOWN}
     * or {@link ElevatorState.ElevatorMovingDirection#MOVING_UP} or {@link ElevatorState.ElevatorMovingDirection#NO_DIRECTION}
     * @return elevator if the match found
     */
    private Elevator getElevator(final int floorNumber, final ElevatorState.ElevatorMovingDirection direction){
        Elevator closestElevator = null;
        int closestFloor = 0;
        int highestFloor = floorConcurrentList.size()+1;
        Elevator currentElevator;
        int currentFloorNumber;
        for(int i = 0; i < elevatorConcurrentList.size(); i++){
            currentElevator = elevatorConcurrentList.get(i);
            currentFloorNumber = currentElevator.getCurrentFloorNumber();

            if( direction == ElevatorState.ElevatorMovingDirection.MOVING_UP){ // go up
                if(currentFloorNumber > closestFloor && currentFloorNumber < floorNumber){
                    closestElevator = currentElevator;
                    closestFloor = currentFloorNumber;
                }
            }else if(direction == ElevatorState.ElevatorMovingDirection.MOVING_DOWN){ // go down
                if(currentFloorNumber < highestFloor && currentFloorNumber > floorNumber){
                    closestElevator = currentElevator;
                    highestFloor = currentFloorNumber;
                }
            }else{ //  ElevatorState.ElevatorMovingDirection.NO_DIRECTION
                if(currentFloorNumber != floorNumber && Math.abs(currentFloorNumber - floorNumber) < highestFloor){
                    closestElevator = currentElevator;
                    highestFloor = Math.abs(currentFloorNumber - floorNumber);

                }
            }
        }
        if(closestElevator!=null){
            LOGGER.info("Closest Elevator "+closestElevator.getElevatorNumber()+" is at the floor "+closestElevator.getCurrentFloorNumber());
        }
        return closestElevator;
    }
}

