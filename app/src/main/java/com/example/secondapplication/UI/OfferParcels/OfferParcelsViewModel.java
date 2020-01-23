package com.example.secondapplication.UI.OfferParcels;

import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.secondapplication.Entities.Parcel;
import com.example.secondapplication.Model.ParcelDataSource;
import com.example.secondapplication.Model.ParcelRepository;

import java.util.List;

public class OfferParcelsViewModel extends AndroidViewModel {

    private ParcelRepository repository;
    private LiveData<List<Parcel>> mText;

    public OfferParcelsViewModel(Application application) {
        super(application);
        repository=new ParcelRepository(application);
        mText = repository.getAllOfferParcels(application);
    }

    public void addDelivery(String id,String deliveryName){
        repository.addDelivery(id, deliveryName, new ParcelDataSource.Action<String>() {
            @Override
            public void OnSuccess(String obj) {
                Toast.makeText(getApplication(), obj, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnFailure(Exception exception) {
                Toast.makeText(getApplication(), exception.getMessage(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnProgress(String status, double percent) {

            }
        });
    }
    public LiveData<List<Parcel>> getOfferParcels() {
        return mText;
    }
}