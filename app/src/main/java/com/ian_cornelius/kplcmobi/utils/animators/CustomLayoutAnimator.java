package com.ian_cornelius.kplcmobi.utils.animators;

/*
So, here is a very important class. We'll use this class with our mainRecycler adapter to pull of the UX anims
I set up for KPLC responses fragment

This class will animate height change of a clicked list item, and include methods that
will be used to allow for the progression of this animation process

Currently, it supports only animation of height change of one view, with respect to another. Thresholds are static,
animated view reference is the only one that changes

If reversing, do motion layout transitions first. Else last

Working beautifully so far.

Working well with reverse

Sooooooo beautiful. Now, finish the layouts, so that we test the inner adapter work.

Once it does. Wrap this up

Motion layout will work. Progress is float value from 0.0 to 1.0. Do the maths genius!
 */

/*
For our main widget
 */
import android.support.v7.widget.RecyclerView;

/*
Layout being animated. Sent in as of type view
 */
import android.util.Log;
import android.view.View;

/*
Value animator
 */
import android.animation.ValueAnimator;

/*
Animator set
 */
import android.animation.AnimatorSet;

/*
MotionLayout
 */
import android.support.constraint.motion.MotionLayout;

/*
Interpolator. Can, later, be passed as init() argument
 */
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.ian_cornelius.kplcmobi.adapters.KPLCResponsesMainRecyclerViewAdapter;

public class CustomLayoutAnimator {

    /*
    Hold our thresholds. Default value zero. So, use it to test if thresholds set or not
     */
    private int minThreshold = 0;
    private int maxThreshold = 0;

    /*
    Hold view to be animated
     */
    private View animatedView;

    /*
    Our adapter for callback
     */
    private KPLCResponsesMainRecyclerViewAdapter adapter;

    /*
    References to our animators
     */
    private ValueAnimator layoutAnimator;
    private AnimatorSet layoutAnimatorSet;

    /*
    Reverse animation or not
     */
    private boolean reverse;

    /*
    Avoid over invoking onLayoutAnimation end
     */
    private boolean alreadyInvokedEnd = false;

    /*
    Get instance
     */
    public static CustomLayoutAnimator getInstance(){

        return new CustomLayoutAnimator();
    }

    /*
    Init method. Used to initialize our thresholds (if not already set by adapter. Done only once, as logic
    allows that only. Then, sets our animated view reference, ready for running the anim. Init goes on to call
    the method to run the animation, such that to run our anim, we only need to invoke init.

    Takes boolean reverse as argument, to know whether we are reversing anim or not
     */
    public void init(RecyclerView mainRecyclerView, View animatedView, boolean reverse){

        /*
        Set our thresholds, and create our animators' instances. Then, animate our views. Else, this portion
        is skipped
         */
        if (minThreshold == 0 || maxThreshold == 0){

            minThreshold = animatedView.getHeight();
            maxThreshold = mainRecyclerView.getHeight();

            //Doing a +20, to push it down a bit. Using margin value of edit text, which is at 32dp
            layoutAnimator = ValueAnimator.ofInt(minThreshold, maxThreshold + 20).setDuration(600);

            layoutAnimatorSet = new AnimatorSet();
            layoutAnimatorSet.play(layoutAnimator);
            layoutAnimatorSet.setInterpolator(new LinearInterpolator());

            //Set up our adapter instance
            adapter = (KPLCResponsesMainRecyclerViewAdapter) mainRecyclerView.getAdapter();

            setUpListener();

        } else{

            //do nothing
        }

        /*
        Just set our animatedView and reverse var to the correct value (standard practice for all
         */
        this.animatedView = animatedView;

        this.reverse = reverse;

        layoutAnimator.start();
    }


    /*
    Method to set up update listener for value animator, if not set
     */
    private void setUpListener(){

        /*
        Set up the updateListener, that we will use to do the layout size updates
         */
        layoutAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                /*
                Call our height updater
                 */
                updateLayoutHeight((Integer) animation.getAnimatedValue());
                adapter.updateScroll();

                /*
                If end of anim, call onAnimEnd in adapter
                 */
                Log.e("ERROR","Value of half range = " + (maxThreshold - minThreshold)/2);
                if (((Integer) animation.getAnimatedValue()).intValue() >= (minThreshold + ((maxThreshold - minThreshold)/2))){

                    Log.e("Alert", "updateScrollCalled");

                    //adapter.updateScroll();

                }

                /*
                reset our already invoked end method, on animation end
                 */
                if ( ((Integer) animation.getAnimatedValue()).intValue() == maxThreshold + 20){

                    //adapter.updateScroll();
                    adapter.toggleReverse();
                }
            }
        });
    }


    /*
    Do actual layout height updates
     */
    private void updateLayoutHeight(Integer animatedValue){

        //Animated value basically telling us where interpolator is at

        if (!reverse){

            /*
            Test our motion layout. Set its progress.

            Works!

            Best to place it b4 changing layout params. Seems to be less laggy this way
             */
//            ((MotionLayout) animatedView).setProgress((float)animatedValue/maxThreshold);

            //We are not reversing the height

            /*
            Set layout height equal to this value
             */
            animatedView.getLayoutParams().height = animatedValue;

            /*
            Force all layouts to see which ones are affected by these changes
             */
            animatedView.requestLayout();


        } else{

            /*
            Test our motion layout. Set its progress.

            Works!
             */
//            ((MotionLayout) animatedView).setProgress(1 - (float)animatedValue/maxThreshold);

            /*
            Set layout height as maxThreshold - (animatedValue - minThreshold)

            (animatedValue - minThreshold = range of values)
             */
            animatedView.getLayoutParams().height = (maxThreshold + 20) - (animatedValue - minThreshold);

            animatedView.requestLayout();

        }
    }
}
