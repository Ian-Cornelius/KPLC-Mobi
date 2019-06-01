package com.ian_cornelius.kplcmobi.utils.data_managers;

/*
Okay, so this guy over here, should make decisions on whether to show a notification or show directly on screen

Here's how it works. Its supposed to be a singleton. Meaning, if its class is loaded, it's only instance should be accessible

If app comes live, will tell the available instance that its active. If not, will be destroyed with Broadcast receiver
once it finishes its task. Hope this logic works!!!!!!!
 */

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.ian_cornelius.kplcmobi.R;
import com.ian_cornelius.kplcmobi.controllers.ConsumptionTrackController;

public class ConsumptionTrackManager {

    /*
    Very important var. Used to tell us if fragment is active or not, thus registered itself to this singleton

    Will not be gc'd since reference extends to frag. If frag deregisters, sets val to null, and say, BroadcastReceiver
    destroyed, so can be gc'd. If frag get it first, BroadcastReceiver should not get another instance but rather this
    single one to push its updates to, and this controllerRegistered value will tell us if to push a notif or not

    Not an activity, so should not bring an exception of some sort. Just raw code, with a clever twist

    AND IT WOOORKED. IT WOORRKEDDD!! DANG!! NO DOCS ON HOW I COULD HAVE BroadcastReceiver and Foreground app talk.
    I might have just made the first architecture, using a singleton as some sort of pipe/socket, thanks to the
    singleton's single instance design constraint. DAAAAAAAAAAAAAAAAAAAAAAAAAANG!!!💪
     */
    private boolean controllerRegistered = false;
    private Context context;
    private ConsumptionTrackController controller;

    private static final ConsumptionTrackManager consumptionTrackManager = new ConsumptionTrackManager();

    public static ConsumptionTrackManager getInstance() {
        return consumptionTrackManager;
    }

    private ConsumptionTrackManager() {
    }


    /*
    This method is called by the Broadcast receiver to update the consumption track values
     */
    public void updateMetrics(int prevUnitsBought, float consumedUnits, Context notifContext){

        if (prevUnitsBought == 0){

            //using prev units bought cause it can never be zero with sample size 1 or greater
            Log.e("CONSUMPTION TRACK", "ERROR IN LOCAL STORE ACCESS");
            Toast.makeText(notifContext, "Failed to calculate consumption track", Toast.LENGTH_LONG).show();
        } else {


            //Simple test now. Just work with checking if registered or not and do appropriate action
            if (controllerRegistered){

                //Show toast. Was using context passed to do that
                //Toast.makeText(context, "Seen your register!!", Toast.LENGTH_SHORT).show();
                //tell controller to update metrics
                controller.updateMetrics(prevUnitsBought, consumedUnits);

            } else {

                //Show notification. Need to pass context to do this
                NotificationManager notificationManager = (NotificationManager) notifContext.getSystemService(Context.NOTIFICATION_SERVICE);

                //Notification notification = new Notification(R.drawable.location_icon, "Wuuuuhuuuuuuu", System.currentTimeMillis());

                Notification.Builder builder = new Notification.Builder(notifContext);
                builder.setContentTitle("Consumption track");
                builder.setContentText("Legend!!");
                builder.setSmallIcon(R.drawable.location_icon);

                notificationManager.notify(0, builder.build());

                Log.e("NOTIFICATIONS","WENT THROUGH THIS ONE WELL");

            }


            //Update record
            ConsumptionTrackLocalStoreManager localStoreManager = ConsumptionTrackLocalStoreManager.getInstance();
            if (!localStoreManager.postChanges(consumedUnits, notifContext)){

                Log.e("CONSUMPTION TRACK", "ERROR UPDATING VALUES");
            }
        }
    }


    /*
    This method is called by controller to register itself
     */
    public void register(Context context, ConsumptionTrackController consumptionTrackController){

        this.controllerRegistered = true;
        this.context = context;
        this.controller = consumptionTrackController;
    }


    /*
    This method is called by controller to deregister itself
     */
    public void deregister(){

        this.controllerRegistered = false;
        this.context = null;
        this.controller = null;
    }
}
