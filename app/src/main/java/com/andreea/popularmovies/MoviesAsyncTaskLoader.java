package com.andreea.popularmovies;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.andreea.popularmovies.model.Movie;
import com.andreea.popularmovies.utils.JsonUtils;
import com.andreea.popularmovies.utils.MovieConstants;
import com.andreea.popularmovies.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.andreea.popularmovies.utils.MovieConstants.MOVIES_SORT_BY_KEY;

public class MoviesAsyncTaskLoader extends AsyncTaskLoader<List<Movie>> {
    private static final String TAG = MoviesAsyncTaskLoader.class.getSimpleName();
    private List<Movie> movies;
    private final Bundle args;

    public MoviesAsyncTaskLoader(Context context, Bundle args) {
        super(context);
        this.args = args;
        if (movies != null) {
            deliverResult(movies);
        } else {
            forceLoad();
        }
    }

    @Override
    public List<Movie> loadInBackground() {
        String sortOrder = args.getString(MOVIES_SORT_BY_KEY);
        URL moviesUrl = NetworkUtils.buildMovieListUrl(sortOrder, MovieConstants.API_KEY);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(moviesUrl)
                .get()
                .build();
        try {
            Response response = client.newCall(request).execute();
            try {
                ResponseBody body = response.body();
                if (response.isSuccessful() && body != null) {
                    String json = body.string();
                    Log.d(TAG, "loadInBackground: " + json);
                    return JsonUtils.parseMoviesResponse(json);
                } else {
                    Log.e(TAG, String.format("loadInBackground: Movies request to %s was not successful.", moviesUrl));
                }
            } finally {
                response.close();
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to parse movies json response: ", e);
        }
        return Collections.emptyList();
    }

    @Override
    public void deliverResult(List<Movie> data) {
        movies = data;
        super.deliverResult(data);
    }
}