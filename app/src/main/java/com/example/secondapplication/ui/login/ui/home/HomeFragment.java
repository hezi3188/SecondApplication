package com.example.secondapplication.ui.login.ui.home;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.secondapplication.Entities.Parcel;
import com.example.secondapplication.Model.ParcelDataSource;
import com.example.secondapplication.R;
import com.example.secondapplication.Util.Utils;

import java.util.List;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private List<Parcel> parcels;
    private  TextView textView;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(this, new Observer<List<Parcel>>() {
            @Override
            public void onChanged(List<Parcel> parcels) {
                String string=parcels.toString();
                textView.setText(string);
            }
        });
        Toast.makeText(getContext(), "onCreate", Toast.LENGTH_SHORT).show();
        //Address address=Utils.getLocationFromAddress(new Geocoder(getActivity()), "הוועד הלאומי, jerusalem");
        //Address address2=Utils.getLocationFromAddress(new Geocoder(getActivity()), "הרב פתאל 45, jerusalem");
        //double n=Utils.distanceBetweenTwoLocations(address.getLatitude(),address.getLongitude(),address2.getLatitude(),address2.getLongitude());
        return root;


    }

    @Override
    public void onResume() {
        Toast.makeText(getContext(), "onResum", Toast.LENGTH_SHORT).show();
        super.onResume();
    }

    @Override
    public void onPause() {
        Toast.makeText(getContext(), "onPause", Toast.LENGTH_SHORT).show();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        Toast.makeText(getContext(), "onDestory", Toast.LENGTH_SHORT).show();

        //ParcelDataSource.stopNotifyOffersParcelList();
        super.onDestroy();
    }
}