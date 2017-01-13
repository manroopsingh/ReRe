package com.example.manroop.ReRe.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.example.manroop.ReRe.pojos.YelpToken;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static com.example.manroop.ReRe.activities.Main_Activity.Latitude;
import static com.example.manroop.ReRe.activities.Main_Activity.Longitude;


public class AccessTokenIntentService extends IntentService {

    public AccessTokenIntentService() {
        super("AccessTokenIntentService");
    }

    public static final String TAG = "AccessTokenService";

    Response response;
    RequestBody formBodyToken;
    Request requestToken;
    OkHttpClient client;
    public static YelpToken token;
    String respToken;


    @Override
    protected void onHandleIntent(Intent intent) {


        client = new OkHttpClient();
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

        Gson gson = new Gson();
        token = gson.fromJson(respToken, YelpToken.class);

        Log.d(TAG, "onHandleIntent: " + token.getAccessToken());

        String type = intent.getStringExtra("type");
        switch (type){
            case "restaurants":
                Intent in = new Intent(this, RestaurantIntentService.class);
                in.putExtra("latitude", Latitude);
                in.putExtra("longitude", Longitude);
                startService(in);
        }
    }

}
