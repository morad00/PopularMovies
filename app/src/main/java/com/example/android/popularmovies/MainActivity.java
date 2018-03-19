package com.example.android.popularmovies;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;

import com.example.android.popularmovies.adapters.MovieAdapter;
import com.example.android.popularmovies.data.FavoriteContract;
import com.example.android.popularmovies.data.FavoriteDbHelper;
import com.example.android.popularmovies.models.MovieModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {
    private Toolbar toolbar;
    private List<MovieModel> listMovies;
    private RecyclerView recyclerView;
    private MovieAdapter adapter;
    private static final int CURSOR_LOADER_ID = 1;
    boolean firsTime = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        listMovies = new ArrayList<>();
        recyclerView.setHasFixedSize(true);

        adapter = new MovieAdapter(listMovies, new MovieAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(MovieModel item) {
                Intent intent = new Intent(MainActivity.this, MovieDetailsActivity.class);
                intent.putExtra("MOVIE", item);
                startActivity(intent);
            }
        });

        recyclerView.setAdapter(adapter);
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        } else {
            recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        }


        new BeginAsyncTask().execute("popular");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this,
                FavoriteContract.MovieEntry.CONTENT_URI,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        List<MovieModel> movieList = new ArrayList<>();
        if (data != null && data.moveToFirst()) {
            do {
                String id = data.getString(FavoriteContract.MovieEntry.COL_MOVIE_ID);
                String title = data.getString(FavoriteContract.MovieEntry.COL_MOVIE_TITLE);
                String posterPath = data.getString(FavoriteContract.MovieEntry.COL_MOVIE_POSTER_PATH);
                String overview = data.getString(FavoriteContract.MovieEntry.COL_MOVIE_OVERVIEW);
                String rating = data.getString(FavoriteContract.MovieEntry.COL_MOVIE_VOTE_AVERAGE);
                String releaseDate = data.getString(FavoriteContract.MovieEntry.COL_MOVIE_RELEASE_DATE);
                MovieModel movieModel = new MovieModel();
                movieModel.setMovieId(id);
                movieModel.setMovieTitle(title);
                movieModel.setMoviePoster(posterPath);
                movieModel.setMovieOverview(overview);
                movieModel.setMovieVote(rating);
                movieModel.setMovieRelease(releaseDate);
                movieList.add(movieModel);
            } while (data.moveToNext());
        }
        adapter.clear();
        adapter.add(movieList);
    }


    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    public class BeginAsyncTask extends AsyncTask<String, Void, Integer> {
        HttpURLConnection httpURLConnection = null;
        BufferedReader bufferedReader = null;
        String strJson;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listMovies.clear();
        }

        @Override
        protected Integer doInBackground(String... params) {
            try {
                String strUrl = "http://api.themoviedb.org/3/movie/" + params[0] + "?api_key=" + BuildConfig.API_KEY;
                URL url = new URL(strUrl);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.connect();
                InputStream inputStream = httpURLConnection.getInputStream();
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                StringBuffer stringBuffer = new StringBuffer();
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line);
                }
                strJson = stringBuffer.toString();
                convertJSON(strJson);
                return 1;
            } catch (MalformedURLException e) {
                e.printStackTrace();
                return 0;
            } catch (IOException e) {
                e.printStackTrace();
                return 0;
            }
        }

        private void convertJSON(String strJson) {
            final String objRESULT = "results";
            final String varID = "id";
            final String varTILTE = "original_title";
            final String varPoster = "poster_path";
            final String varOverview = "overview";
            final String varVoteAvg = "vote_average";
            final String varRealease = "release_date";
            try {
                JSONObject jsonObject = new JSONObject(strJson);
                JSONArray jsonArray = jsonObject.getJSONArray(objRESULT);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject j = jsonArray.getJSONObject(i);
                    MovieModel movieModel = new MovieModel();
                    movieModel.setMovieId(j.getString(varID));
                    movieModel.setMovieTitle(j.getString(varTILTE));
                    movieModel.setMoviePoster(j.getString(varPoster));
                    movieModel.setMovieOverview(j.getString(varOverview));
                    movieModel.setMovieVote(j.getString(varVoteAvg));
                    movieModel.setMovieRelease(j.getString(varRealease));
                    listMovies.add(movieModel);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            if (integer == 0)
                Toast.makeText(MainActivity.this, "No data fetched ", Toast.LENGTH_LONG).show();
            else {
                recyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("KeyForLayoutManagerState", recyclerView.getLayoutManager().onSaveInstanceState());

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (savedInstanceState != null) {
            Parcelable savedRecyclerLayoutState = savedInstanceState.getParcelable("KeyForLayoutManagerState");
            recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.top_rated) {
            new BeginAsyncTask().execute("top_rated");
            return true;
        } else if (id == R.id.most_popular) {
            new BeginAsyncTask().execute("popular");
            return true;
        } else if (id == R.id.fav) {
            if (firsTime == true) {
                getSupportLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
                firsTime = false;
            } else {
                getSupportLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
