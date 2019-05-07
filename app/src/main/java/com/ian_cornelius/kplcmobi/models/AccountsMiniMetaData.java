package com.ian_cornelius.kplcmobi.models;

/*
This class is to hold mini meta data about the user's accounts. Basically tell us which one is active, thus
allow him/her to switch accounts appropriately when requesting for a service.
 */

public class AccountsMiniMetaData {

    private String accountNumber;
    private boolean isActive;

    public boolean isActive() {
        return isActive;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
