package com.ian_cornelius.kplcmobi.models;

/*
This class is to hold mini meta data about the user's accounts. Basically tell us which one is active, thus
allow him/her to switch accounts appropriately when requesting for a service.

TODO: Update this model to include types description, to separate between post-paid and pre-paid accounts

Done. isPostPay var included. Help us know whether post pay screen valid for user, without reading full acc data
 */

import android.support.annotation.NonNull;

public class AccountsMiniMetaData {

    private String accountNumber;
    private boolean isPostPay;
    private boolean isCurrent;

    public boolean isPostPay() {
        return isPostPay;
    }

    public boolean isCurrent() {
        return isCurrent;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setPostPay(boolean postPay) {
        isPostPay = postPay;
    }

    public void setCurrent(boolean current) {
        isCurrent = current;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
