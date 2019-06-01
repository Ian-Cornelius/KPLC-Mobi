package com.ian_cornelius.kplcmobi.utils.services;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.ian_cornelius.kplcmobi.utils.generators.ConsumptionTrackGenerator;

public class AlarmResetService extends IntentService {

    /*
    public constructor
     */
    public AlarmResetService(){

        super("AlarmResetService");
    }


    //Where we write code to be run when service is called
    protected void onHandleIntent(@Nullable Intent intent){

        //Get AlarmManager instance
        AlarmManager alarmManager =(AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //Set up its intent (Class that will be invoked when alarm is triggered).
        Intent alarmIntent  = new Intent(this, ConsumptionTrackGenerator.class);

        //Wrap it up as a broadcast
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, AlarmManager.INTERVAL_HOUR, pendingIntent);
    }
}
