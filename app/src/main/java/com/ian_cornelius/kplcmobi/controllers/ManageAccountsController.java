package com.ian_cornelius.kplcmobi.controllers;

/*
Class to control interactions with widgets in manage accounts layout in settings fragment
 */

import android.support.constraint.motion.MotionScene;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.support.constraint.motion.MotionLayout;
import android.widget.Toast;

import com.ian_cornelius.kplcmobi.R;
import com.ian_cornelius.kplcmobi.ui.fragments.AccountDetailsFragment;
import com.ian_cornelius.kplcmobi.ui.fragments.AddAccountFragment;
import com.ian_cornelius.kplcmobi.ui.fragments.SettingsFragment;

public class ManageAccountsController {

    private SettingsFragment refFrag;

    private TextView mTxtMainAcc, mTxtAcc2, mTxtAcc3, mTxtAcc4;

    private MotionLayout mSettingsLayout, mManageAccLayout;

    private Button mBtnBack, mBtnAddAcc;

    //reverse help us know if not in default transition state
    private boolean reverse = false;

    //Tell us context of manage acc. View acc details = 1, remove account = 2, default = 0
    private int manageContext = 0;

    /*
    public constructor
     */
    public ManageAccountsController(SettingsFragment frag){

        this.refFrag = frag;
    }

    /*
    Method to get widgets, then will set them up
     */
    public void initViews(View mainView, View rootView){

        mSettingsLayout = (MotionLayout) rootView;

        mManageAccLayout = (MotionLayout) mainView;

        mBtnBack = mainView.findViewById(R.id.btnBack);
        mBtnAddAcc = mainView.findViewById(R.id.btnAddAcc);

        mTxtMainAcc = mainView.findViewById(R.id.txtMainAcc);
        mTxtAcc2 = mainView.findViewById(R.id.txtAcc2);
        mTxtAcc3 = mainView.findViewById(R.id.txtAcc3);
        mTxtAcc4 = mainView.findViewById(R.id.txtAcc4);

        setUpViews();
    }

    private void setUpViews(){

        mManageAccLayout.setTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int i, int i1) {

            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int i, int i1, float v) {

                //Log.e("TRANSITION CHANGE","INVOKED - AND SEVERAL TIMES FOR THAT MATTER, IF ANIM EASY FOR IT");
                //getChildFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).replace(R.id.manageAccFragHolder, new AccountDetailsFragment()).commit();
                //solveBug();
            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int i) {

                if (!reverse && manageContext == 1){

                    /*
                    Not adding account. So, account details need to be shown....work with int contexts. More understandable - done that
                     */

                    mBtnAddAcc.setText("Remove account");
                    mBtnAddAcc.setTextColor(mSettingsLayout.getContext().getResources().getColor(R.color.custom_red));
                    refFrag.getChildFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).replace(R.id.manageAccFragHolder, new AccountDetailsFragment()).commit();

                    mBtnBack.setEnabled(true);
                    mBtnAddAcc.setEnabled(true);

                } else if (reverse && manageContext == 1){

                    /*
                    reversing from viewing account details, but not adding
                     */

                    mBtnAddAcc.setText(R.string.add_acc);
                    mBtnAddAcc.setTextColor(mSettingsLayout.getContext().getResources().getColor(R.color.custom_grey));
                    refFrag.getChildFragmentManager().beginTransaction().remove(refFrag.getChildFragmentManager().findFragmentById(R.id.manageAccFragHolder)).commit();

                    //reset context, since we are back to default state
                    manageContext = 0;

                } else if (!reverse && manageContext == 2){

                    /*
                    We are adding account
                     */
                    //Launch appropriate frag
                    refFrag.getChildFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out).replace(R.id.manageAccFragHolder, new AddAccountFragment()).commit();

                    mBtnAddAcc.setEnabled(true);
                    mBtnBack.setEnabled(true);

                } else if (reverse && manageContext == 2){

                    /*
                    Reversing from adding account
                     */
                    //remove frag there before
                    refFrag.getChildFragmentManager().beginTransaction().remove(refFrag.getChildFragmentManager().findFragmentById(R.id.manageAccFragHolder)).commit();

                    Log.e("ADD ACCOUNT","FRAG REMOVED");
                    manageContext = 0;
                } else {}

                reverse = !reverse;
                Log.e("LISTENER STATUS","INVOKED");
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int i, boolean b, float v) {

            }

            @Override
            public boolean allowsTransition(MotionScene.Transition transition) {
                return false;
            }
        });

        mTxtMainAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!reverse){

                    //Set up appropriate root transitions
                    mSettingsLayout.setTransition(R.id.settingsFragmentState0, R.id.settingsFragmentState1);

                    mManageAccLayout.setTransition(R.id.manageAccState0, R.id.manageAccState1);
                    mManageAccLayout.transitionToEnd();

                    mSettingsLayout.transitionToEnd();

                    //Put up contexts at launch. Reset at close
                    manageContext = 1;

                    //avoid fragment launch confusion, by simultaneous random user input
                    //No need to do this for mBtnBack, since its defaulted to not enabled by every transition.
                    //So, we only set it as explicitly enabled, when opening details frag (not reversing), and
                    //disabled when doing reverse, to avoid interruptions before constraint sets are fully effected
                    mBtnAddAcc.setEnabled(false);
                }

            }
        });

        mTxtAcc2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                Only trigger if not reversing
                 */
                if (!reverse){

                    //Set up appropriate root transitions
                    mSettingsLayout.setTransition(R.id.settingsFragmentState0, R.id.settingsFragmentState1);

                    mManageAccLayout.setTransition(R.id.manageAccState0, R.id.manageAccState2);
                    mManageAccLayout.transitionToEnd();

                    mSettingsLayout.transitionToEnd();

                    manageContext = 1;

                    mBtnAddAcc.setEnabled(false);

                }

            }
        });

        mTxtAcc3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!reverse){

                    //Set up appropriate root transitions
                    mSettingsLayout.setTransition(R.id.settingsFragmentState0, R.id.settingsFragmentState1);

                    mManageAccLayout.setTransition(R.id.manageAccState0, R.id.manageAccState3);
                    mManageAccLayout.transitionToEnd();

                    mSettingsLayout.transitionToEnd();

                    manageContext = 1;

                    mBtnAddAcc.setEnabled(false);
                }
            }
        });

        mTxtAcc4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!reverse){

                    //Set up appropriate root transitions
                    mSettingsLayout.setTransition(R.id.settingsFragmentState0, R.id.settingsFragmentState1);

                    mManageAccLayout.setTransition(R.id.manageAccState0, R.id.manageAccState4);
                    mManageAccLayout.transitionToEnd();

                    mSettingsLayout.transitionToEnd();

                    manageContext = 1;

                    mBtnAddAcc.setEnabled(false);
                }
            }
        });

        mBtnAddAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Case 1 - called ONLY to add account (thus context for view acc details was never set)

                if (manageContext == 0 && !reverse){

                    //Set up appropriate root transitions
                    mSettingsLayout.setTransition(R.id.settingsFragmentState0, R.id.settingsFragmentState1);

                    mManageAccLayout.setTransition(R.id.manageAccState0, R.id.manageAccState5);
                    mManageAccLayout.transitionToEnd();

                    mSettingsLayout.transitionToEnd();

                    //Avoid repetitive calls
                    mBtnAddAcc.setEnabled(false);

                    //Set context
                    manageContext = 2;

                } else if (manageContext == 1 && reverse){

                    //Context and reverse == true tells us we were viewing account details, so, this button should now remove the account
                    Toast.makeText(mSettingsLayout.getContext(), "Remove account invoked", Toast.LENGTH_SHORT).show();

                } else if (manageContext == 2 && reverse){

                    //Now adding the actual account. Ensure data has been keyed in
                    Toast.makeText(mSettingsLayout.getContext(), "Adding actual account", Toast.LENGTH_SHORT).show();

                } else {

                    //Illegal scenario. Appearance of this in log should indicate run-time error
                    Log.e("ILLEGAL STATE", "manageContext zero and reverse is true");

                }
            }
        });

        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                Only trigger if reversing
                 */
                if (reverse && (manageContext == 1 || manageContext == 2)){

                    /*
                    Reversing from viewing account, or adding account. All transitions valid as start state is default for both
                     */
                    mManageAccLayout.transitionToStart();
                    mSettingsLayout.transitionToStart();

                    //Disable here. So that once anim starts, can't interrupt
                    mBtnBack.setEnabled(false);

                }
            }
        });
    }
}
