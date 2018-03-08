package com.andreea.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.andreea.popularmovies.data.MovieContract.FavoriteMovie;


public class MovieDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movieDb.db";

    private static final int VERSION = 1;

    MovieDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String CREATE_TABLE = "CREATE TABLE " + FavoriteMovie.TABLE_NAME + " (" +
                FavoriteMovie.COLUMN_MOVIE_ID + " INTEGER PRIMARY KEY, " +
                FavoriteMovie.COLUMN_TITLE + " TEXT NOT NULL, "+
                FavoriteMovie.COLUMN_OVERVIEW + " TEXT, "+
                FavoriteMovie.COLUMN_RELEASE_DATE + " TEXT, "+
                FavoriteMovie.COLUMN_VOTE_AVERAGE + " REAL, "+
                FavoriteMovie.COLUMN_POSTER_PATH + " TEXT);";

        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteMovie.TABLE_NAME);
        onCreate(db);
    }
}
