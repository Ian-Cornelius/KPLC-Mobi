package com.ian_cornelius.kplcmobi.ui.signup;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.firebase.FirebaseNetworkException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.ian_cornelius.kplcmobi.R;
import com.ian_cornelius.kplcmobi.models.User;

/*
For our UI elements
 */

//buttons
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.Button;

/*
Handle clicks
 */
import android.view.View.OnClickListener;
import android.widget.Toast;

import java.util.regex.Pattern;

import com.ian_cornelius.kplcmobi.ui.dialogs.ProcessDialog;
import com.ian_cornelius.kplcmobi.utils.FirebaseUtils.FirebaseStaticReqManager;

public class SignUpActivity extends AppCompatActivity implements FirebaseStaticReqManager.AuthRequestCallBack{

    /*
    Instances of our widgets
     */

    //Buttons
    private ImageButton mBtnBack;
    private Button mBtnCreateAcc;

    //Edit texts
    private EditText mEditName, mEditPhone, mEditEmail, mEditMtrNo, mEditPass, mEditConfirmPass;

    //Show process dialog
    ProcessDialog dialog;

    //Load profile data
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up_activity);

        /*
        Get UI widgets references
         */

        //Buttons
        mBtnBack = findViewById(R.id.btnBack);
        mBtnCreateAcc = findViewById(R.id.btnCreateAcc);

        //Edit texts
        mEditName = findViewById(R.id.editName);
        mEditPhone = findViewById(R.id.editPhone);
        mEditEmail = findViewById(R.id.editEmail);
        mEditMtrNo = findViewById(R.id.editMtrNo);
        mEditPass = findViewById(R.id.editPass);
        mEditConfirmPass = findViewById(R.id.editConfirmPass);

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

        mBtnCreateAcc.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick (View v){

                if (validData()) {

                    //request to sign up user. Use callbacks to save profile data and then proceed to home screen. Else,
                    //Failure callbacks determine next appropriate action
                    //Toast.makeText(getApplicationContext(),"Passed!!!!", Toast.LENGTH_SHORT).show();

                    //Don't forget to disable btnBack to avoid interruptions

                    //Show our custom dialog
                    dialog = new ProcessDialog();
                    dialog.setCancelable(false);

                    //Show custom messages
                    dialog.setMessagesPreShow("Hang on", "We are adding you to the system");
                    dialog.show(getSupportFragmentManager(), "PROCESS_DIALOG");
                    FirebaseStaticReqManager.getInstance().requestAuth(FirebaseStaticReqManager.AuthType.SIGNUP,
                            mEditEmail.getText().toString(), mEditPass.getText().toString(), SignUpActivity.this);
                } else {

                    //Do nothing. validData() will show appropriate errors
                }
            }
        });

    }

    //use this method to validate that correct data has been keyed in
    private boolean validData(){

        //First, ensure we have no empty fields
        if (noEmpty()){

            //continue with other checks

            //check that we have a valid phone number. Start with zero, then any combo of digits, max char
            if (validPhone()){

                //Ensure email is valid
                if (Patterns.EMAIL_ADDRESS.matcher(mEditEmail.getText().toString()).matches()){

                    //Ensure meter no is valid
                    if (validMeter()){

                        //Ensure password is minimum 8 characters
                        if (mEditPass.getText().toString().length() >= 8){

                            //Ensure passwords match
                            if (mEditPass.getText().toString().equals(mEditConfirmPass.getText().toString())){

                                //All tests passed. Return
                                return true;
                            } else {

                                //Set error. Return
                                mEditConfirmPass.setError("Passwords don't match");
                                return false;
                            }
                        } else {

                            mEditPass.setError("Password too short");
                            return false;
                        }
                    } else {

                        //valid meter shows appropriate errors. Return
                        return false;
                    }
                } else{

                    //Show error here cause using base verifier
                    mEditEmail.setError("Email address not valid");
                    return false;
                }

            } else {

                //else return false. Errors cited by validPhone() method
                return false;
            }

        }

        //noEmpty will put up appropriate error message. Return false for validData()
        return false;
    }

    //Check that we have no empty fields
    private boolean noEmpty(){

        //cascading check, releasing at first error detection
        if (TextUtils.isEmpty(mEditName.getText().toString())){

            mEditName.setError(getResources().getString(R.string.no_empty));
            return false;

        } else if (TextUtils.isEmpty(mEditPhone.getText().toString())){

            mEditPhone.setError(getResources().getString(R.string.no_empty));
            return false;

        } else if (TextUtils.isEmpty(mEditEmail.getText().toString())){

            mEditEmail.setError(getResources().getString(R.string.no_empty));
            return false;

        } else if (TextUtils.isEmpty(mEditMtrNo.getText().toString())){

            mEditMtrNo.setError(getResources().getString(R.string.no_empty));
            return false;

        } else if (TextUtils.isEmpty(mEditPass.getText().toString())){

            mEditPass.setError(getResources().getString(R.string.no_empty));
            return false;

        } else if (TextUtils.isEmpty(mEditConfirmPass.getText().toString())){

            mEditConfirmPass.setError(getResources().getString(R.string.confirm_pass_err));
            return false;

        }

        //All fields not empty. Return true
        return true;
    }


    //check that we have a valid phone number
    private boolean validPhone(){

        if (mEditPhone.getText().toString().length() == 10){

            //pattern match
            String expression = "^07([0-9]+)$";
            Pattern pattern = Pattern.compile(expression);
            if (pattern.matcher(mEditPhone.getText().toString()).matches()){

                //valid phone number
                return true;
            } else {

                mEditPhone.setError("Not a valid phone number");

                return false;
            }
        }

        //too short, return false
        mEditPhone.setError("Phone number too short");
        return false;
    }


    //check that the meter no is valid
    private boolean validMeter(){

        if (mEditMtrNo.getText().toString().length() == 10){

            //pattern match
            String expression = "^[1-9]([0-9]+)$";
            Pattern pattern = Pattern.compile(expression);

            if (pattern.matcher(mEditMtrNo.getText().toString()).matches()){

                //valid meter number
                return true;
            } else {

                mEditMtrNo.setError("Not a valid meter number");

                return false;
            }
        }

        //too short
        mEditMtrNo.setError("Meter number too short");
        return false;
    }


    /*
    Overriden AuthCallBackInterfaceMethods
     */
    @Override
    public void onSuccess(){

        //Request to save profile data
        dialog.dismiss();
        Toast.makeText(this,"Signed up user",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFailure(Exception failureException){

        //Show dialog error
//        dialog.dismiss();

        //Process according to exception. Like Collision or Network
        if (failureException instanceof FirebaseAuthUserCollisionException){

            dialog.transitionToError("This email address is already in use. Please key in a different one");
            Log.e("SIGN UP EXCEPTION", failureException.toString());
        }

        if (failureException instanceof FirebaseNetworkException){

            dialog.transitionToError("Seem's like you don't have an active internet connection. Please check and try again");
            Log.e("SIGN UP EXCEPTION",failureException.toString());
        }
    }

    @Override
    public void onBackPressed(){

        //do custom actions on back pressed. Confirm interruption of sign up process
        dialog.setCancelable(true);
        dialog.dismiss();

    }
}
