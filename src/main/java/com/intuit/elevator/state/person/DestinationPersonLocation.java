package com.intuit.elevator.state.person;

/**
 * @author indranil dey
 * Describe Person's current location
 *
 */
public class DestinationPersonLocation extends AbstractPersonLocation {
    /**
     * Static final instance if person not going any where with floorNumber 0
     */
    public static final AbstractPersonLocation GOING_NOWHERE = new DestinationPersonLocation().updateFloorLocationForOtherState(0);

    public DestinationPersonLocation(int floorNumber) {
        super(floorNumber);
    }

    protected DestinationPersonLocation() {
    }

    @Override
    public String toString() {
        if(this==GOING_NOWHERE){
            return "GOING NOWHERE";
        }else{
            return String.format("GOING TO DESTINATION FLOOR %d", floorNumber);
        }
    }
}
