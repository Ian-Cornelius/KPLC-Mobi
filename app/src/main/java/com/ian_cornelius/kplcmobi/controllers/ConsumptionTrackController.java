package com.ian_cornelius.kplcmobi.controllers;

/*
Class to act as our consumption track UI controller.

No need for weak references as it only sets up click listeners. No other post processing done

 */

/*
Import only widgets we will be controlling
 */
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.constraint.motion.MotionLayout;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ian_cornelius.kplcmobi.R;
import com.ian_cornelius.kplcmobi.utils.data_managers.ConsumptionTrackManager;
import com.ian_cornelius.kplcmobi.utils.generators.ConsumptionTrackGenerator;

public class ConsumptionTrackController {

    //Test my alarms
    private ConsumptionTrackManager consumptionTrackManager;

    /*
   Boolean value to know if to reverse disclaimer or not
    */
    private boolean reverseDisclaimer = false;

    /*
    Hold our widget instances
     */
    private MotionLayout mConsumptionTrackMotionLayout;
    private ImageButton mDisclaimerButton;
    private TextView mTxtShowPrevNum, mTxtShowEstConsumed, mTxtShowEstRemaining;


    /*
    Get widget instances
     */
    public void getWidgetInstances(MotionLayout consumptionTrackMotionLayout, ImageButton disclaimerButton){

        this.mConsumptionTrackMotionLayout = consumptionTrackMotionLayout;
        this.mDisclaimerButton = disclaimerButton;

        //get text view references
        mTxtShowPrevNum = consumptionTrackMotionLayout.findViewById(R.id.txtShowPrevNum);
        mTxtShowEstConsumed = consumptionTrackMotionLayout.findViewById(R.id.txtShowEstConsumed);
        mTxtShowEstRemaining = consumptionTrackMotionLayout.findViewById(R.id.txtShowEstRem);

        //now call the widget set up (onClick listeners)
        setUpWidgets();
    }


    /*
    Method to allow us set up the listeners for our widgets, and appropriate behaviour on event trigger
     */
    private void setUpWidgets(){

        /*
        Listen to clicks on image button
         */
        mDisclaimerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!reverseDisclaimer){
                    mConsumptionTrackMotionLayout.transitionToEnd();

                } else{

                    mConsumptionTrackMotionLayout.transitionToStart();

                }

                reverseDisclaimer = !reverseDisclaimer;
            }
        });

        //try my alarms
        //setAlarms(context);
    }


    //Try my alarms
    public void setAlarms(Context context){

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent alarmIntent = new Intent(context, ConsumptionTrackGenerator.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);

        Log.e("alarm intent null", String.valueOf(alarmIntent == null));
        Log.e("pending intent null", String.valueOf(pendingIntent == null));

        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, 0, AlarmManager.INTERVAL_HOUR, pendingIntent);
    }


    /*
    To request register to manager
     */
    public void requestRegister(){

        consumptionTrackManager = ConsumptionTrackManager.getInstance();
        consumptionTrackManager.register(mDisclaimerButton.getContext(), this);

    }

    /*
    To request deregister
     */
    public void requestDeregister(){

        consumptionTrackManager.deregister();
        consumptionTrackManager = null;
    }

    /*
    Update metrics
     */
    public void updateMetrics(int prevUnits, float consumedUnits){

        //Update text views
        mTxtShowPrevNum.setText(String.valueOf(prevUnits));

        if (consumedUnits > prevUnits){

            mTxtShowEstConsumed.setText(String.valueOf(prevUnits));
            mTxtShowEstRemaining.setText("0");

            mTxtShowEstConsumed.setTextColor(mConsumptionTrackMotionLayout.getResources().getColor(R.color.custom_red));
            mTxtShowEstRemaining.setTextColor(mConsumptionTrackMotionLayout.getResources().getColor(R.color.custom_red));

        } else if (prevUnits - consumedUnits < 10){

            mTxtShowEstConsumed.setText(String.valueOf(consumedUnits));
            mTxtShowEstRemaining.setText(String.valueOf(prevUnits - consumedUnits));

            mTxtShowEstConsumed.setTextColor(mConsumptionTrackMotionLayout.getResources().getColor(R.color.custom_red));
            mTxtShowEstRemaining.setTextColor(mConsumptionTrackMotionLayout.getResources().getColor(R.color.custom_red));
        } else if (prevUnits - consumedUnits > 10){

            mTxtShowEstConsumed.setText(String.valueOf(consumedUnits));
            mTxtShowEstRemaining.setText(String.valueOf(prevUnits - consumedUnits));
        }

        //Update ConsumptionTrack recorder model and save val locally. To show later.
        //Use object box
    }
}
