package com.ian_cornelius.kplcmobi.ui.fragments;

/*
Fragment for buy tokens
 */

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.ian_cornelius.kplcmobi.R;
import com.ian_cornelius.kplcmobi.ui.home.HomeActivity;


public class BuyTokensFragment extends Fragment implements HomeActivity.FabButtonToggle{

    /*
    Hold our edit texts and buttons references
     */
    private EditText mEditCashAmnt, mEditUnitsAmnt;
    private Button mBtnViewHist, mBtnPurchase;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View buyTokensLayout = inflater.inflate(R.layout.buy_tokens_fragment_layout,container,false);

        /*
        Get our edit texts and buttons references
         */
        mEditCashAmnt = buyTokensLayout.findViewById(R.id.editCashAmnt);
        mEditUnitsAmnt = buyTokensLayout.findViewById(R.id.editUnitsAmnt);
        mBtnViewHist = buyTokensLayout.findViewById(R.id.btnViewHist);
        mBtnPurchase = buyTokensLayout.findViewById(R.id.btnPurchase);

        /*
        Handle button clicks
         */

        /*
        Get history. Basically launch this fragment, passing in its bundle the call context (to help us know where
        to go back to on back pressed)
         */
        mBtnViewHist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                Switch fragments with proper context
                 */
            }
        });

        /*
        Purchase button
         */
        mBtnPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                /*
                Very important bug fixing. Edit texts get focus, motion layout goes haywire.
                Put right before reverse of layout to default state, or launch a new frag
                 */
                getActivity().getWindow().getDecorView().findViewById(R.id.home_fragments_holder).invalidate();
                getActivity().getWindow().getDecorView().findViewById(R.id.home_fragments_holder).requestLayout();
                getActivity().getWindow().getDecorView().findViewById(R.id.home_fragments_holder).forceLayout();

                /*
                Do error handling. Function return true on correct parse, false otherwise

                If true, perform network request to buy tokens, display progress dialog, then on success,
                show buy tokens success custom snackbar
                 */

                /*
                Show custom snackbar. But first, kill alpha of main_content_layout and switch acc fab activator
                 */
                buyTokensLayout.findViewById(R.id.buy_tokens_main_content_layout).setAlpha(0.1f);

                //request activity to hide fab
                ((HomeActivity)getActivity()).toggleFab(false);

                getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up, R.anim.slide_up).replace(R.id.token_purchase_success_fragment_holder, new BuyTokensSuccessCustomSnackBar()).commit();

                /*
                Disable edit texts and buttons. Actually will help to avoid double purchase
                 */
                toggleButtons(false);
            }
        });



        return buyTokensLayout;
    }

    /*
    Implement method of interface, to communicate about toggle buttons
     */
    @Override
    public void toggleButtons(boolean enable){

        mEditUnitsAmnt.setEnabled(enable);
        mEditCashAmnt.setEnabled(enable);
        mBtnPurchase.setEnabled(enable);
        mBtnViewHist.setEnabled(enable);

        Log.e("INTERFACE AT TOKENS","TOGGLE INVOKED");
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    public interface OnFragmentInteractionListener {

        void onFragmentInteraction();
    }
}
