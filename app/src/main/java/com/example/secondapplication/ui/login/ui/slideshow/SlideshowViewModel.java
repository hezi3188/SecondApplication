package com.example.secondapplication.ui.login.ui.slideshow;

import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.secondapplication.Entities.Parcel;
import com.example.secondapplication.Model.ParcelDataSource;
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
    public LiveData<List<Parcel>> getText() {
        return mText;
    }
}