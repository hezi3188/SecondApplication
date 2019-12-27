package com.example.secondapplication.Model;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.secondapplication.Entities.Customer;

@Database(entities = {Customer.class},version = 1)
public abstract class CustomerDatabase extends RoomDatabase {
    private static CustomerDatabase instance;

    public abstract CustomerDao customerDao();

    public static synchronized CustomerDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    CustomerDatabase.class, "customer_database")
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
        private CustomerDao customerDao;

        private PopulateDbAsyncTask(CustomerDatabase db) {
            customerDao = db.customerDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            return null;
        }
    }
}
