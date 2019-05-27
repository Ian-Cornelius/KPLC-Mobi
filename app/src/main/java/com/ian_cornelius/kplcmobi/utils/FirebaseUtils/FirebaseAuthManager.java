package com.ian_cornelius.kplcmobi.utils.FirebaseUtils;

/*
This class will be used to log in, log out, or sign up new users.

As a singleton such that we can easily get auth state
in the entire app, through the static req manager

Final, so can't be inherited.
 */

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.ian_cornelius.kplcmobi.ui.login.LogInActivity;
import com.ian_cornelius.kplcmobi.ui.signup.SignUpActivity;

public final class FirebaseAuthManager {

    //our only instance
    private static final FirebaseAuthManager authManager = new FirebaseAuthManager();

    /*
    Hold auth instance
     */
    private FirebaseAuth mAuth;

    //static method to get instance
    protected static FirebaseAuthManager getInstance() {
        return authManager;
    }

    //private constructor
    private FirebaseAuthManager() {

        //Initialize mAuth here
        mAuth = FirebaseAuth.getInstance();

    }


    /*
    Get current user
     */
    protected FirebaseUser getCurrentUser(){

        return mAuth.getCurrentUser();
    }

    /*
    Sign up a user
     */
    protected void registerUser(final String email, final String password, SignUpActivity refActivity){

        /*
        Invoke firebase method to create user with email and password - credentials used in KPLC mobi
         */
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(refActivity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    //Tell SignUpActivity to remove dialog. Use AuthCallBack Interface, onSuccess() method
                } else{

                    //Tell SignUpActivity of failure using onFail() callback, passing exception. See if I can digest
                    //the exception to tell user accurate reason of failure
                }
            }
        });
    }


    /*
    Sign in existing users
     */
    protected void logInUser(final String email, final String password, LogInActivity refActivity){

        /*
        Invoke firebase method to create user with email and password
         */

        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(refActivity, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){

                    //Tell LogInActivity to remove dialog. Use AuthCallBack Interface, onSuccess() method
                } else{

                    //Tell LogInActivity of failure using onFail() callback, passing exception. See if I can digest
                    //the exception to tell user accurate reason of failure
                }
            }
        });
    }


    protected interface AuthCallBack{

        //called on successful log in or sign up
        void onSuccess();

        //called if log in or sign up fails
        void onFailure(String authException);
    }

}
