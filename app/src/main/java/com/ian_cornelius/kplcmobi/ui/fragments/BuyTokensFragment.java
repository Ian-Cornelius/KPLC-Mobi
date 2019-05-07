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
import android.widget.TextView;

/*
For our fab animations
 */
import android.support.constraint.motion.MotionLayout;

import com.ian_cornelius.kplcmobi.R;


public class BuyTokensFragment extends Fragment {

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
                } else{
                    mSwitchAccFabActivator.transitionToStart();
                    buyTokensLayout.findViewById(R.id.fabContentHolder).startAnimation(closeAnimation);

                }

                fabReverse = !fabReverse;

            }
        });



        return buyTokensLayout;
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
