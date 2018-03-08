package com.andreea.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andreea.popularmovies.data.MovieContract.FavoriteMovie;
import com.andreea.popularmovies.model.Movie;
import com.andreea.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.andreea.popularmovies.utils.MovieConstants.MOVIE_DETAILS_KEY;
import static com.andreea.popularmovies.utils.TextFormatUtils.formatReleaseDate;
import static com.andreea.popularmovies.utils.TextFormatUtils.formatVoteAverage;

public class DetailsActivity extends AppCompatActivity {
    private static final String TAG = DetailsActivity.class.getSimpleName();
    private Movie movie;

    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbarLayout;
    @BindView(R.id.movie_title_tv)
    TextView titleTv;
    @BindView(R.id.plot_synopsis_tv)
    TextView plotSynopsisTextView;
    @BindView(R.id.release_date_tv)
    TextView releaseDateTextView;
    @BindView(R.id.vote_average_tv)
    TextView voteAverageTextView;
    @BindView(R.id.movie_poster_iv)
    ImageView posterImageView;
    @BindView(R.id.favorite_fab)
    FloatingActionButton favoriteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        Intent moviesIntent = getIntent();

        if (moviesIntent != null && moviesIntent.hasExtra(MOVIE_DETAILS_KEY)) {
            movie = moviesIntent.getParcelableExtra(MOVIE_DETAILS_KEY);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeButtonEnabled(true);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }

        displayMovieDetails(movie);
    }

    private void displayMovieDetails(final Movie movie) {
        if (movie != null) {
            collapsingToolbarLayout.setTitle(movie.getTitle());
            titleTv.setText(movie.getTitle());
            plotSynopsisTextView.setText(movie.getOverview());
            releaseDateTextView.setText(formatReleaseDate(movie.getReleaseDate()));
            voteAverageTextView.setText(formatVoteAverage(movie.getVoteAverage()));
            toggleStarForFavorite(movie);

            String moviePosterUrl = NetworkUtils.buildPosterUrl(movie.getPosterPath());
            Picasso.with(this)
                    .load(moviePosterUrl)
                    .placeholder(android.R.drawable.progress_horizontal)
                    .error(android.R.drawable.stat_notify_error)
                    .into(posterImageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d(TAG, "Picasso successfully loaded poster for movie: " + movie);
                        }

                        @Override
                        public void onError() {
                            Log.e(TAG, "Picasso failed to load poster for movie: " + movie);
                        }
                    });
        } else {
            Toast.makeText(this, getString(R.string.no_details_message), Toast.LENGTH_LONG).show();
        }
    }

    public void onClickAddFavorite(View view) {
        if (movie.isFavorite()) {
            // Remove from favorites
            long id = movie.getId();
            String stringId = Long.toString(id);
            Uri uri = FavoriteMovie.CONTENT_URI;
            uri = uri.buildUpon().appendPath(stringId).build();
            int deletedRows = getContentResolver().delete(uri, null, null);
            if (deletedRows != 0) {
                Log.i(TAG, "Removed favorite movie " + uri);
                // Set movie favorite to false
                movie.setFavorite(false);
                toggleStarForFavorite(movie);
            }
        } else {
            // Add to favorites
            ContentValues contentValues = new ContentValues();
            contentValues.put(FavoriteMovie.COLUMN_TITLE, movie.getTitle());
            contentValues.put(FavoriteMovie.COLUMN_MOVIE_ID, movie.getId());
            contentValues.put(FavoriteMovie.COLUMN_OVERVIEW, movie.getOverview());
            contentValues.put(FavoriteMovie.COLUMN_RELEASE_DATE, movie.getReleaseDate());
            contentValues.put(FavoriteMovie.COLUMN_VOTE_AVERAGE, movie.getVoteAverage());
            contentValues.put(FavoriteMovie.COLUMN_POSTER_PATH, movie.getPosterPath());
            Uri uri = getContentResolver().insert(FavoriteMovie.CONTENT_URI, contentValues);
            if (uri != null) {
                Log.i(TAG, "Added favorite movie " + uri);
                // Set movie favorite to true
                movie.setFavorite(true);
                toggleStarForFavorite(movie);
            }
        }
    }

    private void toggleStarForFavorite(Movie movie) {
        if (movie.isFavorite()) {
            // Set star icon on
            favoriteButton.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_on));
        } else {
            // Set star icon off
            favoriteButton.setImageDrawable(getResources().getDrawable(android.R.drawable.btn_star_big_off));
        }
    }
}
