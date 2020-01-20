package com.example.secondapplication.UI;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.secondapplication.Model.ParcelDataSource;
import com.example.secondapplication.R;
import com.example.secondapplication.UI.Service.ParcelService;
import com.example.secondapplication.UI.UserMangemant.LoginActivity;
import com.example.secondapplication.UI.UserMangemant.UserManagementViewModel;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    UserManagementViewModel userManagementViewModel;
    private FirebaseAuth firebaseAuth;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= 26) {
            getBaseContext().startService(new Intent(getBaseContext(), ParcelService.class));
        }else{
            getBaseContext().startService(new Intent(getBaseContext(), ParcelService.class));
        }
        userManagementViewModel =ViewModelProviders.of(this).get(UserManagementViewModel.class);
        firebaseAuth=FirebaseAuth.getInstance();

        //if the user log out
        if(firebaseAuth.getCurrentUser()==null){
            finish();
            startActivity(new Intent(this, LoginActivity.class));
        }


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        View header=navigationView.getHeaderView(0);

        //Update the header main
        FirebaseUser user=firebaseAuth.getCurrentUser();
        TextView textView= (TextView) header.findViewById(R.id.username_nav_header_main);
        textView.setText(user.getEmail());

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_logout:

                //log out
                userManagementViewModel.logOut();
                finish();
                startActivity(new Intent(this,LoginActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //Stop notify
        ParcelDataSource.stopNotifyUserParcelList();
        ParcelDataSource.stopNotifyOffersParcelList();
    }
}
