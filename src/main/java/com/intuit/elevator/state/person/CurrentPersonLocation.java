package com.intuit.elevator.state.person;


/**
 * @author indranil dey
 * Describe Person's current location
 */
public class CurrentPersonLocation extends AbstractPersonLocation{
    /**
     * Static final instance if the Current location of the person is outside with floorNumber  -1
     */
    public static final AbstractPersonLocation OUTSIDE = new CurrentPersonLocation().updateFloorLocationForOtherState(-1);
    /**
     * Static final instance if the Current location of the person is inside elevator with floorNumber -2
     */
    public static final AbstractPersonLocation IN_ELEVATOR = new CurrentPersonLocation().updateFloorLocationForOtherState(-2);

    public CurrentPersonLocation(int floorNumber) {
        super(floorNumber);
    }

    protected CurrentPersonLocation() {
    }

    @Override
    public String toString() {
        if(this==OUTSIDE){
            return "OUTSIDE";
        }else if(this==IN_ELEVATOR){
            return "IN ELEVATOR";
        }else{
            return String.format("ON FLOOR %d", floorNumber);
        }
    }
}
