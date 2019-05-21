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
import com.ian_cornelius.kplcmobi.controllers.ManageAccountsController;
import com.ian_cornelius.kplcmobi.controllers.PersonalDetailsController;
import com.ian_cornelius.kplcmobi.controllers.SecuritySettingsController;


public class SettingsFragment extends Fragment {

    private ManageAccountsController manageAccountsController;
    private SecuritySettingsController securitySettingsController;
    private PersonalDetailsController personalDetailsController;

    private TextView mTxtMainAcc, mTxtAcc2, mTxtAcc3, mTxtAcc4;

    private MotionLayout mSettingsLayout, mManageAccLayout;

    private Button mBtnBack, mBtnAddAcc;

    //reverse help us know if not in default transition state
    private boolean reverse = false;
    private boolean isAdd = false;

    //Tell us context of manage acc. View acc details = 1, remove account = 2, default = 0
    private int manageContext = 0;


    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        manageAccountsController = new ManageAccountsController(this);
        securitySettingsController = new SecuritySettingsController(this);
        personalDetailsController = new PersonalDetailsController(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View settingsView = inflater.inflate(R.layout.settings_fragment_layout,container, false);

        manageAccountsController.initViews(settingsView.findViewById(R.id.manageAccLayout), settingsView.findViewById(R.id.settingsLayout));

        securitySettingsController.initViews(settingsView.findViewById(R.id.securitySettingsLayout), settingsView.findViewById(R.id.settingsLayout));

        personalDetailsController.initViews(settingsView.findViewById(R.id.changePersonalDetailsLayout), settingsView.findViewById(R.id.settingsLayout));


        return settingsView;
    }

    private void solveBug(){

        Log.e("JUST DOING NOTHING","BYPASSING GOOGLE'S BUG");
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
