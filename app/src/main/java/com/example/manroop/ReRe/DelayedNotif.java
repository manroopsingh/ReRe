package com.example.manroop.ReRe;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.manroop.ReRe.activities.RestaurantListActivity;
import com.example.manroop.ReRe.activities.MapsActivity;

public class DelayedNotif extends BroadcastReceiver {

    private static final String TAG = "br_RelayedNotification";

    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: ");

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(R.drawable.logo)
                .setContentTitle("ReRe")
                .setContentText("Don't forget that you have a reservation today!");

        Intent notificationIntent = new Intent(context.getApplicationContext(), MapsActivity.class);
        notificationIntent.putExtra("latitude", RestaurantListActivity.Latitude);
        notificationIntent.putExtra("longitude", RestaurantListActivity.Longitude);


        PendingIntent contentIntent = PendingIntent.getActivity(context.getApplicationContext(), 0, notificationIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        mBuilder.setContentIntent(contentIntent);
        mBuilder.setAutoCancel(true);
        mBuilder.setLights(Color.BLUE, 500, 500);
        long[] pattern = {500,500,500,500,500,500,500,500,500};
        mBuilder.setVibrate(pattern);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        mBuilder.setSound(alarmSound);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, mBuilder.build());
    }
}
