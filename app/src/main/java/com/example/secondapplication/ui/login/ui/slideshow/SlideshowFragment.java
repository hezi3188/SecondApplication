package com.example.secondapplication.ui.login.ui.slideshow;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.secondapplication.Entities.Parcel;
import com.example.secondapplication.Entities.ParcelStatus;
import com.example.secondapplication.R;
import com.example.secondapplication.Util.Converter;
import com.example.secondapplication.Util.Utils;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    private RecyclerView offerParcelsRecyclerView;
    private FirebaseAuth auth;
    private FirebaseUser user;
    View root;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        offerParcelsRecyclerView=root.findViewById(R.id.offer_recycler_view);
        offerParcelsRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        offerParcelsRecyclerView.setHasFixedSize(true);
        final OfferRecyclerView adapter=new OfferRecyclerView();
        offerParcelsRecyclerView.setAdapter(adapter);
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        slideshowViewModel.getText().observe(this, new Observer<List<Parcel>>() {
            @Override
            public void onChanged(List<Parcel> parcels) {
                adapter.setParcels(parcels);
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
        public void onBindViewHolder(@NonNull final OfferRecyclerView.ParcelViewHolder holder, int position) {
            final Parcel parcel=parcelList.get(position);


            DateFormat dateFormat = new SimpleDateFormat("[dd/MM/yyyy]");
            holder.idTextView.setBackgroundColor(Color.LTGRAY);
            holder.idTextView.setText("Id:"+parcel.getParcelID());
            holder.addressTextView.setText("Address: "+parcel.getAddress());
            holder.nameTextView.setText("User: "+parcel.getCustomerId());
            holder.detailsOffer.setText("Details: "+ Converter.fromParcelTypeToString(parcel.getParcelType())+", "+
                    Converter.fromParcelWeightToString(parcel.getParcelWeight())
                    +" and "+(parcel.isFragile()?"fragile":"no-fragile")+" from "+dateFormat.format(parcel.getGetParcelDate()));

            if(parcel.getStatus()== ParcelStatus.ACCEPTED){
                holder.addDeliveryButton.setEnabled(false);
                holder.addDeliveryButton.setText("The parcel arrived, thanks!!!");
                holder.addDeliveryButton.setBackgroundColor(Color.GREEN);
            }
            else if(parcel.getMessengers().containsKey(Utils.encodeUserEmail(user.getEmail()))){
                holder.addDeliveryButton.setEnabled(false);
                holder.addDeliveryButton.setBackgroundColor(Color.GRAY);
                if(parcel.getMessengers().get(Utils.encodeUserEmail(user.getEmail()))==true){
                    holder.addDeliveryButton.setText("You selected to take the parcel");
                }
                else{
                    holder.addDeliveryButton.setText("You offered the parcel");
                }
            }
            else {
                holder.addDeliveryButton.setEnabled(true);
                holder.addDeliveryButton.setBackgroundColor(Color.GRAY);
                holder.addDeliveryButton.setText("I want to take the Parcel");
            }
            holder.addDeliveryButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    slideshowViewModel.addDelivery(parcel.getParcelID(),user.getEmail());
                }
            });
            holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    int selectedItem=holder.spinner.getSelectedItemPosition();
                    if(selectedItem==0){
                        holder.sendMessageButton.setText("SEND");
                        holder.messageEditText.setEnabled(true);
                        Toast.makeText(getActivity(), "Send message successfully", Toast.LENGTH_SHORT).show();
                    }
                    else if(selectedItem==1){
                        holder.sendMessageButton.setText("SEND");
                        holder.messageEditText.setEnabled(true);
                        Toast.makeText(getActivity(), "Send email successfully", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        holder.sendMessageButton.setText("CALL");
                        holder.messageEditText.setEnabled(false);
                        Toast.makeText(getActivity(), "Calling...", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            holder.sendMessageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int selectedItem=(int)holder.spinner.getSelectedItemPosition();
                    if(selectedItem==0){

                        // Here, thisActivity is the current activity
                        if (ContextCompat.checkSelfPermission(getActivity(),
                                Manifest.permission.SEND_SMS)
                                != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.SEND_SMS},1);


                            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                            // app-defined int constant. The callback method gets the
                            // result of the request.
                        }
                        else {
                            // Permission has already been granted
                            //Getting intent and PendingIntent instance
                            Intent intent=new Intent(getContext(),SlideshowFragment.class);
                            PendingIntent pi=PendingIntent.getActivity(getContext(), 0, intent,0);

                            //Get the SmsManager instance and call the sendTextMessage method to send message
                            SmsManager sms=SmsManager.getDefault();
                            sms.sendTextMessage("0543411320", null, holder.messageEditText.getText().toString(), pi,null);
                            holder.messageEditText.setText("");
                            Toast.makeText(getActivity(), "Send message successfully", Toast.LENGTH_SHORT).show();
                            // Toast.makeText(getActivity(), "Calling...", Toast.LENGTH_SHORT).show();
                        }

                    }
                    else if(selectedItem==1){
                        Intent email = new Intent(Intent.ACTION_SEND);
                        email.putExtra(Intent.EXTRA_EMAIL, new String[]{ parcel.getCustomerId()});
                        email.putExtra(Intent.EXTRA_SUBJECT, user.getEmail()+" want to take your parcel");
                        email.putExtra(Intent.EXTRA_TEXT, holder.messageEditText.getText().toString());

                        //need this to prompts email client only
                        email.setType("message/rfc822");

                        startActivity(Intent.createChooser(email, "Choose an Email client :"));
                        holder.messageEditText.setText("");
                    }
                    else {
                        // Here, thisActivity is the current activity
                        if (ContextCompat.checkSelfPermission(getActivity(),
                                Manifest.permission.CALL_PHONE)
                                != PackageManager.PERMISSION_GRANTED) {

                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE},1);


                            // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                                // app-defined int constant. The callback method gets the
                                // result of the request.
                            }
                        else {
                            // Permission has already been granted
                            Intent callIntent = new Intent(Intent.ACTION_CALL);
                            callIntent.setData(Uri.parse("tel:91-000-000-0000"));
                            startActivity(callIntent);
                           // Toast.makeText(getActivity(), "Calling...", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });

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
            TextView nameTextView;
            TextView addressTextView;
            TextView detailsOffer;
            EditText messageEditText;
            Button sendMessageButton;
            Button addDeliveryButton;
            Spinner spinner;

            ArrayAdapter<String> spinnerArrayAdapter;
            public ParcelViewHolder(@NonNull View itemView) {
                super(itemView);
                idTextView=(TextView)itemView.findViewById(R.id.text_view_id_offer);
                nameTextView=(TextView)itemView.findViewById(R.id.text_view_name_offer);
                addressTextView=(TextView)itemView.findViewById(R.id.address_offer);
                detailsOffer=(TextView)itemView.findViewById(R.id.text_view_details_offer);
                messageEditText =(EditText) itemView.findViewById(R.id.message_offer);
                sendMessageButton=(Button)itemView.findViewById(R.id.button_send_offer);
                addDeliveryButton =(Button)itemView.findViewById(R.id.button_add_delivery_offer);
                spinner=(Spinner)itemView.findViewById(R.id.spinner_offer);
                ArrayList<String>options=new ArrayList<>();
                options.add("SMS");
                options.add("EMAIL");
                options.add("CALL");
                spinnerArrayAdapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,options);
                spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(spinnerArrayAdapter);
            }
        }
    }
}