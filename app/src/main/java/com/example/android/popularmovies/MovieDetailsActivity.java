package com.example.android.popularmovies;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.adapters.ReviewAdapter;
import com.example.android.popularmovies.adapters.TrailerAdapter;
import com.example.android.popularmovies.data.FavoriteContract;
import com.example.android.popularmovies.data.FavoriteDbHelper;
import com.example.android.popularmovies.models.MovieModel;
import com.example.android.popularmovies.models.ReviewModel;
import com.example.android.popularmovies.models.TrailerModel;
import com.example.android.popularmovies.network.FetchReviewTask;
import com.example.android.popularmovies.network.NetworkUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Mourad on 10/2/2017.
 */
public class MovieDetailsActivity extends AppCompatActivity
        implements FetchReviewTask.FetchReviewTaskCallback, LoaderManager.LoaderCallbacks<ArrayList<TrailerModel>> {

    @BindView(R.id.imgview_poster)
    ImageView poster;
    @BindView(R.id.txtview_release_date)
    TextView releaseDate;
    @BindView(R.id.txtview_vote_average)
    TextView voteAvg;
    @BindView(R.id.txtview_original_title)
    TextView title;
    @BindView(R.id.txtview_overview)
    TextView overview;
    @BindView(R.id.fab)
    FloatingActionButton fabFavorite;

    private boolean isFavoriteChanged = false;

    private MovieModel movieModel;
    public ArrayList<ReviewModel> reviewsList = new ArrayList<>();
    public static ReviewAdapter reviewsAdapter;
    public static RecyclerView reviewsRecyclerView;

    private static final String REVIEW_LIST = "reviewList";
    private static final int TRAILERS_LOADER_ID = 0;


    private ArrayList<TrailerModel> trailersList = new ArrayList<>();
    public static TrailerAdapter trailersAdapter;
    private RecyclerView trailersRecyclerView;

    public static String mMovieId;
    String movie_id, thumbnail, movieName, synopsis, rating, release;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("title", title.getText().toString());
        outState.putString("rating", voteAvg.getText().toString());
        outState.putString("date", releaseDate.getText().toString());
        outState.putString("overview", overview.getText().toString());
        outState.putString("image", poster.toString());
        outState.putParcelableArrayList(REVIEW_LIST, (ArrayList<? extends Parcelable>) reviewsList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        movieModel = (MovieModel) getIntent().getParcelableExtra("MOVIE");
        if (movieModel != null) {
//            toolbar.setTitle(movieModel.getMovieTitle());
            movie_id = movieModel.getMovieId();
            thumbnail = movieModel.getMoviePoster();
            movieName = movieModel.getMovieTitle();
            synopsis = movieModel.getMovieOverview();
            rating = movieModel.getDetailedVoteAverage();
            release = movieModel.getMovieRelease();

            Picasso.with(getApplicationContext()).
                    load("https://image.tmdb.org/t/p/w500" + thumbnail).into(poster);
            releaseDate.setText(release);
            voteAvg.setText(rating);
            title.setText(movieName);
            overview.setText(synopsis);
            fabFavorite.setSelected(movieModel.isFavorite());

        }


        // Get Trailers
        mMovieId = movieModel.getMovieId();
        getSupportLoaderManager().initLoader(TRAILERS_LOADER_ID, null, this);
        trailersRecyclerView = (RecyclerView) findViewById(R.id.trailersList);
        trailersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        trailersRecyclerView.setHasFixedSize(true);
        trailersAdapter = new TrailerAdapter(this, trailersList);
        trailersRecyclerView.setAdapter(trailersAdapter);


        // Get reviews
        if (savedInstanceState == null) {
            reviewsRecyclerView = (RecyclerView) findViewById(R.id.reviewsList);
            reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            reviewsAdapter = new ReviewAdapter(reviewsList);
            reviewsRecyclerView.setAdapter(reviewsAdapter);
            new FetchReviewTask(this).execute();
        } else {
            reviewsList = savedInstanceState.getParcelableArrayList(REVIEW_LIST);
            reviewsRecyclerView = (RecyclerView) findViewById(R.id.reviewsList);
            reviewsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            reviewsAdapter = new ReviewAdapter(reviewsList);
            reviewsRecyclerView.setAdapter(reviewsAdapter);
        }

        movieModel.setFavorite(checkIfPresentInFavorites(movieModel.getMovieId()));
        fabFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (movieModel.isFavorite()) { // Already added is removed
                    getContentResolver().delete(FavoriteContract.MovieEntry.CONTENT_URI.buildUpon()
                            .appendPath(String.valueOf(movieModel.getMovieId())).build(), null, null);
                    fabFavorite.setImageResource(R.drawable.ic_favorite_border_white_24dp);
                    Toast.makeText(getApplicationContext(), "Removed from favorites", Toast.LENGTH_LONG).show();
                    movieModel.setFavorite(false);

                } else {
                    ContentValues values = FavoriteDbHelper.getMovieContentValues(movieModel);
                    getContentResolver().insert(FavoriteContract.MovieEntry.CONTENT_URI, values);
                    fabFavorite.setImageResource(R.drawable.ic_favorite_white_24dp);
                    Toast.makeText(getApplicationContext(), "Added To favorites", Toast.LENGTH_LONG).show();
                    movieModel.setFavorite(true);
                }
                fabFavorite.setSelected(movieModel.isFavorite());
            }
        });
    }

    private boolean checkIfPresentInFavorites(String id) {
        Cursor cursor = getContentResolver().query(FavoriteContract.MovieEntry.CONTENT_URI,
                null,
                FavoriteContract.MovieEntry.COLUMN_MOVIE_ID + "='" + id + "'",
                null, null);
        if (cursor != null) {
            if (cursor.getCount() > 0) {
                fabFavorite.setImageResource(R.drawable.ic_favorite_white_24dp);
                return true;
            }
            cursor.close();
        }
        fabFavorite.setImageResource(R.drawable.ic_favorite_border_white_24dp);
        return false;
    }

    @Override
    public void onReviewsReceived(ArrayList<ReviewModel> reviews) {
        this.reviewsList = reviews;
    }


    @Override
    public Loader<ArrayList<TrailerModel>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<ArrayList<TrailerModel>>(this) {

            ArrayList<TrailerModel> mTrailersList = null;

            @Override
            protected void onStartLoading() {
                super.onStartLoading();

                if (mTrailersList != null) {
                    deliverResult(mTrailersList);
                } else {
                    forceLoad();
                }
            }

            public void deliverResult(ArrayList<TrailerModel> data) {
                mTrailersList = data;
                super.deliverResult(data);
            }

            @Override
            public ArrayList<TrailerModel> loadInBackground() {
                String trailersUrl = "https://api.themoviedb.org/3/movie/" +
                        mMovieId + "/videos" + "?api_key=" + BuildConfig.API_KEY;
                ArrayList<TrailerModel> result = NetworkUtils.fetchTrailersData(trailersUrl);
                return result;
            }


        };
    }

    @Override
    public void onLoadFinished(Loader<ArrayList<TrailerModel>> loader, ArrayList<TrailerModel> data) {
        trailersAdapter.addAll(data);
    }

    @Override
    public void onLoaderReset(Loader<ArrayList<TrailerModel>> loader) {

    }
}
