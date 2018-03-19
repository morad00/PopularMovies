package com.example.android.popularmovies.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.ReviewDetails;
import com.example.android.popularmovies.models.ReviewModel;
import com.example.android.popularmovies.models.TrailerModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mourad on 1/29/2018.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {

    private ArrayList<ReviewModel> resultModels;

    public void setReviewsData(ArrayList<ReviewModel> reviewsList) {
        this.resultModels = reviewsList;
        notifyDataSetChanged();
    }

    public ReviewAdapter(ArrayList<ReviewModel> resultModels) {
        this.resultModels = resultModels;
    }


    @Override
    public ReviewAdapter.ReviewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.reviews_list_content, parent, false);
        return new ReviewHolder(row);
    }

    @Override
    public void onBindViewHolder(ReviewAdapter.ReviewHolder holder, int position) {
        ReviewModel reviewModel = resultModels.get(position);
        holder.author.setText(reviewModel.getAuthor());
        holder.content.setText(reviewModel.getcontent());
    }

    @Override
    public int getItemCount() {
        return resultModels.size();
    }

    public class ReviewHolder extends RecyclerView.ViewHolder {
        TextView author, content;

        public ReviewHolder(final View itemView) {
            super(itemView);
            author = (TextView) itemView.findViewById(R.id.reviewAuthor);
            content = (TextView) itemView.findViewById(R.id.reviewContent);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(itemView.getContext(), ReviewDetails.class);
                    intent.putExtra("author", author.getText());
                    intent.putExtra("content", content.getText());
                    itemView.getContext().startActivity(intent);

                }
            });
        }

    }
}
