package com.andreea.popularmovies;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import com.andreea.popularmovies.model.Movie;
import com.andreea.popularmovies.utils.JsonUtils;
import com.andreea.popularmovies.utils.MovieConstants;
import com.andreea.popularmovies.utils.NetworkUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static com.andreea.popularmovies.data.MovieContract.FavoriteMovie.COLUMN_MOVIE_ID;
import static com.andreea.popularmovies.data.MovieContract.FavoriteMovie.COLUMN_OVERVIEW;
import static com.andreea.popularmovies.data.MovieContract.FavoriteMovie.COLUMN_POSTER_PATH;
import static com.andreea.popularmovies.data.MovieContract.FavoriteMovie.COLUMN_RELEASE_DATE;
import static com.andreea.popularmovies.data.MovieContract.FavoriteMovie.COLUMN_TITLE;
import static com.andreea.popularmovies.data.MovieContract.FavoriteMovie.COLUMN_VOTE_AVERAGE;
import static com.andreea.popularmovies.data.MovieContract.FavoriteMovie.CONTENT_URI;
import static com.andreea.popularmovies.utils.MovieConstants.MOVIES_SORT_BY_KEY;
import static com.andreea.popularmovies.utils.MovieSortOrder.FAVORITE;

public class MoviesAsyncTaskLoader extends AsyncTaskLoader<List<Movie>> {
    private static final String TAG = MoviesAsyncTaskLoader.class.getSimpleName();
    private List<Movie> movies;
    private final ContentResolver contentResolver;
    private final Bundle args;

    public MoviesAsyncTaskLoader(Context context, Bundle args) {
        super(context);
        this.contentResolver = context.getContentResolver();
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
        List<Movie> favorites = getMoviesOffline();
        if (FAVORITE.getValue().equals(sortOrder)) {
            // Query the content provider
            return favorites;
        } else {
            // Query TMDb api
            List<Movie> moviesOnline = getMoviesOnline(sortOrder);
            setFavoriteMovies(moviesOnline, favorites);
            return moviesOnline;
        }
    }

    @Override
    public void deliverResult(List<Movie> data) {
        movies = data;
        super.deliverResult(data);
    }

    private List<Movie> getMoviesOnline(String sortOrder) {
        URL moviesUrl = NetworkUtils.buildMovieListUrl(sortOrder, MovieConstants.API_KEY);

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(moviesUrl)
                .get()
                .build();
        try {
            try (Response response = client.newCall(request).execute()) {
                ResponseBody body = response.body();
                if (response.isSuccessful() && body != null) {
                    String json = body.string();
                    Log.d(TAG, "loadInBackground: " + json);
                    return JsonUtils.parseMoviesResponse(json);
                } else {
                    Log.e(TAG, String.format("loadInBackground: Movies request to %s was not successful.", moviesUrl));
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to parse movies json response: ", e);
        }
        return Collections.emptyList();
    }

    private List<Movie> getMoviesOffline() {

        try (Cursor cursor = contentResolver.query(CONTENT_URI,
                null,
                null,
                null,
                null)) {
            if (cursor != null && cursor.getCount() > 0) {
                List<Movie> favoriteMovies = new ArrayList<>(cursor.getCount());
                while (cursor.moveToNext()) {
                    int idIndex = cursor.getColumnIndex(COLUMN_MOVIE_ID);
                    int titleIndex = cursor.getColumnIndex(COLUMN_TITLE);
                    int overviewIndex = cursor.getColumnIndex(COLUMN_OVERVIEW);
                    int voteIndex = cursor.getColumnIndex(COLUMN_VOTE_AVERAGE);
                    int releaseDateIndex = cursor.getColumnIndex(COLUMN_RELEASE_DATE);
                    int posterPathIndex = cursor.getColumnIndex(COLUMN_POSTER_PATH);

                    Movie movie = new Movie();
                    movie.setFavorite(true);
                    movie.setId(cursor.getInt(idIndex));
                    movie.setTitle(cursor.getString(titleIndex));
                    movie.setOverview(cursor.getString(overviewIndex));
                    movie.setVoteAverage(cursor.getDouble(voteIndex));
                    movie.setReleaseDate(cursor.getString(releaseDateIndex));
                    movie.setPosterPath(cursor.getString(posterPathIndex));
                    favoriteMovies.add(movie);
                }
                return favoriteMovies;
            }
        }
        return Collections.emptyList();
    }

    private void setFavoriteMovies(List<Movie> movies, List<Movie> favorites) {
        List<Long> favoriteMovieIds = new ArrayList<>();
        for (Movie favorite : favorites) {
            favoriteMovieIds.add(favorite.getId());
        }
        for (Movie movie : movies) {
            if (favoriteMovieIds.contains(movie.getId())) {
                movie.setFavorite(true);
            }
        }
    }
}