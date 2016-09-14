package com.example.joy.tarr;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.Toast;
import com.example.joy.tarrgui.R;
import java.util.ArrayList;
import java.util.List;

import retrofit.RestAdapter;

/**
 * Created by Himanshu on 3/29/2015.
 */
public class CreateProfile extends Activity implements OnItemSelectedListener {
    private final String URL = "http://192.168.1.16:8080";
    private UserClientApi userService = new RestAdapter.Builder()
            .setEndpoint(URL).setLogLevel(RestAdapter.LogLevel.FULL).build()
            .create(UserClientApi.class);

    private Button mRegisterButton,mClearButton;
    private EditText txtEmail,txtName,txtTaProfessor,txtTaSubject;
    private Spinner spinnerCourse,spinnerBranch,spinnerInstitution;
    private RatingBar rating;
    MultiSelectionSpinner spinnerSkills;
    User  user;

    String _emailID,_name,_course,_branch,_institution,_skills,_taProfessor,_taSubject;
    float _rating=0.0f;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        txtEmail = (EditText) findViewById(R.id.editText_emailID);
        txtName = (EditText) findViewById(R.id.editText_name);
        txtTaProfessor = (EditText) findViewById(R.id.editText_taProfessor);
        txtTaSubject =(EditText) findViewById(R.id.editText_taSubject);
        spinnerCourse = (Spinner) findViewById(R.id.spinner_course);
        spinnerBranch = (Spinner) findViewById(R.id.spinner_branch);
        spinnerInstitution = (Spinner) findViewById(R.id.spinner_institution);
        spinnerSkills = (MultiSelectionSpinner) findViewById(R.id.spinner_skills);
        Intent in = getIntent();
        // get the Bundle that stores the data of this Activity
        Bundle b = in.getExtras();
        if(b!=null) {
            _emailID = b.getString("Email");
            _name = b.getString("Name");

        }

        txtEmail.setText(_emailID);
        txtName.setText(_name);

        spinnerCourse.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> courses = new ArrayList<String>();
        courses.add("BTech 1st year");
        courses.add("BTech 2nd year");
        courses.add("BTech 3rd year");
        courses.add("BTech 4th year");
        courses.add("MTech Ist year");
        courses.add("MTech 2nd year");
        courses.add("PhD");
        courses.add("Research Scholar");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter_course = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, courses);

        // Drop down layout style - list view with radio button
        dataAdapter_course.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

        // attaching data adapter to spinner
//       spinnerCourse.setPrompt("Select Course");
        spinnerCourse.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        dataAdapter_course,
                        R.layout.spinner_row_nothing_selected,
                        this));

        spinnerBranch.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> branch = new ArrayList<String>();
        branch.add("CSE");
        branch.add("ECE");
        branch.add("IT");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter_branch = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, branch);

        // Drop down layout style - list view with radio button
        dataAdapter_branch.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

        // attaching data adapter to spinner
        //spinnerBranch.setAdapter(dataAdapter_branch);
        spinnerBranch.setPrompt("Select Branch");
        spinnerBranch.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        dataAdapter_branch,
                        R.layout.spinner_row_nothing_selected,
                        this));


        spinnerInstitution.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> institution = new ArrayList<String>();
        institution.add("IIIT Delhi");
        institution.add("IIT Delhi");
        institution.add("DTU");
        institution.add("NSIT");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter_institution= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, institution);

        // Drop down layout style - list view with radio button
        dataAdapter_institution.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);

        // attaching data adapter to spinner
        //spinnerInstitution.setAdapter(dataAdapter_institution);
        spinnerInstitution.setPrompt("Select Institution");
        spinnerInstitution.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        dataAdapter_institution,
                        R.layout.spinner_row_nothing_selected,
                        this));

        final List<String> skills = new ArrayList<String>();

        skills.add("C Programming");

        skills.add("C++ Programming");
        skills.add("JAVA Programming");
        skills.add("Android Programming");
        skills.add("Cloud Computing");
        skills.add("Machine Learning");
        skills.add("Data Analytics");
        skills.add("Cryptography");
        skills.add("Bio Informatics");
        skills.add("TOC");
        skills.add("Operating Systems");
        skills.add("Robotics");
        skills.add("Power Electronics");
        spinnerSkills.setItems(skills);

        mRegisterButton = (Button) findViewById(R.id.button_register);
        mClearButton = (Button) findViewById(R.id.button_clear);
        mClearButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                txtEmail.setText("");
                txtName.setText("");
                txtTaProfessor.setText("");
                txtTaSubject.setText("");

            }
        });
        mRegisterButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getData();
                if (_emailID.length() == 0 || _name.length() == 0 || _course.length() == 0 || _branch.length() == 0 || _institution.length() == 0 || _skills.length() == 0 || _taProfessor.length() == 0 || _taSubject.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Complete the form correctly", Toast.LENGTH_SHORT).show();
                } else {

                    Bundle b = new Bundle();
                    b.putString("Email", _emailID);
                    b.putString("Name", _name);
                    b.putString("Course", _course);
                    b.putString("Branch", _branch);
                    b.putString("Institution", _institution);
                    b.putString("Skills", _skills);
                    b.putFloat("Rating", _rating);
                    b.putString("TAProfessor", _taProfessor);
                    b.putString("TASubject", _taSubject);

                    Intent intent = new Intent(CreateProfile.this, DisplayProfile.class);
                    intent.putExtras(b);
                    startActivity(intent);
                }
            }
        });

    }

    public void getData() {
        _emailID = txtEmail.getText().toString();
        _name = txtName.getText().toString();
        _skills = spinnerSkills.getSelectedItemsAsString();
        _taProfessor = txtTaProfessor.getText().toString();
        _taSubject = txtTaSubject.getText().toString();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        switch (parent.getId())
        {
            case R.id.spinner_course:
                if(parent.getItemAtPosition(position)!=null) {
                    _course=parent.getItemAtPosition(position).toString();
                    //Toast.makeText(parent.getContext(), "Selected: " +  _course, Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.spinner_branch:
                if(parent.getItemAtPosition(position)!=null) {
                    _branch=parent.getItemAtPosition(position).toString();
                    //Toast.makeText(parent.getContext(), "Selected: " + _branch, Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.spinner_institution:
                if(parent.getItemAtPosition(position)!=null) {
                    _institution=parent.getItemAtPosition(position).toString();
                    //Toast.makeText(parent.getContext(), "Selected: " + _institution, Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub

    }

private class UserTask extends AsyncTask<String, Void, Boolean>
{

    @Override
    protected Boolean doInBackground(String... params) {
        Boolean ok = userService.addUser(user);
        return ok;
    }
    @Override
    protected void onPostExecute(Boolean b)
    {
        if(b)
            Toast.makeText(getApplicationContext(), "Jai mata Di Done", Toast.LENGTH_LONG).show();
        System.out.println("Registration done");
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
