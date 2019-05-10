package com.ian_cornelius.kplcmobi.ui.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.ian_cornelius.kplcmobi.adapters.PurchaseHistoryRecyclerViewAdapter;
import com.ian_cornelius.kplcmobi.ui.fragments.PurchaseHistoryListFragment;
import com.ian_cornelius.kplcmobi.R;


public class SortContentFragment extends Fragment {

    /*
    For our button widgets
     */
    private Button mBtnOldest, mBtnNewest, mBtnClose;

    /*
    References to fragment and adapters
     */
    private PurchaseHistoryListFragment parentFrag;
    private PurchaseHistoryRecyclerViewAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View sortContentView = inflater.inflate(R.layout.sort_list_fragment, container, false);

        /*
        Get button references
         */
        mBtnOldest = sortContentView.findViewById(R.id.btnOldest);
        mBtnNewest = sortContentView.findViewById(R.id.btnNewest);
        mBtnClose = sortContentView.findViewById(R.id.btnClose);

        /*
        Set up button listeners. Will use interface to interact with recycler adapter to reorder list from tail to
        head (newest to oldest), or head to tail (oldest to newest). So, entries queued. Head node pointer info
        kept, and that of tail too. Head static on entry addition. Tail changes on addition. On deletion, both can

        Never used interface. Just methods specific to them
         */

        mBtnOldest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                Tell adapter to change ordering. Then kill itself
                 */
                adapter.reorderList(false);
                parentFrag.killSortContentFrag();

            }
        });

        mBtnNewest.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v){

                /*
                Tell adapter to change ordering. Kill self
                 */
                adapter.reorderList(true);
                parentFrag.killSortContentFrag();
            }
        });

        mBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                Kill self
                 */
                parentFrag.killSortContentFrag();
            }
        });

        return sortContentView;
    }

    /*
    method to get our instances
     */
    public void getFragmentAndAdapter(PurchaseHistoryListFragment frag, PurchaseHistoryRecyclerViewAdapter adapter){

        this.parentFrag = frag;
        this.adapter = adapter;
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
