package com.example.android.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mourad on 9/29/2017.
 */

public class MovieModel implements Parcelable {
    private String movieId;
    private String movieTitle;
    private String moviePoster;
    private String movieOverview;
    private String movieVote;
    private String movieRelease;
    private boolean isFavorite;

    public MovieModel() {
    }

    public String getMovieId() {
        return movieId;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getMoviePoster() {
        return moviePoster;
    }

    public String getMovieOverview() {
        return movieOverview;
    }

    public String getMovieRelease() {
        return movieRelease;
    }

    public String getMovieVote() {
        return movieVote;
    }

    public String getDetailedVoteAverage() {
        return String.valueOf(getMovieVote()) + "/10";
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public void setMoviePoster(String moviePoster) {
        this.moviePoster = moviePoster;
    }

    public void setMovieOverview(String movieOverview) {
        this.movieOverview = movieOverview;
    }

    public void setMovieVote(String movieVote) {
        this.movieVote = movieVote;
    }

    public void setMovieRelease(String movieRelease) {
        this.movieRelease = movieRelease;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieId);
        dest.writeString(moviePoster);
        dest.writeString(movieTitle);
        dest.writeString(movieOverview);
        dest.writeString(movieRelease);
        dest.writeString(movieVote);
        dest.writeByte(this.isFavorite ? (byte) 1 : (byte) 0);
    }

    private MovieModel(Parcel in) {
        movieId = in.readString();
        moviePoster = in.readString();
        movieTitle = in.readString();
        movieOverview = in.readString();
        movieRelease = in.readString();
        movieVote = in.readString();
        this.isFavorite = in.readByte() != 0;
    }

    public static final Parcelable.Creator<MovieModel> CREATOR = new Parcelable.Creator<MovieModel>() {
        public MovieModel createFromParcel(Parcel in) {
            return new MovieModel(in);
        }

        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };

    public MovieModel[] newArray(int size) {
        return new MovieModel[size];
    }

};



