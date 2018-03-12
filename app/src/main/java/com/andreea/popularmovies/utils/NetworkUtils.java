package com.andreea.popularmovies.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Log;

import java.net.MalformedURLException;
import java.net.URL;

public final class NetworkUtils {
    private static final String TAG = NetworkUtils.class.getSimpleName();
    private static final String PAGE_PARAM = "page";
    private static final String LANGUAGE_PARAM = "language";
    private static final String API_KEY_PARAM = "api_key";

    private static final String DEFAULT_PAGE = "1";
    private static final String DEFAULT_LANGUAGE = "en_US";

    private static final String BASE_MOVIE_URL = "https://api.themoviedb.org/3/movie/";
    private static final String BASE_POSTER_URL = "http://image.tmdb.org/t/p/w185/";

    private static final String REVIEWS_PATH = "reviews";
    private static final String VIDEOS_PATH = "videos";
    public static final String BASE_YOUTUBE_URI = "http://www.youtube.com";

    private NetworkUtils() {
    }

    public static boolean isConnectedToNetwork(Context context) {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = null;
        if (cm != null) {
            activeNetwork = cm.getActiveNetworkInfo();
        }
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

    public static URL buildMovieListUrl(String sortOrder, String movieApiKey) {
        Uri builtUri = Uri.parse(BASE_MOVIE_URL).buildUpon()
                .appendPath(sortOrder)
                .appendQueryParameter(PAGE_PARAM, DEFAULT_PAGE)
                .appendQueryParameter(LANGUAGE_PARAM, DEFAULT_LANGUAGE)
                .appendQueryParameter(API_KEY_PARAM, movieApiKey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, "Failed to build movie db URL:", e);
        }
        return url;
    }

    public static String buildPosterUrl(String posterPath) {
        return BASE_POSTER_URL + posterPath;
    }

    public static URL buildMovieReviewsUrl(long movieId, String movieApiKey) {
        Uri builtUri = Uri.parse(BASE_MOVIE_URL).buildUpon()
                .appendPath(Long.toString(movieId))
                .appendPath(REVIEWS_PATH)
                .appendQueryParameter(PAGE_PARAM, DEFAULT_PAGE)
                .appendQueryParameter(LANGUAGE_PARAM, DEFAULT_LANGUAGE)
                .appendQueryParameter(API_KEY_PARAM, movieApiKey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, "Failed to build movie db reviews URL:", e);
        }
        return url;
    }

    public static URL buildMovieVideosUrl(long movieId, String movieApiKey) {
        Uri builtUri = Uri.parse(BASE_MOVIE_URL).buildUpon()
                .appendPath(Long.toString(movieId))
                .appendPath(VIDEOS_PATH)
                .appendQueryParameter(LANGUAGE_PARAM, DEFAULT_LANGUAGE)
                .appendQueryParameter(API_KEY_PARAM, movieApiKey)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            Log.e(TAG, "Failed to build movie db videos URL:", e);
        }
        return url;
    }

    public static Uri buildYoutubeUri(String videoKey){
        return Uri.parse(BASE_YOUTUBE_URI).buildUpon()
                .appendPath("watch")
                .appendQueryParameter("v", videoKey)
                .build();

    }
}
