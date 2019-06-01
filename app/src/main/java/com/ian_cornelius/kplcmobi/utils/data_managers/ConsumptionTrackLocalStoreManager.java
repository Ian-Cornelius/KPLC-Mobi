package com.ian_cornelius.kplcmobi.utils.data_managers;

/*
This class will manager consumption track local save instances in memory, used by BroadcastReceiver (ConsumptionTrackGenerator)
and ConsumptionTrackController
 */

/*
Okay, box store dumped. Going for sharedpreferences, cause of possible context issues

Will save all vars as keys there, overridden easily. Yeeeeeeeeeeeees!!

Keep a class with these constants in utils. Actually this class.

Okay, got into some interesting scenario.

If I switch account with current setup, will have to wait one hour b4 I can get actual update on consumption track

Need to do something. Update for all accounts, even those not in current selection, and even show notification. Better
UX. So, need accounts manager to keep a local file of available accounts, Generator use this data to determine
number of calculations to be done and final callbacks.

Complicates stuff a bit. TODO Will test multiple accounts later

TODO Simple solution. Instead of account files, blablabla, switch accounts, reset alarm (cancel previous, set new), so fire immediately
and var updated. Boom. So, account manager can invoke a db run to get last hist for acc, if context of switch
is consumption track. And since we'll have firebase local save, internet access not required, unless low
 mem. Yaaaaaaaay!!!!

 But problem is, unselected account can have tokens reduce and user never told... Might have to work with files still
 Use acc number to run calculations, per acc, (determine no. of loops), then populate ConsumptionTrackResult objects
 into arraylist (have acc_no var), send to manager, manager know which one on screen, and show, others notify if
 necessary
 */

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.ian_cornelius.kplcmobi.models.ConsumptionTrackLastHistoryRecord;
import com.ian_cornelius.kplcmobi.models.CurrentConsumption;

public class ConsumptionTrackLocalStoreManager {

    //Static vars for local store
    private final String LOCAL_STORE_NAME = "CONSUMPTION_TRACK_LOCAL_STORE";

    private final String LAST_DATE = "LAST_DATE";
    private final String MONTHLY_AVERAGE = "MONTHLY_AVERAGE";
    private final String PREV_UNITS = "PREV_UNITS";
    private final String CONSUMED_UNITS = "CONSUMED_UNITS";

    //to specify request types
    public static final int REQUEST_LAST_HISTORY = 1;
    public static final int REQUEST_CURRENT_CONSUMPTION = 2;

    //for date format
    public static final String LOCAL_DATE_FORMAT = "dd/M/yyyy hh:mm";

    private static final ConsumptionTrackLocalStoreManager consumptionTrackLocalStoreManager = new ConsumptionTrackLocalStoreManager();

    public static ConsumptionTrackLocalStoreManager getInstance() {

        return consumptionTrackLocalStoreManager;
    }

    private ConsumptionTrackLocalStoreManager() {
    }

    /*
    Method to post changes to shared preferences file.

    Purchase History frag posts latest purchase record

    ConsumptionTrackManager posts latest current consumption data.

    Controller just consumes from this, meaning it reads data. Same for generator
     */
    public boolean postChanges(Object post, Context context){

        if (post instanceof ConsumptionTrackLastHistoryRecord){

            //post request from purchase history frag. Save data
            SharedPreferences preferences = context.getSharedPreferences(LOCAL_STORE_NAME, Context.MODE_PRIVATE);

            //edit and save.
            if (preferences.edit().putString(LAST_DATE, ((ConsumptionTrackLastHistoryRecord) post).getLastDate()).commit()){

                //successful save of last date
                if (preferences.edit().putFloat(MONTHLY_AVERAGE, ((ConsumptionTrackLastHistoryRecord) post).getMonthlyAverageUnits()).commit()){

                    //successful save of monthly average
                    if (preferences.edit().putInt(PREV_UNITS, ((ConsumptionTrackLastHistoryRecord) post).getPrevUnitsBought()).commit()){

                        //successful save of previous units
                        return true;
                    } else {

                        //problem
                        return false;
                    }
                } else {

                    //problem
                    return false;
                }
            } else {

                //problem
                return false;
            }
        } else if (post instanceof Float){

            //post request from consumption track manager
            SharedPreferences preferences = context.getSharedPreferences(LOCAL_STORE_NAME, Context.MODE_PRIVATE);

            //edit and save. Not putting previous units cause already saved
            if (preferences.edit().putFloat(CONSUMED_UNITS, (Float)post).commit()){

                //saved consumed units successfully
                return true;
            } else {

                //problem
                return false;
            }
        }

        //problem. Invalid call
        Log.e("LOCAL STORE ERROR", "INVALID POST CALL");
        return false;

    }


    /*
    Retrieve data.

    Object returned based on request. If request current consumption, send back arraylist ordered as prevUnits, consumedUnits
     */
    public Object readLocal(int requestType, Context context){

        SharedPreferences pref = context.getSharedPreferences(LOCAL_STORE_NAME, Context.MODE_PRIVATE);

        if (requestType == REQUEST_LAST_HISTORY){

            //Request proly coming from Generator
            ConsumptionTrackLastHistoryRecord record = new ConsumptionTrackLastHistoryRecord();

            record.setLastDate(pref.getString(LAST_DATE, null));
            record.setMonthlyAverageUnits(pref.getFloat(MONTHLY_AVERAGE, 0));
            record.setPrevUnitsBought(pref.getInt(PREV_UNITS, 0));

            return record;
        } else if (requestType == REQUEST_CURRENT_CONSUMPTION){

            CurrentConsumption consumption = new CurrentConsumption();

            consumption.setPrevUnitsBought(pref.getInt(PREV_UNITS, 0));
            consumption.setConsumedUnits(pref.getFloat(CONSUMED_UNITS, 0));

            return consumption;
        } else {

            //Invalid request
            Log.e("LOCAL STORE ERROR","INVALID READ REQUEST" );
            return null;
        }
    }
}
