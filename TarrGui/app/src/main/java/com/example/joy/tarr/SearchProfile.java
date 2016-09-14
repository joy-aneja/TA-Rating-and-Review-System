package com.example.joy.tarr;

import android.app.Activity;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Collection;

import retrofit.RestAdapter;
import com.example.joy.tarrgui.R;
/**
 * Created by Himanshu on 4/12/2015.
 */
public class SearchProfile extends Activity {


    private final String URL = "http://192.168.1.16:8080";
    String str_search;
    Collection<User> searchName, searchSkills, searchCourse;
    private UserClientApi userService = new RestAdapter.Builder()
            .setEndpoint(URL).setLogLevel(RestAdapter.LogLevel.FULL).build()
            .create(UserClientApi.class);

    private Button mSearch;
    private EditText txtSearch;
    private TextView txtSearchResult;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        txtSearch = (EditText) findViewById(R.id.editText_search);
        txtSearchResult = (TextView) findViewById(R.id.textView_searchResult);
        mSearch = (Button) findViewById(R.id.button_search);

        mSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                UserTask tsk = new UserTask();
                tsk.execute();



            }
        });
    }
    private class UserTask extends AsyncTask<String, Void, Boolean>
    {

        @Override
        protected Boolean doInBackground(String... params) {
            searchSkills = userService.findBySkillsContainingIgnoreCase(txtSearch.getText().toString());
            searchName = userService.findByNameContainingIgnoreCase(txtSearch.getText().toString());
            searchCourse = userService.findByCourseContainingIgnoreCase(txtSearch.getText().toString());

            if (searchSkills.isEmpty()&& searchName.isEmpty() && searchCourse.isEmpty()) {
                return false;
            } else {

                return true;
            }

        }
        @Override
        protected void onPostExecute(Boolean b)
        {

            if(b){
                if(!searchSkills.isEmpty()){
                    for(User iter:searchSkills){
                        iter.getEmailId();
                        txtSearchResult.setText(txtSearchResult.getText()+" "+iter.getEmailId()+" "+iter.getName());
                    }
                }
                if(!searchName.isEmpty()){
                    for(User iter:searchName){
                        iter.getEmailId();
                        txtSearchResult.setText(txtSearchResult.getText()+" "+iter.getEmailId()+" "+iter.getName());
                    }
                }

                if(!searchCourse.isEmpty()){
                    for(User iter:searchCourse){
                        iter.getEmailId();
                        txtSearchResult.setText(txtSearchResult.getText()+" "+iter.getEmailId()+" "+iter.getName());
                    }
                }


                Toast.makeText(getApplicationContext(), "Jai mata Di Done", Toast.LENGTH_LONG).show();
            }
            else
                Toast.makeText(getApplicationContext(), "Kuch to gadbad hai", Toast.LENGTH_LONG).show();



            System.out.print("Search Profile");
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
