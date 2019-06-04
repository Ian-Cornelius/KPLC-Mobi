package com.ian_cornelius.kplcmobi.utils.FirebaseUtils;

/*
This class simply reads the current token rates in db. No data is stored, as db rules prohibit user from changing this value

Value returned to caller. If db error, show toast, warning.

TODO Network status receiver. Tell us when device is connected to net or not. Not so useful in Firebase. Knows failed requests and refires
 */

import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ian_cornelius.kplcmobi.BuildConfig;

public class FirebaseTokenRatesManager {

    /*
    private constructor. Never get an instance, not singleton, just access static method
     */
    protected FirebaseTokenRatesManager(){

    }

    private DatabaseReference dbRef;

    /*
    Value event listener to support live changes
     */
    private ValueEventListener tokenListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

            //Tell caller of new data
            FirebaseStaticReqManager.getInstance().onRatesChange( Float.valueOf(String.valueOf(dataSnapshot.getValue())));
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

            //Tell caller of failure
            FirebaseStaticReqManager.getInstance().onRatesReadFail(databaseError);
        }
    };

    /*
    Method to request for token rates
     */
    protected void getTokenRates(){

        dbRef = FirebaseDatabase.getInstance().getReference(BuildConfig.TOKEN_RATES_REF);

        dbRef.addValueEventListener(tokenListener);
    }

    /*
    Method to request close of listener
     */
    protected void closeChannel(){

        dbRef.removeEventListener(tokenListener);
    }

    /*
    Communication interface
     */
    protected interface TokenRatesWatcher{

        void onRatesChange(float rate);

        void onRatesReadFail(DatabaseError databaseError);
    }

}
