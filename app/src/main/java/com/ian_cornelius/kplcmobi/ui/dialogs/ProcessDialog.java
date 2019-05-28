package com.ian_cornelius.kplcmobi.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.motion.MotionLayout;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.ian_cornelius.kplcmobi.R;

public class ProcessDialog extends DialogFragment {

    /*
    Just cause context sometimes likes bringing null in some android versions
     */
    private Context mContext;
    private TextView mTxtMainWaitMsg, mTxtExpandedWaitMsg;
    private Button mBtnOk;

    private String mainWaitMsg, expandedWaitMsg;

    //Effect transitions
    private MotionLayout dialogView;


    @Override
    public void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        dialogView = (MotionLayout) inflater.inflate(R.layout.custom_dialog_motion_layout, container);

        mTxtMainWaitMsg = dialogView.findViewById(R.id.txtMainWaitMsg);
        mTxtExpandedWaitMsg = dialogView.findViewById(R.id.txtExpandedWaitMsg);
        mBtnOk = dialogView.findViewById(R.id.btnOk);

        mBtnOk.setEnabled(true);
        mBtnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
                Log.e("ERROR","CALLED DISMISS");
            }
        });

        mTxtMainWaitMsg.setText(mainWaitMsg);
        mTxtExpandedWaitMsg.setText(expandedWaitMsg);

        dialogView.transitionToEnd();

        return dialogView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState){

        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        setStyle(DialogFragment.STYLE_NO_FRAME, android.R.style.Theme);
    }


    /*
    Method to set unique messages, pre dialog show
     */
    public void setMessagesPreShow(String mainWaitMsg, String expandedWaitMsg){

        this.mainWaitMsg = mainWaitMsg;
        this.expandedWaitMsg = expandedWaitMsg;
    }

    /*
    Method to set unique messages post dialog show
     */
    public void setMessagesPostShow(String mainWaitMsg, String expandedWaitMsg){

        mTxtMainWaitMsg.setText(mainWaitMsg);
        mTxtExpandedWaitMsg.setText(expandedWaitMsg);
    }

    /*
    Method to effect transition to error view
     */
    public void transitionToError(String failMessage){

        ((TextView)dialogView.findViewById(R.id.txtFailMsg)).setText(failMessage);
        dialogView.setProgress(1);
        dialogView.transitionToState(R.id.dialogState2);
    }


//    /*
//    Inflate our UI
//     */
//    @NonNull
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState){
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//
//        View dialogView = inflater.inflate(R.layout.custom_dialog_motion_layout, null);
//
//        getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
//
//        ((MotionLayout) dialogView).transitionToEnd();
//
//        builder.setView(dialogView);
//
//        return builder.create();
//
//    }


}
