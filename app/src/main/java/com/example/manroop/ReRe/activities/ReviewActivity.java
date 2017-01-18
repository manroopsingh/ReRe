package com.example.manroop.ReRe.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;

import com.example.manroop.ReRe.R;
import com.example.manroop.ReRe.adapters.ReviewListAdapter;
import com.example.manroop.ReRe.pojos.yelpreviews.Review;
import com.example.manroop.ReRe.pojos.yelpreviews.Reviews;

import java.util.ArrayList;
import java.util.List;

public class ReviewActivity extends AppCompatActivity {

    private static final String TAG = "ReviewActivity";

    private RecyclerView recyclerViewReviews;
    private RecyclerView.LayoutManager layoutManager;
    private ReviewListAdapter reviewListAdapter;
    private List<Review> reviewList = new ArrayList<>();
    private List<Review> reviewList1 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_review);

        recyclerViewReviews = (RecyclerView) findViewById(R.id.recyclerView_reviews);
        layoutManager = new LinearLayoutManager(this);
        recyclerViewReviews.setLayoutManager(layoutManager);
        reviewListAdapter = new ReviewListAdapter(this, reviewList1);
        recyclerViewReviews.setItemAnimator(new DefaultItemAnimator());
        recyclerViewReviews.setAdapter(reviewListAdapter);

    }

    @Override
    protected void onStart() {
        LocalBroadcastManager.getInstance(this).registerReceiver(brGetReviews, new IntentFilter("getReviews"));
        super.onStart();
    }

    public BroadcastReceiver brGetReviews = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            //Log.d(TAG, "onReceive: ");
            Reviews yelpReviews = (Reviews) intent.getSerializableExtra("reviews");
            reviewList = yelpReviews.getReviews();
            reviewList1.addAll(reviewList);
            reviewListAdapter.notifyDataSetChanged();


        }
    };


}
