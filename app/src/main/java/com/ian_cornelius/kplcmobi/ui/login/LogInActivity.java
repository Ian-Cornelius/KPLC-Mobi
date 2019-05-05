package com.ian_cornelius.kplcmobi.ui.login;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ian_cornelius.kplcmobi.R;
import com.ian_cornelius.kplcmobi.ui.home.HomeActivity;
import com.ian_cornelius.kplcmobi.ui.signup.SignUpActivity;

//Get hold of our button widgets
import android.view.View;
import android.widget.Button;

//Handle clicks
import android.view.View.OnClickListener;

public class LogInActivity extends AppCompatActivity {

    //To hold our button instances
    private Button mBtnLogIn, mBtnCreateAcc, mBtnReqForPower;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in_activity);

        /*

        get our widget instances

         */

        //Buttons
        mBtnLogIn = findViewById(R.id.btnLogIn);
        mBtnCreateAcc = findViewById(R.id.btnCreateAcc);
        mBtnReqForPower = findViewById(R.id.btnReqForPower);

        /*
        Handle clicks
         */
        //Log in button
        mBtnLogIn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                startActivity(new Intent(LogInActivity.this, HomeActivity.class));
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

    //Deal with pressing of back button. Avoid going back to splash screen
    @Override
    public void onBackPressed(){

        //close the app
        finishAffinity();
        System.exit(0);
    }
}
