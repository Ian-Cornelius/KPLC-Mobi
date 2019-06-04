package com.ian_cornelius.kplcmobi.controllers;

/*
The purpose of this class is to populate the list of planned power outages provided by the model, by inflating child views.

We expect a maximum of 7 children for the weekly section, and four for the monthly section.

Anything beyond that will be considered an overflow, and we'll need an appropriate way to deal with that.

So, this class will do as follows:

1. Make a request to the PlannedOutagesListModel to get the list of planned outages.
        As it does this, nothing planned here is changed to "getting data..."

2. On receiving list, use the childrenGenerator method to generate the appropriate number of child views, based on size
of list.
        Remember, we are generating for weekly and monthly, so both lists needed.

3. childrenGenerator needs the parent view, which will be our appropriate container, passed in from the NotificationsFragment

No need to do this in a thread as the network request to get actual data will be threaded. It's call back
will invoke the child generator. I don't anticipate significant UI hang when generating child views based on data

On requesting for data, since we are using firebase, we will basically invoke the listener, then wait for its callback
later

***Scrapping off weekly monthly separation. Not so important, and inducing a lot of load of models, utils and controllers
 */


//First, import what we'll need to generate and manipulate widgets, basically, text views
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;
import android.view.View;

//Store instance of fragment calling this controller
import com.ian_cornelius.kplcmobi.ui.fragments.NotificationsFragment;

//For storing data retrieved from model
import java.util.ArrayList;
import com.ian_cornelius.kplcmobi.models.PowerOutagesList;
import com.ian_cornelius.kplcmobi.models.PowerOutagesList.OutageType;
import com.ian_cornelius.kplcmobi.utils.PowerOutagesListGenerator;

import com.ian_cornelius.kplcmobi.R;

import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;

public class PlannedOutagesController{

    /*
    Arraylists to hold our data
     */
    private ArrayList<PowerOutagesList> allOutages;

    //Hold our generator's instance
    private PowerOutagesListGenerator listGenerator;

    //Hold our main container reference
    private View mainViewContainer;

    //Hold our text view references
    private TextView mTxtWeekShowNothing, mTxtMonthShowNothing;

    //Hold our generated textView references
    private ArrayList<TextView> textViews = new ArrayList<>();

    //Hold context to help us generate views dynamically
    Context context;

    //Hold ref of notifications manager calling this controller, to help get resources
    NotificationsFragment notifFrag;

    public void getContextAndInstance(Context context, NotificationsFragment notifFrag){
        this.context = context;
        this.notifFrag = notifFrag;
    }


    /*
    Method to get main container reference
     */
    public void setViewContainer(View mainContainer){

        this.mainViewContainer = mainContainer;

        //Show in UI that we are generating data
        mTxtWeekShowNothing = mainViewContainer.findViewById(R.id.txtWeekNothingPlanned);
        mTxtMonthShowNothing = mainViewContainer.findViewById(R.id.txtMonthNothingPlanned);

        mTxtWeekShowNothing.setText("Getting data...");
        mTxtMonthShowNothing.setText("Getting data...");

        //call retrieveData(), who will request model for data
        //retrieveData();

        //get our generator instance
        listGenerator = PowerOutagesListGenerator.getInstance();

        //now generate children
        generateChildren();
    }

    /*
    Method to retrieveData from model
     */
    private void retrieveData(){

        //call the listener set up

    }



    /*
    Called when data has been populated. Should be called by generateData of list generator, after it has been invoked by listener
     */
    public void generateChildren(){

        /*
        populate data
         */
        allOutages = listGenerator.generateData();

        /*
        Now the drama
        Need to look at size of data. Determine how long we'll loop
        */

        //Our layout params. Basically width and height
        LayoutParams txtViewsParams = new LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        //set margins
        txtViewsParams.setMargins(0,56,0,0);

        //Temporarily hold container and text view reference, plus constraint set values
        View tempContainer;
        TextView tempTextViewRef;

        //Loop through, creating all appropriate children
        for (int i=0; i<allOutages.size(); i++){

            //look at type. If weekly, get to weekly container and generate children, and set its onClickListener
            //plus id track. Otherwise, do for monthly

            if(allOutages.get(i).type == OutageType.WEEKLY){

                //Our container is now for weekly, linear layout
                tempContainer = mainViewContainer.findViewById(R.id.weeklyEntries);

                //Create a text view
                tempTextViewRef = new TextView(context);

                /*
                Add its properties (date, text size, text color and font
                 */

                //Add text, text color, and drawable icon based on affect condition

                if (allOutages.get(i).entry.isWillAffect()){

                    tempTextViewRef.setText(allOutages.get(i).entry.getDate() + "   " + notifFrag.getResources().getString(R.string.affect_outage));
                    tempTextViewRef.setTextColor(notifFrag.getResources().getColor(R.color.kplcGreen));
                    tempTextViewRef.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.hazard_icon,0);
                    tempTextViewRef.setCompoundDrawablePadding(20);

                } else{

                    //No drawable. Normal text
                    tempTextViewRef.setText(allOutages.get(i).entry.getDate());
                    tempTextViewRef.setTextColor(notifFrag.getResources().getColor(R.color.custom_grey));

                }

                //text sizes
                tempTextViewRef.setTextSize(TypedValue.COMPLEX_UNIT_PX, context.getResources().getDimension(R.dimen.text_size_body));

                //Standard text properties
                //tempTextViewRef.setTextSize(16);
                tempTextViewRef.setTypeface(Typeface.createFromAsset(notifFrag.getActivity().getAssets(),"fonts/lato_regular.ttf"));
                tempTextViewRef.setId(i + 1);


                //Set out our layout parameters
                tempTextViewRef.setLayoutParams(txtViewsParams);

                //add this view to our main container
                ((LinearLayout) tempContainer.findViewById(R.id.weeklyEntries)).addView(tempTextViewRef);

                Log.e("Error","Getting textview reference as " + tempContainer.findViewById(i+1) + " id of " + tempContainer.findViewById(i+1).getId());

                /*
                Set up onClick listener
                 */
                tempTextViewRef.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        /*
                        Get our id, use it to find actual text view in textviews list, its index, use that to get its model
                         */
                        Toast.makeText(notifFrag.getContext(),"Date: " + allOutages.get(textViews.indexOf(v)).entry.getDate(),Toast.LENGTH_SHORT).show();

                    }
                });

                //add this to our array list
                textViews.add(tempTextViewRef);

                /*
                NOTE***

                Our TextViews references are added to list in correct sequence with the data they represent. So, we can do a direct mapping. Test and see
                 */

            }

        }

        //reset text
        mTxtWeekShowNothing.setText("Done");
    }

    /*
    TODO: Now, replicate this with notifications, then generate the fragment views that will show us the notification details.
     */

}
