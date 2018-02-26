package com.andreea.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.andreea.popularmovies.model.Movie;
import com.andreea.popularmovies.utils.JsonUtils;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<Movie>>, MoviesAdapter.MovieOnClickHandler {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int COLUMNS = 2;
    private static final int MOVIES_LOADER_ID = 77;
    private static final String MOVIES_URL = "movies-url";
    private RecyclerView recyclerView;
    private static final String API_KEY = BuildConfig.API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.movies_grid_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, COLUMNS);
        recyclerView.setLayoutManager(mLayoutManager);

        // init loader
        String url = "https://api.themoviedb.org/3/movie/popular?page=1&language=en-US&api_key=" + API_KEY;
        Bundle moviesQuery = new Bundle();
        moviesQuery.putString(MOVIES_URL, url);
        getSupportLoaderManager().initLoader(MOVIES_LOADER_ID, moviesQuery, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedItemId = item.getItemId();
        if (selectedItemId == R.id.sort_popular) {
            item.setChecked(true);
            String url = "https://api.themoviedb.org/3/movie/popular?page=1&language=en-US&api_key=" + API_KEY;
            Bundle moviesQuery = new Bundle();
            moviesQuery.putString(MOVIES_URL, url);
            getSupportLoaderManager().restartLoader(MOVIES_LOADER_ID, moviesQuery, this);
            return true;
        }
        if (selectedItemId == R.id.sort_rating) {
            item.setChecked(true);
            String url = "https://api.themoviedb.org/3/movie/top_rated?page=1&language=en-US&api_key=" + API_KEY;
            Bundle moviesQuery = new Bundle();
            moviesQuery.putString(MOVIES_URL, url);
            getSupportLoaderManager().restartLoader(MOVIES_LOADER_ID, moviesQuery, this);
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
        detailsActivityIntent.putExtra("Movie", movie);
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
            OkHttpClient client = new OkHttpClient();
            String url = args.getString(MOVIES_URL);
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            try {
                Response response = client.newCall(request).execute();
                String json = response.body().string();
                Log.d(TAG, "loadInBackground: " + json);

                return JsonUtils.parseMoviesResponse(json);
            } catch (IOException e) {
                Log.e(TAG, "Failed to parse movies json response: ", e);
            }
            return null;
        }

        @Override
        public void deliverResult(List<Movie> data) {
            movies = data;
            super.deliverResult(data);
        }
    }
}
