package com.example.manroop.ReRe;

import com.example.manroop.ReRe.pojos.openTableRestaurants.OpenTableRestaurants;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by INSPIRON on 17-Jan-17.
 */

public class RestCalls {

    private static final String TAG = "RestCallsTag: ";
    String resp;

    public OpenTableRestaurants getOpenTableResults() {


        OkHttpClient client = new OkHttpClient();

        HttpUrl httpUrl = new HttpUrl.Builder()
                .scheme("http")
                .host("opentable.herokuapp.com")
                .addPathSegment("api")
                .addPathSegment("restaurants")
                .addQueryParameter("country", "US")
                .addQueryParameter("state", "GA")
                .addQueryParameter("city", "Atlanta")
                .build();


        final Request request = new Request.Builder()
                .url(httpUrl)
                .build();
        try {
            Response response = client.newCall(request).execute();
            resp = response.body().string();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson = new Gson();
        OpenTableRestaurants openTableRestaurants = gson.fromJson(resp, OpenTableRestaurants.class);
        return  openTableRestaurants;

    }
}
