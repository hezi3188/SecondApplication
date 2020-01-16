package com.example.secondapplication.ui.login;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.secondapplication.R;

import java.util.Random;

public class MyBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int MY_NOTFICATION_ID;
        final String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
        Random r = new Random();
        @SuppressLint("WrongConstant")
        NotificationChannel notificationChannel;
        NotificationManager notificationManager;
        final PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
                new Intent(context, MainActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        String destination = intent.getStringExtra("dest");
        notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notification {s", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription("Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.CYAN);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        final Notification notif = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID).
                setSmallIcon(R.drawable.common_full_open_on_phone)////////////////////////////////////change
                .setContentTitle("New parcel")
                .setContentText("You have a new parcel in your area !!!  " )
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setContentIntent(contentIntent)
                .setContentInfo("Info").build();
        notificationManager.notify(r.nextInt(3000), notif);


    }
}
