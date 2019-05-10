package com.ian_cornelius.kplcmobi.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ian_cornelius.kplcmobi.R;

import com.ian_cornelius.kplcmobi.models.PurchaseHistoryRecord;
import com.ian_cornelius.kplcmobi.utils.PurchaseHistoryRecordsGenerator;

import java.util.ArrayList;

public class PurchaseHistoryRecyclerViewAdapter extends RecyclerView.Adapter<PurchaseHistoryRecyclerViewAdapter.MyViewHolder> {

    /*
    ArrayList to hold our retrieved data from model
     */
    private ArrayList<PurchaseHistoryRecord> purchaseHistoryRecords;

    /*
    Tell us how to order our views
     */
    private boolean newestToOldest = true;
    private int dataSize; //Help us reduce stack calls on getItemCount()

    /*
    Inner class to hold our views
     */
    public class MyViewHolder extends RecyclerView.ViewHolder{

        /*
        We'll define our views here
         */
        TextView mTxtDate, mTxtCash, mTxtTokens, mtxtTokenNo;

        /*
        Public constructor
         */
        public MyViewHolder(View viewItem){

            super(viewItem);

            /*
            now set up the widget's references
             */
            mTxtDate = viewItem.findViewById(R.id.txtShwDate);
            mTxtTokens = viewItem.findViewById(R.id.txtShwTokens);
            mTxtCash = viewItem.findViewById(R.id.txtShwCash);
            mtxtTokenNo = viewItem.findViewById(R.id.txtShowTokenNum);


        }

    }


    /*
    Our constructor for main class
     */
    public PurchaseHistoryRecyclerViewAdapter(){

        //Don't know yet what to do here.

        //Thinking of invoking data request for purchase history stored online, from list generator.
        //Bad idea. Tried before

    }


    /*
    Override onCreateViewHolder method. Use this method to inflate our list item layout, then create a new ViewHolder
    instance with it.
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View viewItem = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.purchase_history_list_item, viewGroup, false);
        return new MyViewHolder(viewItem);
    }

    /*
    Override onBindViewHolder method, which we'll use to put content in our list item widgets, based on what we've gotten from model
     */
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position){

        /*
        Put content in our UI widgets held by MyViewHolder

        Do view ordering here, by manipulating array access

        Position starts from 0, based on get() access. So, to reverse, do getItemCount() - position
         */

        if (newestToOldest){

            //Normal access
            holder.mTxtDate.setText(purchaseHistoryRecords.get(position).date);
            holder.mTxtTokens.setText(purchaseHistoryRecords.get(position).tokens);
            holder.mTxtCash.setText(purchaseHistoryRecords.get(position).cash);
            holder.mtxtTokenNo.setText(purchaseHistoryRecords.get(position).tokenNumber);
        } else{

            //reverse access
            holder.mTxtDate.setText(purchaseHistoryRecords.get(dataSize - position -1).date);
            holder.mTxtTokens.setText(purchaseHistoryRecords.get(dataSize - position -1).tokens);
            holder.mTxtCash.setText(purchaseHistoryRecords.get(dataSize - position -1).cash);
            holder.mtxtTokenNo.setText(purchaseHistoryRecords.get(dataSize - position -1).tokenNumber);
        }

    }

    /*
    Get our item count
     */
    @Override
    public int getItemCount(){

        return purchaseHistoryRecords.size();
    }

    /*
    Call to list generator to get data. Called on attaching adapter to recycler view,
    or on switch accounts
     */
    public void requestHistoryData(String accNo){

        PurchaseHistoryRecordsGenerator.getInstance().getPurchaseHistoryList(accNo);
    }

    /*
    Might be called by list generator, incase we are working with pages of data

    Called by generator, to give the adapter the data retrieved from db

     */
    public void updateAdapterData(ArrayList<PurchaseHistoryRecord> records){

        purchaseHistoryRecords = records;
        dataSize = getItemCount();
        this.notifyDataSetChanged();
    }

    /*
    Might be called to clear arrayList data, on destruction of parent fragment (to allow less data heap, while waiting
    for GC)

    Or, put new data in list (say for different account number)
     */
    public void clearData(){

        purchaseHistoryRecords.clear();
    }

    /*
    Method invoked by sort list fragment, to tell adapter to reorder list
     */
    public void reorderList(boolean newestToOldest){

        if (this.newestToOldest == newestToOldest){

            //Redundant call. Do nothing
        } else {

            //different setting from current. Save setting and reorder
            this.newestToOldest = newestToOldest;

            //data size doesn't change. No need to update here

            notifyDataSetChanged();
        }
    }

}
