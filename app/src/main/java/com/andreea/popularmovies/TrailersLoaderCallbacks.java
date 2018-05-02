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
            String json = "{\n" +
                    "  \"id\": 269149,\n" +
                    "  \"results\": [\n" +
                    "    {\n" +
                    "      \"id\": \"571cb2c0c3a36843150006ed\",\n" +
                    "      \"iso_639_1\": \"en\",\n" +
                    "      \"iso_3166_1\": \"US\",\n" +
                    "      \"key\": \"zQ2XkyDTW34\",\n" +
                    "      \"name\": \"Have a Donut\",\n" +
                    "      \"site\": \"YouTube\",\n" +
                    "      \"size\": 1080,\n" +
                    "      \"type\": \"Clip\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": \"571cb2f5c3a36842aa00078c\",\n" +
                    "      \"iso_639_1\": \"en\",\n" +
                    "      \"iso_3166_1\": \"US\",\n" +
                    "      \"key\": \"g9lmhBYB11U\",\n" +
                    "      \"name\": \"Official US Teaser Trailer\",\n" +
                    "      \"site\": \"YouTube\",\n" +
                    "      \"size\": 1080,\n" +
                    "      \"type\": \"Trailer\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": \"571cb34bc3a3684e98001257\",\n" +
                    "      \"iso_639_1\": \"en\",\n" +
                    "      \"iso_3166_1\": \"US\",\n" +
                    "      \"key\": \"b8hYj0ROMo4\",\n" +
                    "      \"name\": \"Elephant in the Room\",\n" +
                    "      \"site\": \"YouTube\",\n" +
                    "      \"size\": 1080,\n" +
                    "      \"type\": \"Clip\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": \"5797586d9251410639002054\",\n" +
                    "      \"iso_639_1\": \"en\",\n" +
                    "      \"iso_3166_1\": \"US\",\n" +
                    "      \"key\": \"jWM0ct-OLsM\",\n" +
                    "      \"name\": \"Official US Trailer #2\",\n" +
                    "      \"site\": \"YouTube\",\n" +
                    "      \"size\": 1080,\n" +
                    "      \"type\": \"Trailer\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": \"58f21fb6c3a3682e95009661\",\n" +
                    "      \"iso_639_1\": \"en\",\n" +
                    "      \"iso_3166_1\": \"US\",\n" +
                    "      \"key\": \"bY73vFGhSVk\",\n" +
                    "      \"name\": \"Official US Sloth Trailer\",\n" +
                    "      \"site\": \"YouTube\",\n" +
                    "      \"size\": 1080,\n" +
                    "      \"type\": \"Trailer\"\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";
            return JsonUtils.parseVideosResponse(json);
        }

        @Override
        public void deliverResult(List<Video> data) {
            videos = data;
            super.deliverResult(data);
        }
    }
}
