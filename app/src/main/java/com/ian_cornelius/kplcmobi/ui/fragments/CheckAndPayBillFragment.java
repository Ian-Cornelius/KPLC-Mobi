package com.ian_cornelius.kplcmobi.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
import com.ian_cornelius.kplcmobi.ui.home.HomeActivity;


public class CheckAndPayBillFragment extends Fragment implements HomeActivity.FabButtonToggle {

    /*
    For fab activator
     */
    private MotionLayout mCheckAndPayBillMainContent;

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

        View checkAndPayBillView = inflater.inflate(R.layout.check_and_pay_bill_fragment_layout,container,false);

        /*
        For our button
         */
        mBtnPay = checkAndPayBillView.findViewById(R.id.btnPay);
        mBtnYes = checkAndPayBillView.findViewById(R.id.btnYes);
        mBtnNo = checkAndPayBillView.findViewById(R.id.btnNo);


        mCheckAndPayBillMainContent = checkAndPayBillView.findViewById(R.id.checkAndPayBillMainContentLayout);

        /*
        Set up button listeners
         */
        mBtnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //do animation
                mBtnPay.setBackground(null);
                mCheckAndPayBillMainContent.transitionToEnd();

                //hide fab from view
                ((HomeActivity)getActivity()).toggleFab(false, false);

            }
        });

        mBtnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //do reverse animation
                mBtnPay.setBackgroundResource(R.drawable.green_border_btn_bg);
                mCheckAndPayBillMainContent.transitionToStart();

                //Show fab
                ((HomeActivity) getActivity()).toggleFab(true, false);
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

    //method for us to communicate between activity and fragment, concerning fab and btn activations
    @Override
    public void toggleButtons(boolean enable){

        mBtnPay.setEnabled(enable);

        Log.e("TOGGLE AT POST PAID","INVOKED");
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
