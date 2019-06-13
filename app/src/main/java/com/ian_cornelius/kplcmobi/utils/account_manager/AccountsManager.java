package com.ian_cornelius.kplcmobi.utils.account_manager;

/*
This class will be used by switch acc fab, and manage accounts settings to manage accounts

A singleton of course

Save requests should also be done here. Consider for sign up login? Naa....just array list size.

Enforce max acc number here

So, when account is changed, need to inform consumption track manager of change, so that calculations change.

Also, if we are working with a frag, it uses its life cycle to attach itself here for callback on changes

And what changes will they be interested in?

Well, if you switch accounts (need to tell current frag of new account number, for it to update on UI

Need also to know if post pay account is available, for post pay screen to show correct message...


Okay, major models update. Pre-paid and post-paid separated. No more isPostPaid var. Then, mergedCurrentAcc var (Still a node of minimetadata) to tell us which acc number is the merged current, making it easy to switch content in consumption track and
purchase history. Text in consumption track also changes based on acc type. So, will still maintain acc type var, to help us access it details too.

Let's just do this. Maintain single list. Current account consistent in all screens. If current not matching required type, show error on screen

On model update, tell attached frag of change, and let it do appropriate action
 */

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.ian_cornelius.kplcmobi.controllers.ConsumptionTrackController;
import com.ian_cornelius.kplcmobi.models.AccountsFullMetaData;
import com.ian_cornelius.kplcmobi.models.AccountsMiniMetaData;
import com.ian_cornelius.kplcmobi.ui.fragments.BuyTokensFragment;
import com.ian_cornelius.kplcmobi.ui.fragments.CheckAndPayBillFragment;
import com.ian_cornelius.kplcmobi.ui.fragments.SwitchAccFabContentFragment;
import com.ian_cornelius.kplcmobi.utils.FirebaseUtils.FirebaseStaticReqManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;

public class AccountsManager implements FirebaseStaticReqManager.AccountRequestCallback, SwitchAccFabContentFragment.OnAccountSwitchListener{

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
    private int prevActiveAccIndex = -1; //Help when flushing data to db

    //Tell us of ping errors, ping status. Default, true
    //TODO: MAKE USE OF THESE VARS IN SWITCH ACC FAB CONTENT FRAG
    private boolean pingError = true;
    private boolean pingActive = false;

    private Context toastContext;

    //this will reference the object that will receive event callbacks on account selection change
    private Object callbackObj;

    //Know if we have post paid or prepaid account....not necessary. Method to return easily
    public final int LOAD_ERROR = -1;
    private AccountsRequestType accountsRequestType;

    public enum AccountsRequestType{

        POST_PAID,
        PRE_PAID,
        MERGED
    }


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
                prevActiveAccIndex = currentlyActiveAccIndex;
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

    TODO: MODIFY THIS TO CATER FOR POST PAY AND PRE-PAY REQUESTS ONLY, and merged requests

    Have load context, specifying which type of acc the frag needs. Can be POST_PAID, PRE_PAID, MERGED

    For merged, have merged current. Takes val of currently active prepay. Switches to a post paid account on selection

    Merged request, uses isMergedCurrent in model. So, store prev mergedCurrent, for switching purposes?

    Update child on these changes?

    Model update needed. Add isMergedCurrent in metadata

    Maintain two array lists. One for post pay, other for pre-paid. Nope. Just seive data. Then be told of changes. Order by index, so index ref able to help in sorting changes

    On create acc, if has post pay, then new one being created not current. Same logic for pre-paid. In prod, ask for meta details, get JSON payload in format required, store in model, look at saved details in acc manager, (if not sign up, and update current)
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
    Get account type of current account. Used by buy tokens and check and pay bill
     */
    public boolean isCurrentPostPay(){

        return accountsList.get(currentlyActiveAccIndex).isPostPay();
//
//        if (currentlyActiveAccIndex != -1){
//
//            return accountsList.get(currentlyActiveAccIndex).isPostPay();
//        } else {
//
//            return LOAD_ERROR; //useless piece of code. Always check we don't have errors first, then request val
//        }
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


    /*
    Implement interface to tell us when current account has been changed
     */
    @Override
    public void onAccountSwitch(int newIndex){

        //change currently active index
        Log.e("ACC MANAGER", "GOT ACC SWITCH CALLBACK, INDEX " + newIndex);
        currentlyActiveAccIndex = newIndex;
    }

    /*
    Update current selection in db, called by switch acc content fragment on fragment detach
     */
    public void updateCurrentSelection(){

        if (prevActiveAccIndex != currentlyActiveAccIndex){

            Log.e("ACC MANAGER", "TRIGGERED CURRENT FLUSH TO DB");
            accountsList.get(prevActiveAccIndex).setCurrent(false);
            accountsList.get(currentlyActiveAccIndex).setCurrent(true);
            prevActiveAccIndex = currentlyActiveAccIndex;

            //tell attached frag of changes, using interface
            sendEvent();

            //update only if preActiveAccIndex was changed from val of currentIndex. Flush to db
        }

    }

    /*
    Used by classes interested with account manager to register for events
     */
    public void attachForEvents(Object refObj){

        if (validClass(refObj)){

            callbackObj = refObj;
        } else{

            Log.e("Acc Manager Exception", "Illegal attach request from " + refObj.getClass());
        }
    }

    public void detachFromEvents(Object refObj){

        if (callbackObj == refObj){

            callbackObj = null;
        } else{

            Log.e("Acc Manager Exception", "Illegal detach request from " + refObj.getClass());
        }
    }

    /*
    Method invoked to send event callback to attached object
     */
    private void sendEvent(){

        if (callbackObj instanceof ConsumptionTrackController){

            ((ConsumptionTrackController) callbackObj).onAccountsSwitch(getCurrentAccNumber(), isCurrentPostPay());
        } else if (callbackObj instanceof BuyTokensFragment){

            ((BuyTokensFragment) callbackObj).onAccountsSwitch(getCurrentAccNumber(), isCurrentPostPay());
        }

        //By default, local store manager for consumption track always updated with change
    }


    /*
    Test the passed ref is of a valid class
     */
    private boolean validClass(Object refObj){

        return refObj instanceof ConsumptionTrackController || refObj instanceof BuyTokensFragment || refObj instanceof CheckAndPayBillFragment;
    }


    /*
    Interface implemented by class that needs to know when account selection changes
     */
    public interface OnAccountsSwitch{

        void onAccountsSwitch(String accNo, boolean isPostPay);
    }

}

/*
Ignoring account ordering. Will cause problems. Only when viewing, will we show isPrimary at 1st place, rest placed anyhowly.
 */
