package com.example.secondapplication.ui.login.ui.slideshow;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secondapplication.Entities.Parcel;
import com.example.secondapplication.R;
import com.example.secondapplication.ui.login.ui.home.HomeFragment;

import java.util.ArrayList;
import java.util.List;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private RecyclerView offerParcelsRecyclerView;
    View root;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        slideshowViewModel.getText().observe(this, new Observer<List<Parcel>>() {
            @Override
            public void onChanged(List<Parcel> parcels) {
            }
        });
        return root;
    }
    public class OfferRecyclerView extends RecyclerView.Adapter<OfferRecyclerView.ParcelViewHolder>{
        List<Parcel> parcelList=new ArrayList<>();

        @NonNull
        @Override
        public OfferRecyclerView.ParcelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity().getBaseContext()).inflate(R.layout.offered_parcel_item,parent,false);
            return new ParcelViewHolder(v);
        }
        public Parcel getNoteAt(int position) {
            return parcelList.get(position);
        }


        @Override
        public void onBindViewHolder(@NonNull OfferRecyclerView.ParcelViewHolder holder, int position) {

        }
        public void setParcels(List<Parcel> parcels) {
            this.parcelList = parcels;
            notifyDataSetChanged();
        }

        @Override
        public int getItemCount() {
            return parcelList.size();
        }

        public class ParcelViewHolder extends RecyclerView.ViewHolder {
            public ParcelViewHolder(@NonNull View itemView) {
                super(itemView);
            }
        }
    }
}