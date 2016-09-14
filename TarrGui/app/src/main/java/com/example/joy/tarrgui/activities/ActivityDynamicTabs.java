package com.example.joy.tarrgui.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joy.tarr.MainActivity;
import com.example.joy.tarr.Professor;
import com.example.joy.tarr.ProfessorClientApi;
import com.example.joy.tarr.User;
import com.example.joy.tarr.UserClientApi;
import com.example.joy.tarrgui.R;
import com.example.joy.tarrgui.fragments.DummyFragment;
import com.example.joy.tarrgui.fragments.SubjectFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.Plus;
import com.pkmmte.view.CircularImageView;

import java.util.Collection;

import it.neokree.materialtabs.MaterialTab;
import it.neokree.materialtabs.MaterialTabHost;
import it.neokree.materialtabs.MaterialTabListener;
import retrofit.RestAdapter;


public class ActivityDynamicTabs extends ActionBarActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<People.LoadPeopleResult>, MaterialTabListener {
    
    private Toolbar mToolbar;
    private MaterialTabHost mTabHost;
    private ViewPager mViewPager;
    private ViewPagerAdapter mAdapter;

    public String _emailID, _name, _imgurl;
    GoogleApiClient mGoogleApiClient;
    boolean mSignInClicked;

    private final String URL = "http://192.168.1.16:8080";
    private UserClientApi userService = new RestAdapter.Builder()
            .setEndpoint(URL).setLogLevel(RestAdapter.LogLevel.FULL).build()
            .create(UserClientApi.class);
    private ProfessorClientApi profService = new RestAdapter.Builder()
            .setEndpoint(URL).setLogLevel(RestAdapter.LogLevel.FULL).build()
            .create(ProfessorClientApi.class);
    public Collection<User> userCol;
    public User user ;
    public Collection<Professor> profCol;
    public Professor prof ;

    public CircularImageView profileImage;
    public TextView profileName;
    public TextView profileEmail;
    public TextView profileCourse;
    public RatingBar profileRating;
    public TextView profileSkills;
    public TextView ptextAccept, ptextPend, ptextDec;
    public ImageView profileAccept, profileDec, profilePend;


    public static String PNAME = "", PEMAIL="", PURL="";

    public Boolean superflag =false, done =false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_tabs);

        //////////////////////////////////////////////////////
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
        PNAME = _name;
        PEMAIL = _emailID;
        PURL = _imgurl;
        System.out.println("DynamicTabs"+PEMAIL+PURL);

        //////////////////////////////////////////////////////


        mToolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mTabHost = (MaterialTabHost) findViewById(R.id.materialTabHost);
        mViewPager = (ViewPager) findViewById(R.id.viewPager);


        mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                mTabHost.setSelectedNavigationItem(position);

            }
        });

        for (int i = 0; i < mAdapter.getCount(); i++) {
            MaterialTab materialTab = mTabHost.newTab();
            materialTab.setText("Profile");
            materialTab.setTabListener(this);
            mTabHost.addTab(materialTab);
        }

    }


    ///////////////////////////////////////////////////
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
        Toast.makeText(getApplication(),"onresult",Toast.LENGTH_LONG);
    }
    //////////////////////////////////////////////////////

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dynamic_tabs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            if (superflag==false) {
                superflag = true;
                done=true;
                for (int i=0; i<MainActivity.list.size();i++){

                        mTabHost.addTab(mTabHost.newTab()
                                .setText(MainActivity.list.get(i).toString())
                                .setTabListener(ActivityDynamicTabs.this));
                        mTabHost.notifyDataSetChanged();
                        mAdapter.setCount(mAdapter.getCount() + 1);

                }


                return true;
            }
        }
        if (R.id.sign_out_prof == id) {

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
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(MaterialTab materialTab) {
        mViewPager.setCurrentItem(materialTab.getPosition());

    }

    @Override
    public void onTabReselected(MaterialTab materialTab) {

    }

    @Override
    public void onTabUnselected(MaterialTab materialTab) {

    }

    public void onClickUser(View v){

    }


    private class ViewPagerAdapter extends FragmentStatePagerAdapter {

        int count = 1;
        FragmentManager fragmentManager;

        public ViewPagerAdapter(FragmentManager fm) {
            super(fm);
            fragmentManager = fm;
        }

        public Fragment getItem(int num) {
            Fragment fragment = null;
            switch (num){
                case 0:
                    fragment =  new DummyFragment();
                    break;
                default:
                    fragment = SubjectFragment.newInstance2(num);
                    break;
            }
            return fragment;


            /*if (done == false)
            {
                DummyFragment dummyFragment = new DummyFragment();
                return dummyFragment;
            }
            else {

                SubjectFragment sf = new SubjectFragment();
                return sf;
            }*/
        }

        @Override
        public int getCount() {
            return count;
        }

        public void setCount(int newCount) {
            count = newCount;
            notifyDataSetChanged();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return getResources().getStringArray(R.array.tabs)[position];
        }
    }
}
