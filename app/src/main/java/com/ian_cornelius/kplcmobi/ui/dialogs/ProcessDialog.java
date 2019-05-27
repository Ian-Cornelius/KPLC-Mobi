package com.ian_cornelius.kplcmobi.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.ian_cornelius.kplcmobi.R;

public class ProcessDialog extends DialogFragment {

    /*
    Just cause context sometimes likes bringing null in some android versions
     */
    private Context mContext;

    /*
    Inflate our UI
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        View dialogView = inflater.inflate(R.layout.custom_dialog_wait_layout, null);

        builder.setView(dialogView);

        return builder.create();

    }


}
