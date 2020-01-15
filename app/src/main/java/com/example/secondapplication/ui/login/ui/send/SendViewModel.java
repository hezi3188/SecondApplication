package com.example.secondapplication.ui.login.ui.send;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.secondapplication.Entities.Parcel;
import com.example.secondapplication.Model.ParcelRepository;
import com.example.secondapplication.ui.login.RegisterActivity;

import java.util.List;

public class SendViewModel extends AndroidViewModel {

    private ParcelRepository repository;
    private LiveData<List<Parcel>> mText;

    public SendViewModel(Application application) {
        super(application);
        repository=new ParcelRepository(application);
      //  mText = repository.getAllOfferParcels(application);
    }

    public LiveData<List<Parcel>> getText() {
        return mText;
    }
}