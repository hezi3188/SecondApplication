package com.example.secondapplication.ui.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.secondapplication.Entities.Customer;
import com.example.secondapplication.R;
import com.example.secondapplication.Util.Utils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignup;

    SharedPreferences sharedPreferences;
    public static final String myPreference = "myUser";
    public static final String myLatitude = "latitudeKey";
    public static final String myLongitude = "longitudeKey";

    public DatabaseReference reference;


    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference=database.getReference("customers");
        firebaseAuth=FirebaseAuth.getInstance();

        sharedPreferences = getSharedPreferences(myPreference,
                Context.MODE_PRIVATE);


        if(firebaseAuth.getCurrentUser()!=null){
            //profile activity
            finish();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }

        editTextEmail=(EditText)findViewById(R.id.emailEditText);
        editTextPassword=(EditText)findViewById(R.id.passwordEditText);
        buttonSignIn=(Button)findViewById(R.id.loginButton);
        textViewSignup=(TextView)findViewById(R.id.signUpTextView);

        progressDialog=new ProgressDialog(this);

        buttonSignIn.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v==buttonSignIn){
            userLogin();
        }
        if(v==textViewSignup){
            finish();
            startActivity(new Intent(this,RegisterActivity.class));
        }
    }

    private void userLogin() {
        final String email=editTextEmail.getText().toString().trim();
        String password=editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            //email is empty
            Toast.makeText(this, "Please entar email!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please entar password!",Toast.LENGTH_SHORT).show();
            return;
        }
        //if validation are ok
        progressDialog.setMessage("Sign in ...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressDialog.dismiss();
                        if(task.isSuccessful()){
                            reference.child(Utils.encodeUserEmail(email)).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    Customer customer = dataSnapshot.getValue(Customer.class);
                                    SharedPreferences.Editor editor=sharedPreferences.edit();
                                    editor.putString(myLongitude,String.valueOf(customer.getLongitude()));
                                    editor.putString(myLatitude,String.valueOf(customer.getLatitude()));
                                    editor.commit();

                                    //start the profile activity
                                    finish();
                                    startActivity(new Intent(getApplicationContext(),MainActivity.class));

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    databaseError.getCode();
                                }
                            });

                        }
                    }
                });
    }
}
