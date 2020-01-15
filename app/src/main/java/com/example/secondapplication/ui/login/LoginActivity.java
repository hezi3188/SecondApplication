package com.example.secondapplication.ui.login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.secondapplication.R;
import com.example.secondapplication.ui.login.ui.UserManagementViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignup;

    UserManagementViewModel userManagementViewModel;
    public DatabaseReference reference;
    private FirebaseAuth firebaseAuth;

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userManagementViewModel =ViewModelProviders.of(this).get(UserManagementViewModel.class);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        reference=database.getReference("customers");
        firebaseAuth=FirebaseAuth.getInstance();

        //if the user login go to profile activity
        if(firebaseAuth.getCurrentUser()!=null){

            //profile activity
            finish();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }

        //start views
        editTextEmail=(EditText)findViewById(R.id.emailEditText);
        editTextPassword=(EditText)findViewById(R.id.passwordEditText);
        buttonSignIn=(Button)findViewById(R.id.loginButton);
        textViewSignup=(TextView)findViewById(R.id.signUpTextView);
        progressDialog=new ProgressDialog(this);

        //on click
        buttonSignIn.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if(v==buttonSignIn){
            userLogin();
        }
        if(v==textViewSignup){
            //go to sign up
            finish();
            startActivity(new Intent(this,RegisterActivity.class));
        }
    }

    private void userLogin() {
        final String email=editTextEmail.getText().toString().trim();
        String password=editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            //email is empty
            Toast.makeText(this, "Please enter email!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter password!",Toast.LENGTH_SHORT).show();
            return;
        }
        //if validation are ok
        progressDialog.setMessage("Sign in ...");
        progressDialog.show();

        userManagementViewModel.login(email, password, new UserManagementViewModel.Action<String>() {
            @Override
            public void OnSuccess(String obj) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, obj, Toast.LENGTH_SHORT).show();
                finish();
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }

            @Override
            public void OnFailure(Exception exception) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
