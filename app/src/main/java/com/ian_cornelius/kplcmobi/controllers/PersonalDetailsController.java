package com.ian_cornelius.kplcmobi.controllers;

/*
Control the widgets in personal details layout in settings
 */

import android.os.Bundle;
import android.support.constraint.motion.MotionScene;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.support.constraint.motion.MotionLayout;

import com.ian_cornelius.kplcmobi.R;
import com.ian_cornelius.kplcmobi.ui.fragments.ChangeIdPhoneFragment;
import com.ian_cornelius.kplcmobi.ui.fragments.ChangePasswordFragment;
import com.ian_cornelius.kplcmobi.ui.fragments.SettingsFragment;

public class PersonalDetailsController {

    private SettingsFragment refFrag;

    private MotionLayout mSettingsLayout, mPersonalLayout;

    private TextView mTxtChangeId, mTxtChangePhone;
    private Button mBtnBackPers;

    private boolean reverse = false;

    //help us know we are dealing with which context. 1 for change id, 2 for change phone, 0 for default
    private int clickContext = 0;

    /*
    public constructor
     */
    public PersonalDetailsController(SettingsFragment frag){

        this.refFrag = frag;
    }


    /*
    init views
     */
    public void initViews(View mainView, View rootView){

        mSettingsLayout = (MotionLayout) rootView;
        mPersonalLayout = (MotionLayout) mainView;

        mTxtChangeId = mainView.findViewById(R.id.txtChangeId);
        mTxtChangePhone = mainView.findViewById(R.id.txtChangePhone);
        mBtnBackPers = mainView.findViewById(R.id.btnBackPers);

        setUpViews();
    }


    /*
    set up our views
     */
    private void setUpViews(){

        /*
        Listener for motion layout, to launch frags appropriately
         */
        mPersonalLayout.setTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int i, int i1) {

            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int i, int i1, float v) {

            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int i) {

                /*
                Get to know if reversing, plus context, and perform appropriate action
                 */
                if (!reverse && clickContext == 1){

                    //launch frag to change id num (change hints)

                    //First, set proper bundle arguments
                    Bundle bundle = new Bundle();
                    bundle.putString("context","id");

                    ChangeIdPhoneFragment changeIdPhoneFragment = new ChangeIdPhoneFragment();
                    changeIdPhoneFragment.setArguments(bundle);

                    refFrag.getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.zoom_in_fab_content, R.anim.zoom_out_fab_content).replace(R.id.personalDetailsFrag, changeIdPhoneFragment).commit();

                    //set reverse to true
                    reverse = true;
                } else if (reverse && clickContext == 1){

                    //kill frag for change id num
                    refFrag.getChildFragmentManager().beginTransaction().remove(refFrag.getChildFragmentManager().findFragmentById(R.id.personalDetailsFrag)).commit();

                    //set reverse to false
                    reverse = false;

                    //reset context
                    clickContext = 0;
                } else if (!reverse && clickContext == 2){

                    //launch frag for change phone num

                    //First, set proper bundle arguments
                    Bundle bundle = new Bundle();
                    bundle.putString("context","phone");

                    ChangeIdPhoneFragment changeIdPhoneFragment = new ChangeIdPhoneFragment();
                    changeIdPhoneFragment.setArguments(bundle);

                    refFrag.getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.zoom_in_fab_content, R.anim.zoom_out_fab_content).replace(R.id.personalDetailsFrag, changeIdPhoneFragment).commit();

                    //set reverse to true
                    reverse = true;
                } else if(reverse && clickContext == 2){

                    //kill frag for change phone num
                    refFrag.getChildFragmentManager().beginTransaction().remove(refFrag.getChildFragmentManager().findFragmentById(R.id.personalDetailsFrag)).commit();

                    //set reverse to false
                    reverse = false;

                    //reset context
                    clickContext = 0;
                } else {

                    //Illegal state. Log
                    Log.e("ILLEGAL STATE","PERSONAL DETAILS FRAG");
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

        mTxtChangeId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Invoked only if not reversing. Similar effect to disabling after click. I think less code (logic) intensive
                if (!reverse){

                    //Load appropriate transitions
                    mSettingsLayout.setTransition(R.id.settingsFragmentState0, R.id.settingsFragmentState3);

                    mPersonalLayout.setTransition(R.id.personalState0, R.id.personalState1);

                    //transition to end
                    mSettingsLayout.transitionToEnd();
                    mPersonalLayout.transitionToEnd();

                    //change context
                    clickContext = 1;
                }
            }
        });

        mTxtChangePhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //invoked only if not reversing.
                if (!reverse){

                    //Load appropriate transitions
                    mSettingsLayout.setTransition(R.id.settingsFragmentState0, R.id.settingsFragmentState3);

                    mPersonalLayout.setTransition(R.id.personalState0, R.id.personalState2);

                    //transition to end
                    mSettingsLayout.transitionToEnd();
                    mPersonalLayout.transitionToEnd();

                    //change context
                    clickContext = 2;
                }
            }
        });

        mBtnBackPers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (reverse){

                    //transition to start
                    mSettingsLayout.transitionToStart();
                    mPersonalLayout.transitionToStart();

                    //context changes handled by listener
                }
            }
        });

    }

}
