package com.example.secondapplication.ui.login;

import androidx.annotation.NonNull;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.secondapplication.Entities.Customer;
import com.example.secondapplication.R;
import com.example.secondapplication.Util.LocationAutoComplate.PlaceAutoSuggestAdapter;
import com.example.secondapplication.Util.Utils;
import com.example.secondapplication.ViewModel.CustomerViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Locale;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseDatabase databaseReference;
    private DatabaseReference reference;

    private CustomerViewModel customerViewModel;

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText idEditText;
    private AutoCompleteTextView addressEditText;
    private EditText phoneNumberEditText;
    private EditText firstNameNumberEditText;
    private EditText lastNameEditText;
    private Button registerButton;
    private TextView signInTextView;

    private ProgressDialog progressDialog;

    SharedPreferences sharedPreferences;
    public static final String myPreference = "myUser";
    public static final String myLatitude = "latitudeKey";
    public static final String myLongitude = "longitudeKey";



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        customerViewModel= ViewModelProviders.of(this).get(CustomerViewModel.class);
        databaseReference=FirebaseDatabase.getInstance();
        reference=databaseReference.getReference("customers");
        mAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        usernameEditText=(EditText)findViewById(R.id.username);
        passwordEditText=(EditText) findViewById(R.id.password);
        idEditText=(EditText)findViewById(R.id.idCustomer);
        addressEditText =(AutoCompleteTextView) findViewById(R.id.address);
        addressEditText.setAdapter(new PlaceAutoSuggestAdapter(RegisterActivity.this,android.R.layout.simple_list_item_1));
        phoneNumberEditText=(EditText)findViewById(R.id.phoneNumber);
        firstNameNumberEditText=(EditText)findViewById(R.id.firstName);
        lastNameEditText=(EditText)findViewById(R.id.lastname);
        registerButton=(Button)findViewById(R.id.registerButton);
        signInTextView=(TextView)findViewById(R.id.signInFromRegister);

        registerButton.setOnClickListener(this);
        signInTextView.setOnClickListener(this);



        sharedPreferences = getSharedPreferences(myPreference,
                Context.MODE_PRIVATE);

    }

    @Override
    public void onClick(View v) {
        if(v==registerButton){
            registerUser();
        }
        if(v==signInTextView){
            startActivity(new Intent(this,LoginActivity.class));
        }
    }

    private void registerUser() {


        final String email=usernameEditText.getText().toString().trim();
        String password=passwordEditText.getText().toString().trim();
        final String id=idEditText.getText().toString().trim();
        final String address= addressEditText.getText().toString().trim();

        final String phoneNumber=phoneNumberEditText.getText().toString().trim();
        final String lastName=lastNameEditText.getText().toString().trim();
        final String firstName=firstNameNumberEditText.getText().toString().trim();
        if(TextUtils.isEmpty(email)){
            //email is empty
            Toast.makeText(this, "Please enter email!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(id)){
            Toast.makeText(this,"Please enter id!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(address)){
            Toast.makeText(this,"Please enter your full address!",Toast.LENGTH_SHORT).show();
            return;
        }

        if(TextUtils.isEmpty(phoneNumber)){
            Toast.makeText(this,"Please enter phone number!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(firstName)){
            Toast.makeText(this,"Please enter first name!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(lastName)){
            Toast.makeText(this,"Please enter last name!",Toast.LENGTH_SHORT).show();
            return;
        }

        //if validation are ok
        progressDialog.setMessage("Registering user...");
        progressDialog.show();
        final Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Address theAddress=Utils.getLocationFromAddress(geocoder,address);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.putString(myLongitude,String.valueOf(theAddress.getLongitude()));
                            editor.putString(myLatitude,String.valueOf(theAddress.getLatitude()));
                           final Customer customer=new Customer(id,firstName,lastName,address,theAddress.getLatitude(),theAddress.getLongitude(),phoneNumber,email);
                            String key= Utils.encodeUserEmail(email);
                            reference.child(key).setValue(customer).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    customerViewModel.insert(customer);
                                    Toast.makeText(RegisterActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                                    finish();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    mAuth.getCurrentUser().delete();
                                    Toast.makeText(RegisterActivity.this,"Could not register please try again!",Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            });

                        }
                        else
                            Toast.makeText(RegisterActivity.this,"Could not register please try again!",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                });

    }
}