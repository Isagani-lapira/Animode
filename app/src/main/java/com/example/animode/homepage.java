package com.example.animode;


import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class homepage extends AppCompatActivity{

    BottomNavigationView bnNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        //change status bar color
        getWindow().setStatusBarColor(getColor(R.color.primary_color));
        initialize();
        listener();
    }

    private void listener() {
        bnNavigation.setOnItemSelectedListener(item -> {

            int id = item.getItemId();
            //check which tab has been clicked
            if (id == R.id.home_tab)
                setFragment(new home_tab());
            else if (id == R.id.profile_tab)
                setFragment(new profile_tab());
            else if (id == R.id.towatch_tab)
                setFragment(new toWatch_tab());

            return true;
        });

    }

    private void initialize() {
        setFragment(new home_tab()); //set home_tab as default fragment
        bnNavigation = findViewById(R.id.bnNavigation);
    }

    //change fragment display
    private void setFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFrame,fragment)
                .commit();
    }


}