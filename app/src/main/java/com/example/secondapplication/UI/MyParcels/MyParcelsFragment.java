package com.example.secondapplication.UI.MyParcels;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secondapplication.Entities.Parcel;
import com.example.secondapplication.Entities.ParcelStatus;
import com.example.secondapplication.R;
import com.example.secondapplication.Util.Converter;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyParcelsFragment extends Fragment {

    private MyParcelsViewModel myParcelsViewModel;
    private RecyclerView notAcceptedParcelsRecyclerView;
    View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_home, container, false);
        notAcceptedParcelsRecyclerView =root.findViewById(R.id.not_accepted_recycler_view);
        notAcceptedParcelsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        notAcceptedParcelsRecyclerView.setHasFixedSize(true);
        final  NotAcceptedRecyclerView adapter=new NotAcceptedRecyclerView();
        notAcceptedParcelsRecyclerView.setAdapter(adapter);
        myParcelsViewModel =
                ViewModelProviders.of(this).get(MyParcelsViewModel.class);
        myParcelsViewModel.getMyParcels().observe(this, new Observer<List<Parcel>>() {
            @Override
            public void onChanged(List<Parcel> parcels) {
                adapter.setParcels(parcels);
            }
        });
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    //Class for recyclerView
    public class NotAcceptedRecyclerView extends RecyclerView.Adapter<NotAcceptedRecyclerView.ParcelViewHolder>{
        List<Parcel> parcelList=new ArrayList<>();
        @NonNull
        @Override
        public ParcelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(getActivity().getBaseContext()).inflate(R.layout.not_accepted_parcel_item,parent,false);
            return new ParcelViewHolder(v);
        }

        public Parcel getNoteAt(int position) {
            return parcelList.get(position);
        }

        //return all delivers
        public ArrayList<String> getDelivers(Map<String,Boolean> delivers){
            ArrayList<String> arrayList=new ArrayList<>();
            arrayList.add("Choose delivery");
            for (Map.Entry<String,Boolean>entry:delivers.entrySet()) {
                arrayList.add(entry.getKey()+".com");
            }
            return arrayList;
        }

        @Override
        public void onBindViewHolder(@NonNull final ParcelViewHolder holder, int position) {
            final Parcel parcel=parcelList.get(position);
            DateFormat dateFormat = new SimpleDateFormat("[dd/MM/yyyy]");
            ArrayList<String> delivers=new ArrayList<>();
            ArrayAdapter<String> spinnerArrayAdapter;

            holder.idTextView.setBackgroundColor(Color.LTGRAY);
            holder.idTextView.setText("Id:"+parcel.getParcelID());
            holder.statusTextView.setText(Converter.fromParcelStatusToString(parcel.getStatus()));
            holder.descriptionTextView.setText("Desc: "+Converter.fromParcelTypeToString(parcel.getParcelType())+",  "+
                    Converter.fromParcelWeightToString(parcel.getParcelWeight())+"\n           "
            +"and "+(parcel.isFragile()?"fragile":"no-fragile")+" from "+dateFormat.format(parcel.getGetParcelDate()));

            //if the parcel sent
            if(parcel.getStatus()== ParcelStatus.SENT){
                holder.statusTextView.setTextColor(Color.RED);
                holder.offerDeliversSpinner.setAdapter(null);
                delivers.add("No delivers offers");
                spinnerArrayAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,delivers);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                holder.offerDeliversSpinner.setAdapter(spinnerArrayAdapter);
                holder.offerDeliversSpinner.setEnabled(false);
                holder.button.setEnabled(false);
                holder.button.setText("No delivers offers");
            }

            //if someone offer the parcel
            if(parcel.getStatus()==ParcelStatus.IN_COLLECTION_PROCESS){
                holder.statusTextView.setTextColor(Color.rgb(255,165,0));
                holder.offerDeliversSpinner.setEnabled(true);
                holder.offerDeliversSpinner.setAdapter(null);
                delivers=getDelivers(parcel.getMessengers());
                spinnerArrayAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,delivers);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                holder.offerDeliversSpinner.setAdapter(spinnerArrayAdapter);
                holder.button.setEnabled(true);
                holder.button.setText("Choose");
                holder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (holder.offerDeliversSpinner.getSelectedItemPosition()==0){
                            Toast.makeText(getActivity(), "choose deliver", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        myParcelsViewModel.confirmDelivery(parcel.getParcelID(),holder.offerDeliversSpinner.getSelectedItem().toString());

                    }
                });
            }

            //if confirmed deliver
            if(parcel.getStatus()==ParcelStatus.ON_THE_WAY){
                holder.statusTextView.setTextColor(Color.YELLOW);
                holder.offerDeliversSpinner.setAdapter(null);
                holder.button.setEnabled(true);
                holder.offerDeliversSpinner.setEnabled(false);
                holder.button.setText("Accepted parcel");
                delivers.add("Deliver:"+parcel.getDeliveryName());
                spinnerArrayAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,delivers);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                holder.offerDeliversSpinner.setAdapter(spinnerArrayAdapter);
                holder.button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        myParcelsViewModel.acceptedParcel(parcel.getParcelID());
                    }
                });

            }
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
            TextView idTextView;
            TextView statusTextView;
            TextView descriptionTextView;
            Spinner offerDeliversSpinner;
            Button button;
            public ParcelViewHolder(@NonNull View itemView) {
                super(itemView);
                idTextView=(TextView)itemView.findViewById(R.id.text_view_id_not_accepted);
                statusTextView=(TextView)itemView.findViewById(R.id.text_view_status_not_accepted);
                descriptionTextView=(TextView)itemView.findViewById(R.id.text_view_description_not_accepted);
                offerDeliversSpinner=(Spinner)itemView.findViewById(R.id.spinner_not_accepted);
                button=(Button)itemView.findViewById(R.id.button_not_accepted);
            }
        }
    }
}