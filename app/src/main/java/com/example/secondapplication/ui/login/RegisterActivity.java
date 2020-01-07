package com.example.secondapplication.ui.login;

import androidx.annotation.NonNull;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.secondapplication.Entities.Customer;
import com.example.secondapplication.R;
import com.example.secondapplication.Util.Converter;
import com.example.secondapplication.Util.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;
    private FirebaseDatabase databaseReference;
    private DatabaseReference reference;

    private EditText usernameEditText;
    private EditText passwordEditText;
    private EditText idEditText;
    private EditText countryEditText;
    private EditText cityEditText;
    private EditText streetEditText;
    private EditText buildingNumberEditText;
    private EditText postalNumberEditText;
    private EditText phoneNumberEditText;
    private EditText firstNameNumberEditText;
    private EditText lastNameEditText;
    private Button registerButton;
    private TextView signInTextView;

    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        databaseReference=FirebaseDatabase.getInstance();
        reference=databaseReference.getReference("customers");
        mAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        usernameEditText =(EditText) findViewById(R.id.username);
        passwordEditText = (EditText)findViewById(R.id.password);
        idEditText=(EditText)findViewById(R.id.idCustomer);
        countryEditText=(EditText)findViewById(R.id.country);
        cityEditText=(EditText)findViewById(R.id.city);
        streetEditText=(EditText)findViewById(R.id.street);
        buildingNumberEditText=(EditText)findViewById(R.id.buildingNumber);
        postalNumberEditText=(EditText)findViewById(R.id.postalAddress);
        phoneNumberEditText=(EditText)findViewById(R.id.phoneNumber);
        firstNameNumberEditText=(EditText)findViewById(R.id.firstName);
        lastNameEditText=(EditText)findViewById(R.id.lastname);
        registerButton=(Button)findViewById(R.id.registerButton);
        signInTextView=(TextView)findViewById(R.id.signInFromRegister);

        registerButton.setOnClickListener(this);
        signInTextView.setOnClickListener(this);



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
        final String country=countryEditText.getText().toString().trim();
        final String city=cityEditText.getText().toString().trim();
        final String street=streetEditText.getText().toString().trim();
         int buildingNumber=0,postalAddress=0;
        try {
            buildingNumber=Integer.parseInt(buildingNumberEditText.getText().toString().trim());
            postalAddress=Integer.parseInt(postalNumberEditText.getText().toString().trim());
        }
        catch (Exception e){

        }
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
        if(TextUtils.isEmpty(country)){
            Toast.makeText(this,"Please enter country!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(city)){
            Toast.makeText(this,"Please enter city!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(street)){
            Toast.makeText(this,"Please enter street!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(buildingNumberEditText.getText().toString().trim())){
            Toast.makeText(this,"Please enter building number!",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(postalNumberEditText.getText().toString().trim())){
            Toast.makeText(this,"Please enter postal address!",Toast.LENGTH_SHORT).show();
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

        final int finalBuildingNumber = buildingNumber;
        final int finalPostalAddress = postalAddress;
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Customer customer=new Customer(id,firstName,lastName,city,country,street, finalBuildingNumber, finalPostalAddress,phoneNumber,email);
                            String key= Utils.encodeUserEmail(email);
                            reference.child(key).setValue(customer).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
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