package com.example.secondapplication.ui.login.ui.home;

import android.app.Application;
import android.widget.Toast;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.secondapplication.Entities.Parcel;
import com.example.secondapplication.Model.ParcelDataSource;
import com.example.secondapplication.Model.ParcelRepository;

import java.util.List;

public class HomeViewModel extends AndroidViewModel {

    private ParcelRepository repository;
    private LiveData<List<Parcel>> allParcels;

    public HomeViewModel(Application application) {
        super(application);
        repository = new ParcelRepository(application);
        allParcels=repository.getAllParcelsUserNotAccepted();
        // allParcels.setValue("This is home fragment");


    }



    public void confirmDelivery(String id, final String deliveryName){
        repository.confirmDelivery(id, deliveryName, new ParcelDataSource.Action<String>() {
            @Override
            public void OnSuccess(String obj) {
                Toast.makeText(getApplication(), "The delivery "+deliveryName+" selected", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnFailure(Exception exception) {
                Toast.makeText(getApplication(), "Failure please connect to the internet or try again! ", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void OnProgress(String status, double percent) {

            }
        });
    }
    public void acceptedParcel(String id){
        repository.acceptedParcel(id, new ParcelDataSource.Action<String>() {
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

        return allParcels;
    }
}