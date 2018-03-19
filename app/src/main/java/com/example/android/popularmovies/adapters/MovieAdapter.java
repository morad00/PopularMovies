package com.example.android.popularmovies.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.models.MovieModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mourad on 9/29/2017.
 */

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieHolder> {
    private ArrayList<MovieModel> objects;
    private GridView gridView;
    List<MovieModel> movieList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(MovieModel movieModel);
    }

    public MovieAdapter(List<MovieModel> movieList, OnItemClickListener listener) {
        this.movieList = movieList;
        this.listener = listener;
    }


    public void add(List<MovieModel> movies) {
        movieList.clear();
        this.movieList.addAll(movies);
        notifyDataSetChanged();
    }

    public void clear() {
        if (this.movieList != null) {
            int size = this.movieList.size();
            this.movieList.clear();
            this.notifyItemRangeRemoved(0, size);
        }
    }

    @Override
    public MovieHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_row, parent, false);
        return new MovieHolder(row);
    }

    @Override
    public void onBindViewHolder(MovieHolder holder, int position) {
        holder.bind(movieList.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    static class MovieHolder extends RecyclerView.ViewHolder {
        ImageView imgPoster;

        public MovieHolder(View itemView) {
            super(itemView);
            imgPoster = (ImageView) itemView.findViewById(R.id.imgMoviePoster);
        }

        public void bind(final MovieModel movieModel, final OnItemClickListener listener) {
            Picasso.with(itemView.getContext()).
                    load("https://image.tmdb.org/t/p/w500" + movieModel.getMoviePoster()).into(imgPoster);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(movieModel);
                }
            });
        }
    }


}
