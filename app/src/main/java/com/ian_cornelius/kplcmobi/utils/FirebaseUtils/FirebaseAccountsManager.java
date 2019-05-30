package com.ian_cornelius.kplcmobi.utils.FirebaseUtils;

/*
This class is used for access to the accounts fields in the real time db
 */

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.auth.FirebaseUser;

import com.ian_cornelius.kplcmobi.BuildConfig;
import com.ian_cornelius.kplcmobi.models.AccountsMiniMetaData;
import com.ian_cornelius.kplcmobi.models.AccountsFullMetaData;

public class FirebaseAccountsManager {

    private FirebaseDatabase database;
    private DatabaseReference dbRef;

    private FirebaseUser user;

    //Hold our mini metadata model instance
    private AccountsMiniMetaData miniMetaData;

    protected FirebaseAccountsManager(AccountsMiniMetaData miniMetaData){

        user = FirebaseStaticReqManager.getInstance().requestAuthCurrentUser(this);
        database = FirebaseDatabase.getInstance();
        //start with acc list, saving miniMetaData 1st. 1st acc to be created always current. Following not. Enforced by AccManager
        dbRef = database.getReference(BuildConfig.ROOT_REF + user.getUid() + BuildConfig.ACC_LIST_REF + miniMetaData.getAccountNumber());

        this.miniMetaData = miniMetaData;

    }


    /*
    Method to create account. Pass full meta data here
     */
    protected void createAccount(final AccountsFullMetaData fullMetaData){

        dbRef.setValue(miniMetaData, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                if (databaseError == null){

                    //call onCreateAccSuccess() method of caller. Done after saving full data
                    //Now save full data
                    saveFullAccData(fullMetaData);

                } else {

                    //call onCreateAccFail() method of caller
                    FirebaseStaticReqManager.getInstance().onCreateAccFail(databaseError);

                }
            }
        });
    }


    /*
    Actual saving of full account data
     */
    private void saveFullAccData(AccountsFullMetaData fullMetaData){

        //reset database reference
        dbRef = database.getReference(BuildConfig.ROOT_REF + user.getUid() + BuildConfig.ACC_DATA_REF + miniMetaData.getAccountNumber() + BuildConfig.ACC_DETAILS_REF);

        //save data
        dbRef.setValue(fullMetaData, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {

                if (databaseError == null){

                    //call onCreateAccSuccess() method of caller
                    FirebaseStaticReqManager.getInstance().onCreateAccSuccess();

                } else {

                    //call onCreateAccFail() method of caller
                    FirebaseStaticReqManager.getInstance().onCreateAccFail(databaseError);
                }
            }
        });
    }


    /*
    Method to retrieve account data
     */


    /*
    Method to remove account
     */



    /*
    Interface with callbacks. Implemented by caller

    TODO Implement onLoadAccountSuccess/Fail callbacks
     */
    protected interface AccountsAccessCallBack{

        void onCreateAccSuccess();

        void onCreateAccFail(DatabaseError databaseError);
    }

}
