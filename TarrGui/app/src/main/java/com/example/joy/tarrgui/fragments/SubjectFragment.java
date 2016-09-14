package com.example.joy.tarrgui.fragments;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.joy.tarr.MainActivity;
import com.example.joy.tarr.Session;
import com.example.joy.tarr.SessionClientApi;
import com.example.joy.tarr.User;
import com.example.joy.tarr.UserClientApi;
import com.example.joy.tarrgui.R;
import com.example.joy.tarrgui.adapters.AdapterSessionNotify;
import com.example.joy.tarrgui.adapters.AdapterUser;
import com.example.joy.tarrgui.callbacks.SearchUserListner;
import com.example.joy.tarrgui.callbacks.SessionNotifyListner;
import com.example.joy.tarrgui.extras.SortListener;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Vector;

import retrofit.RestAdapter;

/**
 * Created by joy on 21-04-2015.
 */
public class SubjectFragment extends Fragment implements SearchUserListner,SwipeRefreshLayout.OnRefreshListener {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;


    public int fragmentNo =-1;

    private final String URL = "http://192.168.1.16:8080";


    private UserClientApi userServiceSearch = new RestAdapter.Builder()
            .setEndpoint(URL).setLogLevel(RestAdapter.LogLevel.FULL).build()
            .create(UserClientApi.class);


    private ArrayList<User> mListUser = new ArrayList<User>();
    //the adapter responsible for displaying our movies within a RecyclerView
    private AdapterUser mAdapter;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    //the recyclerview containing showing all our movies
    private RecyclerView mRecyclerMovies;
    private EditText search;
    private ImageButton imgbtn;

    Collection<User> searchEmail ;
    public static Vector<User> dynList;

    public SubjectFragment() {
        // Required empty public constructor

    }



    public static SubjectFragment newInstance(String param1, String param2) {
        SubjectFragment fragment = new SubjectFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static SubjectFragment newInstance2(int pos) {
        SubjectFragment fragment = new SubjectFragment();
        fragment.fragmentNo = pos;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        final View view = inflater.inflate(R.layout.fragment_search, container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeUser);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        search= (EditText) view.findViewById(R.id.editText);
        imgbtn = (ImageButton) view.findViewById(R.id.imageButton);
        if (MainActivity.professorView==true){
            search.setVisibility(View.GONE);
            imgbtn.setVisibility(View.GONE);
        }
        mRecyclerMovies = (RecyclerView) view.findViewById(R.id.listUser);
        //set the layout manager before trying to display data
        mRecyclerMovies.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new AdapterUser(getActivity());
        mRecyclerMovies.setAdapter(mAdapter);

        //new SessionTask(getActivity(),mRecyclerMovies).execute("");
        onBoxOfficeMoviesLoaded(mListUser);
        // code here
        System.out.println(MainActivity.listTa.toString());
        new SearchTask(getActivity(),mRecyclerMovies).execute();

        dynList = new Vector<User>();


        return view;
    }

    @Override
    public void onStart(){
        super.onStart();

        new SearchTask(getActivity(),mRecyclerMovies).execute();

    }



    @Override
    public void onBoxOfficeMoviesLoaded(ArrayList<User> listMovies) {
        if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
        mAdapter.setMovies(listMovies);
    }

    @Override
    public void onRefresh() {
        mSwipeRefreshLayout.setRefreshing(false);
    }

    class SearchTask extends AsyncTask<Integer,String,Boolean> {
        RecyclerView tv;
        Activity mContext;
        Collection<User> temp;


        public SearchTask(Activity cont, RecyclerView temp){
            this.tv= temp;
            this.mContext=cont;

        }
        @Override
        protected Boolean doInBackground(Integer... params) {

            for (int j=0;j<MainActivity.listTa.get(fragmentNo-1).size();j++){
                System.out.println("j="+j);

                try{
                    searchEmail = userServiceSearch.findByEmailIdIgnoreCase(MainActivity.listTa.get(fragmentNo-1).get(j).toString())  ;
                    dynList.add(Iterables.getFirst(searchEmail,null));
                }catch (Exception e){
                    e.printStackTrace();
                }

                }


            if (dynList.isEmpty()) {
                return false;
            } else {

                return true;
            }
        }

        protected void onPostExecute(Boolean b)
        {

            if(b){

                    System.out.println("dynlist"+dynList);
                    //if(!mListUser.contains(new ArrayList<User>().addAll(searchName)))
                    if(!mListUser.containsAll(dynList))
                        mListUser.addAll(dynList);
                /*for(User iter:searchName){
                    iter.getEmailId();
                    txtSearchResult.setText(txtSearchResult.getText()+" "+iter.getEmailId()+" "+iter.getName());
                }*/





                Toast.makeText(getActivity(), "Jai mata Di Done", Toast.LENGTH_LONG).show();
            }
            else
                Toast.makeText(getActivity(), "No records found!", Toast.LENGTH_LONG).show();


            onBoxOfficeMoviesLoaded(mListUser);

            System.out.print("Search Professor Profile");
        }
    }


}
