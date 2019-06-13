package com.ian_cornelius.kplcmobi.ui.home;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.constraint.motion.MotionScene;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

/*
For our UI widgets
 */
//motion layouts
import android.support.constraint.motion.MotionLayout;

//TextViews in navigation header and custom action bar
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

//To help us post a thread that will run after some delay
import android.os.Handler;

import com.ian_cornelius.kplcmobi.R;
import com.ian_cornelius.kplcmobi.ui.fragments.BuyTokensFragment;
import com.ian_cornelius.kplcmobi.ui.fragments.CheckAndPayBillFragment;
import com.ian_cornelius.kplcmobi.ui.fragments.InboxFragment;
import com.ian_cornelius.kplcmobi.ui.fragments.KPLCResponsesFragment;
import com.ian_cornelius.kplcmobi.ui.fragments.NotificationsFragment;
import com.ian_cornelius.kplcmobi.ui.fragments.PurchaseHistoryFragment;
import com.ian_cornelius.kplcmobi.ui.fragments.ReportPowerProblemFragment;
import com.ian_cornelius.kplcmobi.ui.fragments.SettingsFragment;
import com.ian_cornelius.kplcmobi.ui.fragments.SwitchAccFabContentFragment;
import com.ian_cornelius.kplcmobi.ui.login.LogInActivity;
import com.ian_cornelius.kplcmobi.utils.FirebaseUtils.FirebaseStaticReqManager;
import com.ian_cornelius.kplcmobi.utils.account_manager.AccountsManager;
import com.ian_cornelius.kplcmobi.utils.generators.ConsumptionTrackGenerator;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{

    //press twice to exit functionality
    private boolean backToExitPressedOnce = false;

    /*
    For our fab animation
     */
    private MotionLayout mSwitchAccFabActivator;
    private Animation closeAnimation;
    private boolean fabReverse;

    /*
    Hold instances of drawer layout implementations
     */
    private DrawerLayout mHomeDrawerLayout;
    private NavigationView mNavView;

    /*
    Hold instances of motion layout
     */
    private MotionLayout mDrawerToggleButton, mDrawerCustomActionBar;

    /*
    Hold instances of text views in navigation header and custom action bar
     */
    private TextView mTxtSettings, mTxtBuyTokens, mTxtPostPaid, mTxtPurchaseHist, mTxtReportPower,
    mTxtKPLCRes, mNewTagKPLCRes, mTxtAllNotifications, mTxtInbox, mNewTagInbox, mTxtLogOut, mTxtFragName;

    //Button in navigation header
    private Button mBtnApplyPower;

    //Image View in custom action bar
    private ImageView mImageView;

    //Know if to autolaunch fab or not
    private boolean autoLaunch = false;


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
        mDrawerCustomActionBar = findViewById(R.id.drawerCustomActionBar);

        //Get custom action bar widgets references
        mTxtFragName = findViewById(R.id.txtFragName);
        mImageView = findViewById(R.id.imageView);

        /*
        Get navigation view instance
         */
        mNavView = findViewById(R.id.navView);


        /*
        Try out our fab animation
         */
        mSwitchAccFabActivator = findViewById(R.id.switchAccFabActivator);

        //Set up the rest of our custom fab
        setUpFab();

        //Hide fab
        //mDrawerCustomActionBar.transitionToStart();
        toggleFab(true, false);

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

        //Put up our notifications fragment, passing in passed in bundle
        NotificationsFragment fragment = new NotificationsFragment();
        fragment.setArguments(getIntent().getBundleExtra("STATUS"));
        getSupportFragmentManager().beginTransaction().replace(R.id.home_fragments_holder,fragment).commit();


    }


    /*
    Method to set up our custom fab
     */
    private void setUpFab(){

        closeAnimation = AnimationUtils.loadAnimation(this,R.anim.zoom_out_fab_content);
        closeAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                //slide away if autoLaunch trigger
                if (autoLaunch){

                    //reset autolaunch here to avoid a relaunch, and successfully pull of an auto launch
                    autoLaunch = false;
                    mDrawerCustomActionBar.transitionToStart();
                }
                getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.fabContentHolder)).commit();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mSwitchAccFabActivator.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                launchFabContent(v);

            }
        });

        setUpFabMotionListener();

    }

    /*
    Method to open our fab content
     */
    private void launchFabContent(View v){

        //refuse to switch accounts if list not loaded
        if (AccountsManager.getInstance().accManagerWait()){

            Toast.makeText(getApplicationContext(), "We're having trouble getting your accounts list. Please check your internet connection", Toast.LENGTH_SHORT).show();
        } else {


            if (!fabReverse){
                mSwitchAccFabActivator.transitionToEnd();
                getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.zoom_in_fab_content,R.anim.zoom_out_fab_content).replace(R.id.fabContentHolder,new SwitchAccFabContentFragment()).commit();

                //Disable our overlapping views. Case problem with buy tokens, check and pay bill, history, and
                //report power
                if (getSupportFragmentManager().findFragmentById(R.id.home_fragments_holder) instanceof BuyTokensFragment){

                    ((BuyTokensFragment) getSupportFragmentManager().findFragmentById(R.id.home_fragments_holder)).toggleButtons(false);

                } else if (getSupportFragmentManager().findFragmentById(R.id.home_fragments_holder) instanceof CheckAndPayBillFragment){

                    ((CheckAndPayBillFragment) getSupportFragmentManager().findFragmentById(R.id.home_fragments_holder)).toggleButtons(false);

                } else if (getSupportFragmentManager().findFragmentById(R.id.home_fragments_holder) instanceof ReportPowerProblemFragment){

                    ((ReportPowerProblemFragment) getSupportFragmentManager().findFragmentById(R.id.home_fragments_holder)).toggleButtons(false);

                }
                //toggleOverlappingWidgets(false, false);

                    /*
                    Kill opacity of main content view. Simply, frag holder. Interface doesn't need to do this
                     */
                ((View)v.getParent()).findViewById(R.id.home_fragments_holder).setAlpha(0.1f);

            } else{
                mSwitchAccFabActivator.transitionToStart();
                ((View)v.getParent()).findViewById(R.id.fabContentHolder).startAnimation(closeAnimation);

                //Enable our overlapping views
                if (getSupportFragmentManager().findFragmentById(R.id.home_fragments_holder) instanceof BuyTokensFragment){

                    ((BuyTokensFragment) getSupportFragmentManager().findFragmentById(R.id.home_fragments_holder)).toggleButtons(true);

                } else if (getSupportFragmentManager().findFragmentById(R.id.home_fragments_holder) instanceof CheckAndPayBillFragment){

                    ((CheckAndPayBillFragment) getSupportFragmentManager().findFragmentById(R.id.home_fragments_holder)).toggleButtons(true);

                } else if (getSupportFragmentManager().findFragmentById(R.id.home_fragments_holder) instanceof ReportPowerProblemFragment){

                    ((ReportPowerProblemFragment) getSupportFragmentManager().findFragmentById(R.id.home_fragments_holder)).toggleButtons(true);

                }
                //toggleOverlappingWidgets(true, false);

                    /*
                    Build back opacity of main content view
                     */
                ((View) v.getParent()).findViewById(R.id.home_fragments_holder).setAlpha(1.0f);

            }

            fabReverse = !fabReverse;
        }

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
                switch fragments. Don't switch it own
                 */
                if (getSupportFragmentManager().findFragmentById(R.id.home_fragments_holder) instanceof SettingsFragment){

                    Toast.makeText(this,"You're already in settings",Toast.LENGTH_SHORT).show();

                } else{

                    //Switch fragment
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in,R.anim.slide_out).replace(R.id.home_fragments_holder,new SettingsFragment()).commit();
                    //change views in action bar
                    changeActionBarImage(R.drawable.settings_bar);
                    mTxtFragName.setText(R.string.settings);
                    mHomeDrawerLayout.closeDrawer(Gravity.START,true);

                    //hide our fab
                    toggleFab(false, false);
                }
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

                    //show our fab
                    toggleFab(true, false);

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

                    //show our fab
                    toggleFab(true, false);

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

                    //hide our fab. Here, only shown with autolaunch
                    toggleFab(false, false);

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

                    //show our fab
                    toggleFab(true, false);

                }
                break;

            case R.id.txtKPLCRes:

                /*
                switch fragments. Don't switch it own
                 */
                if (getSupportFragmentManager().findFragmentById(R.id.home_fragments_holder) instanceof KPLCResponsesFragment){

                    Toast.makeText(this,"You're already in KPLC responses",Toast.LENGTH_SHORT).show();

                } else{

                    //Switch fragment
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in,R.anim.slide_out).replace(R.id.home_fragments_holder,new KPLCResponsesFragment()).commit();
                    //change views in action bar
                    changeActionBarImage(R.drawable.kplc_responses_bar);
                    mTxtFragName.setText(R.string.KPLC_reponses);
                    mHomeDrawerLayout.closeDrawer(Gravity.START,true);

                    //hide our fab
                    toggleFab(false, false);
                }
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

                    //show our fab. Auto launch too much. But nice one. Important in purchase hist cause of double fab
                    toggleFab(true, false);
                }
                break;

            case R.id.txtInbox:

                /*
                switch fragments. Don't switch it own
                 */
                if (getSupportFragmentManager().findFragmentById(R.id.home_fragments_holder) instanceof InboxFragment){

                    Toast.makeText(this,"You're already in inbox",Toast.LENGTH_SHORT).show();

                } else{

                    //Switch fragment
                    getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.slide_in,R.anim.slide_out).replace(R.id.home_fragments_holder,new InboxFragment()).commit();
                    //change views in action bar
                    changeActionBarImage(R.drawable.inbox_bar);
                    mTxtFragName.setText(R.string.inbox);
                    mHomeDrawerLayout.closeDrawer(Gravity.START,true);

                    //hide our fab
                    toggleFab(false, false);
                }
                break;

            case R.id.txtLogOut:

                //Request log out
                FirebaseStaticReqManager.getInstance().requestAuth(FirebaseStaticReqManager.AuthType.LOGOUT, this);

                //remove user's alarms
                removeAlarms();

                //flush acc data stored in AccountManager local vars
                AccountsManager.getInstance().flushData();

                //Switch to log in activity
                startActivity(new Intent(HomeActivity.this, LogInActivity.class));
                finish();
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

//        Thread imageThread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//
//                mImageView.setImageResource(resId);
//
//            }
//        });
//
//        //start thread
//        imageThread.start();

        mImageView.setImageResource(resId);
    }

    /*
    Method to tell activity to hide fab. Can protect it by requesting for asking instance, and ensuring it belongs
    to valid classes, so technically, only those classes can access it.

    Problem with interface is that 4 fragments should have access to this, and not one interface can help me do that.... I bet. Not sure 😁
     */
    public void toggleFab(boolean show, boolean autoLaunch){

        //slide away our fab. Motion layout call
        if (show){

            //Do the empty test, if with autolaunch, to avoid autolaunch failing at launchFab()
            if (autoLaunch){

                if (AccountsManager.getInstance().accManagerWait()){

                    Toast.makeText(getApplicationContext(), "We're having trouble getting your accounts list. Please check your internet connection", Toast.LENGTH_SHORT).show();
                } else {

                    this.autoLaunch = true;
                    mDrawerCustomActionBar.transitionToEnd();

                }
            } else {

                //not auto launching
                mDrawerCustomActionBar.transitionToEnd();
            }

        } else {

            mDrawerCustomActionBar.transitionToStart();
        }

        Log.e("HIDE FAB","REQUESTED");
    }

    /*
    Set up listener for auto launch
     */
    private void setUpFabMotionListener(){

        mDrawerCustomActionBar.setTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int i, int i1) {

            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int i, int i1, float v) {

            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int i) {

                if (autoLaunch){

                    //launch fab content
                    launchFabContent(mSwitchAccFabActivator);

                    //Autolaunch changed to false after we close the fab content
                }
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int i, boolean b, float v) {

            }

            @Override
            public boolean allowsTransition(MotionScene.Transition transition) {
                return false;
            }
        });
    }

    /*
    Method to cancel alarm
     */
    private void removeAlarms(){

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        Intent alarmIntent = new Intent(this, ConsumptionTrackGenerator.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);

        alarmManager.cancel(pendingIntent);
    }

    /*
    No default view. Once back pressed, prompt exit, if not in KPLC responses, with view opened
     */
    private void triggerClose(){

        if (backToExitPressedOnce){

            //means, didn't delay to press back twice (past 2 seconds)
            //kill whole app
            finishAffinity();

        } else {

            //first click, or delayed. Turn to true. Prompt message
            backToExitPressedOnce = true;
            Toast.makeText(this,"Press back twice to exit", Toast.LENGTH_SHORT).show();

            //put a delay on reset. If they click back b4 this reset, app will close
            new Handler().postDelayed(new Runnable(){

                @Override
                public void run(){

                    //reset after 2 seconds, if pressed back again
                    backToExitPressedOnce = false;
                }
            }, 2000);
        }
    }


    /*
    Detect back press. Perform appropriate action
     */
    @Override
    public void onBackPressed(){

        /*
        Basically, get fragment instance we have active. If it is KPLC responses, detect if we have an expanded view,
        close it. Otherwise, do default action
         */
        Fragment currentFrag = getSupportFragmentManager().findFragmentById(R.id.home_fragments_holder);

        /*
        Now the if-elses
         */
        if (currentFrag instanceof KPLCResponsesFragment){

            //check if expanded view
            if (((KPLCResponsesFragment) currentFrag).getMainAdapter().getCurrentView() != null && ((KPLCResponsesFragment) currentFrag).getMainAdapter().isReverse()){

                /*
                We have an expanded view. Close it
                 */
                ((KPLCResponsesFragment) currentFrag).getMainAdapter().closeCurrentView();
            } else {

                //default action
                triggerClose();
            }
        } else {

            triggerClose();
        }
    }

    @Override
    public void onDestroy(){

        super.onDestroy();

        AccountsManager.getInstance().flushData();
    }


    /*
    Interface for button toggle communication between activity and fragments using this fab
     */
    public interface FabButtonToggle{

        void toggleButtons(boolean enable);
    }

    //TODO Woooohooo Firebase is the bomb!! I don't have to refire failed requests. Ask user to switch on data, Firebase remembers failed requesta and retries. Done that beautifully for account manager. DAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAANG!!!
}
