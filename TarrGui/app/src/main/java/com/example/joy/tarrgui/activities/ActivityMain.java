package com.example.joy.tarrgui.activities;


import android.app.AlertDialog;
import android.app.Dialog;

import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joy.tarr.EditProfileStudent;
import com.example.joy.tarr.MainActivity;
import com.example.joy.tarr.Professor;
import com.example.joy.tarr.ProfessorClientApi;
import com.example.joy.tarr.Session;
import com.example.joy.tarr.SessionClientApi;
import com.example.joy.tarr.User;
import com.example.joy.tarr.UserClientApi;
import com.example.joy.tarrgui.fragments.FragmentNotifications;
import com.example.joy.tarrgui.fragments.FragmentProfile;
import com.example.joy.tarrgui.fragments.FragmentRequests;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.google.common.collect.Iterables;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;
import com.example.joy.tarrgui.anim.AnimationUtils;
import com.example.joy.tarrgui.extras.SortListener;
import com.example.joy.tarrgui.fragments.FragmentBoxOffice;
import com.example.joy.tarrgui.fragments.FragmentDrawer;
import com.example.joy.tarrgui.fragments.FragmentSearch;
import com.example.joy.tarrgui.fragments.FragmentUpcoming;
import com.example.joy.tarrgui.logging.L;
import com.example.joy.tarrgui.R;
//import com.example.joy.tarrgui.services.ServiceMoviesBoxOffice;

import java.util.Collection;
import java.util.UUID;
import java.util.Vector;

import me.tatarka.support.job.JobInfo;
import me.tatarka.support.job.JobScheduler;
import retrofit.RestAdapter;


public class ActivityMain extends ActionBarActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<People.LoadPeopleResult>, MaterialTabListener, View.OnClickListener {

    //int representing our 0th tab corresponding to the Fragment where search results are dispalyed
    public static final int TAB_SEARCH_RESULTS = 0;
    //int corresponding to our 1st tab corresponding to the Fragment where box office hits are dispalyed
    public static final int TAB_NOTIFY = 1;
    //int corresponding to our 2nd tab corresponding to the Fragment where upcoming movies are displayed
    public static final int TAB_REQUEST = 2;
    //int corresponding to the number of tabs in our Activity
    public static final int TAB_PROFILE = 3;
    public static final int TAB_COUNT = 4;
    //int corresponding to the id of our JobSchedulerService
    public String _emailID, _name, _imgurl;
    TextView name, email;
    public static String naam = "naam", epta = "epta", picurl = "piC";


    private static final int JOB_ID = 100;
    //tag associated with the FAB menu button that sorts by name
    private static final String TAG_SORT_NAME = "sortName";
    //tag associated with the FAB menu button that sorts by date
    private static final String TAG_SORT_DATE = "sortDate";
    //tag associated with the FAB menu button that sorts by ratings
    private static final String TAG_SORT_RATINGS = "sortRatings";
    //Run the JobSchedulerService every 2 minutes
    private static final long POLL_FREQUENCY = 28800000;
    private JobScheduler mJobScheduler;
    private Toolbar mToolbar;
    //a layout grouping the toolbar and the tabs together
    private ViewGroup mContainerToolbar;
    private MaterialTabHost mTabHost;
    private ViewPager mPager;
    private ViewPagerAdapter mAdapter;
    private FloatingActionButton mFAB;
    private FloatingActionMenu mFABMenu;
    private FragmentDrawer mDrawerFragment;

    GoogleApiClient mGoogleApiClient;
    boolean mSignInClicked;

    private final String URL = "http://192.168.1.16:8080";
    private SessionClientApi sessionService = new RestAdapter.Builder()
            .setEndpoint(URL).setLogLevel(RestAdapter.LogLevel.FULL).build()
            .create(SessionClientApi.class);

    Session session, temp;
    Collection<Session> sCol;


    private UserClientApi userService = new RestAdapter.Builder()
            .setEndpoint(URL).setLogLevel(RestAdapter.LogLevel.FULL).build()
            .create(UserClientApi.class);
    private ProfessorClientApi profService = new RestAdapter.Builder()
            .setEndpoint(URL).setLogLevel(RestAdapter.LogLevel.FULL).build()
            .create(ProfessorClientApi.class);


    Collection<User> user;
    Collection<Professor> prof;

    TextView sUsername;
    TextView sid;
    TextView sEmailid;
    RatingBar sRating;

    Boolean superflag;
    AlertDialog.Builder alert;

    FragmentManager fm = getSupportFragmentManager();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gui);
        Intent in = getIntent();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();

        // get the Bundle that stores the data of this Activity
        //Bundle b = in.getExtras();
        /*if(b!=null) {
            _emailID = b.getString("Email");
            _name = b.getString("Name");
        }*/


        _name = in.getStringExtra(MainActivity.NAME);
        _emailID = in.getStringExtra(MainActivity.EMAIL);
        _imgurl = in.getStringExtra(MainActivity.IMGURL);
        System.out.println(_emailID + _name);
        naam = _name;
        epta = _emailID;
        picurl = _imgurl;

        //setupFAB();
        setupTabs();
        //setupJob();
        setupDrawer();

        AnimationUtils.animateToolbarDroppingDown(mContainerToolbar);

    }

    private void setupDrawer() {
        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        mContainerToolbar = (ViewGroup) findViewById(R.id.container_app_bar);
        //set the Toolbar as ActionBar
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        //setup the NavigationDrawer
        mDrawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        mDrawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);

    }


    public void onDrawerItemClicked(int index) {
        mPager.setCurrentItem(index);
    }

    public View getContainerToolbar() {
        return mContainerToolbar;
    }

    private void setupTabs() {
        mTabHost = (MaterialTabHost) findViewById(R.id.materialTabHost);
        mPager = (ViewPager) findViewById(R.id.viewPager);
        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mAdapter);
        //when the page changes in the ViewPager, update the Tabs accordingly
        mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mTabHost.setSelectedNavigationItem(position);

            }
        });
        //Add all the Tabs to the TabHost
        for (int i = 0; i < mAdapter.getCount(); i++) {
            mTabHost.addTab(
                    mTabHost.newTab()
                            .setIcon(mAdapter.getIcon(i))
                            .setTabListener(this));
        }
    }

   /* private void setupJob() {
        mJobScheduler = JobScheduler.getInstance(this);
        //set an initial delay with a Handler so that the data loading by the JobScheduler does not clash with the loading inside the Fragment
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //schedule the job after the delay has been elapsed
                buildJob();
            }
        }, 30000);
    }*/

    /*private void buildJob() {
        //attach the job ID and the name of the Service that will work in the background
       // JobInfo.Builder builder = new JobInfo.Builder(JOB_ID, new ComponentName(this, ServiceMoviesBoxOffice.class));
        //set periodic polling that needs net connection and works across device reboots
        builder.setPeriodic(POLL_FREQUENCY)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true);
        mJobScheduler.schedule(builder.build());
    }*/

    //Floating Action Button
    private void setupFAB() {
        //define the icon for the main floating action button
        ImageView iconFAB = new ImageView(this);
        iconFAB.setImageResource(R.drawable.ic_edit_white_18dp);
        iconFAB.setOnClickListener(this);

        //set the appropriate background for the main floating action button along with its icon
        /*mFAB = new FloatingActionButton.Builder(this)
                .setContentView(iconFAB)
                .setBackgroundDrawable(R.drawable.selector_button_red)
                .build();


        //define the icons for the sub action buttons
        ImageView iconSortName = new ImageView(this);
        iconSortName.setImageResource(R.drawable.ic_action_alphabets);
        ImageView iconSortDate = new ImageView(this);
        iconSortDate.setImageResource(R.drawable.ic_action_calendar);
        ImageView iconSortRatings = new ImageView(this);
        iconSortRatings.setImageResource(R.drawable.ic_action_important);

        //set the background for all the sub buttons
        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
        itemBuilder.setBackgroundDrawable(getResources().getDrawable(R.drawable.selector_sub_button_gray));


        //build the sub buttons
        SubActionButton buttonSortName = itemBuilder.setContentView(iconSortName).build();
        SubActionButton buttonSortDate = itemBuilder.setContentView(iconSortDate).build();
        SubActionButton buttonSortRatings = itemBuilder.setContentView(iconSortRatings).build();

        //to determine which button was clicked, set Tags on each button
        buttonSortName.setTag(TAG_SORT_NAME);
        buttonSortDate.setTag(TAG_SORT_DATE);
        buttonSortRatings.setTag(TAG_SORT_RATINGS);

        buttonSortName.setOnClickListener(this);
        buttonSortDate.setOnClickListener(this);
        buttonSortRatings.setOnClickListener(this);

        //add the sub buttons to the main floating action button
        mFABMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(buttonSortName)
                .addSubActionView(buttonSortDate)
                .addSubActionView(buttonSortRatings)
                .attachTo(mFAB)
                .build();*/
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present. 
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public void onConnected(Bundle bundle) {
        // TODO Auto-generated method stub
        mSignInClicked = false;

        // updateUI(true);
        Plus.PeopleApi.loadVisible(mGoogleApiClient, null).setResultCallback(
                this);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onStop() {

        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onResult(People.LoadPeopleResult loadPeopleResult) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will 
        // automatically handle clicks on the Home/Up button, so long 
        // as you specify a parent activity in AndroidManifest.xml. 
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement 
        if (id == R.id.action_sign_in) {
            if (mGoogleApiClient.isConnected()) {

                Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                mGoogleApiClient.clearDefaultAccountAndReconnect();
                mGoogleApiClient.disconnect();
                mGoogleApiClient.connect();
                finish();

                /*Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);*/
            }

            return true;
        }


        if (R.id.action_revoke_access == id) {
            Intent i = new Intent(this, EditProfileStudent.class);
            Bundle b = new Bundle();
            b.putString("Name",naam);
            b.putString("Email",epta);
            i.putExtras(b);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onTabSelected(MaterialTab materialTab) {
        //when a Tab is selected, update the ViewPager to reflect the changes
        mPager.setCurrentItem(materialTab.getPosition());
    }

    @Override
    public void onTabReselected(MaterialTab materialTab) {
    }

    @Override
    public void onTabUnselected(MaterialTab materialTab) {
    }

    // click function for small sorting  buttons


    @Override
    public void onClick(View v) {
        //call instantiate ite
        // m since getItem may return null depending on whether the PagerAdapter is of type FragmentPagerAdapter or FragmentStatePagerAdapter
        Toast.makeText(getApplicationContext(),"FAB clicked",Toast.LENGTH_LONG).show();
        Fragment fragment = (Fragment) mAdapter.instantiateItem(mPager, mPager.getCurrentItem());
        if (fragment instanceof SortListener) {

            if (v.getTag().equals(TAG_SORT_NAME)) {
                //call the sort by name method on any Fragment that implements sortlistener
                ((SortListener) fragment).onSortByName();
            }
            if (v.getTag().equals(TAG_SORT_DATE)) {
                //call the sort by date method on any Fragment that implements sortlistener
                ((SortListener) fragment).onSortByDate();
            }
            if (v.getTag().equals(TAG_SORT_RATINGS)) {
                //call the sort by ratings method on any Fragment that implements sortlistener
                ((SortListener) fragment).onSortByRating();
            }
        }

    }


    public void onClickUser(View v) {


        //Search


        sUsername = (TextView) v.findViewById(R.id.movieTitle);

        sEmailid = (TextView) v.findViewById(R.id.movieReleaseDate);
        sRating = (RatingBar) v.findViewById(R.id.movieAudienceScore);

        /*Toast.makeText(getApplicationContext(), "User Clicked", Toast.LENGTH_LONG).show();*/
        AlertDialog.Builder alert = new AlertDialog.Builder(this);


        alert.setMessage("Do you want to send request to " + sUsername.getText().toString() + "?");

// Set an EditText view to get user input
        final EditText topic = new EditText(this);
        final EditText remarks = new EditText(this);
        topic.setHint("topic");
        remarks.setHint("remarks");
        final LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        //alert.setView(R.layout.dialogfragment);

        layout.addView(topic);
        layout.addView(remarks);
        alert.setView(layout);

        alert.setPositiveButton("Send", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //here
                if (!(topic.getText().toString().length() < 1)) {
                    String id = UUID.randomUUID().toString();
                    Vector<String> tmp = new Vector<String>();
                    tmp.add(topic.getText().toString());
                    session = new Session(id, id, epta,
                            sEmailid.getText().toString(), tmp, "", "", "", (float) 0.0, remarks.getText().toString(), false, false, false);
                    SessionTask tsk = new SessionTask();
                    tsk.execute(3);
                } else
                    Toast.makeText(getApplicationContext(), "Please enter atleast one topic", Toast.LENGTH_LONG);
               /* mTabHost.addTab(mTabHost.newTab().setText(input.getText().toString()).setTabListener(ActivityDynamicTabs.this));
                mTabHost.notifyDataSetChanged();
                mAdapter.setCount(mAdapter.getCount() + 1);
*/
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
        // Toast.makeText(getApplicationContext(),"after dialog",Toast.LENGTH_LONG);
    }


    public void onClickUserNotify(View v) {

        superflag = false;
        //  Toast.makeText(getApplicationContext(), "User notify Clicked", Toast.LENGTH_LONG).show();
        sUsername = (TextView) v.findViewById(R.id.movieTitleNotify);
        sEmailid = (TextView) v.findViewById(R.id.movieReleaseDateNotify);
        sRating = (RatingBar) v.findViewById(R.id.movieAudienceScoreNotify);
        sid = (TextView) v.findViewById(R.id.sidNotify);
        /*Toast.makeText(getApplicationContext(), "User Clicked", Toast.LENGTH_LONG).show();*/
        alert = new AlertDialog.Builder(this);

        new SessionStrTaskNotify().execute(sid.getText().toString());


    }


    public void onClickUserRequest(View v) {
        //  Toast.makeText(getApplicationContext(), "User request Clicked", Toast.LENGTH_LONG).show();
        sUsername = (TextView) v.findViewById(R.id.movieTitleRequest);
        sEmailid = (TextView) v.findViewById(R.id.movieReleaseDateRequest);
        sRating = (RatingBar) v.findViewById(R.id.movieAudienceScoreRequest);
        sid = (TextView) v.findViewById(R.id.sidRequest);
        /*Toast.makeText(getApplicationContext(), "User Clicked", Toast.LENGTH_LONG).show();*/
        alert = new AlertDialog.Builder(this);

        new SessionStrTaskRequest().execute(sid.getText().toString());
    }

    private class SessionStrTaskRequest extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            String str = params[0];
            temp = sessionService.getByStr(params[0]);

            if (!(temp == null))
                return true;
            return null;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            // Toast.makeText(getApplicationContext(), "Notify Jai mata Di Done", Toast.LENGTH_LONG).show();
            System.out.println("Notify data downloaded done");


            if (temp.getIsCompleted() == false && temp.getIsConfirmed() == false && temp.getIsDenied() == false) {
                alert.setMessage("Seems like the session has not yet been confirmed by the TA. :( ");

                alert.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

                alert.show();

            } else if (temp.getIsCompleted() == false && temp.getIsConfirmed() == true && temp.getIsDenied() == false) {
                alert.setMessage("Is your session with " + temp.getRequestTo() + " complete? Please rate your experience.");
                final RatingBar rb = new RatingBar(alert.getContext());
                rb.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                rb.setStepSize((float)0.1);
                rb.setNumStars(5);
                final LinearLayout layout = new LinearLayout(alert.getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                layout.addView(rb);
                alert.setView(layout);
                System.out.println("rating blah" + Float.toString(rb.getRating()));
                // Toast.makeText(getApplicationContext(),Float.toString(rb.getRating()),Toast.LENGTH_LONG);
                alert.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        System.out.println("rating blah" + Float.toString(rb.getRating()));
                        if (!(rb.getRating() == (float) 0.0)) {
                            session = new Session(temp.getStr(), temp.getStr(), temp.getRequestBy(),
                                    temp.getRequestTo(), temp.getTopic(), temp.getSessionTime(),
                                    temp.getSessionDuration(),
                                    temp.getSessionLocation(), rb.getRating(), temp.getRemarks(), true, true, false);
                            new SessionTask().execute(2);
                        } else
                            Toast.makeText(getApplicationContext(), "Please enter some rating!", Toast.LENGTH_LONG);

                    }
                });

                alert.show();


            } else if (temp.getIsCompleted() == true && temp.getIsConfirmed() == true && temp.getIsDenied() == false) {
                alert.setMessage("This session has been completed. You gave " + temp.getSessionRating() + " rating to it! :) "
                );

                alert.setNeutralButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                alert.show();
            } else if (temp.getIsDenied() == true) {
                alert.setMessage("This session has been declined by " + temp.getRequestTo() + ". :( , search for another TA!  "
                );

                alert.setNeutralButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                alert.show();
            }
        }
    }

    private class SessionStrTaskNotify extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... params) {
            String str = params[0];
            temp = sessionService.getByStr(params[0]);

            if (!(temp == null))
                return true;
            return null;
        }

        @Override
        protected void onPostExecute(Boolean b) {
            //if(b)

            // Toast.makeText(getApplicationContext(), "Notify Jai mata Di Done", Toast.LENGTH_LONG).show();
            System.out.println("Notify data downloaded done");


            System.out.println("Print after");

            //temp = Iterables.getFirst(sCol, null);

            if (temp.getIsCompleted() == false && temp.getIsConfirmed() == false && temp.getIsDenied() == false) {
                alert.setMessage("Confirm the session to " + sUsername.getText().toString() + "?");
                final EditText time = new EditText(alert.getContext());
                final EditText duration = new EditText(alert.getContext());
                final EditText location = new EditText(alert.getContext());
                time.setHint("Date & Time");
                duration.setHint("Duration(hours)");
                location.setHint("Location");

                final LinearLayout layout = new LinearLayout(alert.getContext());
                layout.setOrientation(LinearLayout.VERTICAL);
                //alert.setView(R.layout.dialogfragment);

                layout.addView(time);
                layout.addView(duration);
                layout.addView(location);
                alert.setView(layout);

                alert.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //here
                        if (!(time.getText().toString().length() < 1 || duration.getText().toString().length() < 1 || location.getText().toString().length() < 1)) {
                            //String id = UUID.randomUUID().toString();
                            //Vector<String> tmp = new Vector<String>();
                            //tmp.add(topic.getText().toString());
                            session = new Session(temp.getStr(), temp.getStr(), temp.getRequestBy(),
                                    temp.getRequestTo(), temp.getTopic(), time.getText().toString(), duration.getText().toString(),
                                    location.getText().toString(), (float) 0.0, temp.getRemarks(), true, false, false);
                            SessionTask tsk = new SessionTask();
                            tsk.execute(1);
                        } else
                            Toast.makeText(getApplicationContext(), "Please enter correct data", Toast.LENGTH_LONG);
               /* mTabHost.addTab(mTabHost.newTab().setText(input.getText().toString()).setTabListener(ActivityDynamicTabs.this));
                mTabHost.notifyDataSetChanged();
                mAdapter.setCount(mAdapter.getCount() + 1);
*/
                    }
                });
                alert.setNeutralButton("Decline",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.

                        session = new Session(temp.getStr(), temp.getStr(), temp.getRequestBy(),
                                temp.getRequestTo(), temp.getTopic(), temp.getSessionTime(), temp.getSessionDuration(),
                                temp.getSessionLocation(), (float) 0.0, temp.getRemarks(), false, false, true);
                        SessionTask tsk = new SessionTask();
                        tsk.execute(0);
                    }
                });
                alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.


                    }
                });
                alert.show();
            } else if (temp.getIsCompleted() == false && temp.getIsConfirmed() == true && temp.getIsDenied() == false) {
                alert.setMessage("You have already confirmed this session on " + temp.getSessionTime() + " for "
                        + temp.getSessionDuration() + " at " + temp.getSessionLocation() + ". Enjoy :)");

                alert.setNeutralButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });

                alert.show();
            } else if (temp.getIsCompleted() == true && temp.getIsConfirmed() == true && temp.getIsDenied() == false) {
                alert.setMessage("This session has been completed. You received " + temp.getSessionRating() + " for it! Hurray! \\m/"
                );

                alert.setNeutralButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                alert.show();
            } else if (temp.getIsDenied() == true) {
                alert.setMessage("This session by " + temp.getRequestBy() + " was declined by you. :(   "
                );

                alert.setNeutralButton("Done", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                alert.show();
            }


        }// onpostexecute
    }

    private void toggleTranslateFAB(float slideOffset) {
        if (mFABMenu != null) {
            if (mFABMenu.isOpen()) {
                mFABMenu.close(true);
            }
            mFAB.setTranslationX(slideOffset * 200);
        }
    }

    public void onDrawerSlide(float slideOffset) {
        toggleTranslateFAB(slideOffset);
    }

    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        int icons[] = {R.drawable.ic_action_search,
                R.drawable.ic_action_important,
                R.drawable.ic_open_in_browser_white_18dp,
                R.drawable.ic_action_personal
        };

        FragmentManager fragmentManager;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            fragmentManager = fm;
        }

        public Fragment getItem(int num) {
            Fragment fragment = null;
//            L.m("getItem called for " + num);
            switch (num) {
                case TAB_SEARCH_RESULTS:
                    fragment = FragmentSearch.newInstance("", "");
                    // Toast.makeText(getApplicationContext(),"tab search",Toast.LENGTH_LONG);
                    System.out.println("tab search");
                    break;
                case TAB_NOTIFY:
                    fragment = FragmentNotifications.newInstance("", "");
                    // Toast.makeText(getApplicationContext(),"tab noti",Toast.LENGTH_LONG);
                    System.out.println("tab notify");

                    break;
                case TAB_REQUEST:
                    fragment = FragmentRequests.newInstance("", "");
                    // Toast.makeText(getApplicationContext(),"tab req",Toast.LENGTH_LONG);
                    System.out.println("tab req");

                    break;
                case TAB_PROFILE:

                    fragment = FragmentProfile.newInstance("", "");
                    // Toast.makeText(getApplicationContext(),"tab profile",Toast.LENGTH_LONG);
                    System.out.println("tab profile");

                    break;
            }
            return fragment;

        }

        @Override
        public int getCount() {
            return TAB_COUNT;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getResources().getStringArray(R.array.tabs)[position];
        }

        private Drawable getIcon(int position) {
            return getResources().getDrawable(icons[position]);
        }
    }


    private class SessionTask extends AsyncTask<Integer, Void, Integer> {

        @Override
        protected Integer doInBackground(Integer... params) {
            Boolean ok = sessionService.addSession(session);

            return params[0];
        }

        @Override
        protected void onPostExecute(Integer b) {
            int code = b;
            //if(b)
            // Toast.makeText(getApplicationContext(), "Jai mata Di Done", Toast.LENGTH_LONG).show();
            new updateRating().execute(code);
            System.out.println("Registration done");
        }
    }

    private class updateRating extends AsyncTask<Integer, Void, Integer> {

        @Override
        protected Integer doInBackground(Integer... params) {
            Boolean ok;
            User tempUser;
            User newUser;
            switch (params[0]) {
                case 0:
                    user = userService.findByEmailIdIgnoreCase(temp.getRequestTo());
                    if (!user.isEmpty()) {
                        tempUser = Iterables.getFirst(user, null);
                        newUser = new User(tempUser.getStr(),tempUser.getStr(),tempUser.getRegid(),
                                tempUser.getEmailId(),tempUser.getName(),tempUser.getCourse(),
                                tempUser.getBranch(), tempUser.getInstitution(), tempUser.getSkills(),
                                tempUser.getRating(),tempUser.getProfessorTa(),tempUser.getSubjectTa(),
                                tempUser.getNotiAccepted(),tempUser.getNotiDeclined()+1,
                                tempUser.getNotiPending()-1,tempUser.getNoti(),tempUser.getRatingHistory());

                        /*tempUser.setNotiDeclined(tempUser.getNotiDeclined() + 1);
                        tempUser.setNotiPending(tempUser.getNotiPending() - 1);
*/
                        //send gcm

                        ok = userService.addUser(newUser);
                        return 0;
                    }
                    break;
                case 1:
                    user = userService.findByEmailIdIgnoreCase(temp.getRequestTo());
                    if (!user.isEmpty()) {
                        tempUser = Iterables.getFirst(user, null);
                        newUser = new User(tempUser.getStr(),tempUser.getStr(),tempUser.getRegid(),
                                tempUser.getEmailId(),tempUser.getName(),tempUser.getCourse(),
                                tempUser.getBranch(), tempUser.getInstitution(), tempUser.getSkills(),
                                tempUser.getRating(),tempUser.getProfessorTa(),tempUser.getSubjectTa(),
                                tempUser.getNotiAccepted()+1,tempUser.getNotiDeclined(),
                                tempUser.getNotiPending()-1,tempUser.getNoti(),tempUser.getRatingHistory());



                        /*
                        tempUser.setNotiAccepted(tempUser.getNotiAccepted() + 1);
                        tempUser.setNotiPending(tempUser.getNotiPending() - 1);
*/
                        ok = userService.addUser(newUser);
                        return 1;
                    }
                    break;

                case 2:
                    user = userService.findByEmailIdIgnoreCase(temp.getRequestTo());
                    if (!user.isEmpty()) {
                        tempUser = Iterables.getFirst(user, null);
                        Vector<String> tempRow = new Vector<String>();
                        tempRow.add(session.getSessionId());
                        tempRow.add(String.valueOf(session.getSessionRating()));
                        Vector<Vector<String>> tempHis = new Vector<Vector<String>>();
                        tempHis = tempUser.getRatingHistory();
                        tempHis.add(tempRow);
                        //rating algorithm
                        float[] rating = new float[tempHis.size()];
                        for (int i = 1; i < tempHis.size();i++) {
                            float number = Float.parseFloat(tempHis.get(i).get(1));
                            float rounded = (int) Math.round(number * 1000) / 1000f;
                            rating[i] = rounded;
                        }

                        float currentRating=tempUser.getRating();
                        int notiAccepted=tempUser.getNotiAccepted();
                        int notiDeclined=tempUser.getNotiDeclined();
                        int notiPending=tempUser.getNotiPending();
                        int noti=tempUser.getNoti();

                        float averageRating=0.0f,finalRating=0.0f;
                        for(int i=1;i<rating.length;i++) {
                            averageRating += rating[i];
                        }
                        averageRating=averageRating/(rating.length-1);


                        float delta=(0.5f*notiAccepted-0.4f*notiDeclined-0.1f*notiPending)/noti;
                        float rounded = (int) Math.round(delta * 1000) / 1000f;
                        finalRating=(0.4f*currentRating+0.6f*averageRating+delta);
                        if(finalRating<0.0f)
                            finalRating=0.0f;
                        else if(finalRating>5.0f)
                            finalRating=5.0f;
                        newUser = new User(tempUser.getStr(),tempUser.getStr(),tempUser.getRegid(),
                                tempUser.getEmailId(),tempUser.getName(),tempUser.getCourse(),
                                tempUser.getBranch(), tempUser.getInstitution(), tempUser.getSkills(),
                                finalRating,tempUser.getProfessorTa(),tempUser.getSubjectTa(),
                                tempUser.getNotiAccepted(),tempUser.getNotiDeclined(),
                                tempUser.getNotiPending(),tempUser.getNoti(),tempHis);



                        /*tempUser.setRatingHistory(tempHis);
*/
                        ok = userService.addUser(newUser);
                        return 2;

                    }
                    break;

                case 3:
                    user = userService.findByEmailIdIgnoreCase(session.getRequestTo());
                    if (!user.isEmpty()) {
                        tempUser = Iterables.getFirst(user, null);

                        newUser = new User(tempUser.getStr(),tempUser.getStr(),tempUser.getRegid(),
                                tempUser.getEmailId(),tempUser.getName(),tempUser.getCourse(),
                                tempUser.getBranch(), tempUser.getInstitution(), tempUser.getSkills(),
                                tempUser.getRating(),tempUser.getProfessorTa(),tempUser.getSubjectTa(),
                                tempUser.getNotiAccepted(),tempUser.getNotiDeclined(),
                                tempUser.getNotiPending()+1,tempUser.getNoti()+1,tempUser.getRatingHistory());

                        System.out.println("Regid"+tempUser.getRegid());
                        sendGcmNotification(tempUser.getRegid());
                        /*tempUser.setNoti(tempUser.getNoti()      + 1);
                        tempUser.setNotiPending(tempUser.getNotiPending() + 1);
*/
                        ok = userService.addUser(newUser);
                        return 3;

                    }
            }
            return -1;
        }

        @Override
        protected void onPostExecute(Integer b) {
            if (b > 0)
                System.out.println("updated user " + Iterables.getFirst(user, null).getName() + " " + b);
        }

    }


    public void sendGcmNotification(String tempregid) {
        SendGcmNotification sendtask = new SendGcmNotification();
        sendtask.execute(tempregid);
    }


    private class SendGcmNotification extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            Boolean b = userService.sendGcm(params[0], "You have a new Session Request! "
                    ,"" );


            // MainActivity.inbox_messages.add(m);
            if(b)
                return "Success" ;
            else
                return "Failure";
        }


        @Override
        protected void onPostExecute(String response) {
            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

        }
    }
}