package com.ian_cornelius.kplcmobi.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.ian_cornelius.kplcmobi.R;


public class ChangeIdPhoneFragment extends Fragment {

    private boolean isPhone = false;

    private EditText mEditOld, mEditNew;


    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        if (getArguments().getString("context").equals("phone")){

            isPhone = true;
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View changeIdPhoneView = inflater.inflate(R.layout.change_id_phone_layout, container, false);

        mEditOld = changeIdPhoneView.findViewById(R.id.editOld);
        mEditNew = changeIdPhoneView.findViewById(R.id.editNew);

        if (isPhone){

            mEditOld.setHint(R.string.old_phone);
            mEditNew.setHint(R.string.new_phone);
            ((Button) changeIdPhoneView.findViewById(R.id.btnChange)).setText(R.string.change_phone_num);
        }

        return changeIdPhoneView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

        /*
        Reduce text bloat, help forced layout refresh
         */
        mEditNew.setText(null);
        mEditOld.setText(null);

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
