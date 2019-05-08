package com.ian_cornelius.kplcmobi.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;

/*
For fab activator
 */
import android.support.constraint.motion.MotionLayout;

/*
For fab content
 */
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.ian_cornelius.kplcmobi.R;


public class CheckAndPayBillFragment extends Fragment {

    /*
    For fab activator
     */
    private MotionLayout mSwitchAccFabActivator, mCheckAndPayBillMainContent;
    private boolean reverse = false;

    /*
    For fab content
     */
    private Animation closeAnim;

    /*
    For checking out
     */
    private Button mBtnPay, mBtnYes, mBtnNo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View checkAndPayBillView = inflater.inflate(R.layout.check_and_pay_bill_fragment_layout,container,false);

        /*
        For our button
         */
        mBtnPay = checkAndPayBillView.findViewById(R.id.btnPay);
        mBtnYes = checkAndPayBillView.findViewById(R.id.btnYes);
        mBtnNo = checkAndPayBillView.findViewById(R.id.btnNo);


        /*
        Get fab activator instance and main content motion layouts
         */
        mSwitchAccFabActivator = checkAndPayBillView.findViewById(R.id.switchAccFabActivator);
        mCheckAndPayBillMainContent = checkAndPayBillView.findViewById(R.id.checkAndPayBillMainContentLayout);

        /*
        For our animation
         */
        closeAnim = AnimationUtils.loadAnimation(getActivity(),R.anim.zoom_out_fab_content);

        closeAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                //remove fab content
                getChildFragmentManager().beginTransaction().remove(getChildFragmentManager().findFragmentById(R.id.fabContentHolder)).commit();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        /*
        Set up its onClick listener and do appropriate actions
         */
        mSwitchAccFabActivator.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick (View v){

                /*
                do appropriate fab action
                 */
                if (!reverse){

                    mSwitchAccFabActivator.transitionToEnd();

                    //Launch the fab content
                    getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.zoom_in_fab_content, R.anim.zoom_out_fab_content).replace(R.id.fabContentHolder, new SwitchAccFabContentFragment()).commit();

                    /*
                    Set alpha down for main content
                     */
                    mCheckAndPayBillMainContent.setAlpha(0.1f);

                    /*
                    Kill button clicks
                     */
                    mBtnPay.setEnabled(false);

                } else{

                    mSwitchAccFabActivator.transitionToStart();
                    checkAndPayBillView.findViewById(R.id.fabContentHolder).startAnimation(closeAnim);

                    /*
                    Set alpha back up for main content
                     */
                    mCheckAndPayBillMainContent.setAlpha(1.0f);

                    /*
                    Enable button clicks
                     */
                    mBtnPay.setEnabled(true);


                }

                reverse = !reverse;
            }


        });


        /*
        Set up button listeners
         */
        mBtnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //do animation
                mBtnPay.setBackground(null);
                mCheckAndPayBillMainContent.transitionToEnd();
                ((MotionLayout) checkAndPayBillView).transitionToEnd();

            }
        });

        mBtnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //do reverse animation
                mBtnPay.setBackgroundResource(R.drawable.green_border_btn_bg);
                mCheckAndPayBillMainContent.transitionToStart();
                ((MotionLayout) checkAndPayBillView).transitionToStart();
            }
        });

        mBtnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(getActivity(),"Mpesa coming soon...", Toast.LENGTH_SHORT).show();
            }
        });

        return checkAndPayBillView;
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
