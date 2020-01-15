package com.example.secondapplication.ui.login;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.secondapplication.ViewModel.CustomerViewModel;
import com.example.secondapplication.ui.login.ui.UserManagementViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Locale;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseDatabase databaseReference;
    private DatabaseReference reference;
    private UserManagementViewModel userManagementViewModel;

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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userManagementViewModel = ViewModelProviders.of(this).get(UserManagementViewModel.class);

        //load views
        databaseReference=FirebaseDatabase.getInstance();
        reference=databaseReference.getReference("customers");
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

        //on click
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

        //define the user
        Customer customer=new Customer();
        customer.setAddress(email);
        customer.setId(id);
        customer.setAddress(address);
        customer.setPhoneNumber(phoneNumber);
        customer.setLastName(lastName);
        customer.setFirstName(firstName);

        userManagementViewModel.register(email, password, customer, new UserManagementViewModel.Action<String>() {
            @Override
            public void OnSuccess(String obj) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this, obj, Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }

            @Override
            public void OnFailure(Exception exception) {
                Toast.makeText(RegisterActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}