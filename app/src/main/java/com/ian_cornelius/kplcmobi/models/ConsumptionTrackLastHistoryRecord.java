package com.ian_cornelius.kplcmobi.models;

/*
This model is basically for holding consumption track history record for last purchase, for the account in question
 */

public class ConsumptionTrackLastHistoryRecord {

    /*
    Hold instances of date of last purchase, monthly average units, and previous units bought
     */
    private String lastDate;
    private float monthlyAverageUnits;
    private int prevUnitsBought;

    public String getLastDate() {
        return lastDate;
    }

    public float getMonthlyAverageUnits() {
        return monthlyAverageUnits;
    }

    public int getPrevUnitsBought() {
        return prevUnitsBought;
    }

    public void setLastDate(String lastDate) {
        this.lastDate = lastDate;
    }

    public void setMonthlyAverageUnits(float monthlyAverageUnits) {
        this.monthlyAverageUnits = monthlyAverageUnits;
    }

    public void setPrevUnitsBought(int prevUnitsBought) {
        this.prevUnitsBought = prevUnitsBought;
    }
}
