package com.intuit.elevator.state.person;

/**
 * @author indranil dey
 * An Person Location class which will describe person's current and destination Location
 * @see com.intuit.elevator.state.person.CurrentPersonLocation
 * @see com.intuit.elevator.state.person.DestinationPersonLocation
 */
public abstract  class AbstractPersonLocation {
    /**
     * floor number only applicable if the person is not current floor or set
     * for destination floor
     */
    protected int floorNumber;

    protected AbstractPersonLocation(final int floorNumber) {
        setFloorNumber(floorNumber);
    }

    protected AbstractPersonLocation(){

    }

    /**
     * set the current or desitination floor number
     * @param floorNumber number of the floor
     * @throws java.lang.IllegalArgumentException in case floor number <=0
     */
    public void setFloorNumber(int floorNumber) {
        if(floorNumber<=0){
            throw new IllegalArgumentException("Invalid Floor Number");
        }
        this.floorNumber = floorNumber;
    }

    public int getFloorNumber() {
        return floorNumber;
    }

    /**
     * This is only applicable for update internally the floor state in case
     * person is outside or in elevator or going no where.
     * @param floorNumber this will be always 0 or less than 0
     * @see com.intuit.elevator.state.person.CurrentPersonLocation#IN_ELEVATOR
     * @see com.intuit.elevator.state.person.CurrentPersonLocation#OUTSIDE
     * @see com.intuit.elevator.state.person.DestinationPersonLocation#GOING_NOWHERE
     * @return @{link AbstractPersonLocation} this instance
     */
    protected AbstractPersonLocation updateFloorLocationForOtherState(int floorNumber){
        if(floorNumber>0){
            throw new IllegalArgumentException("This is just a flag not an actual floor and " +
                    "must less than or equal to 0");
        }
        this.floorNumber = floorNumber;
        return this;
    }
}