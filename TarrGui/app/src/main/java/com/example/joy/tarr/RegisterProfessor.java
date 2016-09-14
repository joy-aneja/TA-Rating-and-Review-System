package com.example.joy.tarr;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

import retrofit.RestAdapter;

/**
 * Created by Himanshu on 4/11/2015.
 */
import com.example.joy.tarrgui.R;
public class RegisterProfessor extends Activity implements OnItemSelectedListener {
    private final String URL = "http://192.168.1.16:8080";
    private ProfessorClientApi profService = new RestAdapter.Builder()
            .setEndpoint(URL).setLogLevel(RestAdapter.LogLevel.FULL).build()
            .create(ProfessorClientApi.class);

    private Button mRegisterButton, mAddCourse, mAddTA;
    private EditText txtEmail,txtName,txtTA;;
    private Spinner spinnerDepartment,spinnerSubject;
    private TextView txtCourseTA;
    Professor professor;

    String _emailID,_name,_department,_courseTA,_TAlist;

    public int searchList (Vector<String> l, String str){
        int flag =0;
        if(l.contains(str)){
            return l.indexOf(str);
        }
        return -1;

        /*for (int k = 0; k<l.size();k++){
            if(str.equalsIgnoreCase(l.get(k))) {
                flag=1;
                return k;
            }

        }
        if (flag == 0)
            return -1;
        if (l.isEmpty())
        return -2;

        return -3;*/
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signupprofessor);

        txtEmail = (EditText) findViewById(R.id.editText_emailID);
        txtName = (EditText) findViewById(R.id.editText_name);
        spinnerDepartment = (Spinner) findViewById(R.id.spinner_department);
        spinnerSubject = (Spinner) findViewById(R.id.spinner_Subject);
        txtTA = (EditText) findViewById(R.id.editText_ta);
        txtCourseTA =(TextView) findViewById(R.id.textView_courseTA);
        mAddTA = (Button) findViewById(R.id.button_addta);
        mRegisterButton = (Button) findViewById(R.id.button_register);

        Intent in = getIntent();
        Bundle b = in.getExtras();
        if(b!=null) {
            _emailID = b.getString("Email");
            _name = b.getString("Name");
        }

        txtEmail.setText(_emailID);
        txtName.setText(_name);

        spinnerDepartment.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> departments = new ArrayList<String>();
        departments.add("CSE");
        departments.add("ECE");
        departments.add("Economics");
        departments.add("Computational Biology");

        ArrayAdapter<String> dataAdapter_departments = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, departments);

        // Drop down layout style - list view with radio button
        dataAdapter_departments.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);


        spinnerDepartment.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        dataAdapter_departments,
                        R.layout.spinner_row_nothing_selected,
                        this));

        spinnerSubject.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> subjects = new ArrayList<String>();
        subjects.add("PCSMA");
        subjects.add("Operating System");
        subjects.add("Advanced Mobile Computing");
        subjects.add("Computer Networks");

        ArrayAdapter<String> dataAdapter_subjects = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, subjects);

        // Drop down layout style - list view with radio button
        dataAdapter_subjects.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);


        spinnerSubject.setAdapter(
                new NothingSelectedSpinnerAdapter(
                        dataAdapter_subjects,
                        R.layout.spinner_row_nothing_selected,
                        this));

        mAddTA.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (_courseTA.length() == 0 ) {
                    Toast.makeText(getApplicationContext(), "Choose Course", Toast.LENGTH_SHORT).show();
                }
                else if(txtTA.getText().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Add TA correctly", Toast.LENGTH_SHORT).show();

                }
                else {
                    txtCourseTA.setText(txtCourseTA.getText() + "," + _courseTA + ":" + txtTA.getText() );
                    txtTA.setText("");
                }
            }
        });


        mRegisterButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                getData();
                if (_emailID.length() == 0 || _name.length() == 0 || _department.length() == 0 || _TAlist.length() == 0) {
                    Toast.makeText(getApplicationContext(), "Complete the form correctly", Toast.LENGTH_SHORT).show();
                } else {

                    Vector<String> courses= new Vector<String>();
                    Vector<Vector<String>> ta=new Vector<Vector<String>>();
                    _TAlist = _TAlist.substring(1);
                    List<String> colon_list = Arrays.asList(_TAlist.split("\\s*,\\s*"));
                    //courses.addAll(courses_list);
                    for (int i =0; i<colon_list.size();i++) {
                        List<String> sub_list = Arrays.asList((colon_list.get(i).split("\\s*:\\s*")));

                        if(searchList(courses,sub_list.get(0))<0) {
                            courses.add(sub_list.get(0));
                            //ta.get(searchList(courses, sub_list.get(0))).add(sub_list.get(1));
                            ta.add(new Vector<String>());
                            ta.get(searchList(courses, sub_list.get(0))).add(sub_list.get(1));

                                                    }
                        else {
                            /*Vector<String> tmp = new Vector<String>();
                            tmp = ta.get(searchList(courses,sub_list.get(0)));
                            tmp.add(sub_list.get(0));
                            ta.set(searchList(courses,sub_list.get(0)),tmp);*/
                            //ta.get(searchList(courses,sub_list.get(0))).add(sub_list.get(1));
                            if (!ta.isEmpty())
                                ta.get(searchList(courses, sub_list.get(0))).add(sub_list.get(1));
                            else {
                                ta.add(new Vector<String>());
                                ta.get(0).add(sub_list.get(1));

                            }
                        }
                    }
                    String id = UUID.randomUUID().toString();
                    professor = new Professor(id,_emailID,
                            _name,_department,courses,ta);
                    ProfTask tsk = new ProfTask();
                    tsk.execute();

                }
            }
        });

    }

    public void getData() {
        _emailID = txtEmail.getText().toString();
        _name = txtName.getText().toString();
        _TAlist=txtCourseTA.getText().toString();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        switch (parent.getId())
        {
            case R.id.spinner_department:
                if(parent.getItemAtPosition(position)!=null) {
                    _department=parent.getItemAtPosition(position).toString();
                }
                break;

            case R.id.spinner_Subject:
                if(parent.getItemAtPosition(position)!=null) {
                    _courseTA=parent.getItemAtPosition(position).toString();
                }
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub

    }

    private class ProfTask extends AsyncTask<String, Void, Boolean>
    {

        @Override
        protected Boolean doInBackground(String... params) {
            Boolean ok = profService.addProfessor(professor);

            return ok;
        }
        @Override
        protected void onPostExecute(Boolean b)
        {
            //if(b)
                Toast.makeText(getApplicationContext(), "Jai mata Di Done", Toast.LENGTH_LONG).show();
            System.out.println("Registration done");
            onBackPressed();
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
