package com.example.android.popularmovies.models;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Mourad on 1/29/2018.
 */

public class TrailerModel implements Parcelable {

    private String movieId;
    private String trailerId;
    private String key;

    public TrailerModel() {

    }

    public String getMovieId() {
        return movieId;
    }

    public void setMovieId(String movieId) {
        this.movieId = movieId;
    }

    public void setTrailerId(String trailerId) {
        this.trailerId = trailerId;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;


    public String getTrailerId() {
        return trailerId;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(movieId);
        dest.writeString(trailerId);
        dest.writeString(key);
        dest.writeString(name);
    }

    protected TrailerModel(Parcel in) {
        movieId = in.readString();
        trailerId = in.readString();
        key = in.readString();
        name = in.readString();
    }

    public static final Creator<TrailerModel> CREATOR = new Creator<TrailerModel>() {
        @Override
        public TrailerModel createFromParcel(Parcel in) {
            return new TrailerModel(in);
        }

        @Override
        public TrailerModel[] newArray(int size) {
            return new TrailerModel[size];
        }
    };
}
