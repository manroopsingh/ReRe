package com.example.manroop.ReRe.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.manroop.ReRe.pojos.yelpBusinesses.Business;
import com.example.manroop.ReRe.pojos.yelpBusinesses.YelpBusiness;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

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
    String lat, lng, Url;

    @Override
    protected void onHandleIntent(Intent intent) {

        lat = intent.getStringExtra("latitude");
        lng = intent.getStringExtra("longitude");
        Url = "https://api.yelp.com/v3/businesses/search";


        Log.d(TAG, "onHandleIntent: " + lat + " " +lng);
//        Log.d(TAG, "onHandleIntent: " + token.getAccessToken());
        client = new OkHttpClient();

        HttpUrl url = new HttpUrl.Builder()
                .scheme("https")
                .host("api.yelp.com")
                .addPathSegment("v3")
                .addPathSegment("businesses")
                .addPathSegment("search")
                .addQueryParameter("term", "food")
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

        List<Business> list = yelpBusiness.getBusinesses();
        for(Business b: list)
        {
            Log.d(TAG, "onHandleIntent: " + b.getLocation().getDisplayAddress());
        }





        Bundle bundle = new Bundle();
        bundle.putSerializable("data", yelpBusiness);

        Intent intent1 = new Intent();
        intent1.setAction("yelp");
        intent1.putExtras(bundle);
        sendBroadcast(intent1);
    }

}
