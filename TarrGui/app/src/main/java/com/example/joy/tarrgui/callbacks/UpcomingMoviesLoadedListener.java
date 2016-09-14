package com.example.joy.tarrgui.callbacks;

import java.util.ArrayList;

import com.example.joy.tarrgui.pojo.Movie;

/**
 * Created by Windows on 13-04-2015.
 */
public interface UpcomingMoviesLoadedListener {
    public void onUpcomingMoviesLoaded(ArrayList<Movie> listMovies);
}
