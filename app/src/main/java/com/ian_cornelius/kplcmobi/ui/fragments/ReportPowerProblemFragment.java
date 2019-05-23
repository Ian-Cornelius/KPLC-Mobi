package com.ian_cornelius.kplcmobi.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.motion.MotionScene;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.support.constraint.motion.MotionLayout;
import android.widget.Toast;

import com.ian_cornelius.kplcmobi.R;
import com.ian_cornelius.kplcmobi.ui.home.HomeActivity;


public class ReportPowerProblemFragment extends Fragment implements HomeActivity.FabButtonToggle {

    /*
    Handling button clicks
     */
    private Button mBtnNext, mBtnSkip, mBtnBack,mBtnSendReport;
    private MotionLayout customProgressBar;

    /*
    Keep track of motion states
     */
    private int progressState = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View reportPowerProblemView = inflater.inflate(R.layout.report_power_fragment_layout, container, false);

        /*
        Get button references
         */
        mBtnNext = reportPowerProblemView.findViewById(R.id.btnNext);
        mBtnBack = reportPowerProblemView.findViewById(R.id.btnBack);
        mBtnSkip = reportPowerProblemView.findViewById(R.id.btnSkip);
        mBtnSendReport = reportPowerProblemView.findViewById(R.id.btnSendReport);

        /*
        Motion layout instances
         */
        customProgressBar = reportPowerProblemView.findViewById(R.id.customProgressBarLayout);

        /*
        Starting at state 1. btnBack, btnSkip and btnSendReport should be disabled
         */
        mBtnBack.setEnabled(false);
        mBtnSkip.setEnabled(false);
        mBtnSendReport.setEnabled(false);

        /*
        Set up click listeners
         */
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (progressState == 1){

                    /*
                    Check that data has been keyed in. Then perform appropriate transitions
                     */
                    if (((DescribePowerProblemFragment)getChildFragmentManager().findFragmentById(R.id.reportFragsHolder)).isDataKeyed()){

                        //flush data

                        //do transitions
                        //customProgressBar.setTransitionDuration(400);
                        //customProgressBar.transitionToState(R.id.state2);

                        /*
                        Motion layout motion layout. Weird bugs. Transition to state ignoring listeners. Must manually
                        set transition at this point, cause it is disabling my buttons
                         */
                        customProgressBar.setTransition(R.id.progressBarState1, R.id.progressBarState2);
                        customProgressBar.transitionToEnd();

                        /*
                        Kill btnNext alone, since it was the only one active at state 1
                         */
                        mBtnNext.setEnabled(false);

                        /*
                        Animate alphas of btnBack and btnSkip, using motion layout for fragment
                         */
                        ((MotionLayout) reportPowerProblemView).setTransition(R.id.reportFragmentScene1Start, R.id.reportFragmentScene1End);
                        ((MotionLayout) reportPowerProblemView).transitionToEnd();


                        //Weird. Super weird bug
                        //((DescribePowerProblemFragment) getChildFragmentManager().findFragmentById(R.id.reportFragsHolder)).flushData();

                        /*
                        Once again, motion layout. I have to refresh views, for new frag to be drawn. Motion layout sort of blocking
                        that.
                         */
                        getActivity().getWindow().getDecorView().findViewById(R.id.home_fragments_holder).invalidate();
                        getActivity().getWindow().getDecorView().findViewById(R.id.home_fragments_holder).requestLayout();
                        getActivity().getWindow().getDecorView().findViewById(R.id.home_fragments_holder).forceLayout();

                        //Switch frags
                        /*
                        Funny, but today is when I get to know what these arguments are for.
                        First anim is how we are going to animate the fragment coming in. Second for the fragment going out/being
                        removed. Jeeze!
                         */
                        getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.slide_out).replace(R.id.reportFragsHolder, new ProvideLocationFragment()).commit();

                        //((HomeActivity) getActivity()).toggleAlpha(0.5f);

                        progressState++;

                        Log.e("HANG", "STUCK AT TRANSITION TO LOCATION");
                    } else {

                        Toast.makeText(getActivity(), "Please describe your problem", Toast.LENGTH_SHORT).show();
                    }

                } else if (progressState == 2){

                    customProgressBar.transitionToState(R.id.state3);
                    killAllButtons();

                    /*
                    Animate alphas of btnBack, btnSkip and btnSendReport, using motion layout for fragment
                     */
                    ((MotionLayout) reportPowerProblemView).setTransition(R.id.reportFragmentScene2Start, R.id.reportFragmentScene2End);
                    ((MotionLayout) reportPowerProblemView).transitionToEnd();

                    getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.slide_out).replace(R.id.reportFragsHolder, new FinishReportFragment()).commit();

                    progressState++;
                }
            }
        });

        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (progressState == 2){

                    customProgressBar.setTransitionDuration(400);
                    customProgressBar.transitionToState(R.id.state1);
                    killAllButtons();

                    /*
                    Animate alphas of btnBack and btnSkip, using motion layout for fragment

                    Transition set incase it was a sequential back press
                     */
                    ((MotionLayout) reportPowerProblemView).setTransition(R.id.reportFragmentScene1End, R.id.reportFragmentScene1Start);
                    ((MotionLayout) reportPowerProblemView).transitionToEnd();

                    //Switch frags
                    getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right).replace(R.id.reportFragsHolder, new DescribePowerProblemFragment()).commit();
                    progressState--;
                } else if (progressState == 3){

                    /*
                    Animate alphas of btnBack and btnSkip, using motion layout for fragment
                    Transition already set when moving from 2 to 3
                     */
                    ((MotionLayout) reportPowerProblemView).transitionToStart();

                    customProgressBar.transitionToStart();
                    killAllButtons();

                    //Switch frags
                    getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_right).replace(R.id.reportFragsHolder, new ProvideLocationFragment()).commit();
                    progressState--;
                }
            }
        });

        mBtnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                Jump from frag location to send report
                 */
                customProgressBar.transitionToState(R.id.state3);
                killAllButtons();

                    /*
                    Animate alphas of btnBack, btnSkip and btnSendReport, using motion layout for fragment
                     */
                ((MotionLayout) reportPowerProblemView).setTransition(R.id.reportFragmentScene2Start, R.id.reportFragmentScene2End);
                ((MotionLayout) reportPowerProblemView).transitionToEnd();

                getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in, R.anim.slide_out).replace(R.id.reportFragsHolder, new FinishReportFragment()).commit();

                progressState++;
            }
        });

        mBtnSendReport.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick (View v){

                /*
                Send full report
                 */
            }
        });

        /*
        Can mess up animation if quickly transit from one state to another. Disable buttons till animation completes
         */
        customProgressBar.setTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int i, int i1) {

            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int i, int i1, float v) {

                //pleaseListen(); It did boy. It did. Thanks!
            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int i) {

                /*
                Re-enable buttons, based on current progress state. If state 1, only enable btnNext,
                because we are coming from state 2. If progress state is 3, only enable back
                 */
                if (progressState == 1){

                    mBtnNext.setEnabled(true);
                } else if (progressState == 3){

                    mBtnBack.setEnabled(true);

                    /*
                    Enable btnSendReport
                     */
                    mBtnSendReport.setEnabled(true);
                } else{

                    //progress state is 2
                    mBtnBack.setEnabled(true);
                    mBtnNext.setEnabled(true);
                    mBtnSkip.setEnabled(true);

                    mBtnSendReport.setEnabled(false); //In case coming from 3 to 2

                    //bug fixing. I honestly don't know what's up. Internal motion layout beta issues

                    //((MotionLayout) reportPowerProblemView).transitionToEnd();
                    //((HomeActivity) getActivity()).toggleAlpha(1.0f);
                    Log.e("HOME CALL", "INVOKED");
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

        /*
        Load our first child frag
         */
        getChildFragmentManager().beginTransaction().replace(R.id.reportFragsHolder, new DescribePowerProblemFragment()).commit();

        return reportPowerProblemView;
    }

    private void killAllButtons(){


                /*
                Kill all buttons
                 */
        mBtnBack.setEnabled(false);
        mBtnNext.setEnabled(false);
        mBtnSkip.setEnabled(false);

        /*
        Just had to do this... DEAD!😂
         */
    }


    /*
    Overridden method to toggle buttons here. Will not "kill" the other one cause I love the comment right under it...for now
     */
    @Override
    public void toggleButtons(boolean enable){

        mBtnBack.setEnabled(enable);
        mBtnNext.setEnabled(enable);
        mBtnSkip.setEnabled(enable);
    }

    /*
    Use this to reduce alpha when find location icon is clicked
     */
    public void raiseAlpha(boolean raiseAlpha){

        if (raiseAlpha){

            /*
            reenable button clicks
             */
            mBtnNext.setEnabled(true);
            mBtnBack.setEnabled(true);
            mBtnSkip.setEnabled(true);

            ((MotionLayout)mBtnNext.getParent()).transitionToState(R.id.reportState2);
            customProgressBar.setAlpha(1.0f);
        } else {

            /*
            disable button clicks
             */
            mBtnNext.setEnabled(false);
            mBtnBack.setEnabled(false);
            mBtnSkip.setEnabled(false);

            ((MotionLayout)mBtnNext.getParent()).transitionToState(R.id.reportIntermediate);
            customProgressBar.setAlpha(0.1f);
        }

    }

    /*
    Just to solve a bug ey...
     */
    private void pleaseListen(){

        Log.e("PLEASE LISTEN","I BET I WILL NOW");
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
}
