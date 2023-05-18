package com.example.animode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;

import java.util.ArrayList;

public class AnimeListAct extends AppCompatActivity{

    RecyclerView rvAnimeList;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    ArrayList<MyAnime> animeList;
    Context context = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anime_list);


        rvAnimeList = findViewById(R.id.rvAnimeList);
        makeRequestAPI();
    }

    private void makeRequestAPI() {
        rvAnimeList.setHasFixedSize(true);
        int numColumn = 3;

        //set the recycler as grid layout with 3 column vertically
        layoutManager = new GridLayoutManager(this,numColumn, GridLayoutManager.VERTICAL,
                false);
        rvAnimeList.setLayoutManager(layoutManager);
        animeList = Application.list;
        adapter = new CustomAdapter(context,animeList);
        rvAnimeList.setAdapter(adapter);
    }

}