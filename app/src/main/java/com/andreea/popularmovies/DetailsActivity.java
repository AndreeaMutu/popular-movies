package com.andreea.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.andreea.popularmovies.model.Movie;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent moviesIntent = getIntent();

        if (moviesIntent != null) {
            if (moviesIntent.hasExtra("Movie")) {
                Movie movie = moviesIntent.getParcelableExtra("Movie");
                Toast.makeText(this, movie.toString(), Toast.LENGTH_LONG).show();
            }
        }
    }
}
