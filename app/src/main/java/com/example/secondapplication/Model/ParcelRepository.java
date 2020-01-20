package com.example.secondapplication.Model;

import android.app.Application;
import android.content.Context;
import android.os.AsyncTask;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.secondapplication.Entities.Parcel;

import java.util.List;

public class ParcelRepository {



    private static ParcelDao parcelDao;

    private LiveData<List<Parcel>> allParcelsUserNotAccepted;
    private LiveData<List<Parcel>> allParcelsUserAccepted;
    private static MutableLiveData<List<Parcel>> offer;

    private MutableLiveData<List<Parcel>> getOfferData(){
        if(offer!=null)
            return offer;
        return new MutableLiveData<>();
    }

    public ParcelRepository(Application application) {

        //load parcels
        ParcelDatabase parcelDatabase = ParcelDatabase.getInstance(application);
        parcelDao = parcelDatabase.parcelDao();
        offer= getOfferData();
        allParcelsUserAccepted = parcelDao.getAllParcelsUserAccepted();
        allParcelsUserNotAccepted = parcelDao.getAllParcelsUserNotAccepted();

        //notify
        ParcelDataSource.notifyUserParcelList(new ParcelDataSource.OnUserNotify<Parcel>() {
            @Override
            public void onStart() {
                deleteAllParcels();
            }

            @Override
            public void onChildAdd(Parcel p) {
                insert(p);
            }

            @Override
            public void onChildUpdate(Parcel p) {
                update(p);
            }

            @Override
            public void onChildRemoved(Parcel p) {
                delete(p);
            }

            @Override
            public void onFailure(Exception exception) {

            }
        });
    }





    public void insert(Parcel parcel) {
        new InsertParcelAsyncTask(parcelDao).execute(parcel);
    }

    public void update(Parcel parcel) {
        new UpdateParcelAsyncTask(parcelDao).execute(parcel);
    }

    public void delete(Parcel parcel) {
        new DeleteParcelAsyncTask(parcelDao).execute(parcel);
    }

    public static void deleteAllParcels() {
        new DeleteAllParcelsAsyncTask(parcelDao).execute();
    }



    public LiveData<List<Parcel>> getAllParcelsUserAccepted() {return allParcelsUserAccepted;}

    public LiveData<List<Parcel>> getAllParcelsUserNotAccepted(){return allParcelsUserNotAccepted;}

    public LiveData<List<Parcel>> getAllOfferParcels(Context context){
        ParcelDataSource.notifyOffersParcelList(context,new ParcelDataSource.NotifyDataChange<List<Parcel>>() {
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
