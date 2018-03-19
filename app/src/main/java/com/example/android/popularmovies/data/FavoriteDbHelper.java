package com.example.android.popularmovies.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.popularmovies.models.MovieModel;
import com.example.android.popularmovies.data.FavoriteContract.MovieEntry;

import java.util.ArrayList;

/**
 * Created by Mourad on 2/3/2018.
 */

public class FavoriteDbHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 9;
    private static final String DATABASE_NAME = "movies.db";

    public FavoriteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + MovieEntry.TABLE_NAME
                + " (" +
                MovieEntry._ID + " INTEGER PRIMARY KEY," +
                MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                MovieEntry.COLUMN_MOVIE_TITLE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_MOVIE_POSTER_PATH + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_MOVIE_OVERVIEW + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE + " TEXT NOT NULL, " +
                MovieEntry.COLUMN_MOVIE_RELEASE_DATE + " TEXT NOT NULL" +
                " );";
        db.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FavoriteContract.MovieEntry.TABLE_NAME);
        onCreate(db);
    }


    public static ContentValues getMovieContentValues(MovieModel movies) {
        ContentValues values = new ContentValues();
        values.put(FavoriteContract.MovieEntry.COLUMN_MOVIE_ID, movies.getMovieId());
        values.put(FavoriteContract.MovieEntry.COLUMN_MOVIE_TITLE, movies.getMovieTitle());
        values.put(FavoriteContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH, movies.getMoviePoster());
        values.put(FavoriteContract.MovieEntry.COLUMN_MOVIE_OVERVIEW, movies.getMovieOverview());
        values.put(FavoriteContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE, movies.getDetailedVoteAverage());
        values.put(FavoriteContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE, movies.getMovieRelease());

        return values;
    }
}