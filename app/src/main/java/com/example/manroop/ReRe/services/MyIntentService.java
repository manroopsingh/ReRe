package com.example.manroop.ReRe.services;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.yelp.clientlib.connection.YelpAPI;
import com.yelp.clientlib.connection.YelpAPIFactory;
import com.yelp.clientlib.entities.Business;
import com.yelp.clientlib.entities.SearchResponse;
import com.yelp.clientlib.entities.options.CoordinateOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;

public class MyIntentService extends IntentService {

    ArrayList<Business> businesses;
    List<Business > bsns = new ArrayList<>();
    private static final String TAG = "Yelp!!!";
    private String consumerKey, consumerSecret, token, tokenSecret;
    private String Lat, Long="";


    public MyIntentService() {
        super("MyIntentService");
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        consumerKey = "8wc09ieW4l9sB7hgamRXEA";
        consumerSecret = "TGd1idm86DPr9XZng81yLy8Y4As";
        token = "OIgkIfcNv_81vBkfKTZ8DaRbEvK46Fdr";
        tokenSecret = "BlcADT5JOvHnqBaPb8XvRY2hsN8";

        Lat = intent.getStringExtra("latitude");
        Long = intent.getStringExtra("longitude");

        Log.d(TAG, "onHandleIntent: " + Lat);
        Log.d(TAG, "onHandleIntent: " + Long);

        YelpAPIFactory apiFactory = new YelpAPIFactory(consumerKey, consumerSecret, token, tokenSecret);
        YelpAPI yelpAPI = apiFactory.createAPI();

        Map<String, String> params = new HashMap<>();

// general params
        params.put("term", "food");
        params.put("limit", "20");

// locale params
        params.put("lang", "en");

        CoordinateOptions coordinate = CoordinateOptions.builder()
                .latitude(Double.parseDouble(Lat))
                .longitude(Double.parseDouble(Long)).build();
        Call<SearchResponse> call = yelpAPI.search(coordinate, params);
        SearchResponse searchResponse = null;
        try {
            searchResponse = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //int totalNumberOfResult = searchResponse.total();  // 3

        businesses = searchResponse.businesses();


//        //Log.d(TAG, "onHandleIntent: "+totalNumberOfResult);
//        for (int i = 0; i < 3; i++) {
//            Log.d(TAG, "Businesses: "
//                    + businesses.get(i).name()
//                    + " "
//                    + businesses.get(i).rating()
//                    + " "
//                    + businesses.get(i).displayPhone()
//                    + " "
//                    + businesses.get(i).location().address());
//
//        }


        Bundle bundle = new Bundle();
        bundle.putSerializable("data", businesses);

        Intent intent1 = new Intent();
        intent1.setAction("yelp");
        intent1.putExtras(bundle);
        sendBroadcast(intent1);


    }




}

