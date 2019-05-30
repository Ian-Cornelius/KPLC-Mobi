package com.ian_cornelius.kplcmobi.models;

/*
This class holds full data about a user's account.
 */

import android.support.annotation.NonNull;

public class AccountsFullMetaData {

    private boolean isPrimary;
    private String ownerName;
    private String status;
    private String accType;
    private String accArrears;

    public boolean isPrimary() {
        return isPrimary;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getStatus() {
        return status;
    }

    public String getAccType() {
        return accType;
    }

    public String getAccArrears() {
        return accArrears;
    }

    public void setPrimary(boolean primary) {
        isPrimary = primary;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setAccType(String accType) {
        this.accType = accType;
    }

    public void setAccArrears(String accArrears) {
        this.accArrears = accArrears;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
