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

    /*
    Radio button instances
     */
    private AppCompatRadioButton mRadioBlackout, mRadioBlown, mRadioOther;
    private Button mBtnClose;

    private EditText mEditDesc;

    /*
    For live character count
     */
    private TextView mTxtCharCount;
    private int charCount = 0;
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
        mRadioOther.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v){

                ((MotionLayout) descProbView).transitionToEnd();
                mEditDesc.setEnabled(true);
            }
        });

        mBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((MotionLayout) descProbView).transitionToStart();
                mEditDesc.setEnabled(false);
                mRadioOther.setChecked(false);
            }
        });

        return descProbView;
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
