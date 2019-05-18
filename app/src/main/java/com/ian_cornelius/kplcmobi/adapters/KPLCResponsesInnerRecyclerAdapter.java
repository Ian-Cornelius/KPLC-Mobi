package com.ian_cornelius.kplcmobi.adapters;

/*
I am thinking of using this same adapter for inbox. They hold the same structure, just different context (one)
is a string of responses concerning a certain power report. So, inside, same structure. Only that, outside,
one is packaged as KPLC responses, other as general messages.

So, will *recycle* this one 😂

Load data needs to be aware of what data it needs to load, based on "context" sent in.
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import com.ian_cornelius.kplcmobi.R;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class KPLCResponsesInnerRecyclerAdapter extends RecyclerView.Adapter<KPLCResponsesInnerRecyclerAdapter.MyViewHolder> {

    /*
    Hold actual messages
     */
    private ArrayList<String> messages = new ArrayList<>(); //Should not be initialized. Will take ref. from generated

    /*
    Test extra animation
     */
    private Animation slideIn;


    /*
    Add another ViewHolder, and statically defined view types, publicly accessed, and final.
     */

    /*
    Inner class to hold our views
     */
    public class MyViewHolder extends RecyclerView.ViewHolder{

        /*
        We'll define our views here
         */


        /*
        Public constructor. Provide widget references
         */
       public MyViewHolder(View viewItem){

           super(viewItem);

           /*
           Save our widget references
            */
       }
    }


    /*
     Constructor for main class
     */
    public KPLCResponsesInnerRecyclerAdapter(Context context){

        /*
        Add dummy test data. Will do nothing here actually, once testing done
         */
        slideIn = AnimationUtils.loadAnimation(context, R.anim.text_switcher_fade_in);
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
        View viewItem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.kplc_responses_inner_recycler_client_list_item, viewGroup, false);

        return new MyViewHolder(viewItem);
    }


    /*
    Override onBindViewHolder method, which we'll use to put content in our list item widgets, based on what we've gotten from model
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position){

        /*
        Run anim. A bit expensive on java side
         */
        holder.itemView.startAnimation(slideIn);

    }

    @Override
    public int getItemCount(){

        return messages.size();
    }


    /*
    We'll actually use this to load dummy data
     */
    public void loadData(){

        /*
        Dummy data
         */
        messages.add("Sema");
        messages.add("Sema kitu");
        messages.add("dummy");
        messages.add("another dummy");
        messages.add("ya mwisho");
        messages.add("the actual last one");

        notifyDataSetChanged();

    }

    /*
    called to invalidate data, once the layout is about to be shrunk
     */
    public void invalidateData(){

        messages.clear();

        notifyDataSetChanged();
    }

}
