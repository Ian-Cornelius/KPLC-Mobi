package com.ian_cornelius.kplcmobi.controllers;

/*
Controller for security settings
 */

import android.support.constraint.motion.MotionScene;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.support.constraint.motion.MotionLayout;

import com.ian_cornelius.kplcmobi.R;
import com.ian_cornelius.kplcmobi.ui.fragments.ChangePasswordFragment;
import com.ian_cornelius.kplcmobi.ui.fragments.SettingsFragment;
import com.ian_cornelius.kplcmobi.ui.home.HomeActivity;

public class SecuritySettingsController {

    //Hold ref to our fragment
    private SettingsFragment refFrag;

    //Widgets
    private TextView mTxtChangePass;
    private Button mBtnBackSec;

    private MotionLayout mSettingsLayout, mSecurityLayout;

    public boolean reverse = false;


    /*
    public constructor
     */
    public SecuritySettingsController(SettingsFragment frag){

        this.refFrag = frag;
    }


    /*
    init views
     */
    public void initViews(View mainView, View rootView){

        mSettingsLayout = (MotionLayout) rootView;
        mSecurityLayout = (MotionLayout) mainView;

        mTxtChangePass = mainView.findViewById(R.id.txtChangePass);
        mBtnBackSec = mainView.findViewById(R.id.btnBackSec);

        setUpViews();
    }

    private void setUpViews(){

        /*
        Set up listener for motion layout, to set up frag on end
         */
        mSecurityLayout.setTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int i, int i1) {

            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int i, int i1, float v) {

            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int i) {

                /*
                Launch or kill fragment based on whether its there or not. Simple implementation

                Fails cause of GC. Takes time to dump...

                Use reverse
                 */
                if (!reverse){

                    //Launch frag
                    refFrag.getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.zoom_in_fab_content, R.anim.zoom_out_fab_content).replace(R.id.securityFragHolder, new ChangePasswordFragment()).commit();

                    Log.e("SECURITY FRAG","LAUNCHED");

                    reverse = true;

                } else{

                    //Remove frag
                    refFrag.getChildFragmentManager().beginTransaction().remove(refFrag.getChildFragmentManager().findFragmentById(R.id.securityFragHolder)).commit();

                    Log.e("SECURITY FRAG","REMOVED");

                    reverse = false;

                    //reactivate clicks on text
                    mTxtChangePass.setEnabled(true);
                }

            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int i, boolean b, float v) {

            }

            @Override
            public boolean allowsTransition(MotionScene.Transition transition) {
                return false;
            }
        });

        //Click listeners for widgets
        mTxtChangePass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Appropriate root transitions
                mSettingsLayout.setTransition(R.id.settingsFragmentState0, R.id.settingsFragmentState2);

                //Transition all layouts to end
                mSecurityLayout.transitionToEnd();
                mSettingsLayout.transitionToEnd();

                //disable clicks on this
                mTxtChangePass.setEnabled(false);
            }
        });

        mBtnBackSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                Bug solving for motion layout
                 */
                ((HomeActivity) mBtnBackSec.getContext()).getWindow().getDecorView().findViewById(R.id.home_fragments_holder).invalidate();
                ((HomeActivity) mBtnBackSec.getContext()).getWindow().getDecorView().findViewById(R.id.home_fragments_holder).requestLayout();
                ((HomeActivity) mBtnBackSec.getContext()).getWindow().getDecorView().findViewById(R.id.home_fragments_holder).forceLayout();

                //Transition all layouts to start
                mSettingsLayout.transitionToStart();
                mSecurityLayout.transitionToStart();
            }
        });
    }

}
