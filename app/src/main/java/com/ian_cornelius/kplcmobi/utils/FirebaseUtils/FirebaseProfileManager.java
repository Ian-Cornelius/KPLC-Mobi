package com.ian_cornelius.kplcmobi.utils.FirebaseUtils;

/*
Use this class to manage Firebase profile details of account holder. Not a singleton cause its needed
in very few instances (1st time to save profile data, and later on in settings when user feels like changing a few
personal details)
 */

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

import com.google.firebase.database.ValueEventListener;
import com.ian_cornelius.kplcmobi.BuildConfig;
import com.ian_cornelius.kplcmobi.models.User;

public class FirebaseProfileManager {

    /*
    Firebase database instances
     */
    private FirebaseDatabase database;
    private DatabaseReference dbRef;

    //Help get uid
    private FirebaseUser user;


    /*
    protected constructor to initialize class
     */
    protected FirebaseProfileManager(){

        //Get user first
        user = FirebaseStaticReqManager.getInstance().requestAuthCurrentUser(this);
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference(BuildConfig.ROOT_REF + user.getUid() + BuildConfig.PROFILE_REF);
    }

    /*
    Method to save profile data.
     */
    protected void saveProfileData(User user){

        dbRef.setValue(user, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                //Make sure we don't have an error
                if (databaseError == null){

                    //call onSaveSuccess() method of FirebaseStaticReqManager
                    FirebaseStaticReqManager.getInstance().onSaveSuccess();
                } else {

                    //call onSaveFail() method of FirebaseStaticReqManager. In login, prompts deletion of this user
                    //since we can't have a user without profile info
                    FirebaseStaticReqManager.getInstance().onSaveFail();
                }
            }
        });
    }

    /*
    Method to retrieve profile data
     */
    protected void getProfileData(){

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //call onProfileReqSuccess(), passing in user object
                FirebaseStaticReqManager.getInstance().onProfileReqSuccess(dataSnapshot.getValue(User.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                //call onProfileReqFail(), tell user changing personal details that request could not be completed
                FirebaseStaticReqManager.getInstance().onProfileReqFail();
            }
        });
    }


    /*
    Interface to implement callbacks
     */
    protected interface ProfileManagerCallbacks{

        void onSaveSuccess();

        void onSaveFail();

        void onProfileReqSuccess(User user);

        void onProfileReqFail();
    }
}
