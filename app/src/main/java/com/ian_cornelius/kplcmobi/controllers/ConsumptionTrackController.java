package com.ian_cornelius.kplcmobi.controllers;

/*
Class to act as our consumption track UI controller.

No need for weak references as it only sets up click listeners. No other post processing done

 */

/*
Import only widgets we will be controlling
 */
import android.support.constraint.motion.MotionLayout;
import android.view.View;
import android.widget.ImageButton;

public class ConsumptionTrackController {

    /*
   Boolean value to know if to reverse disclaimer or not
    */
    private boolean reverseDisclaimer = false;

    /*
    Hold our widget instances
     */
    private MotionLayout mConsumptionTrackMotionLayout;
    private ImageButton mDisclaimerButton;


    /*
    Get widget instances
     */
    public void getWidgetInstances(MotionLayout consumptionTrackMotionLayout, ImageButton disclaimerButton){

        this.mConsumptionTrackMotionLayout = consumptionTrackMotionLayout;
        this.mDisclaimerButton = disclaimerButton;

        //now call the widget set up
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
    }
}
