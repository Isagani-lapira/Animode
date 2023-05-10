package com.example.animode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.VideoView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class homepage extends AppCompatActivity {

    BottomNavigationView bnNavigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);


        initialize();
        listener();
    }

    private void listener() {
        bnNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                int id = item.getItemId();
                //check which tab has been clicked
                if (id == R.id.home_tab)
                    setFragment(new home_tab());
                else if (id == R.id.profile_tab)
                    setFragment(new profile_tab());
                else if (id == R.id.towatch_tab)
                    setFragment(new toWatch_tab());

                return true;
            }
        });
    }

    private void initialize() {
        setFragment(new home_tab()); //set home_tab as default fragment

        bnNavigation = findViewById(R.id.bnNavigation);
    }

    private void setFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFrame,fragment)
                .commit();
    }
}