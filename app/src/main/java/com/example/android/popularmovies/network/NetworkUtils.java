package com.example.android.popularmovies.network;

import android.util.Log;

import com.example.android.popularmovies.models.ReviewModel;
import com.example.android.popularmovies.models.TrailerModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Mourad on 1/31/2018.
 */

public class NetworkUtils {

    private static URL createURL(String stringURL) {
        URL url = null;
        try {
            url = new URL(stringURL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        if (url == null) {
            return jsonResponse;
        }
        HttpURLConnection urlConnection = null;
        BufferedReader bufferedReader = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            InputStream inputStream = urlConnection.getInputStream();
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                StringBuffer stringBuffer = new StringBuffer();
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }
                jsonResponse = stringBuffer.toString();

            } else {
            }
        } catch (IOException e) {
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return jsonResponse;
    }

    private static ArrayList<ReviewModel> extractReviewsFromJson(String url) {
        ArrayList<ReviewModel> reviewsList = new ArrayList<>();
        final String MOVIE_ID = "id";
        final String RESULTS = "results";
        final String AUTHOR = "author";
        final String CONTENT = "content";

        try {
            JSONObject reviewsJson = new JSONObject(url);
            String movieId = reviewsJson.getString(MOVIE_ID);
            JSONArray reviewsArray = reviewsJson.getJSONArray(RESULTS);
            for (int i = 0; i < reviewsArray.length(); i++) {
                JSONObject reviewJSONObject = reviewsArray.getJSONObject(i);
                String author = reviewJSONObject.getString(AUTHOR);
                String content = reviewJSONObject.getString(CONTENT);
                ReviewModel reviews = new ReviewModel();
                reviews.setId(movieId);
                reviews.setAuthor(author);
                reviews.setContent(content);

                reviewsList.add(reviews);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return reviewsList;
    }

    public static ArrayList<ReviewModel> fetchReviewData(String requestUrl) {
        URL url = createURL(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<ReviewModel> reviews = extractReviewsFromJson(jsonResponse);
        Log.v("reviewsListFromNetwork", reviews.toString());
        return reviews;
    }


    private static ArrayList<TrailerModel> extractTrailersFromJson(String url) {
        ArrayList<TrailerModel> trailersList = new ArrayList<>();
        final String MOVIE_ID = "id";
        final String TRAILER_ID = "id";
        final String RESULTS = "results";
        final String KEY = "key";
        final String NAME = "name";
        try {
            JSONObject trailersJson = new JSONObject(url);
            String movieId = trailersJson.getString(MOVIE_ID);
            JSONArray trailersArray = trailersJson.getJSONArray(RESULTS);
            for (int i = 0; i < trailersArray.length(); i++) {
                JSONObject trailerJSONObject = trailersArray.getJSONObject(i);
                String trailerId = trailerJSONObject.getString(TRAILER_ID);
                String key = trailerJSONObject.getString(KEY);
                String name = trailerJSONObject.getString(NAME);
                TrailerModel trailers = new TrailerModel();
                trailers.setMovieId(movieId);
                trailers.setTrailerId(trailerId);
                trailers.setKey(key);
                trailers.setName(name);
                trailersList.add(trailers);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return trailersList;
    }

    public static ArrayList<TrailerModel> fetchTrailersData(String requestUrl) {
        URL url = createURL(requestUrl);
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ArrayList<TrailerModel> trailers = extractTrailersFromJson(jsonResponse);
        return trailers;
    }
}
