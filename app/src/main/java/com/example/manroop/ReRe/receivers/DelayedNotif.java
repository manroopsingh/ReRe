package com.example.manroop.ReRe.receivers;


import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.manroop.ReRe.R;
import com.example.manroop.ReRe.activities.MyReservationsActivity;

public class DelayedNotif extends BroadcastReceiver {

    private static final String TAG = "br_rec_delayed";

    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: ");
        String restName = intent.getStringExtra("restName");

        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, new Intent(context, MyReservationsActivity.class), 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                context).setSmallIcon(R.drawable.logo)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setContentTitle(restName)
                .setContentText("You have a reservation in one hour. Tap to view");


        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, mBuilder.build());
    }
}
