package com.ian_cornelius.kplcmobi.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ian_cornelius.kplcmobi.R;

//Motion layouts in fragment
import android.support.constraint.motion.MotionLayout;

//Image button in consumption track layout
import android.widget.ImageButton;

/*
Controllers for UI
 */
import com.ian_cornelius.kplcmobi.controllers.ConsumptionTrackController;
import com.ian_cornelius.kplcmobi.controllers.PlannedOutagesController;


public class NotificationsFragment extends Fragment {

    /*
    Motion layout for managing the transitions in consumption track layout
     */
    private MotionLayout mConsumptionTrackLayoutContainer;

    /*
    Image button in consumption track layout
     */
    private ImageButton mBtnDisclaimer;

    /*
    Controller for consumption track layout
     */
    private ConsumptionTrackController consumptionTrackController;

    /*
    Controller for planned power outages
     */
    private PlannedOutagesController plannedOutagesController;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        /*
        Inflate our UI
         */
        View notificationsView = inflater.inflate(R.layout.notifications_fragment_layout,container,false);

        /*
        Get consumption track motion layout instance
         */
        mConsumptionTrackLayoutContainer = notificationsView.findViewById(R.id.consumptionTrackLayoutContainer);

        /*
        Get image button instance
         */
        mBtnDisclaimer = notificationsView.findViewById(R.id.btnDisclaimer);

        //send these instances to our controller where the necessary UI transitions and controls will be done
        consumptionTrackController = new ConsumptionTrackController();
        consumptionTrackController.getWidgetInstances(mConsumptionTrackLayoutContainer, mBtnDisclaimer);

        //send instance of planned outages layout to its controller
        plannedOutagesController = new PlannedOutagesController();

        //pass context
        plannedOutagesController.getContextAndInstance(this.getContext(),this);
        //Pass our container. Now just our main notifications
        plannedOutagesController.setViewContainer(notificationsView);

        return notificationsView;
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
