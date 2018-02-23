package com.andreea.popularmovies;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.andreea.popularmovies.model.Movie;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity implements
        LoaderManager.LoaderCallbacks<List<Movie>> {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int COLUMNS = 2;
    private static final int MOVIES_LOADER_ID = 77;
    private List<Movie> movieList = new ArrayList<>();
    private MoviesAdapter adapter;
    private static final String API_KEY = BuildConfig.API_KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MoviesAdapter adapter = new MoviesAdapter(this, movieList);
        fillListWithMockMovies();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.movies_grid_view);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, COLUMNS);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        // init loader
        getSupportLoaderManager().initLoader(MOVIES_LOADER_ID, null, this);
    }

    private void fillListWithMockMovies() {
        Movie m1 = new Movie("http://img.moviepostershop.com/ghostbusters-movie-poster-2016-1020775586.jpg");
        movieList.add(m1);
        movieList.add(m1);
        movieList.add(m1);
        movieList.add(m1);
        movieList.add(m1);
        movieList.add(m1);
        movieList.add(m1);
    }

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        return new MoviesAsyncTaskLoader(this);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        movieList = data;
//        adapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {

    }

    private static class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.PosterViewHolder> {


        private Context context;
        private List<Movie> movieList;

        public class PosterViewHolder extends RecyclerView.ViewHolder {
            public ImageView posterImageView;

            public PosterViewHolder(View view) {
                super(view);
                posterImageView = (ImageView) view.findViewById(R.id.movie_poster_iv);
            }
        }

        public MoviesAdapter(Context context, List<Movie> movieList) {
            this.context = context;
            this.movieList = movieList;
        }

        @Override
        public PosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.movie_item, parent, false);

            return new PosterViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final PosterViewHolder holder, int position) {
            final Movie movie = movieList.get(position);

            Picasso.with(context)
                    .load(movie.getPosterUrl())
                    .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                    .error(android.R.drawable.stat_notify_error)
                    .into(holder.posterImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.i(TAG, "Picasso successfully loaded poster for movie: " + movie);
                        }

                        @Override
                        public void onError() {
                            Log.e(TAG, "Picasso failed to load poster for movie: " + movie);
                        }
                    });
        }

        @Override
        public int getItemCount() {
            return movieList.size();
        }
    }

    private static class MoviesAsyncTaskLoader extends AsyncTaskLoader<List<Movie>> {
        private List<Movie> movies;

        public MoviesAsyncTaskLoader(Context context) {
            super(context);
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
            String url = "https://api.themoviedb.org/3/movie/popular?page=1&language=en-US&api_key="+API_KEY;
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            try {
                Response response = client.newCall(request).execute();
                Log.d(TAG, "loadInBackground: "+response.body().string());
            } catch (IOException e) {
                e.printStackTrace();
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
