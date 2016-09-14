package com.example.joy.tarrgui.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;


import com.example.joy.tarr.Professor;
import com.example.joy.tarr.ProfessorClientApi;
import com.example.joy.tarr.User;
import com.example.joy.tarr.UserClientApi;
import com.example.joy.tarrgui.R;
import com.example.joy.tarrgui.activities.ActivityDynamicTabs;
import com.example.joy.tarrgui.activities.ActivityMain;
import com.google.common.collect.Iterables;
import com.pkmmte.view.CircularImageView;

import java.io.InputStream;
import java.util.Collection;

import retrofit.RestAdapter;

/**
 * Created by joy on 20-04-2015.
 */
public class DummyFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CircularImageView profileImage;
    public TextView profileName;
    public TextView profileEmail;
    public TextView profileCourse;
    public RatingBar profileRating;
    public TextView profileSkills;
    public TextView ptextAccept, ptextPend, ptextDec;
    public ImageView profileAccept, profileDec, profilePend;


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


    public static FragmentProfile newInstance(String param1, String param2) {
        FragmentProfile fragment = new FragmentProfile();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profileImage = (CircularImageView) view.findViewById(R.id.profileImage);
        profileName = (TextView) view.findViewById(R.id.profileName);
        profileCourse = (TextView) view.findViewById(R.id.profileCourse);
        profileEmail = (TextView) view.findViewById(R.id.profileEmail);
        profileRating = (RatingBar) view.findViewById(R.id.profileRating);
        profileSkills = (TextView) view.findViewById(R.id.profileSkills);
        profileAccept = (ImageView) view.findViewById(R.id.profileAccept);
        profileDec = (ImageView) view.findViewById(R.id.profileDec);
        profilePend =(ImageView) view.findViewById(R.id.profilePend);
        ptextAccept = (TextView) view.findViewById(R.id.ptextAccepted);
        ptextPend = (TextView) view.findViewById(R.id.ptextPend);
        ptextDec = (TextView) view.findViewById(R.id.ptextDec);



        return view ;
    }

    @Override
    public void onStart(){
        super.onStart();
        new UserTask().execute();
        new LoadProfileImage(profileImage).execute(ActivityDynamicTabs.PURL);


    }

    private class UserTask extends AsyncTask<String, Void, Integer>
    {
        @Override
        protected void onPreExecute(){
            System.out.println("DummyFrag"+ActivityDynamicTabs.PEMAIL+ActivityDynamicTabs.PURL);
        }
        @Override
        protected Integer doInBackground(String... params) {



                //Log.e(TAG, "check prof");
                profCol = profService.findByEmailIdIgnoreCase(ActivityDynamicTabs.PEMAIL);
                Professor temp1 = null;
                //Log.e(TAG, "prof" + prof);
                //Log.e(TAG, "Email" + email);
                if (!profCol.isEmpty()) {

                    temp1 = Iterables.getFirst(profCol, null);
                    //Log.e(TAG, "prof done" + temp1);
                    return 2;
                } else {
                    // Log.e(TAG, "nothing" + temp1);
                    return 0;
                }
        }



        @Override
        protected void onPostExecute(Integer b)
        {
            switch(b) {

                case 2:
                    prof = Iterables.getFirst(profCol, null);
                    profileName.setText(prof.getName().toString());
                    profileEmail.setText(prof.getEmailId().toString());
                    profileCourse.setText(prof.getDepartment().toString());
                    profileSkills.setText(prof.getCourses().toString());
                    profileRating.setVisibility(View.GONE);
                    profileAccept.setVisibility(View.GONE);
                    profileDec.setVisibility(View.GONE);
                    profilePend.setVisibility(View.GONE);
                    ptextAccept.setVisibility(View.GONE);
                    ptextDec.setVisibility(View.GONE);
                    ptextPend.setVisibility(View.GONE);
                    break;
            }
        }
    }

    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        CircularImageView bmImage;

        public LoadProfileImage(CircularImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                //Log.e("Error", e.getMessage());
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



        }
    }



}
