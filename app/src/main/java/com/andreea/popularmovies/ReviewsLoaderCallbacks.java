package com.andreea.popularmovies;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.andreea.popularmovies.model.Review;
import com.andreea.popularmovies.utils.JsonUtils;
import com.andreea.popularmovies.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.andreea.popularmovies.utils.MovieConstants.API_KEY;
import static com.andreea.popularmovies.utils.MovieConstants.MOVIE_ID_KEY;


public class ReviewsLoaderCallbacks implements LoaderManager.LoaderCallbacks<List<Review>> {
    private static final String TAG = ReviewsLoaderCallbacks.class.getSimpleName();
    private final Context context;
    private final ReviewsAdapter reviewsAdapter;

    public ReviewsLoaderCallbacks(Context context, ReviewsAdapter reviewsAdapter) {
        this.context = context;
        this.reviewsAdapter = reviewsAdapter;
    }

    @Override
    public Loader<List<Review>> onCreateLoader(int id, Bundle args) {
        return new ReviewsAsyncTaskLoader(context, args);
    }

    @Override
    public void onLoadFinished(Loader<List<Review>> loader, List<Review> data) {
        Log.d(TAG, "onLoadFinished: " + data);
        reviewsAdapter.refreshReviews(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Review>> loader) {

    }

    private static class ReviewsAsyncTaskLoader extends AsyncTaskLoader<List<Review>> {
        private static final String TAG = ReviewsAsyncTaskLoader.class.getSimpleName();
        private final Bundle args;
        private List<Review> reviews;

        public ReviewsAsyncTaskLoader(Context context, Bundle args) {
            super(context);
            this.args = args;
            if (reviews != null) {
                deliverResult(reviews);
            } else {
                forceLoad();
            }
        }

        @Override
        public List<Review> loadInBackground() {
            long movieId = args.getLong(MOVIE_ID_KEY);
            URL reviewsUrl = NetworkUtils.buildMovieReviewsUrl(movieId, API_KEY);

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(reviewsUrl)
                    .get()
                    .build();
            try {
                Response response = client.newCall(request).execute();
                try {
                    ResponseBody body = response.body();
                    if (response.isSuccessful() && body != null) {
                        String json = body.string();
                        Log.d(TAG, "loadInBackground: " + json);
                        return JsonUtils.parseReviewsResponse(json);
                    } else {
                        Log.e(TAG, String.format("loadInBackground: Reviews request to %s was not successful.", reviewsUrl));
                    }
                } finally {
                    response.close();
                }
            } catch (IOException e) {
                Log.e(TAG, "Failed to parse reviews json response: ", e);
            }
            return Collections.emptyList();
        }

        @Override
        public void deliverResult(List<Review> data) {
            reviews = data;
            super.deliverResult(data);
        }
    }
}
