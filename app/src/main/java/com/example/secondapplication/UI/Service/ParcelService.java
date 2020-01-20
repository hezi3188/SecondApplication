package com.example.secondapplication.UI.Service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.secondapplication.Entities.Parcel;
import com.example.secondapplication.Model.ParcelDataSource;
import com.example.secondapplication.R;
import com.example.secondapplication.UI.ProfileActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Random;

public class ParcelService extends Service {
    FirebaseAuth auth;
    @Override
    public void onCreate() {
        super.onCreate();
        auth=FirebaseAuth.getInstance();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        int MY_NOTFICATION_ID;
        final String NOTIFICATION_CHANNEL_ID = "my_channel_id_01";
        final Random r = new Random();
        @SuppressLint("WrongConstant")
        NotificationChannel notificationChannel;
        final NotificationManager notificationManager;
        final PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                new Intent(getApplicationContext(), ProfileActivity.class), PendingIntent.FLAG_UPDATE_CURRENT);
        notificationManager = (NotificationManager) getBaseContext().getSystemService(Context.NOTIFICATION_SERVICE);
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
        final Notification notif = new NotificationCompat.Builder(getBaseContext(), NOTIFICATION_CHANNEL_ID).
                setSmallIcon(R.drawable.common_full_open_on_phone)////////////////////////////////////change
                .setContentTitle("New parcel")
                .setContentText("You have a new parcel in your area !!!  " )
                .setDefaults(Notification.DEFAULT_LIGHTS)
                .setContentIntent(contentIntent)
                .setContentInfo("Info").build();
        if(auth.getCurrentUser()!=null){
            ParcelDataSource.notifyService(getBaseContext(), new ParcelDataSource.ServiceNotify<Parcel>() {
                @Override
                public void onNewChildAdded(Parcel obj) {
                    notificationManager.notify(r.nextInt(3000), notif);
                }

                @Override
                public void onFailure(Exception exception) {

                }
            });

        }
        /*ParcelDataSource.notifyOffersParcelList(getBaseContext(), new ParcelDataSource.NotifyDataChange<List<Parcel>>() {
            @Override
            public void onDataChanged(List<Parcel> obj) {
                Intent myIntent=new Intent("New parcel");
                sendBroadcast(myIntent);
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });*/
        if (Build.VERSION.SDK_INT >= 26) {
            return START_REDELIVER_INTENT;

        }else{
            return START_STICKY;

        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}