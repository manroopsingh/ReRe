package com.example.manroop.ReRe.services;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v4.content.LocalBroadcastManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GeocodingIntentService extends IntentService {

    private static final String TAG = "GeoIntentService";

    public GeocodingIntentService() {
        super("GeocodingIntentService");
    }

    private List<String> addressList;
    private ArrayList<Location> addressListGps;
    private Location location;

    @Override
    protected void onHandleIntent(Intent intent) {

        addressList = intent.getStringArrayListExtra("address");

        addressListGps = new ArrayList<>();

        double lat = 0.0, lng = 0.0;

        Geocoder geoCoder = new Geocoder(this, Locale.getDefault());

        for (String address : addressList) {
            try {
                List<Address> addresses = geoCoder.getFromLocationName(address, 1);
                if (addresses.size() > 0) {
                    lat = addresses.get(0).getLatitude();
                    lng = addresses.get(0).getLongitude();
                    location = new Location("");
                    location.setLatitude(lat);
                    location.setLongitude(lng);
                    addressListGps.add(location);
                    //Log.d(TAG, "onHandleIntent: "+ address + location.getLatitude());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Intent intentBroadcast = new Intent("addressList");
        intentBroadcast.putParcelableArrayListExtra("addresses", addressListGps);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intentBroadcast);


    }
}
