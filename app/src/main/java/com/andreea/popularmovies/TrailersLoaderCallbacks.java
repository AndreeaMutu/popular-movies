package com.andreea.popularmovies;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;

import com.andreea.popularmovies.model.Video;
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


public class TrailersLoaderCallbacks implements LoaderManager.LoaderCallbacks<List<Video>> {
    private static final String TAG = TrailersLoaderCallbacks.class.getSimpleName();
    private final Context context;
    private final VideosAdapter videosAdapter;

    public TrailersLoaderCallbacks(Context context, VideosAdapter videosAdapter) {
        this.context = context;
        this.videosAdapter = videosAdapter;
    }

    @Override
    public Loader<List<Video>> onCreateLoader(int id, Bundle args) {
        return new TrailersAsyncTaskLoader(context, args);
    }

    @Override
    public void onLoadFinished(Loader<List<Video>> loader, List<Video> data) {
        Log.d(TAG, "onLoadFinished: " + data);
        videosAdapter.refresh(data);
    }

    @Override
    public void onLoaderReset(Loader<List<Video>> loader) {

    }

    private static class TrailersAsyncTaskLoader extends AsyncTaskLoader<List<Video>> {
        private static final String TAG = TrailersAsyncTaskLoader.class.getSimpleName();
        private final Bundle args;
        private List<Video> videos;

        public TrailersAsyncTaskLoader(Context context, Bundle args) {
            super(context);
            this.args = args;
            if (videos != null) {
                deliverResult(videos);
            } else {
                forceLoad();
            }
        }

        @Override
        public List<Video> loadInBackground() {
            long movieId = args.getLong(MOVIE_ID_KEY);
            URL videosUrl = NetworkUtils.buildMovieVideosUrl(movieId, API_KEY);

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(videosUrl)
                    .get()
                    .build();
            try {
                try (Response response = client.newCall(request).execute()) {
                    ResponseBody body = response.body();
                    if (response.isSuccessful() && body != null) {
                        String json = body.string();
                        Log.d(TAG, "loadInBackground: " + json);
                        return JsonUtils.parseVideosResponse(json);
                    } else {
                        Log.e(TAG, String.format("loadInBackground: Videos request to %s was not successful.", videosUrl));
                    }
                }
            } catch (IOException e) {
                Log.e(TAG, "Failed to parse videos json response: ", e);
            }
            return Collections.emptyList();
        }

        @Override
        public void deliverResult(List<Video> data) {
            videos = data;
            super.deliverResult(data);
        }
    }
}
