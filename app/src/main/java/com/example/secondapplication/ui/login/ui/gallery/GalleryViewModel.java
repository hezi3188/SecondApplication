package com.example.secondapplication.ui.login.ui.gallery;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.secondapplication.Entities.Parcel;
import com.example.secondapplication.Model.ParcelRepository;

import java.util.List;

public class GalleryViewModel extends AndroidViewModel {

    private LiveData<List<Parcel>> allParcels;
    private ParcelRepository repository;

    public GalleryViewModel(Application application) {
        super(application);
        repository=new ParcelRepository(application);

        allParcels = repository.getAllParcelsUserAccepted();
    }

    public LiveData<List<Parcel>> getText() {
        return allParcels;
    }
}