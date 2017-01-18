package com.example.manroop.ReRe.services;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.example.manroop.ReRe.pojos.yelpBusinessDetail.BusinessDetail;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static com.example.manroop.ReRe.services.AccessTokenIntentService.token;


public class RestDetailIntentService extends IntentService {

    private static final String TAG = "RestDetailInService";

    public RestDetailIntentService() {
        super("RestDetailIntentService");
    }

    Response responseRestDetail;
    Request requestRestDetail;
    OkHttpClient client;
    String bizId, respRestDetail;

    @Override
    protected void onHandleIntent(Intent intent) {

        bizId = intent.getStringExtra("id");
        client = new OkHttpClient();

        String Url = "https://api.yelp.com/v3/businesses/" + bizId;
        requestRestDetail = new Request.Builder()
                .header("Authorization", "Bearer " + token.getAccessToken())
                .url(Url)
                .build();

        respRestDetail = null;
        try {
            responseRestDetail = client.newCall(requestRestDetail).execute();
            respRestDetail = responseRestDetail.body().string();

        } catch (IOException e) {
            e.printStackTrace();
        }

        Gson gson1 = new Gson();

        BusinessDetail businessDetail = gson1.fromJson(respRestDetail, BusinessDetail.class);

        Intent intentBizDetail = new Intent("getBizDetail");
        intentBizDetail.putExtra("bizDetail", businessDetail);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intentBizDetail);

    }

}
