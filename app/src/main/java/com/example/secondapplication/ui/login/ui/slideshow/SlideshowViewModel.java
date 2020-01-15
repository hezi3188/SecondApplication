package com.example.secondapplication.ui.login.ui.slideshow;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.secondapplication.Entities.Parcel;
import com.example.secondapplication.Model.ParcelRepository;

import java.util.List;

public class SlideshowViewModel extends AndroidViewModel {

    private ParcelRepository repository;
    private LiveData<List<Parcel>> mText;

    public SlideshowViewModel(Application application) {
        super(application);
        repository=new ParcelRepository(application);
        mText = repository.getAllOfferParcels(application);
    }

    public LiveData<List<Parcel>> getText() {
        return mText;
    }
}