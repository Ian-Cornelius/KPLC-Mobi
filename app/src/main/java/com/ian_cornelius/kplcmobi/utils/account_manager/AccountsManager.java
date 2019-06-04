package com.ian_cornelius.kplcmobi.utils.account_manager;

/*
This class will be used by switch acc fab, and manage accounts settings to manage accounts

A singleton of course

Save requests should also be done here. Consider for sign up login? Naa....just array list size.

Enforce max acc number here
 */

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.ian_cornelius.kplcmobi.models.AccountsFullMetaData;
import com.ian_cornelius.kplcmobi.models.AccountsMiniMetaData;
import com.ian_cornelius.kplcmobi.ui.fragments.SwitchAccFabContentFragment;
import com.ian_cornelius.kplcmobi.utils.FirebaseUtils.FirebaseStaticReqManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;

public class AccountsManager implements FirebaseStaticReqManager.AccountRequestCallback{

    /*
    Constant, static, string values. Help when creating accounts
     */
    public static final String POST_PAID = "Post-Paid";
    public static final String PRE_PAID = "Pre-Paid";
    public static final String STATUS_ACTIVE = "Active";
    public static final String ARREARS_NIL = "NIL";


    //Our only instance
    private static final AccountsManager accountsManager = new AccountsManager();

    //Private constructor
    private AccountsManager(){

    }

    //Get our instance
    public static AccountsManager getInstance(){

        return accountsManager;
    }



    /*
    Now, member variables of interest
     */
    //Hold our array list, of accounts mini meta data, and full data
    private ArrayList<AccountsMiniMetaData> accountsList = new ArrayList<>();
    private ArrayList<AccountsFullMetaData> accountsDetails = new ArrayList<>();

    //Hold index of currently active account. Default -1, so that error thrown if default access tried
    private int currentlyActiveAccIndex = -1;

    //Tell us of ping errors, ping status. Default, true
    //TODO: MAKE USE OF THESE VARS IN SWITCH ACC FAB CONTENT FRAG
    private boolean pingError = true;
    private boolean pingActive = false;

    private Context toastContext;


    /*
    Now, method invoked by LogInActivity or SignUpActivity, to request this Accounts Manager to load user accounts data

    For log in, done if user already signed in test true, or on successful sign in

    Thinking of setting up everything at once and reducing network calls thereof... Yes. I'll do that
     */
    public void setUpData(Context context){ //Just to help me toast

        //Request FirebaseStaticReqManager for data

        //Ping active status true, to avoid multiple calls
        pingActive = true;
        this.toastContext = context;
        FirebaseStaticReqManager.getInstance().requestAccList(this);
    }


    /*
    method to explicitly request manager to get data again, if error b4 and handler not yet fired.

    Noticed Firebase manages refires if network not available. So, not expecting such an error.

    But still, method useful if database error actually occured.

    TODO: BASED ON THIS CONSTRAINT, HAVE A PROBLEM. IF ACCESS REQUEST WAS FROM FRAG, FIREBASE STATIC REQ MANAGER WILL TRY TO INVOKE METHOD ON NOT SUCCESSFUL FRAG. ALL NON-BLOCKING REQUESTS SHOULD BE LIFECYCLE AWARE
     */
    public void reloadData(){

        //request for data again, remove handler
    }


    /*
    Method to save acc list retrieved
     */
    private void saveAccList(DataSnapshot snapshot){

        //Help me store proper index for the current account
        //TODO See how to manage multiple accounts
        int index = 0;

        //Extract and save data
        Toast.makeText(toastContext, "Made it to get acc list", Toast.LENGTH_LONG).show();

        //Log snapshot
        Log.e("SNAPSHOT", ((snapshot.getChildren())).toString()); //getValue brings back a hashmap, getChildren, iterable Datasnapshots....hahahhahahaaaa

        for (DataSnapshot datasnapshot: snapshot.getChildren()) {

            //save these values. Super compressed code. Legendary!
            accountsList.add(datasnapshot.getValue(AccountsMiniMetaData.class));

            //set index for current here, since its based on metadata
            if (datasnapshot.getValue(AccountsMiniMetaData.class).isCurrent()){

                currentlyActiveAccIndex = index;
            }

            //update index
            index++;

            Log.e("FOR EACH", accountsList.get(0).getAccountNumber());
            Log.e("CURRENT INDEX", String.valueOf(currentlyActiveAccIndex));
        }

    }

    /*
    Method to save acc details retrieved
     */
    private void saveAccDetails(DataSnapshot snapshot){

        //Extract and save data
        Toast.makeText(toastContext, "Made it to get acc details", Toast.LENGTH_LONG).show();

        //Log snapshot
        Log.e("SNAPSHOT", ((snapshot.getChildren())).toString()); //getValue brings back a hashmap, getChildren, iterable Datasnapshots....hahahhahahaaaa

        for (DataSnapshot datasnapshot: snapshot.getChildren()) {

            //save these values. Super compressed code. Legendary!

            //Save, based on index? See if firebase ordering will work well
            accountsDetails.add(datasnapshot.getValue(AccountsFullMetaData.class));

            Log.e("FOR EACH", accountsDetails.get(0).getOwnerName());
        }
    }



    /*
    Implementation of AccRequestCallback
     */
    @Override
    public void onCreateAccSuccess(){

        //do nothing. Not needed
    }

    @Override
    public void onCreateAccFail(DatabaseError databaseError){

        //do nothing. Not needed
    }

    @Override
    public void onGetAccListSuccess(DataSnapshot snapshot){

        //save data. Request for full data
        saveAccList(snapshot);

        //request for full details
        FirebaseStaticReqManager.getInstance().requestAccDetails(this);

    }

    @Override
    public void onGetAccListFail(DatabaseError databaseError){

        //Log error
        Log.e("ACC LIST READ ERR", databaseError.toString());
        //save error stat
        pingError = true;
    }

    @Override
    public void onGetAccDetailsSuccess(DataSnapshot snapshot){

        saveAccDetails(snapshot);

        //do this here. Done with db calls
        pingActive = false;
        pingError = false;
    }

    @Override
    public void onGetAccDetailsFail(DatabaseError databaseError){

        //Log error
        Log.e("ACC DETAILS READ ERR", databaseError.toString());
        //save error stat
        pingError = true;
    }


    /*
    Method to get accounts list
     */
    public ArrayList<AccountsMiniMetaData> getAccountsList(Object refActivity){

        if (refActivity instanceof SwitchAccFabContentFragment){


            return this.accountsList;
        }

        return null;
    }

    /*
    Add an account from settings

    Save locally, before saving to db. Thus, reduce network calls for updating
     */
    public void addAccount(){


    }

    /*
    Get current account number, for display in Buy Power options and report power problem
     */
    public String getCurrentAccNumber(){

        if (currentlyActiveAccIndex != -1){

            return accountsList.get(currentlyActiveAccIndex).getAccountNumber();
        } else {

            return "Getting your listed accounts...";
        }
    }


    /*
    Know current account retrieval status
     */
    public boolean accManagerWait(){

        return pingError || pingActive;
    }


    /*
    Noticed a serious error. Being a singleton, if someone logs out and logs in with another account, other persons acc details leak. So, must flush on log out
     */
    //TODO Put an instance check here??
    public void flushData(){

        accountsList.clear();
        accountsDetails.clear();
    }

}
