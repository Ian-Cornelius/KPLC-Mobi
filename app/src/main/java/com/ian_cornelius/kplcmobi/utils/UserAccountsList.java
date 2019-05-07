package com.ian_cornelius.kplcmobi.utils;

/*
Class to hold a list of our accounts

A singleton

Works by having its only instance, query object box for accounts mini meta data, on the singleton's creation.

Object box will return a list of all instances of our model stored in the box store. That will be used to generate
our array list's contents.

So, constructor can use new, as object box will not store instance of this class (for simplicity purposes)

Order accounts list well too
 */

import android.util.Log;

import com.ian_cornelius.kplcmobi.models.AccountsMiniMetaData;

import java.util.ArrayList;

public class UserAccountsList {

    /*
    Arraylist to hold our list of user accounts
     */
    public ArrayList<AccountsMiniMetaData> accountsMiniMetaDataArrayList = new ArrayList<>();

    /*
    Holds our only instance, created by init()
     */
    private static final UserAccountsList userAccountsList = new UserAccountsList();

    /*
    Private constructor.

    Used iff we don't get a local store of this singleton's instance

     */
    private UserAccountsList(){

        initUserAccountsList();

    }


    /*
    What we invoke to get our list
     */
    private final void initUserAccountsList(){

        /*
        Called by init(), which initializes the singleton. Looks for this object, if stored locally. If not, searches
        for online record.

        Might need to implement callback that will be used by invoking method of init() to get the results once populated
        */

        /*
        Check if object is available locally, using object box. If available, load and send to caller, using interface

        If not, set up firebase, set up a request, and wait for it to finish. On finish, invoke firebasePopulateList()
        that will create an instance of this class, set up the appropriate values, save it locally, then send it to caller

        Primary account defaults to active
         */

        /*
        Do dummy data population
         */

        //Add data to it
        AccountsMiniMetaData metaData = new AccountsMiniMetaData();

        //First set
        metaData.setAccountNumber("34156787903");
        metaData.setActive(false);

        accountsMiniMetaDataArrayList.add(metaData);

        //Second set
        metaData = new AccountsMiniMetaData();
        metaData.setAccountNumber("12569872341");
        metaData.setActive(true);

        accountsMiniMetaDataArrayList.add(metaData);

        Log.e("Error","At UserAccountsList, size after add 2 " + accountsMiniMetaDataArrayList.size());

        //third set
        //ALWAYS set up a new node
        metaData = new AccountsMiniMetaData();
        metaData.setAccountNumber("43109318734");
        metaData.setActive(false);

        accountsMiniMetaDataArrayList.add(metaData);

        //fourth set
        metaData = new AccountsMiniMetaData();
        metaData.setAccountNumber("43100094875");
        metaData.setActive(false);

        Log.e("Error","JVM Automatically called singleton constructor");

    }


    /*
    init() method cannot be changed, even if inherited
     */
    private final void init(){


    }

    public static UserAccountsList getInstance(){

        return userAccountsList;
    }


}
