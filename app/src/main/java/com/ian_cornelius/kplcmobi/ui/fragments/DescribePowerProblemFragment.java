package com.ian_cornelius.kplcmobi.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v7.widget.AppCompatRadioButton;
import android.widget.Button;
import android.support.constraint.motion.MotionLayout;
import android.widget.EditText;

/*
Live character count
 */
import android.text.TextWatcher;

import com.ian_cornelius.kplcmobi.R;


public class DescribePowerProblemFragment extends Fragment {

    private boolean isFlushing = false;

    /*
    Radio button instances
     */
    private AppCompatRadioButton mRadioBlackout, mRadioBlown, mRadioOther;
    private Button mBtnClose;

    private EditText mEditDesc;

    /*
    For live character count, and determination of whether we have content keyed/selected in here or not
     */
    private TextView mTxtCharCount;
    private int charCount = 0;
    private boolean isRadioSelected = false;

    private final TextWatcher characterCounter = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            /*
            Set our text view to current length
             */
            mTxtCharCount.setText(String.valueOf(s.length()) + "/200");
            charCount = s.length();

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View descProbView = inflater.inflate(R.layout.describe_problem_layout, container, false);

        /*
        Get widget references
         */
        mRadioBlackout = descProbView.findViewById(R.id.radioBlackout);
        mRadioBlown = descProbView.findViewById(R.id.radioBlownTransformer);
        mRadioOther = descProbView.findViewById(R.id.radioOther);

        mBtnClose = descProbView.findViewById(R.id.btnClose);

        mEditDesc = descProbView.findViewById(R.id.editDescription);
        mTxtCharCount = descProbView.findViewById(R.id.txtCharCount);

        mEditDesc.addTextChangedListener(characterCounter);
        mEditDesc.setEnabled(false);

        /*
        Set up on click listeners
         */
        mRadioBlackout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                If radio blown transformer was previously checked, remove check.

                We do not remove for radio other because it is automatically removed when the edit text is closed
                 */
                if (mRadioBlown.isChecked()){

                    mRadioBlown.setChecked(false);
                }

                //Set our isRadioSelected var to true
                isRadioSelected = true;
            }
        });

        mRadioBlown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.e("Error","Radio blown clicked");
                /*
                If radio blackout was checked, remove check
                 */
                if (mRadioBlackout.isChecked()){

                    mRadioBlackout.setChecked(false);
                }

                isRadioSelected = true;
            }
        });

        mRadioOther.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                /*
                if blackout or blown transformer was previously checked, remove check
                 */
                if (mRadioBlackout.isChecked()){

                    mRadioBlackout.setChecked(false);
                }
                if (mRadioBlown.isChecked()){

                    mRadioBlown.setChecked(false);
                }


                /*
                Transition to our edit text
                 */

                ((MotionLayout) descProbView).transitionToEnd();
                mEditDesc.setEnabled(true);

                /*
                We don't bother with the value of isRadioSelected here. Because, we need actual text to be
                keyed in. So, we work with charCount var instead. Updated by textwatcher.

                Nope. Created a loop hole. Select other, then this thing progresses
                 */
                isRadioSelected = false;
            }
        });

        mBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                Transition off our edit text
                 */
                ((MotionLayout) descProbView).transitionToStart();

                /*
                Disable the edit text, and remove check on other. Also, clear edit text text
                 */
                mEditDesc.setText("");
                mEditDesc.setEnabled(false);
                mRadioOther.setChecked(false);

                /*
                Reset char count var. Yes.

                Also, since all radios now not selected, reset isRadioSelected
                 */
                charCount = 0;
                isRadioSelected = false;
            }
        });

        return descProbView;
    }

    /*
    Method to help us validate there are entries keyed in, to allow us to flush them to
    model then proceed.
     */
    public boolean isDataKeyed(){

        return isRadioSelected || (charCount > 0);
    }

    /*
    This method flushes the keyed in data to the report power outage model. Takes the model instance created by parent
    frag as argument
     */
    public void flushData(){

        /*
        Save our data in model
         */
//        mEditDesc.setText(null);
//        ((MotionLayout) mRadioBlown.getParent()).transitionToStart();
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
