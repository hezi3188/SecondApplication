package com.example.secondapplication.Model;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.secondapplication.Entities.Parcel;

@Database(entities = {Parcel.class},version = 1)
public abstract class ParcelDatabase extends RoomDatabase {
    private static ParcelDatabase instance;

    public abstract ParcelDao parcelDao();

    public static synchronized ParcelDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    ParcelDatabase.class, "parcel_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private ParcelDao parcelDao;

        private PopulateDbAsyncTask(ParcelDatabase db) {
            parcelDao = db.parcelDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }

}
