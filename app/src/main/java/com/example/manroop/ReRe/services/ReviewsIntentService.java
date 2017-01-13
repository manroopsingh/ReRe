package com.example.manroop.ReRe.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.util.Log;

import com.example.manroop.ReRe.pojos.YelpToken;
import com.example.manroop.ReRe.pojos.yelpreviews.Review;
import com.example.manroop.ReRe.pojos.yelpreviews.Reviews;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class ReviewsIntentService extends IntentService {

    public static final String TAG = "ReviewIntentService: ";

    Response response, responseReviews;
    RequestBody formBodyToken, formBodyReviews;
    Request requestToken, requestReviews;
    OkHttpClient client, clientResponse;
    YelpToken token;
    String bizId, respToken, respReviews;

    public ReviewsIntentService() {
        super("ReviewsIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        bizId = intent.getStringExtra("id");
        client = new OkHttpClient();

        Gson gson = getAccessToken();
        token = gson.fromJson(respToken, YelpToken.class);

        Gson gson1 = getReviews();
        Reviews reviews = gson1.fromJson(respReviews, Reviews.class);

        List<Review> listReview = reviews.getReviews();
        for (Review r : listReview) {
            Log.d(TAG, "onHandleIntent: " + r.getText());

        }

    }

    @NonNull
    private Gson getReviews() {
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


        return new Gson();
    }

    @NonNull
    private Gson getAccessToken() {
        formBodyToken = new FormBody.Builder()
                .add("grant_type", "client_credentials")
                .add("client_id", "9PLcR6tRbK77F49ooLKuKg")
                .add("client_secret", "1dDMiSn3c8Wfcpr5bTdCeimXWd8Sx6WOWWjqbuxhGWUkweHmTQkvaZjoLBRY30of")
                .build();
        requestToken = new Request.Builder()
                .url("https://api.yelp.com/oauth2/token")
                .post(formBodyToken)
                .build();

        respToken = null;
        try {
            response = client.newCall(requestToken).execute();
            respToken = response.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new Gson();
    }


}