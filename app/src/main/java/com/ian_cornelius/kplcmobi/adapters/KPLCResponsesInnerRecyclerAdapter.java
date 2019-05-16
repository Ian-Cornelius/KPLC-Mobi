package com.ian_cornelius.kplcmobi.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import com.ian_cornelius.kplcmobi.R;

public class KPLCResponsesInnerRecyclerAdapter extends RecyclerView.Adapter<KPLCResponsesInnerRecyclerAdapter.MyViewHolder> {

    /*
    Hold actual messages
     */
    private ArrayList<String> messages = new ArrayList<>(); //Should not be initialized. Will take ref. from generated


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
    public KPLCResponsesInnerRecyclerAdapter(){

        /*
        Add dummy test data. Will do nothing here actually, once testing done
         */
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
