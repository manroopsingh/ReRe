package com.example.manroop.ReRe.services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.util.List;
import java.util.Locale;

public class GeocodingIntentService extends IntentService {

    private static final String TAG = "GeoIntentService";

    public GeocodingIntentService() {
        super("GeocodingIntentService");
    }

    private List<String> addressList;

    @Override
    protected void onHandleIntent(Intent intent) {


        addressList = intent.getStringArrayListExtra("address");

        //Log.d(TAG, "onHandleIntent: " + address);
        double lat = 0.0, lng = 0.0;

        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());

        for (String address : addressList) {
            try {
                List<Address> addresses = geoCoder.getFromLocationName(address, 1);
                if (addresses.size() > 0) {
                    lat = addresses.get(0).getLatitude();
                    lng = addresses.get(0).getLongitude();

                    Log.d(TAG, "onHandleIntent: "+ address + lat + lng);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}
