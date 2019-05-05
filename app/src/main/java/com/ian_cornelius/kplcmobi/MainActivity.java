package com.ian_cornelius.kplcmobi;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//For our motion layout splash screen play
import android.support.constraint.motion.MotionLayout;

//To switch our activities
import android.content.Intent;

//For our login activity
import com.ian_cornelius.kplcmobi.ui.login.LogInActivity;

public class MainActivity extends AppCompatActivity {

    //To hold our motion layout instance
    private MotionLayout launchRootLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.launch_activity);

        //Get our motion layout instance
        launchRootLayout = findViewById(R.id.launchRootLayout);

        //Handle the splash screen
        playSplash();
    }


    //Method to trigger splash screen
    private void playSplash(){

        launchRootLayout.transitionToEnd();

        //set up our listener to switch to log in activity once splash screen ends
        launchRootLayout.setTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int i, int i1) {

            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int i, int i1, float v) {

            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int i) {

                startActivity(new Intent(getApplicationContext(),LogInActivity.class));
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int i, boolean b, float v) {

            }
        });
    }
}
