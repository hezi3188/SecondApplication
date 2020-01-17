package com.example.secondapplication.ui.login.ui.gallery;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secondapplication.Entities.Parcel;
import com.example.secondapplication.R;
import com.example.secondapplication.Util.Converter;

import java.util.ArrayList;
import java.util.List;


public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private RecyclerView acceptedParcelRecyclerView;
    View root;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_gallery, container, false);

        acceptedParcelRecyclerView =root.findViewById(R.id.accepted_recycler_view);
        acceptedParcelRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        acceptedParcelRecyclerView.setHasFixedSize(true);
        final AcceptedRecyclerView adapter=new AcceptedRecyclerView();
        acceptedParcelRecyclerView.setAdapter(adapter);
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        galleryViewModel.getText().observe(this, new Observer<List<Parcel>>() {
            @Override
            public void onChanged(List<Parcel> parcels) {
                adapter.setParcels(parcels);
            }
        });
        return root;
    }



    public class AcceptedRecyclerView extends RecyclerView.Adapter<AcceptedRecyclerView.ParcelViewHolder>{

        private List<Parcel> parcelList=new ArrayList<>();
        @NonNull
        @Override
        public AcceptedRecyclerView.ParcelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
          View v = LayoutInflater.from(getActivity().getBaseContext()).inflate(R.layout.accepted_parcel_item,parent,false);
          return new ParcelViewHolder(v);
        }
        public Parcel getNoteAt(int position) {
            return parcelList.get(position);
        }


        public void setParcels(List<Parcel> parcels) {
            this.parcelList = parcels;
            notifyDataSetChanged();
        }
        @Override
        public void onBindViewHolder(@NonNull AcceptedRecyclerView.ParcelViewHolder holder, int position) {
            Parcel parcel=parcelList.get(position);

            holder.deliveryTextView.setText("DELIVERY: "+parcel.getDeliveryName());
            holder.typeTextView.setText("DESC: "+Converter.fromParcelTypeToString(parcel.getParcelType())+",");
            holder.idTextView.setText("ID:"+parcel.getParcelID());
            holder.idTextView.setBackgroundColor(Color.LTGRAY);
            holder.statusTextView.setText(Converter.fromParcelStatusToString(parcel.getStatus()));
            holder.statusTextView.setTextColor(Color.GREEN);
            holder.weightTextView.setText(Converter.fromParcelWeightToString(parcel.getParcelWeight()));

        }

        @Override
        public int getItemCount() {
            return parcelList.size();
        }
        class ParcelViewHolder extends RecyclerView.ViewHolder{

            TextView idTextView;
            TextView statusTextView;
            TextView typeTextView;
            TextView weightTextView;
            TextView deliveryTextView;
            public ParcelViewHolder(@NonNull View itemView) {
                super(itemView);
                idTextView =(TextView)itemView.findViewById(R.id.text_view_id_accepted);
                statusTextView=(TextView)itemView.findViewById(R.id.text_view_status_accepted);
                typeTextView =(TextView)itemView.findViewById(R.id.text_view_type_accepted);
                weightTextView=(TextView)itemView.findViewById(R.id.text_view_weight_accepted);
                deliveryTextView=(TextView)itemView.findViewById(R.id.text_view_delivery_person_accepted);
            }
        }
    }

}


