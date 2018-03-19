package com.example.android.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.models.TrailerModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mourad on 1/29/2018.
 */

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ReviewHolder> {
    private ArrayList<TrailerModel> trailerList;
    private Context mContext;

    public TrailerAdapter(Context mContext, ArrayList<TrailerModel> trailerList) {
        this.mContext = mContext;
        this.trailerList = trailerList;
    }

    public void addAll(ArrayList<TrailerModel> trailerList) {
        this.trailerList = trailerList;
        notifyDataSetChanged();
    }

    @Override
    public TrailerAdapter.ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailers_list_content, parent, false);
        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(TrailerAdapter.ReviewHolder holder, int position) {
        holder.title.setText(trailerList.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {
        public TextView title;

        public ReviewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.trailerTitle);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (getAdapterPosition() != RecyclerView.NO_POSITION) {
                        String videoId = trailerList.get(getAdapterPosition()).getKey();
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + videoId));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("VIDEO_ID", videoId);
                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }
}
