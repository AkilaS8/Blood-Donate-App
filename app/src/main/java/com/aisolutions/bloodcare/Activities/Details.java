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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.aisolutions.bloodcare.Fragments.Details_Ambulance;
import com.aisolutions.bloodcare.Fragments.Details_Bank;
import com.aisolutions.bloodcare.Fragments.Details_Emergency;
import com.aisolutions.bloodcare.Fragments.Details_News;
import com.aisolutions.bloodcare.R;
import com.google.android.material.navigation.NavigationView;

public class Details extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    Toolbar toolbar;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        drawerLayout = findViewById(R.id.details_nav_drawerLayout);
        navigationView = findViewById(R.id.nav_details_navigation);
        toolbar = findViewById(R.id.details_toolbar);
        backBtn = findViewById(R.id.details_main_menu_button);

        replaceFragment(new Details_News());

        //set the toolbar
        setSupportActionBar(toolbar);

        //set the navigation
        navigationView.bringToFront();
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //set menu item click
        navigationView.setNavigationItemSelectedListener(this);

        //default selected item
        navigationView.setCheckedItem(R.id.details_nav_news);

        //back button to main menu
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Details.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.details_nav_news:
                replaceFragment(new Details_News());
                break;
            case R.id.details_nav_bank:
                replaceFragment(new Details_Bank());
                break;
            case R.id.details_nav_ambulance:
                replaceFragment(new Details_Ambulance());
                break;
            case R.id.details_nav_emergency:
                replaceFragment(new Details_Emergency());
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.details_frameLayout,fragment);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
            Intent intent = new Intent(Details.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        Intent intent = new Intent(Details.this, MainActivity.class);
        startActivity(intent);
        finish();
        return true;
    }


}