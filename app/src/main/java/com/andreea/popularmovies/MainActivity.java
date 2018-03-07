package com.andreea.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.andreea.popularmovies.model.Movie;
import com.andreea.popularmovies.utils.NetworkUtils;

import java.util.List;

import static com.andreea.popularmovies.utils.MovieConstants.MOVIES_LOADER_ID;
import static com.andreea.popularmovies.utils.MovieConstants.MOVIES_SORT_BY_KEY;
import static com.andreea.popularmovies.utils.MovieConstants.MOVIE_DETAILS_KEY;
import static com.andreea.popularmovies.utils.MovieConstants.MOVIE_GRID_COLUMNS;
import static com.andreea.popularmovies.utils.NetworkUtils.MovieSortOrder.POPULAR;
import static com.andreea.popularmovies.utils.NetworkUtils.MovieSortOrder.TOP_RATED;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<Movie>>, MoviesAdapter.MovieOnClickHandler {
    private static final String TAG = MainActivity.class.getSimpleName();

    private final Bundle sortOrderBundle = new Bundle();
    private RecyclerView recyclerView;
    private View snackbarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        snackbarView = findViewById(android.R.id.content);
        recyclerView = (RecyclerView) findViewById(R.id.movies_grid_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, MOVIE_GRID_COLUMNS);
        recyclerView.setLayoutManager(mLayoutManager);

        if (!NetworkUtils.isConnectedToNetwork(this)) {
            displayNoInternetMessage();
            return;
        }

        // Set default sort order to Popular Movies
        sortOrderBundle.putString(MOVIES_SORT_BY_KEY, POPULAR.getValue());

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
            sortOrderBundle.putString(MOVIES_SORT_BY_KEY, POPULAR.getValue());
            getSupportLoaderManager().restartLoader(MOVIES_LOADER_ID, sortOrderBundle, this);
            return true;
        }
        if (selectedItemId == R.id.sort_rating) {
            item.setChecked(true);
            sortOrderBundle.putString(MOVIES_SORT_BY_KEY, TOP_RATED.getValue());
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
        if (data == null || data.isEmpty()) {
            return;
        }
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
}
