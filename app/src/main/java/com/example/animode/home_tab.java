package com.example.animode;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import java.util.ArrayList;


public class home_tab extends Fragment {

    VideoView vvVideo;
    View view;

    RecyclerView recyclerView, rvRandom;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    ArrayList<MyAnime> list;
    ImageView ivSearch;

    public home_tab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //set the status bar as this fragment created
        getActivity().getWindow().setStatusBarColor(getResources().getColor(R.color.primary_color));
        view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_home_tab,container
                ,false);

        initialize();

        // Inflate the layout for this fragment
        return view;
    }

    private void initialize() {
        vvVideo = view.findViewById(R.id.vvVideo);
        ivSearch = view.findViewById(R.id.ivSearch);
        setVideo();

        //go to search activity
        ivSearch.setOnClickListener(v->{
            startActivity(new Intent(getContext(),search_activity.class));
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        //set up the recyler view for trending anime
        recyclerView = view.findViewById(R.id.recyler);
        apiRequest(recyclerView,0);


        //set up the recycler view for random anime
        rvRandom = view.findViewById(R.id.rvRandom);
        apiRequest(rvRandom,1);

    }

    private void apiRequest(RecyclerView recyclerView,int ID) {
        recyclerView.setHasFixedSize(true);
        int numColumn = 3;
        layoutManager = new GridLayoutManager(getActivity(),numColumn, GridLayoutManager.VERTICAL,
                false);
        recyclerView.setLayoutManager(layoutManager);
        if(ID==0)
            list = Application.myAnime_list;
        else
            list = Application.random;

        adapter = new CustomAdapter(this.getActivity(),list);
        recyclerView.setAdapter(adapter);
    }


    //display the video coming from resource
    private void setVideo() {
        String url = "android.resource://"+getContext().getPackageName()+"/"+R.raw.anime_video;
        vvVideo.setVideoURI(Uri.parse(url));
        vvVideo.start();
    }
}