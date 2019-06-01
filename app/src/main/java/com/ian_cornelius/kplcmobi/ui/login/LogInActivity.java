package com.ian_cornelius.kplcmobi.ui.login;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.FirebaseNetworkException;
import com.ian_cornelius.kplcmobi.R;
import com.ian_cornelius.kplcmobi.models.ConsumptionTrackLastHistoryRecord;
import com.ian_cornelius.kplcmobi.ui.dialogs.ProcessDialog;
import com.ian_cornelius.kplcmobi.ui.home.HomeActivity;
import com.ian_cornelius.kplcmobi.ui.signup.SignUpActivity;

//Get hold of our widgets
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

//Handle clicks
import android.view.View.OnClickListener;
import android.widget.Toast;

//Process auth requests
import com.ian_cornelius.kplcmobi.utils.FirebaseUtils.FirebaseStaticReqManager;
import com.ian_cornelius.kplcmobi.utils.data_managers.ConsumptionTrackLocalStoreManager;

public class LogInActivity extends AppCompatActivity implements FirebaseStaticReqManager.AuthRequestCallBack{

    //To hold our button instances
    private Button mBtnLogIn, mBtnCreateAcc, mBtnReqForPower;

    //To hold edit text instances
    private EditText mEditEmail, mEditPass;

    //Show progress
    private ProcessDialog processDialog;

    //Tell us of a saved auth
    public static final String SAVED_AUTH = "SAVED_AUTH";
    public static final String NEW_AUTH = "NEW_AUTH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in_activity);

        /*
        Test local consumption track persistence with pseudo-data
         */
        ConsumptionTrackLocalStoreManager localStoreManager = ConsumptionTrackLocalStoreManager.getInstance();

        //Post local history. Repeated posts don't affect maths which varies based on time elapsed
        ConsumptionTrackLastHistoryRecord record = new ConsumptionTrackLastHistoryRecord();
        record.setPrevUnitsBought(30);
        record.setMonthlyAverageUnits(30);
        record.setLastDate("30/5/2019 08:55");

        localStoreManager.postChanges(record, getApplicationContext());

        //expect consumed units to be updated accordingly, once broadcast 1st fires. And it was. And
        //I think it will work beautifully

        //By pass all this if user still logged in
        if (FirebaseStaticReqManager.getInstance().requestAuthCurrentUser(this) != null){

            //Switch of activity done on Success.

            //Put in bundle args
            Bundle bundle = new Bundle();
            bundle.putString("STATUS",SAVED_AUTH);
            Intent intent = new Intent(LogInActivity.this, HomeActivity.class);
            intent.putExtra("STATUS",bundle);
            startActivity(intent);
        }

        /*

        get our widget instances

         */

        //Buttons
        mBtnLogIn = findViewById(R.id.btnLogIn);
        mBtnCreateAcc = findViewById(R.id.btnCreateAcc);
        mBtnReqForPower = findViewById(R.id.btnReqForPower);

        //Edit texts
        mEditEmail = findViewById(R.id.editEmail);
        mEditPass = findViewById(R.id.editPass);

        /*
        Handle clicks
         */
        //Log in button
        mBtnLogIn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                //Validate data keyed in
                if (validData()){

                    //request login
                    processDialog = new ProcessDialog();
                    processDialog.setCancelable(false);

                    processDialog.setMessagesPreShow("Please wait", "You're being logged in");
                    processDialog.show(getSupportFragmentManager(), "PROCESS DIALOG");
                    FirebaseStaticReqManager.getInstance().requestAuth(FirebaseStaticReqManager.AuthType.LOGIN,
                            mEditEmail.getText().toString(), mEditPass.getText().toString(), LogInActivity.this);
                    //Show custom dialog, request for login. Switch of activity done on Success. Need to call finish? Proly yes. So that its removed from stack, reduce heap size. Thus on log out, start

                } else{

                    Toast.makeText(getApplicationContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }

            }
        });

        //Create account button
        mBtnCreateAcc.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                //launch Sign Up Activity
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
            }
        });

    }

    //Validate data keyed in
    private boolean validData(){

        if (!TextUtils.isEmpty(mEditEmail.getText().toString()) && !TextUtils.isEmpty(mEditPass.getText().toString())){

            return true;
        }

        return false;
    }


    /*
    Override AuthRequestCallBacks
     */
    @Override
    public void onSuccess(){

        //Switch to home activity
        Bundle bundle = new Bundle();
        bundle.putString("STATUS", NEW_AUTH);
        Intent intent = new Intent(LogInActivity.this, HomeActivity.class);
        intent.putExtra("STATUS", bundle);
        startActivity(intent);
        finish();

    }

    @Override
    public void onFailure(Exception authException){

        if (authException instanceof FirebaseNetworkException){

            processDialog.transitionToError("Please ensure you have an active internet connection and try again");
            Log.e("LOG IN EXCEPTION", authException.toString());
        } else {

            processDialog.transitionToError("You have entered invalid credentials. Please try again");
            Log.e("LOG IN EXCEPTION", authException.toString());
        }
    }

    //Deal with pressing of back button. Avoid going back to splash screen
    @Override
    public void onBackPressed(){

        //close the app
        finishAffinity();
        System.exit(0);
    }
}
