package com.ian_cornelius.kplcmobi.models;

/*
Model basically holding user data we save in profile
 */

import android.support.annotation.NonNull;

public final class User {

    private String userName;
    private String phoneNumber;
    private String email;

    /*
    Default constructor
     */
    public User(){


    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
