package com.example.secondapplication.ui.login.ui.home;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.secondapplication.Entities.Parcel;
import com.example.secondapplication.Model.ParcelRepository;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private ParcelRepository repository;
    private LiveData<List<Parcel>> allParcels;

    public HomeViewModel(Application application) {
        super(application);
        repository = new ParcelRepository(application);

        allParcels=repository.getAllOfferParcels(getApplication());
        // allParcels.setValue("This is home fragment");


    }


    public LiveData<List<Parcel>> getText() {

        return allParcels;
    }
}