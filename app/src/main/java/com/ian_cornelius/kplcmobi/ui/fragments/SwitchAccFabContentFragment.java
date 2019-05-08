package com.ian_cornelius.kplcmobi.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.motion.MotionLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

//For radio buttons
import android.widget.RadioButton;
import android.widget.Toast;

import com.ian_cornelius.kplcmobi.R;
import com.ian_cornelius.kplcmobi.utils.UserAccountsList;
import com.ian_cornelius.kplcmobi.models.AccountsMiniMetaData;


public class SwitchAccFabContentFragment extends Fragment implements View.OnClickListener{

    /*
    Help us access the user accounts list
     */
    private UserAccountsList userAccountsList;

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
        userAccountsList = UserAccountsList.getInstance();

        //Set up our texts appropriately
        setUpRadioTexts();

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
        if (userAccountsList.accountsMiniMetaDataArrayList == null){

            Log.e("Error","Getting an empty arraylist for accounts");
            Toast.makeText(getActivity(),"We can't get the list of accounts that belong to you. " +
                    "Please check your internet connection",Toast.LENGTH_LONG).show();
        } else{

            //Get our size
            listSize = userAccountsList.accountsMiniMetaDataArrayList.size();

            Log.e("Error","Getting list size as " + listSize);


            /*
            Populate radio text, and checked status. Only one should be checked (since based first on consumed data,
            need to enforce at model
             */
            for (AccountsMiniMetaData metaData: userAccountsList.accountsMiniMetaDataArrayList) {

                //increment counter
                Log.e("Error","Increment counter from " + counter);
                counter++;

                if (counter == 1){

                    //Set up is for radio button 1
                    mRadioBtnAcc1.setText(metaData.getAccountNumber());
                    mRadioBtnAcc1.setChecked(metaData.isActive());

                    //If checked, hold our ID for transition purposes
                    if (metaData.isActive()){

                        previouslyCheckedRadioBtn = mRadioBtnAcc1;
                    }

                } else if (counter == 2){

                    Log.e("Error","Getting into counter == 2, value of acc no. as " + metaData.getAccountNumber());
                    //Set up is for radio button 2
                    mRadioBtnAcc2.setText(metaData.getAccountNumber());
                    mRadioBtnAcc2.setChecked(metaData.isActive());

                    //If checked, hold our ID for transition purposes
                    if (metaData.isActive()){

                        previouslyCheckedRadioBtn = mRadioBtnAcc2;

                        /*
                        Put (current) in correct position
                         */
                        //mSwitchAccFabContentLayout.loadLayoutDescription(R.xml.select_2_and_1_scenes);
                        mSwitchAccFabContentLayout.setTransition(R.id.select2From1Start,R.id.select2From1End);
                        mSwitchAccFabContentLayout.setProgress(100);
                    }

                } else if (counter == 3){

                    //Set up is for radio button 1
                    mRadioBtnAcc3.setText(metaData.getAccountNumber());
                    mRadioBtnAcc3.setChecked(metaData.isActive());

                    //If checked, hold our ID for transition purposes
                    if (metaData.isActive()){

                        previouslyCheckedRadioBtn = mRadioBtnAcc3;

                        /*
                        Put (current) in correct position
                         */
                        mSwitchAccFabContentLayout.setTransition(R.id.select3From1Start,R.id.select3From1End);
                        mSwitchAccFabContentLayout.setProgress(100);
                    }

                } else if (counter == 4){

                    //Set up is for radio button 1
                    mRadioBtnAcc4.setText(metaData.getAccountNumber());
                    mRadioBtnAcc4.setChecked(metaData.isActive());

                    //If checked, hold our ID for transition purposes
                    if (metaData.isActive()){

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
                    Radio button 2 gone
                     */
                    mRadioBtnAcc2.setVisibility(View.INVISIBLE);

                } else if(maxSize - i == 2){

                    /*
                    Radio button 3 gone
                     */
                    Log.e("Error","Radio btn 3 should be gone");
                    mRadioBtnAcc3.setVisibility(View.INVISIBLE);

                } else if(maxSize - i == 3){

                    /*
                    Radio button 4 gone
                     */
                    mRadioBtnAcc4.setVisibility(View.INVISIBLE);
                } else {

                    /*
                    Unexpected condition
                     */
                    Log.e("Error","Unexpected loop overflow condition. Possible accountsList model problem");
                }
            }

        }
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

                    /*
                    Do appropriate transition. We know the currently checked button. Use the previously checked
                    button for final determination
                     */
                    if (previouslyCheckedRadioBtn.getId() == R.id.radioBtnAcc2){

//                        playCurrentAnimation(R.xml.select_2_and_1_scenes, true);
                        mSwitchAccFabContentLayout.setTransition(R.id.select1From2Start,R.id.select1From2End);
                        mSwitchAccFabContentLayout.transitionToEnd();
                    } else if (previouslyCheckedRadioBtn.getId() == R.id.radioBtnAcc3){

                        //playCurrentAnimation(R.xml.select_3_and_1_scenes,true);
                    }

                    /*
                    Now set the previously checked button to this one
                     */
                    previouslyCheckedRadioBtn = mRadioBtnAcc1;

                    break;

                case R.id.radioBtnAcc2:

                    mRadioBtnAcc2.setChecked(true);

                    previouslyCheckedRadioBtn.setChecked(false);

                    if(previouslyCheckedRadioBtn.getId() == R.id.radioBtnAcc1){

//                        playCurrentAnimation(R.xml.select_2_and_1_scenes, false);
                        mSwitchAccFabContentLayout.setTransition(R.id.select2From1Start,R.id.select2From1End);
                        mSwitchAccFabContentLayout.transitionToEnd();
                    }

                    /*
                    Now set the previously checked button to this one
                     */
                    previouslyCheckedRadioBtn = mRadioBtnAcc2;

                    break;

                case R.id.radioBtnAcc3:

                    mRadioBtnAcc3.setChecked(true);

                    previouslyCheckedRadioBtn.setChecked(false);

                    if(previouslyCheckedRadioBtn.getId() == R.id.radioBtnAcc1){

//                        playCurrentAnimation(R.xml.select_3_and_1_scenes, false);
                        mSwitchAccFabContentLayout.setTransition(R.id.select3From1Start,R.id.select3From1End);
                        mSwitchAccFabContentLayout.transitionToEnd();
                    }

                    /*
                    Now set the previously checked button to this one
                     */
                    previouslyCheckedRadioBtn = mRadioBtnAcc3;

                    break;

                default:


            }
        }
    }

    private void playCurrentAnimation(int motionScene, boolean reverse){

        mSwitchAccFabContentLayout.loadLayoutDescription(motionScene);
        mSwitchAccFabContentLayout.setTransitionDuration(600);
        Log.e("Error","Set transition duration " + mSwitchAccFabContentLayout.getTransitionTimeMs());
        if (reverse){

            mSwitchAccFabContentLayout.setTransitionDuration(600);
            Log.e("Error","Invoked anim reverse");
            mSwitchAccFabContentLayout.transitionToStart();
        } else{

            Log.e("Error","Invoked anim forward");
            mSwitchAccFabContentLayout.transitionToEnd();
        }
//        mSwitchAccFabContentLayout.setTransition(beginId, endId);
//        mSwitchAccFabContentLayout.transitionToEnd();

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    //TODO: This alpha-5 version of motion layout is seriously buggy. Need a way to deal with the current animation
    //TODO: If all fails, use set progress hack. Or keyframe hack
}
