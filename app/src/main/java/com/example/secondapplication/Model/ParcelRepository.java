package com.example.secondapplication.Model;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.secondapplication.Entities.Customer;
import com.example.secondapplication.Entities.Parcel;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class ParcelRepository {
    private ParcelDao parcelDao;
    private CustomerDao customerDao;
    private ParcelDataSource parcelDataSource;
    private LiveData<List<Customer>> customer;

    private LiveData<List<Parcel>> allParcelsUserNotAccepted;
    private LiveData<List<Parcel>> allParcelsUserAccepted;

    SharedPreferences sharedPreferences;
    public static final String myPreference = "myUser";
    public static final String myLatitude = "latitudeKey";
    public static final String myLongitude = "longitudeKey";

    public ParcelRepository(Application application) {
        ParcelDatabase parcelDatabase = ParcelDatabase.getInstance(application);
        CustomerDatabase customerDatabase = CustomerDatabase.getInstance(application);
        parcelDataSource=ParcelDataSource.getInstance();
        parcelDao = parcelDatabase.parcelDao();
        customerDao=customerDatabase.customerDao();





        allParcelsUserAccepted = parcelDao.getAllParcelsUserAccepted();
        allParcelsUserNotAccepted = parcelDao.getAllParcelsUserNotAccepted();
        ParcelDataSource.notifyUserParcelList(new ParcelDataSource.NotifyDataChange<List<Parcel>>() {
            @Override
            public void onDataChanged(List<Parcel> obj) {

                deleteAllParcels();
                for (Parcel p: obj) {
                    insert(p);
                }
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });
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



    public LiveData<List<Parcel>> getAllParcelsUserAccepted() {return allParcelsUserAccepted;}

    public LiveData<List<Parcel>> getAllParcelsUserNotAccepted(){return allParcelsUserNotAccepted;}

    public LiveData<List<Parcel>> getAllOfferParcels(Context context){
        sharedPreferences = context.getSharedPreferences(myPreference,
                Context.MODE_PRIVATE);
        String s=sharedPreferences.getString(myLatitude,"");
        final MutableLiveData<List<Parcel>> offer=new MutableLiveData<>();
        ParcelDataSource.notifyOffersParcelList(new ParcelDataSource.NotifyDataChange<List<Parcel>>() {
            @Override
            public void onDataChanged(List<Parcel> obj) {
                offer.setValue(obj);
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });
        return offer;
    }

    public void acceptedParcel(String id, final ParcelDataSource.Action<String> action){
        ParcelDataSource.acceptedParcel(id,action);
    }

    public void addDelivery(final String id, final String deliveryName, final ParcelDataSource.Action<String> action){
        ParcelDataSource.addDelivery(id,deliveryName,action);
    }
    public void confirmDelivery(final String id, final String deliveryName, final ParcelDataSource.Action<String> action){
        ParcelDataSource.confirmDelivery(id,deliveryName,action);
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
