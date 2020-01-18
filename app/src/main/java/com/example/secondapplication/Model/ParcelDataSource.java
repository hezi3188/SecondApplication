package com.example.secondapplication.Model;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.secondapplication.Entities.Parcel;
import com.example.secondapplication.Entities.ParcelStatus;
import com.example.secondapplication.Util.Utils;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ParcelDataSource {

    //----------singleton-------//
    private  ParcelDataSource(){}
    private static ParcelDataSource instance=null;

    public static ParcelDataSource getInstance() {
        if(instance==null)
            instance=new ParcelDataSource();
        return instance;
    }
    public interface OnUserNotify<Parcel>{
        void onStart();
        void onChildAdd(Parcel p);
        void onChildUpdate(Parcel p);
        void onChildRemoved(Parcel p);
        void onFailure(Exception exception);
    }
    //--------interfaces-----------//
    public interface  Action<T>{
        void OnSuccess(T obj);
        void OnFailure(Exception exception);
        void OnProgress(String status, double percent);
    }
    public interface NotifyDataChange<T>{
        void  onDataChanged(T obj);
        void onFailure(Exception exception);
    }
    public interface ServiceNotify<T>{
        void  onNewChildAdded(T obj);
        void onFailure(Exception exception);
    }

    //----------startFields----------//
    static DatabaseReference reference;
    static FirebaseAuth firebaseAuth;
    static FirebaseUser firebaseUser;
    static List<Parcel> userParcelList;
    static List<Parcel> offerParcelList;
    static SharedPreferences sharedPreferences;
    public static final String myPreference = "myUser";
    public static final String myLatitude = "latitudeKey";
    public static final String myLongitude = "longitudeKey";
    static {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        reference = database.getReference("parcels");
        userParcelList = new ArrayList<>();
        offerParcelList = new ArrayList<>();
    }
    private static ChildEventListener parcelsUserChildEventListener;
    private static ChildEventListener parcelsOffersChildEventListener;
    private static ChildEventListener serviceChildEventListener;

    //--------------methods------------------//
    public static void notifyUserParcelList(final OnUserNotify<Parcel> notifyParcelsDataChange){
        if(notifyParcelsDataChange != null){
            if (parcelsUserChildEventListener != null){
                notifyParcelsDataChange.onFailure(new Exception("ERROR"));
                return;
            }
            userParcelList.clear();
            notifyParcelsDataChange.onStart();
            parcelsUserChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Parcel parcel = dataSnapshot.getValue(Parcel.class);
                    //userParcelList.add(parcel);
                    notifyParcelsDataChange.onChildAdd(parcel);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Parcel parcel = dataSnapshot.getValue(Parcel.class);
                    notifyParcelsDataChange.onChildUpdate(parcel);
/*
                    for(int i = 0; i< userParcelList.size(); i++){
                        if(parcel.getParcelID().equals(userParcelList.get(i).getParcelID())){
                            //userParcelList.set(i,parcel);
                            notifyParcelsDataChange.onChildUpdate(parcel);
                        }
                    }*/
                   // notifyParcelsDataChange.onDataChanged(userParcelList);
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    Parcel parcel = dataSnapshot.getValue(Parcel.class);
                    notifyParcelsDataChange.onChildRemoved(parcel);
                  /*  for(int i = 0; i< userParcelList.size(); i++){
                        if(parcel.getParcelID().equals(userParcelList.get(i).getParcelID())){
                            userParcelList.remove(i);
                            break;
                        }
                    }
                    notifyParcelsDataChange.onDataChanged(userParcelList);*/
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    notifyParcelsDataChange.onFailure(databaseError.toException());
                }
            };
            firebaseUser=firebaseAuth.getCurrentUser();
            Query query=reference.orderByChild("customerId").equalTo(firebaseUser.getEmail());
            query.addChildEventListener(parcelsUserChildEventListener);
        }
    }
    public static void stopNotifyUserParcelList(){
        if(parcelsUserChildEventListener !=null){
            reference.removeEventListener(parcelsUserChildEventListener);
            parcelsUserChildEventListener =null;
        }
    }
    public static void notifyService(Context context,final ServiceNotify<Parcel> parcelServiceNotify){
        if(parcelServiceNotify != null){
            if (serviceChildEventListener != null){
                parcelServiceNotify.onFailure(new Exception("ERROR"));
                return;
            }
            sharedPreferences = context.getSharedPreferences(myPreference,Context.MODE_PRIVATE);

            //get user location
            final double latitude=Double.parseDouble(sharedPreferences.getString(myLatitude,""));
            final double longitude=Double.parseDouble(sharedPreferences.getString(myLongitude,""));
            final Date currentDate=new Date(System.currentTimeMillis());
            serviceChildEventListener=new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Parcel parcel = dataSnapshot.getValue(Parcel.class);

                    //if the parcel is old
                    if(currentDate.after(parcel.getGetParcelDate()))
                        return;

                    //if the user logout
                    if(firebaseAuth.getCurrentUser()==null)
                        return;
                    //calculate distance location
                    double distance=Utils.distanceBetweenTwoLocations(latitude,longitude,parcel.getLatitude(),parcel.getLongitude());

                    firebaseUser=firebaseAuth.getCurrentUser();
                    //if its my parcel
                    if(distance>50.0||(parcel.getCustomerId()).equals(firebaseUser.getEmail())||parcel.getStatus()==ParcelStatus.ACCEPTED||parcel.getStatus()==ParcelStatus.ON_THE_WAY)
                        return;


                    parcelServiceNotify.onNewChildAdded(parcel);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            };
        }
        reference.addChildEventListener(serviceChildEventListener);
        }
    public static void stopNotifyService(){
        if(serviceChildEventListener !=null){
            reference.removeEventListener(serviceChildEventListener);
            serviceChildEventListener =null;
        }
    }

    public static void notifyOffersParcelList(Context context,final NotifyDataChange<List<Parcel>> notifyParcelsDataChange){
        if(notifyParcelsDataChange != null){
            if (parcelsOffersChildEventListener != null){
                notifyParcelsDataChange.onFailure(new Exception("ERROR"));
                return;
            }
            offerParcelList.clear();

            sharedPreferences = context.getSharedPreferences(myPreference,Context.MODE_PRIVATE);

            //get user location
            final double latitude=Double.parseDouble(sharedPreferences.getString(myLatitude,""));
            final double longitude=Double.parseDouble(sharedPreferences.getString(myLongitude,""));

            parcelsOffersChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Parcel parcel = dataSnapshot.getValue(Parcel.class);
                    //calculate distance location
                    double distance=Utils.distanceBetweenTwoLocations(latitude,longitude,parcel.getLatitude(),parcel.getLongitude());
                    firebaseUser=firebaseAuth.getCurrentUser();
                    //if its my parcel or someone other take it
                    if(distance>50.0||(parcel.getCustomerId()).equals(firebaseUser.getEmail())||(parcel.getStatus()==ParcelStatus.ACCEPTED&&!parcel.getDeliveryName().equals(firebaseUser.getEmail())))
                        return;


                    offerParcelList.add(parcel);
                    notifyParcelsDataChange.onDataChanged(offerParcelList);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Parcel parcel = dataSnapshot.getValue(Parcel.class);

                    //calculate distance location
                    double distance=Utils.distanceBetweenTwoLocations(latitude,longitude,parcel.getLatitude(),parcel.getLongitude());

                    //if someone offer the parcel or the location move; Remove the parcel
                    if(distance>50.0||(!parcel.getDeliveryName().equals(firebaseUser.getEmail())&&(parcel.getStatus()==ParcelStatus.ON_THE_WAY||parcel.getStatus()==ParcelStatus.ACCEPTED))){
                        for(int i = 0; i< offerParcelList.size(); i++){
                            if(parcel.getParcelID().equals(offerParcelList.get(i).getParcelID())){
                                offerParcelList.remove(i);
                            }
                        }
                    }

                    for(int i = 0; i< offerParcelList.size(); i++){
                        if(parcel.getParcelID().equals(offerParcelList.get(i).getParcelID())){
                            offerParcelList.set(i,parcel);
                        }
                    }
                    notifyParcelsDataChange.onDataChanged(offerParcelList);
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    Parcel parcel = dataSnapshot.getValue(Parcel.class);
                    double distance=Utils.distanceBetweenTwoLocations(latitude,longitude,parcel.getLatitude(),parcel.getLongitude());

                    //if its my parcel
                    if(distance>50.0||(parcel.getCustomerId()).equals(firebaseUser.getEmail())||(parcel.getStatus()==ParcelStatus.ACCEPTED&&!parcel.getDeliveryName().equals(firebaseUser.getEmail())))
                        return;

                    for(int i = 0; i< offerParcelList.size(); i++){
                        if(parcel.getParcelID().equals(offerParcelList.get(i).getParcelID())){
                            offerParcelList.remove(i);
                            break;
                        }
                    }
                    notifyParcelsDataChange.onDataChanged(offerParcelList);
                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    notifyParcelsDataChange.onFailure(databaseError.toException());
                }
            };
            firebaseUser=firebaseAuth.getCurrentUser();
            Query query=reference.orderByChild("getParcelDate");
            query.addChildEventListener(parcelsOffersChildEventListener);
        }
    }
    public static void stopNotifyOffersParcelList(){
        if(parcelsOffersChildEventListener !=null){
            Query query=reference.orderByChild("getParcelDate");
            query.removeEventListener(parcelsOffersChildEventListener);
            parcelsOffersChildEventListener =null;
        }
    }

    public static void acceptedParcel(String id, final Action<String> action){
        ParcelStatus parcelStatus=ParcelStatus.ACCEPTED;
        reference.child(id).child("status").setValue(parcelStatus).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                action.OnSuccess("Accepted Parcel");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                action.OnFailure(new Exception("Error"));
            }
        });
    }

    public static void addDelivery(final String id, final String deliveryName, final Action<String> action){
        reference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Parcel parcel = dataSnapshot.getValue(Parcel.class);
                parcel.putMessenger(Utils.encodeUserEmail(deliveryName),false);
                parcel.setStatus(ParcelStatus.IN_COLLECTION_PROCESS);
                reference.child(id).setValue(parcel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        action.OnSuccess("Added delivery");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        action.OnFailure(new Exception("Error"));
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                action.OnFailure(new Exception("Error"));
            }
        });
    }


    public static void confirmDelivery(final String id, final String deliveryName, final Action<String> action){
        reference.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Parcel parcel = dataSnapshot.getValue(Parcel.class);
                parcel.getMessengers().put(Utils.encodeUserEmail(deliveryName),true);
                parcel.setDeliveryName(deliveryName);
                parcel.setStatus(ParcelStatus.ON_THE_WAY);
                reference.child(id).setValue(parcel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        action.OnSuccess("Confirm deliver "+deliveryName+" is succeeded");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        action.OnFailure(new Exception("Error"));
                    }
                }
            );
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                action.OnFailure(new Exception("Error"));
            }
        });
    }

}
