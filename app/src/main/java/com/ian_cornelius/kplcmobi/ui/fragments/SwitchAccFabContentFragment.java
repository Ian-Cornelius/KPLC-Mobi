package com.ian_cornelius.kplcmobi.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.motion.MotionLayout;
import android.support.constraint.motion.MotionScene;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

//For radio buttons
import android.widget.RadioButton;
import android.widget.Toast;

import com.ian_cornelius.kplcmobi.R;
import com.ian_cornelius.kplcmobi.utils.UserAccountsList;
import com.ian_cornelius.kplcmobi.models.AccountsMiniMetaData;
import com.ian_cornelius.kplcmobi.utils.account_manager.AccountsManager;

import java.util.ArrayList;


public class SwitchAccFabContentFragment extends Fragment implements View.OnClickListener{

    /*
    Help us know the previously checked radio button, avoiding need to loop through all radio buttons, finding which
    one was checked then setting up appropriate motion layout transition
     */
    private RadioButton previouslyCheckedRadioBtn;

    /*
   For our animation
    */
    private MotionLayout mSwitchAccFabContentLayout;

    /*
    For radio buttons
     */
    private RadioButton mRadioBtnAcc1, mRadioBtnAcc2, mRadioBtnAcc3, mRadioBtnAcc4;

    /*
    Hold our accounts list
     */
    private ArrayList<AccountsMiniMetaData> accountsList = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View switchAccFabContentView = inflater.inflate(R.layout.switch_acc_fab_content_layout,container,false);

        /*
        Motion layout instance
         */
        mSwitchAccFabContentLayout = switchAccFabContentView.findViewById(R.id.switchAccFabContent);

        /*
        Get radio button references
         */
        mRadioBtnAcc1 = switchAccFabContentView.findViewById(R.id.radioBtnAcc1);
        mRadioBtnAcc2 = switchAccFabContentView.findViewById(R.id.radioBtnAcc2);
        mRadioBtnAcc3 = switchAccFabContentView.findViewById(R.id.radioBtnAcc3);
        mRadioBtnAcc4 = switchAccFabContentView.findViewById(R.id.radioBtnAcc4);

        /*
        Set up onClick listener for all these radio buttons. Could have used radio button, but its messing up my
        UI and motion layout transitions
         */
        mRadioBtnAcc1.setOnClickListener(this);
        mRadioBtnAcc2.setOnClickListener(this);
        mRadioBtnAcc3.setOnClickListener(this);
        mRadioBtnAcc4.setOnClickListener(this);

        //get the user accounts list. Need to do this asynchronously. Show progress dialog. So, shouldn't be invoked
        //in this thread. Invoke progress dialog. When thread finishes, calls a method in main UI thread to close
        //progress dialog and show populated list

        //Changes. Accounts list process invoked on entrance to any frag that needs it. Singleton maintains state
        //Singleton state only changed by settings, under manage accounts (addition or removal of an account)
        //userAccountsList = UserAccountsList.getInstance();

        accountsList = AccountsManager.getInstance().getAccountsList(this);

        //Set up our texts appropriately
        setUpRadioTexts();

        //set up motion layout listener to avoid motion interference on run, by user
        setUpMotionListener();

        return switchAccFabContentView;
    }

    /*
    Set up texts on the radio and checked status
     */
    private void setUpRadioTexts(){

        //Know our array list size, to avoid IndexOutOfBoundsException
        int listSize;

        //Set maximum bounds limit
        int maxSize = 4;

        //Counter to help us appropriately assign widgets
        int counter = 0;

        /*
        Check if array list is empty (should never be, for a signed in user). If it is, major error
         */
        if (accountsList.size() == 0){

            Log.e("Error","Getting an empty arraylist for accounts");
            Toast.makeText(getActivity(),"We can't get the list of accounts that belong to you. " +
                    "Please check your internet connection",Toast.LENGTH_LONG).show();
        } else{

            //Get our size
            listSize = accountsList.size();

            Log.e("Error","Getting list size as " + listSize);


            /*
            Populate radio text, and checked status. Only one should be checked (since based first on consumed data,
            need to enforce at model
             */
            for (AccountsMiniMetaData metaData: accountsList) {

                //increment counter
                Log.e("Error","Increment counter from " + counter);
                counter++;

                if (counter == 1){

                    //Set up is for radio button 1
                    mRadioBtnAcc1.setText(metaData.getAccountNumber());
                    mRadioBtnAcc1.setChecked(metaData.isCurrent());

                    //If checked, hold our ID for transition purposes
                    if (metaData.isCurrent()){

                        previouslyCheckedRadioBtn = mRadioBtnAcc1;
                    }

                } else if (counter == 2){

                    Log.e("Error","Getting into counter == 2, value of acc no. as " + metaData.getAccountNumber());
                    //Set up is for radio button 2
                    mRadioBtnAcc2.setText(metaData.getAccountNumber());
                    mRadioBtnAcc2.setChecked(metaData.isCurrent());

                    //If checked, hold our ID for transition purposes
                    if (metaData.isCurrent()){

                        previouslyCheckedRadioBtn = mRadioBtnAcc2;

                        /*
                        Put (current) in correct position
                         */
                        //mSwitchAccFabContentLayout.loadLayoutDescription(R.xml.select_2_and_1_scenes);
//                        mSwitchAccFabContentLayout.setTransition(R.id.select2From1Start,R.id.select2From1End);
//                        mSwitchAccFabContentLayout.setProgress(100);

                        mSwitchAccFabContentLayout.transitionToState(R.id.baseState2);

                    }

                } else if (counter == 3){

                    //Set up is for radio button 1
                    mRadioBtnAcc3.setText(metaData.getAccountNumber());
                    mRadioBtnAcc3.setChecked(metaData.isCurrent());

                    //If checked, hold our ID for transition purposes
                    if (metaData.isCurrent()){

                        previouslyCheckedRadioBtn = mRadioBtnAcc3;

                        /*
                        Put (current) in correct position
                         */
//                        mSwitchAccFabContentLayout.setTransition(R.id.select3From1Start,R.id.select3From1End);
//                        mSwitchAccFabContentLayout.setProgress(100);

                        mSwitchAccFabContentLayout.transitionToState(R.id.baseState3);
                    }

                } else if (counter == 4){

                    //Set up is for radio button 1
                    mRadioBtnAcc4.setText(metaData.getAccountNumber());
                    mRadioBtnAcc4.setChecked(metaData.isCurrent());

                    //If checked, hold our ID for transition purposes
                    if (metaData.isCurrent()){

                        previouslyCheckedRadioBtn = mRadioBtnAcc4;
                    }

                } else{

                    //Getting over inflated data. Problem
                    Log.e("Error","Getting inflated accounts metadata");
                }

            }

            /*
            Close the null ones. Model needs to be updated appropriately by settings when acc is added or deleted

            Loop works by looping number of times equal to those radio buttons that have no model instance i.e
            maxSize - counter. If else control structure still used, but with different logic this time
             */
            for (int i = maxSize - counter; i > 0; i--){

                /*
                if else to regulate what values we change

                max(i) == 3. So, if 3 not having values, i = 3 will work to start from 1, which is correct
                 */
                if (maxSize - i == 1){

                    /*
                    Radio button 2 gone, and not set up for clicks
                     */
                    mRadioBtnAcc2.setVisibility(View.INVISIBLE);
                    mRadioBtnAcc2.setEnabled(false);

                } else if(maxSize - i == 2){

                    /*
                    Radio button 3 gone
                     */
                    Log.e("Error","Radio btn 3 should be gone");
                    mRadioBtnAcc3.setVisibility(View.INVISIBLE);
                    mRadioBtnAcc3.setEnabled(false);

                } else if(maxSize - i == 3){

                    /*
                    Radio button 4 gone
                     */
                    mRadioBtnAcc4.setVisibility(View.INVISIBLE);
                    mRadioBtnAcc4.setEnabled(false);
                } else {

                    /*
                    Unexpected condition
                     */
                    Log.e("Error","Unexpected loop overflow condition. Possible accountsList model problem");
                }
            }

        }
    }

    private void setUpMotionListener(){

        mSwitchAccFabContentLayout.setTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int i, int i1) {

                toggleClick(false);
            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int i, int i1, float v) {

            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int i) {

                toggleClick(true);
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int i, boolean b, float v) {

            }

            @Override
            public boolean allowsTransition(MotionScene.Transition transition) {
                return false;
            }
        });
    }


    @Override
    public void onClick(View v){

        /*
        Handle the logic of switching the radio button's checked status, update in accountsList and do appropriate transition
         */

        //Directly avoid doing anything for redundant selection. If null pointer thrown, then problem with model
        if (v.getId() == previouslyCheckedRadioBtn.getId()){

            //Show toast, that this is current selection
            Toast.makeText(getActivity(),"This is your current account selection.",Toast.LENGTH_SHORT).show();
        } else{


            /*
            Use switch case. No need to automatically close fragment
             */
            switch (v.getId()){


                case R.id.radioBtnAcc1:

                    Log.e("Error","Get into this radioBtn1click");
                    /*
                    Set the checked radio button as new checked
                     */
                    mRadioBtnAcc1.setChecked(true);

                    /*
                    Remove checked from the previous button
                     */
                    Log.e("Error","Previously checked should be instance of radio 2" + String.valueOf(previouslyCheckedRadioBtn == mRadioBtnAcc2));
                    previouslyCheckedRadioBtn.setChecked(false);

                    //Transition to appropriate state
                    mSwitchAccFabContentLayout.transitionToState(R.id.baseState1);

                    /*
                    Now set the previously checked button to this one
                     */
                    previouslyCheckedRadioBtn = mRadioBtnAcc1;

                    //TODO Request model update here, at accounts manager, b4 changing prev checked btn. Can do after. Just change index
                    AccountsManager.getInstance().onAccountSwitch(0);

                    break;

                case R.id.radioBtnAcc2:

                    mRadioBtnAcc2.setChecked(true);

                    previouslyCheckedRadioBtn.setChecked(false);

                    //Transition to appropriate state
                    mSwitchAccFabContentLayout.transitionToState(R.id.baseState2);

                    /*
                    Now set the previously checked button to this one
                     */
                    previouslyCheckedRadioBtn = mRadioBtnAcc2;

                    AccountsManager.getInstance().onAccountSwitch(1);

                    break;

                case R.id.radioBtnAcc3:

                    mRadioBtnAcc3.setChecked(true);

                    previouslyCheckedRadioBtn.setChecked(false);

                    //Transition to appropriate state
                    mSwitchAccFabContentLayout.transitionToState(R.id.baseState3);

                    /*
                    Now set the previously checked button to this one
                     */
                    previouslyCheckedRadioBtn = mRadioBtnAcc3;

                    AccountsManager.getInstance().onAccountSwitch(2);

                    break;

                case R.id.radioBtnAcc4:

                    mRadioBtnAcc4.setChecked(true);

                    previouslyCheckedRadioBtn.setChecked(false);

                    //Transition to appropriate state
                    mSwitchAccFabContentLayout.transitionToState(R.id.baseState4);

                    /*
                    Now set previously checked button to this one
                     */
                    previouslyCheckedRadioBtn = mRadioBtnAcc4;

                    AccountsManager.getInstance().onAccountSwitch(3);

                    break;

                default:


            }
        }
    }

    /*
    No method needed to tell of changes in list, since cannot be changed dynamically
     */

    private void toggleClick(boolean enable){

        mRadioBtnAcc1.setEnabled(enable);
        mRadioBtnAcc2.setEnabled(enable);
        mRadioBtnAcc3.setEnabled(enable);
        mRadioBtnAcc4.setEnabled(enable);
    }


    //TODO Method to update current selection index, since using same array list, just rad btn num - 1. Check interface below. Update manager, for it to update locally and also post to db, then update ref activity, which locks itself to switch account when fired, removes lock when stopped


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

        //Tell accounts manager to update changes. If failure, okay. Doing here to avoid excess network requests
        AccountsManager.getInstance().updateCurrentSelection();

    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnAccountSwitchListener {
        // TODO: Use this interface to tell when current account has been switched
        void onAccountSwitch(int newIndex);
    }

    //TODO: This alpha-5 version of motion layout is seriously buggy. Need a way to deal with the current animation
    //TODO: If all fails, use set progress hack. Or keyframe hack
    //TODO: Okay, this buggy boy worked, after beta-1 release
}
