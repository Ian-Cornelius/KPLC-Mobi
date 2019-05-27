package com.ian_cornelius.kplcmobi.ui.login;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ian_cornelius.kplcmobi.R;
import com.ian_cornelius.kplcmobi.ui.home.HomeActivity;
import com.ian_cornelius.kplcmobi.ui.signup.SignUpActivity;

//Get hold of our widgets
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

//Handle clicks
import android.view.View.OnClickListener;
import android.widget.Toast;

//Process auth requests
import com.ian_cornelius.kplcmobi.utils.FirebaseUtils.FirebaseStaticReqManager;

public class LogInActivity extends AppCompatActivity {

    //To hold our button instances
    private Button mBtnLogIn, mBtnCreateAcc, mBtnReqForPower;

    //To hold edit text instances
    private EditText mEditEmail, mEditPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.log_in_activity);

        //By pass all this if user still logged in
        if (FirebaseStaticReqManager.getInstance().requestAuthCurrentUser(this) != null){

            //Switch of activity done on Success.
            startActivity(new Intent(LogInActivity.this, HomeActivity.class));
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

                    //Show custom dialog, request for login. Switch of activity done on Success. Need to call finish? Proly yes. So that its removed from stack, reduce heap size. Thus on log out, start
                    startActivity(new Intent(LogInActivity.this, HomeActivity.class));
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

    //Deal with pressing of back button. Avoid going back to splash screen
    @Override
    public void onBackPressed(){

        //close the app
        finishAffinity();
        System.exit(0);
    }
}
