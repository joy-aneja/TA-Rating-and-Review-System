package com.example.joy.tarr;

import android.app.AlertDialog;
import android.app.Application;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joy.tarrgui.activities.ActivityDynamicTabs;
import com.example.joy.tarrgui.materialtest.MyApplication;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.plus.People;
import com.google.android.gms.plus.People.LoadPeopleResult;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.google.common.collect.Iterables;
import com.example.joy.tarrgui.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Vector;

import retrofit.RestAdapter;
import com.example.joy.tarrgui.*;

/**
 * Created by Joy on 3/29/2015.
 */
public class MainActivity extends FragmentActivity implements
        ConnectionCallbacks, OnConnectionFailedListener,
        ResultCallback<People.LoadPeopleResult>, View.OnClickListener {

    private static final String TAG = "ta-evaluator";
    public final static String NAME = "com.example.joy.tarr.USERNAME";
    public final static String EMAIL = "com.example.joy.tarr.PASSWORD";
    public final static String IMGURL = "com.example.joy.tarr.IMGURL";

    private static final int STATE_DEFAULT = 0;
    private static final int STATE_SIGN_IN = 1;
    private static final int STATE_IN_PROGRESS = 2;

    private static final int RC_SIGN_IN = 0;

    private static final int DIALOG_PLAY_SERVICES_ERROR = 0;

    private static final String SAVED_PROGRESS = "sign_in_progress";

    private static final int PROFILE_PIC_SIZE = 400;

    // GoogleApiClient wraps our service connection to Google Play services and
    // provides access to the users sign in state and Google's APIs.
    private static GoogleApiClient mGoogleApiClient;
    private int mSignInProgress;
    public String personPhotoUrl;
    // Used to store the PendingIntent most recently returned by Google Play
    // services until the user clicks 'sign in'.
    public PendingIntent mSignInIntent;

    // Used to store the error code most recently returned by Google Play services
    // until the user clicks 'sign in'.
    private int mSignInError;
    private boolean mSignInClicked=false;

    private SignInButton mSignInButton;
    private ImageView imageProfilePic;
    private Button mSignOutButton;
    private Button mRegisterButton,mRegisterProfButton;
    private TextView mStatus, mEmail;
   /* private ListView mCirclesListView;
    private ArrayAdapter<String> mCirclesAdapter;
    private ArrayList<String> mCirclesList;
*/
    private final String URL = "http://192.168.1.16:8080";
    private UserClientApi userService = new RestAdapter.Builder()
            .setEndpoint(URL).setLogLevel(RestAdapter.LogLevel.FULL).build()
            .create(UserClientApi.class);
    private ProfessorClientApi profService = new RestAdapter.Builder()
            .setEndpoint(URL).setLogLevel(RestAdapter.LogLevel.FULL).build()
            .create(ProfessorClientApi.class);


    Collection<User> user;
    Collection<Professor> prof;

    public static Boolean professorView = false;

    Professor temp1;
    public static Vector<String> list = new Vector<String>();
    public static Vector<Vector<String>> listTa = new Vector<Vector<String>>();


    GoogleCloudMessaging gcm;
    public static String regid = "";
    String PROJECT_NUMBER = "963429126664";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSignInButton = (SignInButton) findViewById(R.id.sign_in_button);
        mSignOutButton = (Button) findViewById(R.id.sign_out_button);
        mRegisterButton = (Button) findViewById(R.id.register_button);
        mRegisterProfButton = (Button) findViewById(R.id.button_prof);
        mStatus = (TextView) findViewById(R.id.sign_in_status);
        mEmail = (TextView) findViewById(R.id.circles_title);
        //mCirclesListView = (ListView) findViewById(R.id.circles_list);
        imageProfilePic = (ImageView) findViewById(R.id.image_profilepic);

        mSignInButton.setOnClickListener(this);
        mSignOutButton.setOnClickListener(this);
        mRegisterButton.setOnClickListener(this);
        mRegisterProfButton.setOnClickListener(this);
/*

        mCirclesList = new ArrayList<String>();
        mCirclesAdapter = new ArrayAdapter<String>(this, R.layout.circle_member, mCirclesList);
        mCirclesListView.setAdapter(mCirclesAdapter);
*/

        if (savedInstanceState != null) {
            mSignInProgress = savedInstanceState
                    .getInt(SAVED_PROGRESS, STATE_DEFAULT);
        }

        mGoogleApiClient = buildGoogleApiClient();
    }

    private GoogleApiClient buildGoogleApiClient() {

        return new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();
    }

    public static GoogleApiClient getmGoogleApiClient(){
        return mGoogleApiClient;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_PROGRESS, mSignInProgress);
    }

    @Override
    public void onClick(View v) {
        if (!mGoogleApiClient.isConnecting()) {

            switch (v.getId()) {
                case R.id.sign_in_button:
                    mStatus.setText(R.string.status_signing_in);
                    resolveSignInError();
                    break;
                case R.id.sign_out_button:

                    Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
                    mGoogleApiClient.disconnect();
                    mGoogleApiClient.connect();
                    break;
                case R.id.register_button:

                    Person currentUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                    String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                    String userName = currentUser.getDisplayName();
                    Bundle b = new Bundle();
                    b.putString("Email", email);
                    b.putString("Name", userName);
                    Intent intent = new Intent(v.getContext(), CreateProfile.class);
                    intent.putExtras(b);
                    startActivity(intent);

                    break;
                /*case R.id.button_prof:

                    Person currentProf = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                    email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                    userName = currentProf.getDisplayName();
                    b = new Bundle();
                    b.putString("Email", email);
                    b.putString("Name", userName);
                    intent = new Intent(v.getContext(), RegisterProfessor.class);
                    intent.putExtras(b);
                    startActivity(intent);

                    break;*/
            }
        }
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        // Reaching onConnected means we consider the user signed in.
        Log.i(TAG, "onConnected");

        try {
            // Update the UI after signin
            updateUI(true);
            // Update the user interface to reflect that the user is signed in.

            mSignInButton.setEnabled(false);
            mSignOutButton.setEnabled(true);
            mRegisterButton.setEnabled(true);
            //mRegisterProfButton.setEnabled(true);

            // Retrieve some profile information to personalize our app for the user.
            Person currentUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);

            mStatus.setText(String.format(
                    getResources().getString(R.string.signed_in_as),
                    currentUser.getDisplayName()));
            mEmail.setText("Email: "+Plus.AccountApi.getAccountName(mGoogleApiClient));

            personPhotoUrl = currentUser.getImage().getUrl();
            personPhotoUrl = personPhotoUrl.substring(0,
                    personPhotoUrl.length() - 2)
                    + PROFILE_PIC_SIZE;
            getRegId();
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void getRegId(){
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcm == null) {
                        gcm = GoogleCloudMessaging.getInstance(getApplicationContext());
                    }
                    regid = gcm.register(PROJECT_NUMBER);
                   /* editor.putString("userregid",regid);
                    editor.apply();*/
                    msg = "GCM ID=" + regid;
                    Log.i("GCM",  msg);

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();

                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();

                if(Plus.AccountApi.getAccountName(mGoogleApiClient).equalsIgnoreCase("aneja.joy@gmail.com")||
                        Plus.AccountApi.getAccountName(mGoogleApiClient).equalsIgnoreCase("psingh@iiitd.ac.in")||
                        Plus.AccountApi.getAccountName(mGoogleApiClient).equalsIgnoreCase("himanshuvar@gmail.com")||
                        Plus.AccountApi.getAccountName(mGoogleApiClient).equalsIgnoreCase("vikas.spsingh.singh95@gmail.com")
                        ){
                    System.out.println("lucky");
                    new checkProf().execute(Plus.AccountApi.getAccountName(mGoogleApiClient));

                }
                else
                    new LoadProfileImage(imageProfilePic).execute(personPhotoUrl);
                // Indicate that the sign in process is complete.
                mSignInProgress = STATE_DEFAULT;
            }
        }.execute(null, null, null);


    }


    @Override
    public void onConnectionFailed(ConnectionResult result) {
              Log.i(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                      + result.getErrorCode());

        if (result.getErrorCode() == ConnectionResult.API_UNAVAILABLE) {
        } else if (mSignInProgress != STATE_IN_PROGRESS) {
            mSignInIntent = result.getResolution();
            mSignInError = result.getErrorCode();

            if (mSignInProgress == STATE_SIGN_IN) {
                resolveSignInError();
            }
        }

        onSignedOut();
    }
    /**
     * Updating the UI, showing/hiding buttons and profile layout
     * */
    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            mSignInButton.setVisibility(View.GONE);
            mSignOutButton.setVisibility(View.VISIBLE);
            mRegisterButton.setVisibility(View.VISIBLE);
            //mRegisterProfButton.setVisibility(View.VISIBLE);
            imageProfilePic.setVisibility(View.VISIBLE);
        } else {
            mSignInButton.setVisibility(View.VISIBLE);
            mSignOutButton.setVisibility(View.GONE);
            mRegisterButton.setVisibility(View.GONE);
            mRegisterProfButton.setVisibility(View.GONE);
            imageProfilePic.setVisibility(View.GONE);
        }
    }


    private void resolveSignInError() {
        if (mSignInIntent != null) {
            try {
                mSignInProgress = STATE_IN_PROGRESS;
                startIntentSenderForResult(mSignInIntent.getIntentSender(),
                        RC_SIGN_IN, null, 0, 0, 0);
            } catch (SendIntentException e) {
                Log.i(TAG, "Sign in intent could not be sent: "
                        + e.getLocalizedMessage());
                // The intent was canceled before it was sent.  Attempt to connect to
                // get an updated ConnectionResult.
                mSignInProgress = STATE_SIGN_IN;
                mGoogleApiClient.connect();
            }
        } else {
            showDialog(DIALOG_PLAY_SERVICES_ERROR);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        switch (requestCode) {
            case RC_SIGN_IN:
                if (resultCode == RESULT_OK) {
                    mSignInProgress = STATE_SIGN_IN;
                } else {

                    mSignInProgress = STATE_DEFAULT;
                }

                if (!mGoogleApiClient.isConnecting()) {

                    mGoogleApiClient.connect();
                }
                break;
        }
    }

    @Override
    public void onResult(LoadPeopleResult peopleData) {
        /*if (peopleData.getStatus().getStatusCode() == CommonStatusCodes.SUCCESS) {
            mCirclesList.clear();
            PersonBuffer personBuffer = peopleData.getPersonBuffer();
            try {

                int count = personBuffer.getCount();
                for (int i = 0; i < count; i++) {
                    mCirclesList.add(personBuffer.get(i).getDisplayName());
                }
            } finally {
                personBuffer.close();
            }

            mCirclesAdapter.notifyDataSetChanged();

        } else {
            Log.e(TAG, "Error requesting visible circles: " + peopleData.getStatus());
        }*/
    }

    private void onSignedOut() {
        // Update the UI to reflect that the user is signed out.
        mSignInButton.setEnabled(true);
        mSignOutButton.setEnabled(false);
       // mRevokeButton.setEnabled(false);
        mRegisterButton.setEnabled(false);
        mRegisterProfButton.setEnabled(false);
        updateUI(false);
        mStatus.setText(R.string.status_signed_out);
        mEmail.setText("");
        /*mCirclesList.clear();
        mCirclesAdapter.notifyDataSetChanged();*/
    }

    @Override
    public void onConnectionSuspended(int cause) {

        mGoogleApiClient.connect();
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch(id) {
            case DIALOG_PLAY_SERVICES_ERROR:
                if (GooglePlayServicesUtil.isUserRecoverableError(mSignInError)) {
                    return GooglePlayServicesUtil.getErrorDialog(
                            mSignInError,
                            this,
                            RC_SIGN_IN,
                            new DialogInterface.OnCancelListener() {
                                @Override
                                public void onCancel(DialogInterface dialog) {
                                    Log.e(TAG, "Google Play services resolution cancelled");
                                    mSignInProgress = STATE_DEFAULT;
                                    mStatus.setText(R.string.status_signed_out);
                                }
                            });
                } else {
                    return new AlertDialog.Builder(this)
                            .setMessage(R.string.play_services_error)
                            .setPositiveButton(R.string.close,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            Log.e(TAG, "Google Play services error could not be "
                                                    + "resolved: " + mSignInError);
                                            mSignInProgress = STATE_DEFAULT;
                                            mStatus.setText(R.string.status_signed_out);
                                        }
                                    }).create();
                }
            default:
                return super.onCreateDialog(id);
        }
    }
    private class UserTask extends AsyncTask<String, Void, Integer>
    {

        @Override
        protected Integer doInBackground(String... params) {

          try {
              String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
              user = userService.findByEmailIdIgnoreCase(email);
              User temp = null;

              Log.e(TAG, "user" + user);
              Log.e(TAG, "Email" + email);
              if (!user.isEmpty()) {

                  temp = Iterables.getFirst(user, null);
                  Log.e(TAG, "user " + temp);
                  return 1;
              } else {

                 return 0;
              }
          }
          catch(Exception e){
              e.printStackTrace();
          }
            return -1;
        }
        @Override
        protected void onPostExecute(Integer b)
        {
            switch(b){
                case 1:
                    Toast.makeText(getApplicationContext(), "You are already registered, user", Toast.LENGTH_LONG).show();
                    mRegisterButton.setEnabled(false);
                    mRegisterProfButton.setEnabled(false);
                    Person currentUser = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                    String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                    String userName = currentUser.getDisplayName();
                    /*Bundle bundle = new Bundle();
                    bundle.putString("Email", email);
                    bundle.putString("Name", userName);*/

                    Intent intent = new Intent(getApplicationContext(), com.example.joy.tarrgui.activities.ActivityMain.class);
                    //intent.putExtras(bundle);
                    intent.putExtra(NAME,userName);
                    intent.putExtra(EMAIL,email);
                    intent.putExtra(IMGURL,personPhotoUrl);
                    startActivity(intent);
                    break;

                default:
                    Toast.makeText(getApplicationContext(), "You are not registered, Need to register", Toast.LENGTH_LONG).show();
                    break;
            }

        }
    }


 private class checkProf extends AsyncTask<String, Void, Integer >{

     @Override
     protected Integer doInBackground(String... params) {

         prof = profService.findByEmailIdIgnoreCase(params[0]);

         /*Log.e(TAG, "prof" + prof);
         Log.e(TAG, "Email" + params[0]);
         */
         if (!prof.isEmpty()) {

             temp1 = Iterables.getFirst(prof, null);
             //Log.e(TAG, "prof done" + temp1);
             return 2;
         } else {
             //Log.e(TAG, "nothing" + temp1);
             return 0;
         }


     }
    @Override
     protected void onPostExecute(Integer result) {
         switch(result){
             case 2:
                 Person currentProf = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                 String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                 String userName = currentProf.getDisplayName();
                 list = temp1.getCourses();
                 listTa = temp1.getTa();
                 Intent intent = new Intent(getApplicationContext(), com.example.joy.tarrgui.activities.ActivityDynamicTabs.class);
                 intent.putExtra(NAME,userName);
                 intent.putExtra(EMAIL,email);
                 intent.putExtra(IMGURL,personPhotoUrl);
                 professorView = true;
                 System.out.println("prof MainActivity "+EMAIL+URL);
                 startActivity(intent);

                 break;

             case 0:

                 Person currentProf2 = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
                 email = Plus.AccountApi.getAccountName(mGoogleApiClient);
                 userName = currentProf2.getDisplayName();
                 Bundle b = new Bundle();
                 b.putString("Email", email);
                 b.putString("Name", userName);
                 intent = new Intent(getApplicationContext(), RegisterProfessor.class);
                 intent.putExtras(b);
                 startActivity(intent);

                 System.out.println("no professor found");



         }
     }

     }


 private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
     ImageView bmImage;

     public LoadProfileImage(ImageView bmImage) {
         this.bmImage = bmImage;
     }

     protected Bitmap doInBackground(String... urls) {
         String urldisplay = urls[0];
         Bitmap mIcon11 = null;
         try {
             InputStream in = new java.net.URL(urldisplay).openStream();
             mIcon11 = BitmapFactory.decodeStream(in);
         } catch (Exception e) {
             Log.e("Error", e.getMessage());
             e.printStackTrace();
         }
         return mIcon11;
     }

     protected void onPostExecute(Bitmap result) {
         bmImage.setImageBitmap(result);
         /*View v = findViewById(R.id.image_profilepic);
         v.setDrawingCacheEnabled(true);

         Bitmap bitmap = v.getDrawingCache();
         File root = Environment.getExternalStorageDirectory();
         //System.out.println(root.getAbsolutePath()+"/drawable/image.jpg");
         File file = null;
         try
         {
             file = File.createTempFile("image",".jpg",root);
             FileOutputStream ostream = new FileOutputStream(file);
             bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
             ostream.close();
         }
         catch (Exception e)
         {
             e.printStackTrace();
         }
         System.out.println(file.getAbsolutePath());
*/

         UserTask tsk = new UserTask();
         tsk.execute();

         }
 }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show();
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show();
        }
    }




}
