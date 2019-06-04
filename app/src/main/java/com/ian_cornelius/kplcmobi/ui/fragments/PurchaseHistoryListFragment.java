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
import android.widget.Button;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.ian_cornelius.kplcmobi.R;

import com.ian_cornelius.kplcmobi.adapters.PurchaseHistoryRecyclerViewAdapter;
import com.ian_cornelius.kplcmobi.utils.PurchaseHistoryRecordsGenerator;


public class PurchaseHistoryListFragment extends Fragment {

    /*
    Recycler view instance and its adapter
     */
    private RecyclerView mPurchaseHistoryListRecycler;
    private PurchaseHistoryRecyclerViewAdapter adapter;

    /*
    Sort button to sort list. Activate its frag.
     */
    private Button mBtnSort;

    /*
    New exit anim
     */
    private Animation mSlideDownFadeOut;

    private View fragView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View purchaseHistListFragment = inflater.inflate(R.layout.purchase_history_list_fragment_layout, container, false);

        /*
        Get button reference
         */
        mBtnSort = purchaseHistListFragment.findViewById(R.id.btnSort);
        fragView = purchaseHistListFragment.findViewById(R.id.sortContentFragmentHolder);

        /*
        New anim
         */
        mSlideDownFadeOut = AnimationUtils.loadAnimation(getActivity(), R.anim.slide_down_fade_out);

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

        /*
        Handle clicks on button sort
         */
        mBtnSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                Launch its frag and kill button clicks to avoid dual or over creation of frags
                 */
                SortContentFragment fragment = new SortContentFragment();
                fragment.getFragmentAndAdapter(getFragInstance(),adapter);
                getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up_fade_in, R.anim.classic_zoom_out).replace(R.id.sortContentFragmentHolder, fragment).commit();

                mBtnSort.setEnabled(false);

            }
        });

        /*
        Animation listener
         */
        mSlideDownFadeOut.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                /*
                Kill fragment, enable sort button
                 */
                getChildFragmentManager().beginTransaction().remove(getChildFragmentManager().findFragmentById(R.id.sortContentFragmentHolder)).commit();

                mBtnSort.setEnabled(true);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


        return purchaseHistListFragment;
    }


    /*
    Okay, so here's why I am not using interfaces - the fragments in question are not reusable. So no need. Restrict
    communication, what I think is best case.

    Save for the switch accounts fragment. Best to put an interface here. Or probably not.
     */

    /*
    get fragment instance
     */
    protected PurchaseHistoryListFragment getFragInstance(){

        return this;
    }

   /*
   Method to request for fragment destruction
    */
   protected void killSortContentFrag(){

       /*
       Play animation, which will kill frag on end
        */
       fragView.startAnimation(mSlideDownFadeOut);
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
