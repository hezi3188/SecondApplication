package com.example.secondapplication.ui.login.ui;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.example.secondapplication.Entities.Customer;

public class LoginRegisterViewModel extends AndroidViewModel {
    SharedPreferences sharedPreferences;
    public LoginRegisterViewModel(@NonNull Application application) {
        super(application);
    }

    public void register(String email, String password, Customer customer){

    }
}
