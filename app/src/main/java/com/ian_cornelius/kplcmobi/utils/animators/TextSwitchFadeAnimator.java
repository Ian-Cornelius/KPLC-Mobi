package com.ian_cornelius.kplcmobi.utils.animators;

/*
Animate simple change in text, using fade in fade out alpha, and value animator
 */
import android.util.Log;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;
import android.animation.ValueAnimator;
import android.animation.AnimatorSet;
import android.view.animation.AccelerateDecelerateInterpolator;

public class TextSwitchFadeAnimator {

    private static ValueAnimator textAnimator;
    private static AnimatorSet textAnimatorSet;
    private static boolean setText = false;

    /*
    Static method to invoke this anim
     */
    public static void switchText(TextView mainTarget, TextView secTarget, String mainMsg, String secMessage){

        init();
        setUpListener(mainTarget, secTarget, mainMsg, secMessage);
//        textAnimatorSet.play(textAnimator);

        //start the anim boy...
        textAnimatorSet.start();

    }

    //Method to instantiate our vars
    private static void init(){

        textAnimator = ValueAnimator.ofFloat(-30f,30f).setDuration(100);
        textAnimatorSet = new AnimatorSet();
        textAnimatorSet.play(textAnimator);
        textAnimatorSet.setInterpolator(new DecelerateInterpolator());
    }

    //Method to set up listeners
    private static void setUpListener(final TextView mainTarget, final TextView secTarget, final String newMainMsg, final String newSecMessage){

        textAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                //Log.e("TXT ANIMATOR", "Animated val " + (Float) animation.getAnimatedValue());
                //Set the value of alpha, based on which half of range we are in
                if (((Float) animation.getAnimatedValue()).floatValue() < 0f){

                    mainTarget.setAlpha(- ((Float) animation.getAnimatedValue()).floatValue()/30);
                    secTarget.setAlpha(- ((Float) animation.getAnimatedValue()).floatValue()/30);
                    //Log.e("TXT ANIMATOR ALPHA","VAL " + target.getAlpha());
                } else if (((Float) animation.getAnimatedValue()).floatValue() == 0f){

                    mainTarget.setText(newMainMsg);
                    secTarget.setText(newSecMessage);
                    setText = true;
                } else {

                    //account for interpolator not generating val 0
                    if (!setText){
                         mainTarget.setText(newMainMsg);
                         secTarget.setText(newSecMessage);
                         setText = true;
                    }
                    mainTarget.setAlpha(((Float) animation.getAnimatedValue()).floatValue()/30);
                    secTarget.setAlpha(((Float) animation.getAnimatedValue()).floatValue()/30);

                }
                //call destroy onEnd
                if (((Float) animation.getAnimatedValue()).floatValue() == 1f){

                    destroy();
                }
            }
        });
    }

    //help avoid memory leaks. Clean up for gc to garbage collect, since they are static
    private static void destroy(){

        textAnimator = null;
        textAnimatorSet = null;
        setText = false;
    }
}
