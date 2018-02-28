package com.andreea.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.andreea.popularmovies.model.Movie;

public class DetailsActivity extends AppCompatActivity {
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

        if (movie != null) {
            ctl.setTitle(movie.getTitle());
            titleTv.setText(movie.getTitle());
            plotSynopsisTextView.setText(movie.getOverview());
        }
    }
}
