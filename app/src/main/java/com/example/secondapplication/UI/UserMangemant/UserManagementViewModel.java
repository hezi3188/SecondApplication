package com.example.secondapplication.UI.UserMangemant;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import com.example.secondapplication.Entities.Customer;
import com.example.secondapplication.Model.ParcelDataSource;
import com.example.secondapplication.Model.ParcelRepository;
import com.example.secondapplication.Util.Utils;
import com.example.secondapplication.UI.Service.ParcelService;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;

public class UserManagementViewModel extends AndroidViewModel {

    public interface  Action<T>{
        void OnSuccess(T obj);
        void OnFailure(Exception exception);
    }

    FirebaseAuth auth;
    public DatabaseReference reference;
    public static final String myPreference = "myUser";
    public static final String myName = "myName";
    public static final String myLatitude = "latitudeKey";
    public static final String myLongitude = "longitudeKey";
    SharedPreferences sharedPreferences;


    public UserManagementViewModel(@NonNull Application application) {
        super(application);
        sharedPreferences = application.getSharedPreferences(myPreference,
                Context.MODE_PRIVATE);
        auth=FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference=database.getReference("customers");
    }

    public void register(final String email, final String password, final Customer newCustomer ,final Action<String> action){
        final Geocoder geocoder = new Geocoder(getApplication(), Locale.getDefault());
        if(Utils.realLocation(geocoder,newCustomer.getAddress())==false){
            action.OnFailure(new Exception("The location error or no connection to internet, please enter a real location or connection Internet!"));
            return;
        }
        auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Address theAddress=Utils.getLocationFromAddress(geocoder,newCustomer.getAddress());
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString(myLongitude,String.valueOf(theAddress.getLongitude()));
                            editor.putString(myLatitude,String.valueOf(theAddress.getLatitude()));
                            editor.commit();
                            final Customer customer=new Customer(newCustomer.getId(),newCustomer.getFirstName(),newCustomer.getLastName(),newCustomer.getAddress(),theAddress.getLatitude(),theAddress.getLongitude(),newCustomer.getPhoneNumber(),email);
                            String key= Utils.encodeUserEmail(email);
                            reference.child(key).setValue(customer).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    action.OnSuccess("Registered successfully");
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    auth.getCurrentUser().delete();
                                    action.OnFailure(new Exception("Could not register please try again!"));
                                }
                            });

                        }
                        else {
                             action.OnFailure(new Exception(task.getException().getMessage()));
                        }
                    }
                });

    }

    public void login(final String email, final String password, final Action<String> action){
        auth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    reference.child(Utils.encodeUserEmail(email)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Customer customer = dataSnapshot.getValue(Customer.class);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString(myLongitude,String.valueOf(customer.getLongitude()));
                            editor.putString(myLatitude,String.valueOf(customer.getLatitude()));
                            editor.commit();
                            action.OnSuccess("Login succeed");
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            action.OnFailure(new Exception("Fail register, please try again!"));
                        }
                    });

                }
                else
                    action.OnFailure(new Exception(task.getException().getMessage()));
            }
        });
    }

    public void logOut(){
        ParcelDataSource.stopNotifyUserParcelList();
        ParcelDataSource.stopNotifyOffersParcelList();
        ParcelRepository.deleteAllParcels();
        ParcelDataSource.stopNotifyService();
        getApplication().stopService(new Intent(getApplication().getBaseContext(),ParcelService.class));
        auth.signOut();

    }
}