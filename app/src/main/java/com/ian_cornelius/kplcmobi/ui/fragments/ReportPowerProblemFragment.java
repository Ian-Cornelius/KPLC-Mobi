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
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Button;
import android.support.constraint.motion.MotionLayout;
import android.widget.TextSwitcher;

import com.ian_cornelius.kplcmobi.R;


public class ReportPowerProblemFragment extends Fragment {

    /*
    Handling button clicks
     */
    private Button mBtnNext, mBtnSkip, mBtnBack;
    private MotionLayout customProgressBar;

    private TextView mTxtProgressStatus;

    /*
    Keep track of motion states
     */
    private int progressState = 1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View reportPowerProblemView = inflater.inflate(R.layout.report_power_fragment_layout, container, false);

        /*
        Get button references
         */
        mBtnNext = reportPowerProblemView.findViewById(R.id.btnNext);
        mBtnBack = reportPowerProblemView.findViewById(R.id.btnBack);
        mBtnSkip = reportPowerProblemView.findViewById(R.id.btnSkip);

        /*
        Text view reference
         */
        mTxtProgressStatus = reportPowerProblemView.findViewById(R.id.txtCurrentPoint);

        /*
        Motion layout instances
         */
        customProgressBar = reportPowerProblemView.findViewById(R.id.customProgressBarLayout);

        /*
        Set up click listeners
         */
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (progressState == 1){

                    customProgressBar.setTransitionDuration(400);
                    customProgressBar.transitionToState(R.id.state2);
                    killAllButtons();

                    progressState++;
                } else if (progressState == 2){

                    customProgressBar.transitionToState(R.id.state3);
                    killAllButtons();

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
                    progressState--;
                } else if (progressState == 3){

                    customProgressBar.transitionToStart();
                    killAllButtons();
                    progressState--;
                }
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

            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int i) {

                /*
                Re-enable buttons
                 */
                mBtnBack.setEnabled(true);
                mBtnNext.setEnabled(true);
                mBtnSkip.setEnabled(true);

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
