package com.andreea.popularmovies.utils;

import com.andreea.popularmovies.BuildConfig;

public class MovieConstants {
    public static final String MOVIE_DETAILS_KEY = "movie";
    public static final String MOVIES_SORT_BY_KEY = "sortBy";
    public static final String SELECTED_SORT_OPTION= "sortOption";
    public static final String MOVIE_ID_KEY = "movieId";

    public static final String API_KEY = BuildConfig.API_KEY;

    public static final int MOVIES_LOADER_ID = 77;
    public static final int REVIEWS_LOADER_ID = 52;
    public static final int VIDEOS_LOADER_ID = 39;
    public static final int MOVIE_GRID_COLUMNS = 2;

    private MovieConstants() {
    }
}
