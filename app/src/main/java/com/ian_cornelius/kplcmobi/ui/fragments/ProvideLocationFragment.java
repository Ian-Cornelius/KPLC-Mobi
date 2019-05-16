package com.ian_cornelius.kplcmobi.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.design.widget.TextInputLayout;
import android.widget.ImageView;
import android.support.constraint.motion.MotionLayout;
import android.widget.Button;

import com.ian_cornelius.kplcmobi.R;


public class ProvideLocationFragment extends Fragment {

    /*
    Widget references
     */
    private ImageView mLocationIcon;
    private Button mBtnCancel, mBtnRetry;
    private TextInputLayout mEditLocation;

    /*
    To test our other transitions
     */
    private boolean invokeSecond = false;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View provideLocationView = inflater.inflate(R.layout.provide_location_layout, container, false);

        mLocationIcon = provideLocationView.findViewById(R.id.locationIcon);
        mBtnCancel = provideLocationView.findViewById(R.id.btnCancel);
        mBtnRetry = provideLocationView.findViewById(R.id.btnRetry);
        mEditLocation = provideLocationView.findViewById(R.id.editLocation);

        /*
        btn retry not clickable
         */
        mBtnRetry.setEnabled(false);

        /*
        Test our transitions
         */
        mLocationIcon.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                if (!invokeSecond){

                /*
                Play transition
                 */
                    ((MotionLayout) provideLocationView).transitionToState(R.id.findLocationState2);

                /*
                Kill alphas
                 */
                    ((ReportPowerProblemFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.home_fragments_holder)).raiseAlpha(false);

                /*
                Allow second invokation
                 */
                    invokeSecond = true;

                    mEditLocation.setEnabled(false);

                } else {

                    /*
                    Play second transition
                     */
                    ((MotionLayout) provideLocationView).transitionToState(R.id.findLocationState3);

                    /*
                Raise alphas
                 */
                    ((ReportPowerProblemFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.home_fragments_holder)).raiseAlpha(true);

                    /*
                    btnRetry now clickable
                     */
                    mBtnRetry.setEnabled(true);

                }
            }
        });

        mBtnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                Play transition
                 */
                ((MotionLayout) provideLocationView).transitionToState(R.id.findLocationState1);

                /*
                Raise alphas
                 */
                ((ReportPowerProblemFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.home_fragments_holder)).raiseAlpha(true);

                /*
                Don't invoke second
                 */
                invokeSecond = false;

                mEditLocation.setEnabled(true);
            }
        });

        //Btn retry. Take back to state2
        mBtnRetry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                Play transition
                 */
                ((MotionLayout) provideLocationView).transitionToState(R.id.findLocationState2);

                /*
                Kill alphas
                 */
                ((ReportPowerProblemFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.home_fragments_holder)).raiseAlpha(false);

                /*
                btnRetry no longer clickable
                 */
                mBtnRetry.setEnabled(false);

            }
        });

        return provideLocationView;
    }

    /*
    Methods to handle actual GPS tagging, and appropriate data setting, to be put
     */

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
