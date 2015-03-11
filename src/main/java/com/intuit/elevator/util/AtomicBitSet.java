package com.intuit.elevator.util;


import java.util.concurrent.atomic.AtomicIntegerArray;

/**
 * Atomic Bit set class to to handle the on off flag for the destination floors for each elevator
 */
public class AtomicBitSet {
    private final AtomicIntegerArray array;

    public AtomicBitSet(int length) {
        int intLength = (length + 31) / 32;
        array = new AtomicIntegerArray(intLength);
    }

    public void set(long n) {
        int bit = 1 << n;
        int idx = (int) (n >>> 5);
        while (true) {
            int num = array.get(idx);
            int num2 = num | bit;
            if (num == num2 || array.compareAndSet(idx, num, num2))
                return;
        }
    }

    public boolean get(long n) {
        int bit = 1 << n;
        int idx = (int) (n >>> 5);
        int num = array.get(idx);
        return (num & bit) != 0;
    }

    // Return all the comma separated in value for which the bit is on
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("");
        String separator="";
        int length = array.length()*32 - 31;
        for(int i=0; i <length;i++){
            boolean isSet = get(i);
            if(isSet) {
                builder.append(separator);
                builder.append(i);
                separator=",";
            }
        }
        return builder.toString();
    }

}