package com.ian_cornelius.kplcmobi.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.motion.MotionLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.widget.Button;
import android.widget.Toast;

import com.ian_cornelius.kplcmobi.R;


public class PurchaseHistoryFragment extends Fragment {

    /*
    Allow us to switch between text view and graph view
     */
    private Button mBtnText, mBtnGraph;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View purchaseHistoryView = inflater.inflate(R.layout.purchase_history_fragment_layout,container,false);

        /*
        Get button references
         */
        mBtnText = purchaseHistoryView.findViewById(R.id.btnText);
        mBtnGraph = purchaseHistoryView.findViewById(R.id.btnGraph);

        /*
        Set up our listeners
         */
        mBtnText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                switch to text view
                 */
                if (getChildFragmentManager().findFragmentById(R.id.fullHistoryFragHolder) instanceof PurchaseHistoryListFragment){

                    Toast.makeText(getActivity(),"You're already viewing your history as text",Toast.LENGTH_SHORT).show();
                } else{

                    //Set up our list view - default start view, by setting up its fragment
                    getChildFragmentManager().beginTransaction().replace(R.id.fullHistoryFragHolder, new PurchaseHistoryListFragment()).commit();

                    ((MotionLayout) purchaseHistoryView).transitionToStart();

                    mBtnGraph.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.graph_icon_grey,0,0);
                    mBtnText.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.text_icon,0,0);

                }
            }
        });

        mBtnGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                switch to text view
                 */
                if (getChildFragmentManager().findFragmentById(R.id.fullHistoryFragHolder) instanceof PurchaseHistoryGraphFragment){

                    Toast.makeText(getActivity(),"You're already viewing your history as a graph",Toast.LENGTH_SHORT).show();
                } else{

                    //Set up our list view - default start view, by setting up its fragment
                    getChildFragmentManager().beginTransaction().replace(R.id.fullHistoryFragHolder, new PurchaseHistoryGraphFragment()).commit();

                    ((MotionLayout) purchaseHistoryView).transitionToEnd();

                    mBtnText.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.text_icon_grey,0,0);
                    mBtnGraph.setCompoundDrawablesWithIntrinsicBounds(0,R.drawable.graph_icon_yellow,0,0);

                }
            }
        });

        //Set up our list view - default start view, by setting up its fragment
        getChildFragmentManager().beginTransaction().replace(R.id.fullHistoryFragHolder, new PurchaseHistoryListFragment()).commit();

        return purchaseHistoryView;
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
