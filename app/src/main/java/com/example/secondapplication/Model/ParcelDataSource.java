package com.example.secondapplication.Model;

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

    //----------startFields----------//
    static DatabaseReference reference;
    static FirebaseAuth firebaseAuth;
    static FirebaseUser firebaseUser;
    static List<Parcel> userParcelList;
    static List<Parcel> offerParcelList;
    static {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        firebaseAuth=FirebaseAuth.getInstance();
        reference = database.getReference("parcels");
        userParcelList = new ArrayList<>();
        offerParcelList = new ArrayList<>();
    }
    private static ChildEventListener parcelsUserChildEventListener;
    private static ChildEventListener parcelsOffersChildEventListener;

    //--------------methods------------------//
    public static void notifyUserParcelList(final NotifyDataChange<List<Parcel>> notifyParcelsDataChange){
        if(notifyParcelsDataChange != null){
            if (parcelsUserChildEventListener != null){
                notifyParcelsDataChange.onFailure(new Exception("ERROR"));
                return;
            }
            userParcelList.clear();

            parcelsUserChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Parcel parcel = dataSnapshot.getValue(Parcel.class);
                    //if(parcel)
                    userParcelList.add(parcel);
                    notifyParcelsDataChange.onDataChanged(userParcelList);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Parcel parcel = dataSnapshot.getValue(Parcel.class);
                    for(int i = 0; i< userParcelList.size(); i++){
                        if(parcel.getParcelID().equals(userParcelList.get(i).getParcelID())){
                            userParcelList.set(i,parcel);
                        }
                    }
                    notifyParcelsDataChange.onDataChanged(userParcelList);
                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                    Parcel parcel = dataSnapshot.getValue(Parcel.class);
                    for(int i = 0; i< userParcelList.size(); i++){
                        if(parcel.getParcelID().equals(userParcelList.get(i).getParcelID())){
                            userParcelList.remove(i);
                            break;
                        }
                    }
                    notifyParcelsDataChange.onDataChanged(userParcelList);
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
            Query query=reference.orderByChild("customerId").equalTo(Utils.encodeUserEmail(firebaseUser.getEmail()));
            query.removeEventListener(parcelsUserChildEventListener);
            parcelsUserChildEventListener =null;
        }
    }

    public static void notifyOffersParcelList(final NotifyDataChange<List<Parcel>> notifyParcelsDataChange){
        if(notifyParcelsDataChange != null){
            if (parcelsOffersChildEventListener != null){
                notifyParcelsDataChange.onFailure(new Exception("ERROR"));
                return;
            }
            offerParcelList.clear();

            parcelsOffersChildEventListener = new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Parcel parcel = dataSnapshot.getValue(Parcel.class);

                    firebaseUser=firebaseAuth.getCurrentUser();
                    //if its my parcel
                    if((parcel.getCustomerId()).equals(firebaseUser.getEmail())||parcel.getStatus()==ParcelStatus.ACCEPTED||parcel.getStatus()==ParcelStatus.ON_THE_WAY)
                        return;


                    offerParcelList.add(parcel);
                    notifyParcelsDataChange.onDataChanged(offerParcelList);
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Parcel parcel = dataSnapshot.getValue(Parcel.class);

                    //if someone offer the parcel; Remove the parcel
                    if(parcel.getStatus()==ParcelStatus.ACCEPTED||parcel.getStatus()==ParcelStatus.ON_THE_WAY){
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

                    //if its my parcel
                    if((parcel.getCustomerId()).equals(firebaseUser.getEmail())||parcel.getStatus()==ParcelStatus.ACCEPTED||parcel.getStatus()==ParcelStatus.ON_THE_WAY)
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
                }).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
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

}
