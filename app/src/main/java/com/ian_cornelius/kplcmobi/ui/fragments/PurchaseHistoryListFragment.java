package com.ian_cornelius.kplcmobi.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.LinearLayoutManager;

import com.ian_cornelius.kplcmobi.R;

import com.ian_cornelius.kplcmobi.adapters.PurchaseHistoryRecyclerViewAdapter;
import com.ian_cornelius.kplcmobi.utils.PurchaseHistoryRecordsGenerator;


public class PurchaseHistoryListFragment extends Fragment {

    /*
    Recycler view instance and its adapter
     */
    private RecyclerView mPurchaseHistoryListRecycler;
    private PurchaseHistoryRecyclerViewAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View purchaseHistListFragment = inflater.inflate(R.layout.purchase_history_list_fragment_layout, container, false);

        /*
        Instantiate recycler view and adapters
         */
        mPurchaseHistoryListRecycler = purchaseHistListFragment.findViewById(R.id.purchaseHistoryListRecycler);
        adapter = new PurchaseHistoryRecyclerViewAdapter();
        mPurchaseHistoryListRecycler.setAdapter(adapter);

        /*
        Important - attach layoutmanager
         */
        mPurchaseHistoryListRecycler.setLayoutManager( new LinearLayoutManager(getActivity()));

        /*
        Important - pass in adapter reference to generator here
         */
        PurchaseHistoryRecordsGenerator.getInstance().getAdapterReference(adapter);
        adapter.requestHistoryData("324");

        return purchaseHistListFragment;
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
