package com.andreea.popularmovies.utils;


import com.andreea.popularmovies.model.Movie;
import com.andreea.popularmovies.model.MovieResponse;
import com.andreea.popularmovies.model.Review;
import com.andreea.popularmovies.model.ReviewResponse;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Collections;
import java.util.List;

public final class JsonUtils {
    private JsonUtils() {
    }

    public static List<Movie> parseMoviesResponse(String moviesResponseJson) {
        Gson gson = initGson();
        MovieResponse movieResponse = gson.fromJson(moviesResponseJson, MovieResponse.class);
        if (movieResponse != null) {
            return movieResponse.getResults();
        }
        return Collections.emptyList();
    }

    public static List<Review> parseReviewsResponse(String reviewsResponseJson) {
        Gson gson = initGson();
        ReviewResponse reviewResponse = gson.fromJson(reviewsResponseJson, ReviewResponse.class);
        if (reviewResponse != null) {
            return reviewResponse.getResults();
        }
        return Collections.emptyList();
    }

    private static Gson initGson() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES);
        return gsonBuilder.create();
    }
}
