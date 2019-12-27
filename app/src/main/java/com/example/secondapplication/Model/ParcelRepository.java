package com.example.secondapplication.Model;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.secondapplication.Entities.Parcel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ParcelRepository {
    private ParcelDao parcelDao;
    private DatabaseReference parcelsRef;

    private LiveData<List<Parcel>> allParcels;

    public ParcelRepository(Application application) {
        ParcelDatabase database = ParcelDatabase.getInstance(application);
        // Write a message to the database
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        parcelsRef = firebaseDatabase.getReference("parcels");

        parcelDao = database.parcelDao();

        allParcels = parcelDao.getAllParcels();
    }

    public void insert(Parcel parcel) { new InsertParcelAsyncTask(parcelDao).execute(parcel);}

    public void update(Parcel parcel) {
        new UpdateParcelAsyncTask(parcelDao).execute(parcel);
    }

    public void delete(Parcel parcel) {
        new DeleteParcelAsyncTask(parcelDao).execute(parcel);
    }

    public void deleteAllParcels() {
        new DeleteAllParcelsAsyncTask(parcelDao).execute();
    }

    public LiveData<List<Parcel>> getAllParcels() {
        return allParcels;
    }

    //region AsyncTask implementation

    private static class InsertParcelAsyncTask extends AsyncTask<Parcel, Void, Void> {
        private ParcelDao parcelDao;

        private InsertParcelAsyncTask(ParcelDao parcelDao) {
            this.parcelDao = parcelDao;
        }

        @Override
        protected Void doInBackground(Parcel... parcels) {
            parcelDao.insert(parcels[0]);
            return null;
        }
    }

    private static class UpdateParcelAsyncTask extends AsyncTask<Parcel, Void, Void> {
        private ParcelDao parcelDao;

        private UpdateParcelAsyncTask(ParcelDao parcelDao) {
            this.parcelDao = parcelDao;
        }

        @Override
        protected Void doInBackground(Parcel... parcels) {
            parcelDao.update(parcels[0]);
            return null;
        }
    }

    private static class DeleteParcelAsyncTask extends AsyncTask<Parcel, Void, Void> {
        private ParcelDao parcelDao;

        private DeleteParcelAsyncTask(ParcelDao parcelDao) {
            this.parcelDao = parcelDao;
        }

        @Override
        protected Void doInBackground(Parcel... parcels) {
            parcelDao.delete(parcels[0]);
            return null;
        }
    }

    private static class DeleteAllParcelsAsyncTask extends AsyncTask<Void, Void, Void> {
        private ParcelDao parcelDao;

        private DeleteAllParcelsAsyncTask(ParcelDao parcelDao) {
            this.parcelDao = parcelDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            parcelDao.deleteAllParcels();
            return null;
        }
    }
}
