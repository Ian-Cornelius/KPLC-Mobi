package com.ian_cornelius.kplcmobi.adapters;

import android.support.v7.widget.RecyclerView;

/*
Hold our threads list, containing objects of MessageThread type. (thread name, messages node ref.)
 */
import java.util.ArrayList;

import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;

import android.support.annotation.NonNull;

/*
For layout animations
 */
import android.animation.ValueAnimator;
import android.animation.AnimatorSet;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.support.v7.widget.LinearLayoutManager;

import com.ian_cornelius.kplcmobi.utils.animators.CustomLayoutAnimator;

import com.ian_cornelius.kplcmobi.models.MessageThread;

import com.ian_cornelius.kplcmobi.R;
import com.ian_cornelius.kplcmobi.utils.layout_managers.CustomLinearLayoutManager;

public class KPLCResponsesMainRecyclerViewAdapter extends RecyclerView.Adapter<KPLCResponsesMainRecyclerViewAdapter.MyViewHolder> {

    private ArrayList<String> messageThreads = new ArrayList<>();

    /*
    Trying out layout animations
     */
    ValueAnimator layoutAnimator = ValueAnimator.ofInt(363, 983).setDuration(2000);

    //Jumped some code

    /*
    Create a new animator set that we will use to play the animation
     */
    AnimatorSet set = new AnimatorSet();

    private RecyclerView mainRecycler;

    /*
    Test my animator
     */
    private CustomLayoutAnimator customLayoutAnimator = CustomLayoutAnimator.getInstance();
    private boolean reverse = false;
    private int currentPosition = 0;

    /*
    Inner class to hold our views
     */
    public class MyViewHolder extends RecyclerView.ViewHolder{

        /*
        We'll define our views here
         */
        //test our recycler view, and its adapter
        private RecyclerView innerRecyclerView;
        private KPLCResponsesInnerRecyclerAdapter innerRecyclerAdapter;

        /*
        Public constructor
         */
        public MyViewHolder(View viewItem){

            super(viewItem);

//            setUpAnimator(viewItem);
            /*
            Set up widget references here
             */
            innerRecyclerView = viewItem.findViewById(R.id.innerContentsRecycler);

            /*
            Set up adapter. Set it up here, so that its unique for each
             */
            innerRecyclerAdapter = new KPLCResponsesInnerRecyclerAdapter();

            innerRecyclerView.setLayoutManager(new LinearLayoutManager(viewItem.getContext()));
            ((LinearLayoutManager)innerRecyclerView.getLayoutManager()).setReverseLayout(true);
            innerRecyclerView.setAdapter(innerRecyclerAdapter);

            viewItem.setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v){

                    /*
                    Start our animation for the layout
                     */
//                    set.play(layoutAnimator);
//                    set.setInterpolator(new AccelerateDecelerateInterpolator());
//                    set.start();
                    customLayoutAnimator.init(mainRecycler,v,reverse);
                    currentPosition = getAdapterPosition();

                    //Attempt loading data
                    if (!reverse){

                        innerRecyclerAdapter.loadData();
                    } else {

                        innerRecyclerAdapter.invalidateData();
                        Log.e("Error", "Invalidate data called at reverse value " + reverse);
                    }

                    Log.e("Error", "Scrolling status " + mainRecycler.isVerticalScrollBarEnabled());

                    Log.e("Error", "OnClickTriggered " + " position " + getAdapterPosition());
                    Log.e("Error", "Getting height params as " + v.getHeight() + "\n main recycler " + mainRecycler.getHeight());
                }
            });
        }
    }


    /*
    Constructor for main class, the adapter
     */
    public KPLCResponsesMainRecyclerViewAdapter(){

        messageThreads.add("Something");
        messageThreads.add("Something else");
        messageThreads.add("Another one");
        messageThreads.add("Let's see how well you do");
        messageThreads.add("It's the last one");

    }

    private void setUpAnimator(final View item){

        /*
        Set up our animators
         */
        layoutAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                /*
                Get the value the interpolator is at
                 */
                Integer value = (Integer) animation.getAnimatedValue();

                //Set layout height 1:1 of the tick
                item.getLayoutParams().height = value.intValue();

                Log.e("Error", "int value " + value.toString());

                //Force all layouts to see which ones are affected by this layout's height change
                item.requestLayout();
            }
        });
    }


    /*
    Override onCreateViewHolder method. Use this method to inflate our list item layout, then create a new ViewHolder
    instance with it.
    */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i){

        /*
        Inflate our item
         */
        View viewItem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.kplc_responses_main_recycler_list_item, viewGroup, false);

        return new MyViewHolder(viewItem);
    }

    /*
    Override onBindViewHolder method, which we'll use to put content in our list item widgets, based on what we've gotten from model
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position){


    }

    @Override
    public int getItemCount(){

        return messageThreads.size();
    }

    /*
    Get mainRecycler view instance
     */
    public void getRecyclerInstance(RecyclerView mainRecycler){

        this.mainRecycler = mainRecycler;
    }

    /*
    Be told of end of layout animation
     */
    public void updateScroll(){

        /*
                    test scrolling then disabling scrolling
                     */
        if (!reverse){

            ((CustomLinearLayoutManager) mainRecycler.getLayoutManager()).scrollToPositionWithOffset(currentPosition,0);
            ((CustomLinearLayoutManager) mainRecycler.getLayoutManager()).setScrollEnabled(false);

        } else{

            //Scroll back to original position. So, need to save it
            ((CustomLinearLayoutManager) mainRecycler.getLayoutManager()).scrollToPositionWithOffset(currentPosition,0);
            ((CustomLinearLayoutManager) mainRecycler.getLayoutManager()).setScrollEnabled(true);
        }
    }

    /*
    Called at end of animation. Toggles reverse
     */
    public void toggleReverse(){

        reverse = !reverse;
    }

}
