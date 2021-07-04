package com.aisolutions.bloodcare.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.aisolutions.bloodcare.Fragments.Details_Ambulance;
import com.aisolutions.bloodcare.Fragments.Details_Bank;
import com.aisolutions.bloodcare.Fragments.Details_Emergency;
import com.aisolutions.bloodcare.Fragments.Details_News;
import com.aisolutions.bloodcare.Fragments.Profile_DonorProfile;
import com.aisolutions.bloodcare.Fragments.Profile_MyProfile;
import com.aisolutions.bloodcare.Fragments.Profile_MyRequest;
import com.aisolutions.bloodcare.R;
import com.google.android.material.navigation.NavigationView;

public class Profile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        drawerLayout = findViewById(R.id.profile_nav_drawerLayout);
        navigationView = findViewById(R.id.nav_profile_navigation);
        toolbar = findViewById(R.id.profile_toolbar);
        backBtn = findViewById(R.id.profile_main_menu_button);

        replaceFragment(new Profile_MyProfile());

        setSupportActionBar(toolbar);

        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        navigationView.setCheckedItem(R.id.profile_nav_user);

        //back button to main menu
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.profile_frameLayout, fragment);
        transaction.commit();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.profile_nav_user:
                replaceFragment(new Profile_MyProfile());
                break;
            case R.id.profile_nav_donor:
                replaceFragment(new Profile_DonorProfile());
                break;
            case R.id.profile_nav_request:
                replaceFragment(new Profile_MyRequest());
                break;
            case R.id.profile_nav_logout:
                shutDown();
                break;
        }

        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void shutDown(){
        Intent intent = new Intent(Profile.this, Login.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
            Intent intent = new Intent(Profile.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(Profile.this, MainActivity.class);
        startActivity(intent);
        finish();
        return true;
    }
}