package com.example.secondapplication.ViewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.secondapplication.Entities.Parcel;
import com.example.secondapplication.Model.ParcelRepository;

import java.util.List;

public class ParcelViewModel extends AndroidViewModel {
    private ParcelRepository repository;
    private LiveData<List<Parcel>> allParcels;

    public ParcelViewModel(@NonNull Application application) {
        super(application);
        repository = new ParcelRepository(application);
        allParcels = repository.getAllParcels();
    }

    public void insert(Parcel parcel) {
        repository.insert(parcel);
    }

    public void update(Parcel parcel) {
        repository.update(parcel);
    }

    public void delete(Parcel parcel) {
        repository.delete(parcel);
    }

    public void deleteAllParcels() {
        repository.deleteAllParcels();
    }

    public LiveData<List<Parcel>> getAllParcels() {
        return allParcels;
    }
}
