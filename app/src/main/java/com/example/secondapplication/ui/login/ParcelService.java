package com.example.secondapplication.ui.login;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.secondapplication.Entities.Parcel;
import com.example.secondapplication.Model.ParcelDataSource;

import java.util.List;

public class ParcelService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        Toast.makeText(this, "service creat", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "service destory", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ParcelDataSource.notifyService(getBaseContext(), new ParcelDataSource.ServiceNotify<Parcel>() {
            @Override
            public void onNewChildAdded(Parcel obj) {
                Intent myIntent=new Intent("New parcel");
                sendBroadcast(myIntent);
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });
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


        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
