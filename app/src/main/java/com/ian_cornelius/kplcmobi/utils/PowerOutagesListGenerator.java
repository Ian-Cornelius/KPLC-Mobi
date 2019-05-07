package com.ian_cornelius.kplcmobi.utils;

/*
Class used to generate the list of power outages from DB.

A singleton
 */

import com.ian_cornelius.kplcmobi.models.PowerOutagesList;

import java.util.ArrayList;

public class PowerOutagesListGenerator {

    /*
    Avoid data re-population
     */
    private boolean alreadyPopulated = false;

    //Hold array of data
    public ArrayList<PowerOutagesList> outagesList = new ArrayList<>();

    //Our only instance
    private static final PowerOutagesListGenerator powerOutagesListGenerator = new PowerOutagesListGenerator();

    //Private constructor
    private PowerOutagesListGenerator(){


    }

    //use to get the instance
    public static PowerOutagesListGenerator getInstance(){

        return powerOutagesListGenerator;
    }

    //Generate our data
    public ArrayList<PowerOutagesList> generateData(){

        if (!alreadyPopulated){

            //dummy data
            PowerOutagesList list = new PowerOutagesList();
            list.setType(PowerOutagesList.OutageType.WEEKLY);
            list.createEntry();
            list.entry.setDate("4/12/2019");
            list.entry.setDescLink("Uri");
            list.entry.setWillAffect(false);
            outagesList.add(list);

            //dummy data
            //dummy data
            list = new PowerOutagesList();
            list.setType(PowerOutagesList.OutageType.WEEKLY);
            list.createEntry();
            list.entry.setDate("6/12/2019");
            list.entry.setDescLink("Uri");
            list.entry.setWillAffect(true);
            outagesList.add(list);

            //dummy data
            //dummy data
            list = new PowerOutagesList();
            list.setType(PowerOutagesList.OutageType.WEEKLY);
            list.createEntry();
            list.entry.setDate("5/04/2018");
            list.entry.setDescLink("Uri");
            list.entry.setWillAffect(false);
            outagesList.add(list);

            //change alreadyGenerated value
            alreadyPopulated = true;
        }


        return outagesList;
    }


}
