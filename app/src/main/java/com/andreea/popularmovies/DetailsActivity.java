package com.andreea.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.andreea.popularmovies.model.Movie;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class DetailsActivity extends AppCompatActivity {
    private static final String TAG = DetailsActivity.class.getSimpleName();
    private static final String MOVIE_KEY = "Movie";
    private Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent moviesIntent = getIntent();

        if (moviesIntent != null && moviesIntent.hasExtra(MOVIE_KEY)) {
            movie = moviesIntent.getParcelableExtra(MOVIE_KEY);
        }

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout ctl = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        TextView titleTv = (TextView) findViewById(R.id.movie_title_tv);
        TextView plotSynopsisTextView = (TextView) findViewById(R.id.plot_synopsis_tv);
        TextView releaseDateTextView = (TextView) findViewById(R.id.release_date_tv);
        TextView voteAverageTextView = (TextView) findViewById(R.id.vote_average_tv);
        ImageView posterImageView = (ImageView) findViewById(R.id.movie_poster_iv);

        if (movie != null) {
            ctl.setTitle(movie.getTitle());
            titleTv.setText(movie.getTitle());
            plotSynopsisTextView.setText(movie.getOverview());
            releaseDateTextView.setText(movie.getReleaseDate());
            voteAverageTextView.setText(movie.getVoteAverage()+"/10");

            Picasso.with(this)
                    .load(R.drawable.zoo)
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
        }
    }
}
