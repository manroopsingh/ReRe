package com.example.manroop.ReRe.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;

import com.example.manroop.ReRe.pojos.yelpBusinesses.YelpBusiness;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.manroop.ReRe.services.AccessTokenIntentService.token;


public class RestaurantIntentService extends IntentService {

    private static final String TAG = "ResIntentServiceTag: ";

    public RestaurantIntentService() {
        super("RestaurantIntentService");
    }

    Response responseRes;
    RequestBody formBodyRes;
    Request requestRes;
    OkHttpClient client;
    String respRes;
    String lat, lng, Url,term;

    @Override
    protected void onHandleIntent(Intent intent) {

        term = intent.getStringExtra("search");
        lat = intent.getStringExtra("latitude");
        lng = intent.getStringExtra("longitude");
        Url = "https://api.yelp.com/v3/businesses/search";

        if(intent.getStringExtra("search")!=null)
            term = intent.getStringExtra("search");
        else
            term = "food";

        client = new OkHttpClient();

        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("api.yelp.com")
                .addPathSegment("v3")
                .addPathSegment("businesses")
                .addPathSegment("search")
                .addQueryParameter("term", term)
                .addQueryParameter("latitude", lat)
                .addQueryParameter("longitude", lng)
                .addQueryParameter("limit","20")
                .build();


        requestRes = new Request.Builder()
                .header("Authorization", "Bearer " + token.getAccessToken())
                .url(url)
                .build();

        respRes = null;
        try {
            responseRes = client.newCall(requestRes).execute();
            respRes = responseRes.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }
        Gson gson = new Gson();
        YelpBusiness yelpBusiness = gson.fromJson(respRes,YelpBusiness.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("data", yelpBusiness);


        Intent intent1 = new Intent("restaurantList");
        intent1.putExtras(bundle);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent1);
    }

}
