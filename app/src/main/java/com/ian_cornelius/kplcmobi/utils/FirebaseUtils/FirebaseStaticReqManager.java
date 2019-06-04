package com.ian_cornelius.kplcmobi.utils.FirebaseUtils;

/*
All static firebase requests will pass through this class.

Not inherited
 */

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.ian_cornelius.kplcmobi.models.AccountsFullMetaData;
import com.ian_cornelius.kplcmobi.models.AccountsMiniMetaData;
import com.ian_cornelius.kplcmobi.models.User;
import com.ian_cornelius.kplcmobi.ui.fragments.AddAccountFragment;
import com.ian_cornelius.kplcmobi.ui.fragments.BuyTokensFragment;
import com.ian_cornelius.kplcmobi.ui.fragments.SettingsFragment;
import com.ian_cornelius.kplcmobi.ui.home.HomeActivity;
import com.ian_cornelius.kplcmobi.ui.login.LogInActivity;
import com.ian_cornelius.kplcmobi.ui.signup.SignUpActivity;
import com.ian_cornelius.kplcmobi.utils.account_manager.AccountsManager;
import com.ian_cornelius.kplcmobi.utils.generators.ConsumptionTrackGenerator;

public final class FirebaseStaticReqManager implements FirebaseAuthManager.AuthCallBack, FirebaseProfileManager.ProfileManagerCallbacks,
                    FirebaseAccountsManager.AccountsAccessCallBack, FirebaseTokenRatesManager.TokenRatesWatcher {

    //Hold current reference activity or frag, plus reqAuthType for interface callback, plus special for
    //non-blocking acc_manager requests
    private Object accManagerRef = null;
    private Object currentRefActivity;
    private AuthType reqAuthType;

    //save heap. Save refs to chained call objects to avoid redundant creations
    private FirebaseAccountsManager firebaseAccountsManager = null;
    private FirebaseTokenRatesManager firebaseTokenRatesManager = null;

    private static final FirebaseStaticReqManager ourInstance = new FirebaseStaticReqManager();

    public static FirebaseStaticReqManager getInstance() {
        return ourInstance;
    }

    private FirebaseStaticReqManager() {
    }

    public enum AuthType{

        LOGIN,
        LOGOUT,
        SIGNUP
    }


    /*
    All methods signature: request_service(), eg. requestAuth(params)
     */

    /*
    Methods to request for auth first. Overloaded to support one for login/signup, and one for logout
     */

    //For log in or sign up
    public void requestAuth(AuthType authType, String email, String password, Object refActivity){

        if (authType == AuthType.LOGIN){

            Log.e("ERROR", "AUTH TYPE LOGIN");

            Log.e("refActivity", String.valueOf(refActivity instanceof LogInActivity));
            //Ensure method is being called from correct activity
            if (refActivity instanceof LogInActivity){

                //Save ref activity and authType
                currentRefActivity = refActivity;
                reqAuthType = authType;
                FirebaseAuthManager.getInstance().logInUser(email, password, (LogInActivity) refActivity);
            } else {

                Log.e("INVALID AUTH REQUEST","Coming from " + refActivity.getClass());
            }
        } else if (authType == AuthType.SIGNUP){

            //Ensure method is being called from correct activity
            if (refActivity instanceof SignUpActivity){

                //save ref activity and authType
                currentRefActivity = refActivity;
                reqAuthType = authType;
                FirebaseAuthManager.getInstance().registerUser(email, password, (SignUpActivity) refActivity);
            } else {

                Log.e("INVALID AUTH REQUEST", "Coming from " + refActivity.getClass());
            }
        } else {

            Log.e("AUTH TYPE EXCEPTION", "Invalid auth type");
        }
    }

    //For log out
    public void requestAuth(AuthType authType, Object refActivity){

        if (authType == AuthType.LOGOUT){

            if (refActivity instanceof HomeActivity){

                //save refActivity and authType
                reqAuthType = authType;
                currentRefActivity = refActivity;

                //invoke log out
                FirebaseAuthManager.getInstance().logOutUser();
            }
        } else {

            Log.e("INVALID AUTH REQUEST", "Coming from " + refActivity.getClass());
        }
    }

    //Get current user
    public FirebaseUser requestAuthCurrentUser(Object refActivity){

        if (correctAuthUserCaller(refActivity)){

            return FirebaseAuthManager.getInstance().getCurrentUser();
        } else {

            Log.e("INVALID AUTH REQUEST", "Coming from " + refActivity.getClass());
        }

        return null;
    }

    //get list of correct callers
    private boolean correctAuthUserCaller(Object refActivity){

        return (refActivity instanceof  LogInActivity || refActivity instanceof FirebaseProfileManager
        || refActivity instanceof FirebaseAccountsManager || refActivity instanceof ConsumptionTrackGenerator);
    }


    /*
    AuthCallBack interface methods
     */
    @Override
    public void onSuccess(){

        //Use currentRefActivity and AuthType to determine casting
        if (reqAuthType == AuthType.LOGIN){

            ((LogInActivity) currentRefActivity).onSuccess();

            currentRefActivity = null;
            reqAuthType = null;

        } else if (reqAuthType == AuthType.SIGNUP){

            ((SignUpActivity) currentRefActivity).onSuccess();

            //remove currentRefActivity, and authType.
            /*
            Bad idea. Based on stack trace, onSuccess call will call the profile guy, run him. But once done, waiting
            for callback, this code below runs. Sets my currentRefs to null (despite setting them b4), and
            thus save acc data callback never invoked at caller cause of null pointer.

            By design, sign up will chain calls, and its only one with this call access. So, don't set currentRefActivity to null here
             */
            //currentRefActivity = null;
            reqAuthType = null;
        }

    }

    @Override
    public void onFailure(Exception authException){

        //Use currentRefActivity and AuthType to determine casting
        if (reqAuthType == AuthType.LOGIN){

            ((LogInActivity) currentRefActivity).onFailure(authException);

            currentRefActivity = null;
            reqAuthType = null;

        } else if (reqAuthType == AuthType.SIGNUP){

            ((SignUpActivity) currentRefActivity).onFailure(authException);
        }

        //remove currentRefActivity, and authType. Chain failed. So okay.
        currentRefActivity = null;
        reqAuthType = null;
    }


    /*
    Now an auth interface to be used by anyone who will seek auth services, specifically log in, out, or sign up
     */
    public interface AuthRequestCallBack{

        void onSuccess();

        void onFailure (Exception authException);
    }



    /*
    Save/load profile data methods
     */

    //Save
    public void requestSaveProfile(User user, Object refActivity){

        //ensure proper caller
        if (refActivity instanceof SignUpActivity){

            //Might need to save currentRefActivity but not AuthType
            currentRefActivity = refActivity;

            //Request for this profile to be saved by FirebaseProfileManager
            new FirebaseProfileManager().saveProfileData(user);
        } else {

            Log.e("INVALID PROFILE REQUEST", "Coming from " + refActivity.getClass());
        }
    }

    //Load
    public void requestLoadProfile(Object refActivity){

        //ensure proper caller
        if (refActivity instanceof SettingsFragment){

            //Might need to save currentRefActivity but not AuthType
            currentRefActivity = refActivity;

            //request to load data
            new FirebaseProfileManager().getProfileData();
        } else {

            Log.e("INVALID PROFILE REQUEST", "Coming from " + refActivity.getClass());
        }
    }


    /*
    Implementation of profile request callbacks
     */
    @Override
    public void onSaveSuccess(){

        //Tell requester of success

        if (currentRefActivity instanceof SignUpActivity){

            Log.e("FIREBASE REQ","INVOKED CALLBACK");
            ((SignUpActivity) currentRefActivity).onSaveSuccess();
        }
        Log.e("CURRENT REF", "Null " + String.valueOf(currentRefActivity == null));

        //Remove reference to currentRefActivity, if not sign up activity, cause of chaining
        if (!(currentRefActivity instanceof SignUpActivity)){

            currentRefActivity = null;
        }
    }

    @Override
    public void onSaveFail(DatabaseError databaseError){

        //Tell requester of failure

        if (currentRefActivity instanceof SignUpActivity){

            ((SignUpActivity) currentRefActivity).onSaveFail(databaseError);
        }
        //Remove reference to currentRefActivity
        currentRefActivity = null;
    }

    @Override
    public void onProfileReqSuccess(User user){

        //Send the user object to requester

        //Remove reference to currentRefActivity
        currentRefActivity = null;
    }

    @Override
    public void onProfileReqFail(DatabaseError databaseError){

        //Tell requester of failure

        //Remove reference to currentRefActivity
        currentRefActivity = null;
    }

    /*
    Interface implemented by anyone seeking to load/save profile data
     */
    public interface ProfileRequestCallback{

        void onSaveSuccess();

        void onSaveFail(DatabaseError databaseError);

        void onProfileReqSuccess(User user);

        void onProfileReqFail(DatabaseError databaseError);
    }


    /*
    Create/Delete account methods
     */

    //create account
    public void requestCreateAccount(Object refActivity, AccountsMiniMetaData miniMetaData, AccountsFullMetaData fullMetaData){

        if (refActivity instanceof SignUpActivity){ //Only sign up activity has direct access. Since no limits needed

            //perform call, save refActivity.
            currentRefActivity = refActivity;

            //Not chained @static req manager. Just write. So no need to store this ref
            new FirebaseAccountsManager(miniMetaData).createAccount(fullMetaData);
        } else {

            Log.e("INVALID ACC REQUEST","Coming from " + refActivity.getClass());
        }
    }

    //load account. Since we are loading acc list and details together, chain this call. Okay, bad idea, cause I have to save
    public void requestAccList(Object refActivity){

        if (refActivity instanceof AccountsManager){

            //Correct call. Save refActivity for callback, load data. Might face issue here, since its a non-blocking call. Thinking of having a separate ref for acc manager, just to avoid this confusion.
            //Yes, diff ref for acc manager
            accManagerRef = refActivity;

            //Loosely chained. Store ref to avoid double creation by static req manager
            firebaseAccountsManager = new FirebaseAccountsManager();
            //call for data load
            firebaseAccountsManager.getAccsList();
        } else {

            Log.e("INVALID ACC LIST REQ", "Coming from " + refActivity.getClass());
        }
    }

    //Load acc details
    public void requestAccDetails(Object refActivity){

        if (refActivity instanceof  AccountsManager){

            //Correct call.
           // accManagerRef = refActivity;

            //call for data load
            firebaseAccountsManager.getAccDetails();
        } else {

            Log.e("INVALID ACC DETAILS REQ", "Coming from " + refActivity.getClass());
        }
    }

    //remove account


    /*
    Implementation of FirebaseAccounts Manager interface for callbacks
     */
    @Override
    public void onCreateAccSuccess(){

        //Tell caller of success
        ((SignUpActivity) currentRefActivity).onCreateAccSuccess();

        //Null refActivity
        currentRefActivity = null;
    }

    @Override
    public void onCreateAccFail(DatabaseError databaseError){

        //Tell caller of failure
        ((SignUpActivity) currentRefActivity).onCreateAccFail(databaseError);

        //Null refActivity
        currentRefActivity = null;

    }

    @Override
    public void onGetAccListSuccess(DataSnapshot snapshot){


        //only acc manager can request this
        ((AccountsManager) accManagerRef).onGetAccListSuccess(snapshot);

    }

    @Override
    public void onGetAccListFail(DatabaseError databaseError){


        //only acc manager can request this
        ((AccountsManager) accManagerRef).onGetAccListFail(databaseError);

        accManagerRef = null;

        //remove ref to firebase accounts manager
        firebaseAccountsManager = null;
    }

    @Override
    public void onGetAccDetailsSuccess(DataSnapshot snapshot){

        ((AccountsManager) accManagerRef).onGetAccDetailsSuccess(snapshot);

        //Done. Safely put as null
        accManagerRef = null;

        //remove ref to firebase accounts manager
        firebaseAccountsManager = null;
    }

    @Override
    public void onGetAccDetailsFail(DatabaseError databaseError){

        ((AccountsManager) accManagerRef).onGetAccDetailsFail(databaseError);

        accManagerRef = null;

        //remove ref to firebase accounts manager
        firebaseAccountsManager = null;
    }

    /*
    Implemented by actual caller
     */
    public interface AccountRequestCallback{

        void onCreateAccSuccess();

        void onCreateAccFail(DatabaseError databaseError);

        void onGetAccListSuccess(DataSnapshot snapshot);

        void onGetAccListFail(DatabaseError databaseError);

        void onGetAccDetailsSuccess(DataSnapshot snapshot);

        void onGetAccDetailsFail (DatabaseError databaseError);
    }



    /*
    Method to get token rates
     */
    public void requestTokenRates(Object refActivity){

        if (refActivity instanceof BuyTokensFragment){

            //correct call. Request FirebaseTokenRates Manager...no parallel calls. So safe to do line below
            currentRefActivity = refActivity;

            firebaseTokenRatesManager = new FirebaseTokenRatesManager();
            firebaseTokenRatesManager.getTokenRates();

        } else {

            Log.e("INVALID TOKEN RATES REQ", "Coming from " + refActivity.getClass());
        }
    }

    /*
    If activity closes, request close channel
     */
    public void requestCloseTokenChannel(Object refActivity){

        if (refActivity instanceof BuyTokensFragment){

            //correct call. Close channel
            firebaseTokenRatesManager.closeChannel();

            firebaseTokenRatesManager = null;

            currentRefActivity = null;
        }
    }

    /*
    Implementation of interface callbacks
     */
    @Override
    public void onRatesChange(float rate){


    }

    @Override
    public void onRatesReadFail(DatabaseError databaseError){

        //Not sure of immediate killing. What if it refires? Like the rest btw?

    }

    /*
    Interface for whoever requires token rates access
     */
    public interface TokenRatesWatcher{

        void onRatesChange(float rate);

        void onRatesReadFail(DatabaseError databaseError);

    }

    //TODO: DATABASE ERROR FAIL IN CASE OF NO NETWORK?

}
