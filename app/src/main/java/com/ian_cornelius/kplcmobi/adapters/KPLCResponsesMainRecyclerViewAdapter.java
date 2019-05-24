package com.ian_cornelius.kplcmobi.adapters;

import android.support.constraint.utils.ImageFilterView;
import android.support.v7.widget.RecyclerView;

/*
Hold our threads list, containing objects of MessageThread type. (thread name, messages node ref.)

Data saving idea - get a new message, load it, save it to local store, now work with faster, local store access. Take
care of out of memory exceptions.

So, listener for watcher space fires, gets data referenced, saves in local store, if memory not full, wait for
next event. Like it!
 */
import java.util.ArrayList;

import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.LayoutInflater;
import android.widget.EditText;

import android.support.annotation.NonNull;

import android.support.v7.widget.LinearLayoutManager;

import com.ian_cornelius.kplcmobi.ui.home.HomeActivity;
import com.ian_cornelius.kplcmobi.utils.animators.CustomLayoutAnimator;

import com.ian_cornelius.kplcmobi.models.MessageThread;

import com.ian_cornelius.kplcmobi.R;

/*
For layout animations
 */
import com.ian_cornelius.kplcmobi.utils.layout_managers.CustomLinearLayoutManager;

import android.support.constraint.motion.MotionLayout;
import android.text.TextWatcher;

public class KPLCResponsesMainRecyclerViewAdapter extends RecyclerView.Adapter<KPLCResponsesMainRecyclerViewAdapter.MyViewHolder> {

    private ArrayList<String> messageThreads = new ArrayList<>();

    private RecyclerView mainRecycler;

    /*
    Test my animator
     */
    private CustomLayoutAnimator customLayoutAnimator = CustomLayoutAnimator.getInstance();
    private boolean reverse = false;
    private int currentPosition = 0;
    private int rollBackPosition  = 0;

    /*
    Text watcher to assist in final animation
     */
    private View currentView = null;
    private TextWatcher editWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            if (s.length() == 0){

                //close edit text
                ((MotionLayout)currentView).transitionToState(R.id.resListItemState2);
                //((MotionLayout) currentView).setTransition(R.id.resListItemState3, R.id.resListItemState2);
                //((MotionLayout) currentView).transitionToEnd();
            }

            if (s.length() == 1 && before == 0){

                //open it, exposing send btn
                ((MotionLayout) currentView).transitionToState(R.id.resListItemState3);
                //((MotionLayout) currentView).setTransition(R.id.resListItemState2, R.id.resListItemState3);
                //((MotionLayout) currentView).transitionToEnd();
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    /*
    Inner class to hold our views
     */
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        /*
        We'll define our views here
         */
        //test our recycler view, and its adapter
        private RecyclerView innerRecyclerView;
        private KPLCResponsesInnerRecyclerAdapter innerRecyclerAdapter;

        /*
        For edit text
         */
        private EditText mEditReply;

        /*
        Public constructor
         */
        public MyViewHolder(View viewItem){

            super(viewItem);

            /*
            Set up widget references here
             */
            innerRecyclerView = viewItem.findViewById(R.id.innerContentsRecycler);
            mEditReply = viewItem.findViewById(R.id.editReply);

            /*
            Text watcher for each edit text
             */
            mEditReply.addTextChangedListener(editWatcher);

            /*
            Set up adapter. Set it up here, so that its unique for each
             */
            innerRecyclerAdapter = new KPLCResponsesInnerRecyclerAdapter(viewItem.getContext());

            innerRecyclerView.setLayoutManager(new LinearLayoutManager(viewItem.getContext()));
            ((LinearLayoutManager)innerRecyclerView.getLayoutManager()).setReverseLayout(true);
            innerRecyclerView.setAdapter(innerRecyclerAdapter);

            viewItem.setOnClickListener(this);
        }


        /*
        handle clicks. Maybe this will help reduce heap size (we are not creating new listeners with each item)

        okay. See some subtle changes according to profiler. Proof only with significant data size.

        Will keep it this way, however.
         */
        @Override
        public void onClick(View v){

            //First, check we don't have an already expanded view...Okay, not necessary. Blocked by design
            //from ever clicking another. So, must close current to ever click another...

            //Save this as current view
            currentView = v;

            //see what view ref am getting
            Log.e("VIEW REF ONCLICK", " " + v);

            //disable clicks on this view - to avoid repetitive executions
            //Have to do this here, so that once anim starts, not disrupted. Re-enabling done
            //at anim end, for similar reasons.
            v.setEnabled(false);

            //Always forward movement, not reversing. Reverse only useful for layout animator and scroll updater to only
            //animate properly and enable scrolling effectively
            ((MotionLayout) v).transitionToState(R.id.resListItemState2);
            //((MotionLayout) v).setTransition(R.id.resListItemState1, R.id.resListItemState2);
            //((MotionLayout) v).transitionToEnd();

            customLayoutAnimator.init(mainRecycler,v,reverse);
            currentPosition = getAdapterPosition(); //Only need to set current position here.
            rollBackPosition = ((CustomLinearLayoutManager) mainRecycler.getLayoutManager()).findFirstCompletelyVisibleItemPosition();

            //Load data. Not reversing
            //innerRecyclerAdapter.loadData();

            Log.e("Error", "Scrolling status " + mainRecycler.isVerticalScrollBarEnabled());

            Log.e("Error", "OnClickTriggered " + " position " + getAdapterPosition());
            Log.e("Error", "Getting height params as " + v.getHeight() + "\n main recycler " + mainRecycler.getHeight());

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

        messageThreads.add("and a little bit more");
        messageThreads.add("yes");
        messageThreads.add("legend!!");
        messageThreads.add("It works!!");

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
        View viewItem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.kplc_responses_main_recycler_list_item_test, viewGroup, false);

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
            ((CustomLinearLayoutManager) mainRecycler.getLayoutManager()).scrollToPositionWithOffset(rollBackPosition,0);
            ((CustomLinearLayoutManager) mainRecycler.getLayoutManager()).setScrollEnabled(true);
        }
    }

    /*
    Called at end of animation (by custom layout animator). Toggles reverse
     */
    public void toggleReverse(){

        /*
        Re-enable view, only if reversing
         */
        if (reverse){


            currentView.setEnabled(true);

            //invalidate inner recycler data
            ((KPLCResponsesInnerRecyclerAdapter)((RecyclerView)currentView.findViewById(R.id.innerContentsRecycler)).getAdapter()).invalidateData();

            //Set current view to null, done at toggle reverse. Doing it at close curret view will give null pointer
            //cause animator may still be running, and toggle reverse will need it

            //Bug fixing
            ((HomeActivity)currentView.getContext()).getWindow().getDecorView().findViewById(R.id.home_fragments_holder).invalidate();
            ((HomeActivity) currentView.getContext()).getWindow().getDecorView().findViewById(R.id.home_fragments_holder).requestLayout();
            ((HomeActivity) currentView.getContext()).getWindow().getDecorView().findViewById(R.id.home_fragments_holder).forceLayout();

            ((ImageFilterView) currentView.findViewById(R.id.listItemBackground)).setCrossfade(0.0f);

            currentView = null;
            Log.e("CURRENT VIEW VAL NULL"," " + String.valueOf(currentView == null));

        } else {

            //See what loading data for inner adapter at this point does
            ((KPLCResponsesInnerRecyclerAdapter)((RecyclerView)currentView.findViewById(R.id.innerContentsRecycler)).getAdapter()).loadData();

            //So, kill numMessages counter and set its text to zero. Update model. Logic for processing data still missing

            //Bug fixing
            ((HomeActivity)currentView.getContext()).getWindow().getDecorView().findViewById(R.id.home_fragments_holder).invalidate();
            ((HomeActivity) currentView.getContext()).getWindow().getDecorView().findViewById(R.id.home_fragments_holder).requestLayout();
            ((HomeActivity) currentView.getContext()).getWindow().getDecorView().findViewById(R.id.home_fragments_holder).forceLayout();

        }
        reverse = !reverse;
    }

    /*
    Use this method to get to know if we have an expanded view or not. Represented by var
    currentView. If null, no expanded view. Else, we have an expanded view.

    Used by HomeActivity to know if we have an expanded view, and close it onBackPressed
     */
    public View getCurrentView(){

        return currentView;
    }

    //Used by HomeActivity to close, onBackPressed. Restricts interruption of motion, And relation, so
    //restricts motion to run to end
    public boolean isReverse(){

        return  this.reverse;
    }


    /*
    Method to tell this adapter to close currentView. Basically, all onClick functions done at reverse, done here

    Invoked by HomeActivity, onBackPressed, if getCurrentView != null
     */
    public void closeCurrentView(){

        //Clear all text in edit text, for this view. Doing this before transition to state 1, to avoid state confusion
        ((EditText)currentView.findViewById(R.id.editReply)).setText(null); //can ignore this and choose to have a history of previous text

        /**
         * Very vital piece of code here. Help solve motion layout refresh layout bugs
         * TODO Put this code in every fragment with edit text
         */
        ((HomeActivity)currentView.getContext()).getWindow().getDecorView().findViewById(R.id.home_fragments_holder).invalidate();
        ((HomeActivity) currentView.getContext()).getWindow().getDecorView().findViewById(R.id.home_fragments_holder).requestLayout();
        ((HomeActivity) currentView.getContext()).getWindow().getDecorView().findViewById(R.id.home_fragments_holder).forceLayout();

        Log.e("Close view","INVOKED CLOSE VIEW");

        /*
        Invoke motion layout transition
         */
        ((MotionLayout) currentView).transitionToState(R.id.resListItemState1);
        //((MotionLayout) currentView).setTransition(R.id.resListItemState2, R.id.resListItemState1);
        //((MotionLayout) currentView).transitionToEnd();

        //Invoke layout animator
        customLayoutAnimator.init(mainRecycler, currentView, reverse);



    }

    /*
    This guy was failing because the current position was shifting?? So, can't send back a consistent view?

    Don't know. Was switching between adjacent one i.e upper and lower one.

    What I remember is, getAdapterPosition, which is what sets our current position var, gets us the position
    of this widget in our adapter (direct correlation with the array position/index). Get layout position accurately places it in the layout.

    Actually that was the problem. Current position was not accurately referencing my current view

    Try out a new UX, waiting for motion layout to finish, then,
     */
    public void runMotionLayout(){


    }

}
