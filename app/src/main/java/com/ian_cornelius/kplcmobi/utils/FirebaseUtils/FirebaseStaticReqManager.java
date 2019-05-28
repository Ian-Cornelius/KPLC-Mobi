package com.ian_cornelius.kplcmobi.utils.FirebaseUtils;

/*
All static firebase requests will pass through this class.

Not inherited
 */

import android.util.Log;

import com.google.firebase.auth.FirebaseUser;
import com.ian_cornelius.kplcmobi.models.User;
import com.ian_cornelius.kplcmobi.ui.fragments.SettingsFragment;
import com.ian_cornelius.kplcmobi.ui.home.HomeActivity;
import com.ian_cornelius.kplcmobi.ui.login.LogInActivity;
import com.ian_cornelius.kplcmobi.ui.signup.SignUpActivity;

public final class FirebaseStaticReqManager implements FirebaseAuthManager.AuthCallBack, FirebaseProfileManager.ProfileManagerCallbacks{

    //Hold current reference activity or frag, plus reqAuthType for interface callback
    private Object currentRefActivity;
    private AuthType reqAuthType;

    private static final FirebaseStaticReqManager ourInstance = new FirebaseStaticReqManager();

    public static FirebaseStaticReqManager getInstance() {
        return ourInstance;
    }

    private FirebaseStaticReqManager() {
    }

    public static enum AuthType{

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
            }
        } else {

            Log.e("INVALID AUTH REQUEST", "Coming from " + refActivity.getClass());
        }
    }

    //Get current user
    public FirebaseUser requestAuthCurrentUser(Object refActivity){

        if (refActivity instanceof  LogInActivity || refActivity instanceof FirebaseProfileManager){

            return FirebaseAuthManager.getInstance().getCurrentUser();
        } else {

            Log.e("INVALID AUTH REQUEST", "Coming from " + refActivity.getClass());
        }

        return null;
    }


    /*
    AuthCallBack interface methods
     */
    @Override
    public void onSuccess(){

        //Use currentRefActivity and AuthType to determine casting
        if (reqAuthType == AuthType.LOGIN){


        } else if (reqAuthType == AuthType.SIGNUP){

            ((SignUpActivity) currentRefActivity).onSuccess();
        }
    }

    @Override
    public void onFailure(Exception authException){

        //Use currentRefActivity and AuthType to determine casting
        if (reqAuthType == AuthType.LOGIN){


        } else if (reqAuthType == AuthType.SIGNUP){

            ((SignUpActivity) currentRefActivity).onFailure(authException);
        }

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
    }

    @Override
    public void onSaveFail(){

        //Tell requester of failure
    }

    @Override
    public void onProfileReqSuccess(User user){

        //Send the user object to requester
    }

    @Override
    public void onProfileReqFail(){

        //Tell requester of failure
    }

    /*
    Interface implemented by anyone seeking to load/save profile data
     */
    public interface ProfileRequestCallback{

        void onSaveSuccess();

        void onSaveFail();

        void onProfileReqSuccess();

        void onProfileReqFail();
    }

}
