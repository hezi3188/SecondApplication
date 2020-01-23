package com.example.secondapplication.UI.History;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.secondapplication.Entities.Parcel;
import com.example.secondapplication.Model.ParcelRepository;

import java.util.List;

public class HistoryViewModel extends AndroidViewModel {

    private LiveData<List<Parcel>> allParcels;
    private ParcelRepository repository;

    public HistoryViewModel(Application application) {
        super(application);
        repository=new ParcelRepository(application);

        allParcels = repository.getAllParcelsUserAccepted();
    }

    public LiveData<List<Parcel>> getHistoryParcels() {
        return allParcels;
    }
}