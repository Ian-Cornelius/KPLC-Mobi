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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;

/*
For our fab animations
 */
import android.support.constraint.motion.MotionLayout;

import com.ian_cornelius.kplcmobi.R;


public class BuyTokensFragment extends Fragment {

    /*
    Hold our edit texts and buttons references
     */
    private EditText mEditCashAmnt, mEditUnitsAmnt;
    private Button mBtnViewHist, mBtnPurchase;


    /*
    For our fab animation
     */
    private MotionLayout mSwitchAccFabActivator,mSwitchAccFabContent;
    private Animation closeAnimation;
    private boolean fabReverse;


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
        Try out our fab animation
         */

        mSwitchAccFabActivator = buyTokensLayout.findViewById(R.id.switchAccFabActivator);
        closeAnimation = AnimationUtils.loadAnimation(getActivity(),R.anim.zoom_out_fab_content);
        closeAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                getChildFragmentManager().beginTransaction().remove(getChildFragmentManager().findFragmentById(R.id.fabContentHolder)).commit();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mSwitchAccFabActivator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!fabReverse){
                    mSwitchAccFabActivator.transitionToEnd();
                    getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.zoom_in_fab_content,R.anim.zoom_out_fab_content).replace(R.id.fabContentHolder,new SwitchAccFabContentFragment()).commit();

                    //Disable our overlapping views
                    toggleOverlappingWidgets(false, false);

                    /*
                    Kill opacity of main content view
                     */
                    buyTokensLayout.findViewById(R.id.buy_tokens_main_content_layout).setAlpha(0.1f);

                } else{
                    mSwitchAccFabActivator.transitionToStart();
                    buyTokensLayout.findViewById(R.id.fabContentHolder).startAnimation(closeAnimation);

                    //Enable our overlapping views
                    toggleOverlappingWidgets(true, false);

                    /*
                    Build back opacity of main content view
                     */
                    buyTokensLayout.findViewById(R.id.buy_tokens_main_content_layout).setAlpha(1.0f);

                }

                fabReverse = !fabReverse;

            }
        });

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
                Do error handling. Function return true on correct parse, false otherwise

                If true, perform network request to buy tokens, display progress dialog, then on success,
                show buy tokens success custom snackbar
                 */

                /*
                Show custom snackbar. But first, kill alpha of main_content_layout and switch acc fab activator
                 */
                buyTokensLayout.findViewById(R.id.buy_tokens_main_content_layout).setAlpha(0.1f);
                mSwitchAccFabActivator.setAlpha(0.1f);
                getChildFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_up, R.anim.slide_up).replace(R.id.token_purchase_success_fragment_holder, new BuyTokensSuccessCustomSnackBar()).commit();

                /*
                Disable edit texts and buttons. Actually will help to avoid double purchase
                 */
                toggleOverlappingWidgets(false, true);
            }
        });



        return buyTokensLayout;
    }

    /*
    Method to activate and deactivate clicks on buttons and edits texts under our custom fab content layout
     */
    private void toggleOverlappingWidgets(boolean enabled, boolean includeFabActivator){

        mEditUnitsAmnt.setEnabled(enabled);
        mEditCashAmnt.setEnabled(enabled);
        mBtnPurchase.setEnabled(enabled);
        mBtnViewHist.setEnabled(enabled);

        if (includeFabActivator){

            mSwitchAccFabActivator.setEnabled(!includeFabActivator);
        }

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
