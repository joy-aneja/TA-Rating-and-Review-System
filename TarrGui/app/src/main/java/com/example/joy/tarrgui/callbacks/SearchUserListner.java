package com.example.joy.tarrgui.callbacks;

import com.example.joy.tarr.User;
import com.example.joy.tarrgui.pojo.Movie;

import java.util.ArrayList;

/**
 * Created by joy on 15-04-2015.
 */
public interface SearchUserListner {
    public void onBoxOfficeMoviesLoaded(ArrayList<User> listMovies);
}
