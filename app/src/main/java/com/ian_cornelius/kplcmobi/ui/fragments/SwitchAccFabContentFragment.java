package com.ian_cornelius.kplcmobi.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.motion.MotionLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ian_cornelius.kplcmobi.R;


public class SwitchAccFabContentFragment extends Fragment {

    /*
   For our animation
    */
    private MotionLayout mSwitchAccFabContentLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View switchAccFabContentView = inflater.inflate(R.layout.switch_acc_fab_content_layout,container,false);

        /*
        Try out our fab animation
         */

        /*
        mSwitchAccFabActivator = switchAccFabView.findViewById(R.id.switchAccFabActivator);

        mSwitchAccFabActivator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!fabReverse){
                    mSwitchAccFabActivator.transitionToEnd();
                } else{
                    mSwitchAccFabActivator.transitionToStart();
                }

                fabReverse = !fabReverse;

            }
        });
        */

        return switchAccFabContentView;
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
