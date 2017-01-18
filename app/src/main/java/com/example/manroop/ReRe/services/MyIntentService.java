package com.example.manroop.ReRe.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Response;

public class MyIntentService extends IntentService {

    Response<Business> response;
    private static final String TAG = "Yelp!!!";
    private String consumerKey, consumerSecret, token, tokenSecret;
    private String bizId;


    public MyIntentService() {
        super("MyIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        consumerKey = "8wc09ieW4l9sB7hgamRXEA";
        consumerSecret = "TGd1idm86DPr9XZng81yLy8Y4As";
        token = "OIgkIfcNv_81vBkfKTZ8DaRbEvK46Fdr";
        tokenSecret = "BlcADT5JOvHnqBaPb8XvRY2hsN8";

        bizId = intent.getStringExtra("id");

        YelpAPIFactory apiFactory = new YelpAPIFactory(consumerKey, consumerSecret, token, tokenSecret);
        YelpAPI yelpAPI = apiFactory.createAPI();

        Map<String, String> params = new HashMap<>();
        params.put("lang", "fr");

        Call<Business> call = yelpAPI.getBusiness(bizId, params);
        try {
            response = call.execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Business business = response.body();

        Intent intent1 = new Intent("getBizDetail2");
        intent1.putExtra("bizDetail2", business);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent1);
    }

}

