package com.ian_cornelius.kplcmobi.models;

/*
This class simply holds the current consumption data, that the frag can reference when launched, and has not been updated.
Once updated, uses updated value, and locally saves in box store

Useful when retrieving data
 */

public class CurrentConsumption {

    /*
    Hold previous units bought, estimated number of units consumed, and estimated remaining units
     */
    private int prevUnitsBought;
    private float consumedUnits;
    //private float remainingUnits; derived. Don't save

    public int getPrevUnitsBought() {
        return prevUnitsBought;
    }

    public float getConsumedUnits() {
        return consumedUnits;
    }

//    public float getRemainingUnits() {
//        return remainingUnits;
//    }

    public void setPrevUnitsBought(int prevUnitsBought) {
        this.prevUnitsBought = prevUnitsBought;
    }

    public void setConsumedUnits(float consumedUnits) {
        this.consumedUnits = consumedUnits;
    }

//    public void setRemainingUnits(float remainingUnits) {
//        this.remainingUnits = remainingUnits;
//    }
}
