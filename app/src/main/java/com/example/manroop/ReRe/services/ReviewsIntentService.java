package com.example.manroop.ReRe.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.manroop.ReRe.pojos.yelpreviews.Review;
import com.example.manroop.ReRe.pojos.yelpreviews.Reviews;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.manroop.ReRe.services.AccessTokenIntentService.token;


public class ReviewsIntentService extends IntentService {

    public static final String TAG = "ReviewIntentService: ";

    Response responseReviews;
    Request requestReviews;
    OkHttpClient client;
    String bizId, respReviews;

    public ReviewsIntentService() {
        super("ReviewsIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        bizId = intent.getStringExtra("id");
        client = new OkHttpClient();

        Log.d(TAG, "onHandleIntent: ");
        String Url = "https://api.yelp.com/v3/businesses/" + bizId + "/reviews";
        requestReviews = new Request.Builder()
                .header("Authorization", "Bearer " + token.getAccessToken())
                .url(Url)
                .build();

        respReviews = null;
        try {
            responseReviews = client.newCall(requestReviews).execute();
            respReviews = responseReviews.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson1 = new Gson();

        Reviews reviews = gson1.fromJson(respReviews, Reviews.class);

        List<Review> listReview = reviews.getReviews();
        for (Review r : listReview) {
            Log.d(TAG, "onHandleIntent: " + r.getText());

        }

        Intent intentReviews = new Intent("getReviews");
        intentReviews.putExtra("reviews", reviews);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intentReviews);

    }


}