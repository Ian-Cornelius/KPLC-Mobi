package com.ian_cornelius.kplcmobi.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.ian_cornelius.kplcmobi.utils.layout_managers.CustomLinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.support.v7.widget.RecyclerView;

import com.ian_cornelius.kplcmobi.adapters.KPLCResponsesMainRecyclerViewAdapter;

import com.ian_cornelius.kplcmobi.R;


public class KPLCResponsesFragment extends Fragment {

    /*
    For our recycler view
     */
    private RecyclerView mainRecyclerView;
    private KPLCResponsesMainRecyclerViewAdapter mainAdapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View responsesView = inflater.inflate(R.layout.kplc_responses_fragment_layout, container, false);

        /*
        Get our recycler view reference
         */
        mainRecyclerView = responsesView.findViewById(R.id.mainRecycler);

        /*
        mainAdapter reference
         */
        mainAdapter = new KPLCResponsesMainRecyclerViewAdapter();

        mainAdapter.getRecyclerInstance(mainRecyclerView);
        mainRecyclerView.setLayoutManager(new CustomLinearLayoutManager(getActivity()));
        mainRecyclerView.setAdapter(mainAdapter);

        return responsesView;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }

    /*
    Help us get access to our mainAdapter, and get to know whether we have an expanded view or not,
    then perform necessary action
     */
    public KPLCResponsesMainRecyclerViewAdapter getMainAdapter(){

        return this.mainAdapter;
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
