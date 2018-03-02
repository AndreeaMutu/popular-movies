package com.andreea.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.andreea.popularmovies.model.Movie;
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

import static com.andreea.popularmovies.utils.MovieConstants.MOVIE_DETAILS_KEY;
import static com.andreea.popularmovies.utils.NetworkUtils.MovieSortOrder.POPULAR;
import static com.andreea.popularmovies.utils.NetworkUtils.MovieSortOrder.TOP_RATED;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<Movie>>, MoviesAdapter.MovieOnClickHandler {
    private static final String TAG = MainActivity.class.getSimpleName();

    private static final int GRID_COLUMNS = 2;
    private static final int MOVIES_LOADER_ID = 77;
    private static final String MOVIES_SORT_BY = "sortBy";
    private Bundle sortOrderBundle = new Bundle();
    private RecyclerView recyclerView;
    private View snackbarView;
    private static final String API_KEY = BuildConfig.API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        snackbarView = findViewById(android.R.id.content);
        recyclerView = (RecyclerView) findViewById(R.id.movies_grid_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, GRID_COLUMNS);
        recyclerView.setLayoutManager(mLayoutManager);

        if (!NetworkUtils.isConnectedToNetwork(this)) {
            displayNoInternetMessage();
            return;
        }

        // Set default sort order to Popular Movies
        sortOrderBundle.putString(MOVIES_SORT_BY, POPULAR.getValue());

        // Initialize movie loader
        getSupportLoaderManager().initLoader(MOVIES_LOADER_ID, sortOrderBundle, this);
    }

    private void displayNoInternetMessage() {
        Snackbar.make(snackbarView, getString(R.string.no_internet_message), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!NetworkUtils.isConnectedToNetwork(this)) {
            displayNoInternetMessage();
            return super.onOptionsItemSelected(item);
        }
        int selectedItemId = item.getItemId();
        if (selectedItemId == R.id.sort_popular) {
            item.setChecked(true);
            sortOrderBundle.putString(MOVIES_SORT_BY, POPULAR.getValue());
            getSupportLoaderManager().restartLoader(MOVIES_LOADER_ID, sortOrderBundle, this);
            return true;
        }
        if (selectedItemId == R.id.sort_rating) {
            item.setChecked(true);
            sortOrderBundle.putString(MOVIES_SORT_BY, TOP_RATED.getValue());
            getSupportLoaderManager().restartLoader(MOVIES_LOADER_ID, sortOrderBundle, this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        return new MoviesAsyncTaskLoader(this, args);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        Log.d(TAG, "onLoadFinished: " + data);
        MoviesAdapter adapter = new MoviesAdapter(this, data, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {

    }

    @Override
    public void onClick(Movie movie) {
        Intent detailsActivityIntent = new Intent(this, DetailsActivity.class);
        detailsActivityIntent.putExtra(MOVIE_DETAILS_KEY, movie);
        startActivity(detailsActivityIntent);
    }

    private static class MoviesAsyncTaskLoader extends AsyncTaskLoader<List<Movie>> {
        private List<Movie> movies;
        private Bundle args;

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
        protected void onStartLoading() {
            super.onStartLoading();
        }

        @Override
        public List<Movie> loadInBackground() {
            String sortOrder = args.getString(MOVIES_SORT_BY);
            URL moviesUrl = NetworkUtils.buildMovieListUrl(sortOrder, API_KEY);

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url(moviesUrl)
                    .get()
                    .build();
            try (Response response = client.newCall(request).execute()) {
                ResponseBody body = response.body();
                if (response.isSuccessful() && body != null) {
                    String json = body.string();
                    Log.d(TAG, "loadInBackground: " + json);
                    return JsonUtils.parseMoviesResponse(json);
                } else {
                    Log.e(TAG, String.format("loadInBackground: Movies request to %s was not successful.", moviesUrl));
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
}
