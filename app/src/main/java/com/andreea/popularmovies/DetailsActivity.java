package com.andreea.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.andreea.popularmovies.model.Movie;
import com.andreea.popularmovies.utils.NetworkUtils;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import static com.andreea.popularmovies.utils.MovieConstants.MOVIE_DETAILS_KEY;

public class DetailsActivity extends AppCompatActivity {
    private static final String TAG = DetailsActivity.class.getSimpleName();
    private Movie movie;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private TextView titleTv;
    private TextView plotSynopsisTextView;
    private TextView releaseDateTextView;
    private TextView voteAverageTextView;
    private ImageView posterImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
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

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        titleTv = (TextView) findViewById(R.id.movie_title_tv);
        plotSynopsisTextView = (TextView) findViewById(R.id.plot_synopsis_tv);
        releaseDateTextView = (TextView) findViewById(R.id.release_date_tv);
        voteAverageTextView = (TextView) findViewById(R.id.vote_average_tv);
        posterImageView = (ImageView) findViewById(R.id.movie_poster_iv);

        displayMovieDetails(movie);
    }

    private void displayMovieDetails(final Movie movie) {
        if (movie != null) {
            collapsingToolbarLayout.setTitle(movie.getTitle());
            titleTv.setText(movie.getTitle());
            plotSynopsisTextView.setText(movie.getOverview());
            releaseDateTextView.setText(movie.getReleaseDate());
            voteAverageTextView.setText(String.format("%s/10", movie.getVoteAverage()));

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
}
