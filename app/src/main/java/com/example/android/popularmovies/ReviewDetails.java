package com.example.android.popularmovies;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by Mourad on 1/31/2018.
 */

public class ReviewDetails extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.review_details);
        TextView author = (TextView) findViewById(R.id.reviewDetailsAuthor);
        TextView content = (TextView) findViewById(R.id.reviewDetailsContent);
        author.setText(getIntent().getStringExtra("author"));
        content.setText(getIntent().getStringExtra("content"));

    }
}
