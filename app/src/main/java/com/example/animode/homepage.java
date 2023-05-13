package com.example.animode;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class homepage extends AppCompatActivity implements CustomAdapter.itemClicked{

    FirebaseAuth auth;
    BottomNavigationView bnNavigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        auth = FirebaseAuth.getInstance();
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

    @Override
    public void onItemClicked(int index) {
        //show the details for specific anime
        Intent intent = new Intent(this,individual_anime.class);
        intent.putExtra("position",index);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //if not have been login it will redirect to login
        FirebaseUser currentUser = auth.getCurrentUser();
        if(currentUser==null)
            startActivity(new Intent(this,loginpage.class));
    }
}