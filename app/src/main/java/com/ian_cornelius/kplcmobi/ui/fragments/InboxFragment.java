package com.ian_cornelius.kplcmobi.ui.fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.EditText;
import android.support.v7.widget.RecyclerView;
import android.text.TextWatcher;
import android.support.constraint.motion.MotionLayout;

import com.ian_cornelius.kplcmobi.adapters.KPLCResponsesInnerRecyclerAdapter;

import com.ian_cornelius.kplcmobi.R;
import com.ian_cornelius.kplcmobi.ui.home.HomeActivity;
import com.ian_cornelius.kplcmobi.utils.layout_managers.CustomLinearLayoutManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class InboxFragment extends Fragment {

    private RecyclerView mInboxRecycler;
    private KPLCResponsesInnerRecyclerAdapter mInboxAdapter;
    private EditText mEditMsg;
    private MotionLayout inboxView;
    private TextWatcher charWatcher;

    private int prevLineCount = 1;


   @Override
   public void onCreate(Bundle savedInstanceState){

       super.onCreate(savedInstanceState);

       charWatcher = new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {

               if (s.toString().matches("\\s*")){

                   inboxView.transitionToStart();
               } else {

                   inboxView.transitionToEnd();
               }

               if (prevLineCount != mEditMsg.getLineCount()){

                   //Bug fixing
                   ((HomeActivity)mEditMsg.getContext()).getWindow().getDecorView().findViewById(R.id.home_fragments_holder).invalidate();
                   ((HomeActivity) mEditMsg.getContext()).getWindow().getDecorView().findViewById(R.id.home_fragments_holder).requestLayout();
                   ((HomeActivity) mEditMsg.getContext()).getWindow().getDecorView().findViewById(R.id.home_fragments_holder).forceLayout();

               }
           }

           @Override
           public void afterTextChanged(Editable s) {

           }
       };
   }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

       inboxView = (MotionLayout) inflater.inflate(R.layout.inbox_fragment_layout, container, false);

       mInboxRecycler = inboxView.findViewById(R.id.inboxRecycler);
       mEditMsg = inboxView.findViewById(R.id.editMsg);

       mEditMsg.addTextChangedListener(charWatcher);

       mInboxAdapter = new KPLCResponsesInnerRecyclerAdapter(getActivity());

       mInboxRecycler.setLayoutManager(new CustomLinearLayoutManager(getActivity()));

       ((CustomLinearLayoutManager)mInboxRecycler.getLayoutManager()).setReverseLayout(true);

       mInboxRecycler.setAdapter(mInboxAdapter);

       mInboxAdapter.loadData();

       return inboxView;
    }

    @Override
    public void onAttach(Context context){

       super.onAttach(context);
    }

    @Override
    public void onDetach(){

       super.onDetach();
    }

}
