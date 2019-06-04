package com.ian_cornelius.kplcmobi.utils.FirebaseUtils;

/*
This class is used for access to the accounts fields in the real time db
 */

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.database.ValueEventListener;
import com.ian_cornelius.kplcmobi.BuildConfig;
import com.ian_cornelius.kplcmobi.models.AccountsMiniMetaData;
import com.ian_cornelius.kplcmobi.models.AccountsFullMetaData;

public class FirebaseAccountsManager {

    private FirebaseDatabase database;
    private DatabaseReference dbRef;

    private FirebaseUser user;

    //Hold our mini metadata model instance
    private AccountsMiniMetaData miniMetaData;

    /*
    Constructor used when creating account, so that we pass in the data needed (mini metadata first), then
    full data later, and set 1st db ref
     */
    protected FirebaseAccountsManager(AccountsMiniMetaData miniMetaData){

        user = FirebaseStaticReqManager.getInstance().requestAuthCurrentUser(this);
        database = FirebaseDatabase.getInstance();
        //start with acc list, saving miniMetaData 1st. 1st acc to be created always current. Following not. Enforced by AccManager
        dbRef = database.getReference(BuildConfig.ROOT_REF + user.getUid() + BuildConfig.ACC_LIST_REF + miniMetaData.getAccountNumber());

        this.miniMetaData = miniMetaData;

    }


    /*
    Constructor used when retrieving account data. db ref set based on retrieval type
     */
    protected FirebaseAccountsManager(){

        user = FirebaseStaticReqManager.getInstance().requestAuthCurrentUser(this);
        database = FirebaseDatabase.getInstance();
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
        dbRef = database.getReference(BuildConfig.ROOT_REF + user.getUid() + BuildConfig.ACC_DETAILS_REF + miniMetaData.getAccountNumber());

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
    Method to retrieve account data, accs list with miniMetaData
     */
    protected void getAccsList(){

        //set correct dbRef
        dbRef = database.getReference(BuildConfig.ROOT_REF + user.getUid() + BuildConfig.ACC_LIST_REF);

        //read data once. This data doesn't change often
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //success. Pass datasnapshot to caller
                FirebaseStaticReqManager.getInstance().onGetAccListSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                //failure. Pass exception to caller
                FirebaseStaticReqManager.getInstance().onGetAccListFail(databaseError);
            }
        });
    }

    /*
    Method to retrieve account data, accs full data (done per account, cause of nesting) Nesting removed. Db restructured. Request once
     */
    protected void getAccDetails(){

        //set correct dbRef
        dbRef = database.getReference(BuildConfig.ROOT_REF + user.getUid() + BuildConfig.ACC_DETAILS_REF);

        //read data once. This data doesn't change often
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //success, pass data snapshot to caller
                FirebaseStaticReqManager.getInstance().onGetAccDetailsSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                //failure, pass error to caller
                FirebaseStaticReqManager.getInstance().onGetAccDetailsFail(databaseError);
            }
        });

    }



    /*
    Method to remove account
     */



    /*
    Interface with callbacks. Implemented by caller

    TODO Implement ongetAccountListSuccess/Fail callbacks
     */
    protected interface AccountsAccessCallBack{

        void onCreateAccSuccess();

        void onCreateAccFail(DatabaseError databaseError);

        void onGetAccListSuccess(DataSnapshot snapshot);

        void onGetAccListFail(DatabaseError databaseError);

        void onGetAccDetailsSuccess(DataSnapshot snapshot);

        void onGetAccDetailsFail(DatabaseError databaseError);
    }

}
