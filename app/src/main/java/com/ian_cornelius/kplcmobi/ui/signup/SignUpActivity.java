package com.ian_cornelius.kplcmobi.ui.signup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ian_cornelius.kplcmobi.R;

/*
For our UI elements
 */

//buttons
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

/*
Handle clicks
 */
import android.view.View.OnClickListener;

public class SignUpActivity extends AppCompatActivity {

    /*
    Instances of our widgets
     */

    //Buttons
    ImageButton mBtnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);

        /*
        Get UI widgets references
         */

        //Buttons
        mBtnBack = findViewById(R.id.btnBack);

        /*
        Handle clicks
         */

        //Back Button
        mBtnBack.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                Log.e("Error","Hit back button");
                //get back to previous activity
                finish();
            }
        });

    }
}
