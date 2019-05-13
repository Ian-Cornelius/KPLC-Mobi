package com.ian_cornelius.kplcmobi.ui.home;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/*
For our UI widgets
 */
//motion layouts
import android.support.constraint.motion.MotionLayout;

//TextViews in navigation header and custom action bar
import android.widget.TextView;

//Button in navigation header
import android.widget.Button;

//change image at header appropriately
import android.widget.ImageView;

/*
For navigation drawer
 */
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.ian_cornelius.kplcmobi.R;
import com.ian_cornelius.kplcmobi.ui.fragments.BuyTokensFragment;
import com.ian_cornelius.kplcmobi.ui.fragments.CheckAndPayBillFragment;
import com.ian_cornelius.kplcmobi.ui.fragments.NotificationsFragment;
import com.ian_cornelius.kplcmobi.ui.fragments.PurchaseHistoryFragment;
import com.ian_cornelius.kplcmobi.ui.fragments.ReportPowerProblemFragment;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    /*
    Hold instances of drawer layout implementations
     */
    private DrawerLayout mHomeDrawerLayout;
    private NavigationView mNavView;

    /*
    Hold instances of motion layout
     */
    private MotionLayout mDrawerToggleButton;

    /*
    Hold instances of text views in navigation header and custom action bar
     */
    private TextView mTxtSettings, mTxtBuyTokens, mTxtPostPaid, mTxtPurchaseHist, mTxtReportPower,
    mTxtKPLCRes, mNewTagKPLCRes, mTxtAllNotifications, mTxtInbox, mNewTagInbox, mTxtLogOut, mTxtFragName;

    //Button in navigation header
    private Button mBtnApplyPower;

    //Image View in custom action bar
    private ImageView mImageView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        /*
        Get drawer layout instance
         */
        mHomeDrawerLayout = findViewById(R.id.home_drawer_layout);

        /*
        Get motion layout instances
         */
        mDrawerToggleButton = findViewById(R.id.drawer_toggle_btn);

        //Get custom action bar widgets references
        mTxtFragName = findViewById(R.id.txtFragName);
        mImageView = findViewById(R.id.imageView);

        /*
        Get navigation view instance
         */
        mNavView = findViewById(R.id.navView);


        /*
        Extract the navigation header layout
         */
        View mNavHeaderView = mNavView.getHeaderView(0);

        /*
        Extract widget references
         */
        mTxtSettings = mNavHeaderView.findViewById(R.id.txtSettings);
        mTxtBuyTokens = mNavHeaderView.findViewById(R.id.txtBuyTokens);
        mTxtPostPaid = mNavHeaderView.findViewById(R.id.txtPostPaid);
        mTxtPurchaseHist = mNavHeaderView.findViewById(R.id.txtPurchaseHist);
        mTxtReportPower = mNavHeaderView.findViewById(R.id.txtReportPower);
        mTxtKPLCRes = mNavHeaderView.findViewById(R.id.txtKPLCRes);
        mNewTagKPLCRes = mNavHeaderView.findViewById(R.id.newTagKPLCRes);
        mTxtAllNotifications = mNavHeaderView.findViewById(R.id.txtAllNotifications);
        mTxtInbox = mNavHeaderView.findViewById(R.id.txtInbox);
        mNewTagInbox = mNavHeaderView.findViewById(R.id.newTagInbox);
        mTxtLogOut = mNavHeaderView.findViewById(R.id.txtLogOut);

        mBtnApplyPower = mNavHeaderView.findViewById(R.id.btnApplyPower);

        /*
        Attach listener to these widgets
         */
        mTxtSettings.setOnClickListener(this);
        mTxtBuyTokens.setOnClickListener(this);
        mTxtPostPaid.setOnClickListener(this);
        mTxtPurchaseHist.setOnClickListener(this);
        mTxtReportPower.setOnClickListener(this);
        mTxtKPLCRes.setOnClickListener(this);
        mTxtAllNotifications.setOnClickListener(this);
        mTxtInbox.setOnClickListener(this);
        mTxtLogOut.setOnClickListener(this);

        mBtnApplyPower.setOnClickListener(this);


        //Set on click listener for whole motion layout
        mDrawerToggleButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){

                //Get to know if drawer is open, and perform appropriate action
                if (mHomeDrawerLayout.isDrawerOpen(Gravity.START)){

                    //Animate to end
                    mDrawerToggleButton.transitionToStart();

                    //close drawer
                    mHomeDrawerLayout.closeDrawer(Gravity.START);
                } else{

                    //launch animation
                    mDrawerToggleButton.transitionToEnd();

                    //open drawer
                    mHomeDrawerLayout.openDrawer(Gravity.START);
                }
            }
        });

        /*
        Set up our navigation drawer listener. Help us animate toggle button as drawer is slid
         */
        mHomeDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {

                mDrawerToggleButton.setProgress(v);
            }

            @Override
            public void onDrawerOpened(@NonNull View view) {

            }

            @Override
            public void onDrawerClosed(@NonNull View view) {

            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });

        //Put up our notifications fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.home_fragments_holder,new NotificationsFragment()).commit();


    }

    /*
    onClick method to implement our listeners
     */
    public void onClick(View view){

        /*
        Switch case to navigate through options
         */
        switch (view.getId()){

            case R.id.txtSettings:

                /*
                call fragment switcher, with appropriate argument

                Revoked. Too complex while I can simply check the current fragment, if not same, then switch.
                No overhead of calling another function to run same thing.
                 */

                /*
                Check if current fragment matches the one trying to switch to
                 */
                Toast.makeText(this,"Clicked on settings",Toast.LENGTH_SHORT).show();
                break;

            case R.id.txtBuyTokens:

                if (getSupportFragmentManager().findFragmentById(R.id.home_fragments_holder) instanceof BuyTokensFragment){

                    Toast.makeText(this,"You're already in buy tokens",Toast.LENGTH_SHORT).show();

                } else{

                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in,R.anim.slide_out).replace(R.id.home_fragments_holder, new BuyTokensFragment()).commit();

                    //change views in action bar
                    mTxtFragName.setText(R.string.buy_tokens);
                    changeActionBarImage(R.drawable.buy_tokens_bar);
                    mHomeDrawerLayout.closeDrawer(Gravity.START,true);

                }
                break;

            case R.id.txtPostPaid:

                if (getSupportFragmentManager().findFragmentById(R.id.home_fragments_holder) instanceof CheckAndPayBillFragment){

                    Toast.makeText(this,"You're already in check and pay bill",Toast.LENGTH_SHORT).show();

                } else{

                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in,R.anim.slide_out).replace(R.id.home_fragments_holder, new CheckAndPayBillFragment()).commit();

                    //change views in action bar
                    mTxtFragName.setText(R.string.buy_post_paid);
                    changeActionBarImage(R.drawable.buy_tokens_bar);
                    mHomeDrawerLayout.closeDrawer(Gravity.START,true);

                }
                break;

            case R.id.txtPurchaseHist:

                if (getSupportFragmentManager().findFragmentById(R.id.home_fragments_holder) instanceof PurchaseHistoryFragment){

                    Toast.makeText(this,"You're already in purchase history",Toast.LENGTH_SHORT).show();

                } else{

                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in,R.anim.slide_out).replace(R.id.home_fragments_holder, new PurchaseHistoryFragment()).commit();

                    //change views in action bar
                    mTxtFragName.setText(R.string.purchase_history);
                    changeActionBarImage(R.drawable.purchase_history_bar);
                    mHomeDrawerLayout.closeDrawer(Gravity.START,true);

                }
                break;

            case R.id.txtReportPower:

                if (getSupportFragmentManager().findFragmentById(R.id.home_fragments_holder) instanceof ReportPowerProblemFragment){

                    Toast.makeText(this,"You're already in report power problem",Toast.LENGTH_SHORT).show();

                } else{

                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in,R.anim.slide_out).replace(R.id.home_fragments_holder, new ReportPowerProblemFragment()).commit();

                    //change views in action bar
                    mTxtFragName.setText(R.string.report_power);
                    changeActionBarImage(R.drawable.report_power_bar);
                    mHomeDrawerLayout.closeDrawer(Gravity.START,true);

                }
                break;

            case R.id.txtKPLCRes:

                //call fragment switcher, with appropriate argument
                Toast.makeText(this,"Clicked on kplc responses",Toast.LENGTH_SHORT).show();
                break;

            case R.id.txtAllNotifications:

                /*
                switch fragments. Don't switch it own
                 */
                if (getSupportFragmentManager().findFragmentById(R.id.home_fragments_holder) instanceof NotificationsFragment){

                    Toast.makeText(this,"You're already in notifications",Toast.LENGTH_SHORT).show();

                } else{

                    //Switch fragment
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in,R.anim.slide_out).replace(R.id.home_fragments_holder,new NotificationsFragment()).commit();
                    //change views in action bar
                    changeActionBarImage(R.drawable.notification_bar);
                    mTxtFragName.setText(R.string.notifications);
                    mHomeDrawerLayout.closeDrawer(Gravity.START,true);
                }
                break;

            case R.id.txtInbox:

                //call fragment switcher, with appropriate argument
                Toast.makeText(this,"Clicked on inbox",Toast.LENGTH_SHORT).show();
                break;

            case R.id.txtLogOut:

                //switch to log in activity. Should I use finish? or just intent...good question... Need to destroy this activity completely on log out
                Toast.makeText(this,"Clicked on log out",Toast.LENGTH_SHORT).show();
                break;

            case R.id.btnApplyPower:

                //switch to apply power activity. Use intent, cause we need to preserve this activity's state automatically
                Toast.makeText(this,"Clicked on apply power",Toast.LENGTH_SHORT).show();
                break;

            default:

                    //Should never reach here. If so, problem. Log. Need to use timber for this
                    Log.e("Error","Reached default in Nav Header onClick");


        }
    }

    /*
    Method to load image resources properly, in separate thread from UI
     */
    private void changeActionBarImage(final int resId){

        Thread imageThread = new Thread(new Runnable() {
            @Override
            public void run() {

                mImageView.setImageResource(resId);

            }
        });

        //start thread
        imageThread.start();
    }
}
