package com.ian_cornelius.kplcmobi.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.ian_cornelius.kplcmobi.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChangePasswordFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class ChangePasswordFragment extends Fragment {

    private EditText mEditOldPass, mEditNewPass;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View changePassView = inflater.inflate(R.layout.change_password_layout, container, false);

        mEditOldPass = changePassView.findViewById(R.id.editOldPass);
        mEditNewPass = changePassView.findViewById(R.id.editNewPass);

        return changePassView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

        //Reduce text bloat, easen layout refresh
        mEditNewPass.setText(null);
        mEditOldPass.setText(null);
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
