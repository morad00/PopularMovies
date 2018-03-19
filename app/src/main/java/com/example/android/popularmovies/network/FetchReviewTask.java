package com.example.android.popularmovies.network;

import android.os.AsyncTask;
import android.util.Log;

import com.example.android.popularmovies.BuildConfig;
import com.example.android.popularmovies.MovieDetailsActivity;
import com.example.android.popularmovies.models.ReviewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mourad on 1/31/2018.
 */

public class FetchReviewTask extends AsyncTask<Void, Void, ArrayList<ReviewModel>> {
    FetchReviewTaskCallback callback;

    public FetchReviewTask(FetchReviewTaskCallback callback) {
        this.callback = callback;
    }

    public interface FetchReviewTaskCallback {
        void onReviewsReceived(ArrayList<ReviewModel> reviews);
    }

    @Override
    protected ArrayList<ReviewModel> doInBackground(Void... voids) {
        String reviewsUrl = "https://api.themoviedb.org/3/movie/" +
                MovieDetailsActivity.mMovieId + "/reviews" + "?api_key=" + BuildConfig.API_KEY;
        ArrayList<ReviewModel> result = NetworkUtils.fetchReviewData(reviewsUrl);
        return result;
    }

    @Override
    protected void onPostExecute(ArrayList<ReviewModel> reviewsList) {
        super.onPostExecute(reviewsList);
        MovieDetailsActivity.reviewsAdapter.setReviewsData(reviewsList);
        callback.onReviewsReceived(reviewsList);
    }
}
