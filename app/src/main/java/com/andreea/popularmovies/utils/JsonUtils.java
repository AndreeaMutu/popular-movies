package com.andreea.popularmovies.utils;


import com.andreea.popularmovies.model.Movie;
import com.andreea.popularmovies.model.MovieResponse;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Collections;
import java.util.List;

public final class JsonUtils {
    public static List<Movie> parseMoviesResponse(String moviesResponseJson) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        Gson gson = gsonBuilder.create();
        MovieResponse movieResponse = gson.fromJson(moviesResponseJson, MovieResponse.class);
        if (movieResponse != null) {
            return movieResponse.getResults();
        }
        return Collections.emptyList();
    }
}
