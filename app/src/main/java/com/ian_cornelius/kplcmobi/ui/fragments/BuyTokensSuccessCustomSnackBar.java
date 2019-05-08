package com.ian_cornelius.kplcmobi.ui.fragments;

/*
This fragment is to be shown once a token purchase process is completed successfully.

Just consumes the returned data and displays it on the UI widgets
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ian_cornelius.kplcmobi.R;


public class BuyTokensSuccessCustomSnackBar extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View customSnackBarView = inflater.inflate(R.layout.custom_snackbar_content_holder, container, false);

        /*
        Build UI widgets content based on response. Use interface to kill progress dialog in parent fragment, once
        this method completes. Noticed some lag
         */

        return customSnackBarView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
}
