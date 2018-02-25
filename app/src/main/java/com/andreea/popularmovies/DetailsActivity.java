package com.andreea.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent moviesIntent = getIntent();

        if (moviesIntent != null) {
            if (moviesIntent.hasExtra(Intent.EXTRA_TEXT)) {
                String movieToString = moviesIntent.getStringExtra(Intent.EXTRA_TEXT);
                Toast.makeText(this,movieToString,Toast.LENGTH_LONG).show();
            }
        }
    }
}
