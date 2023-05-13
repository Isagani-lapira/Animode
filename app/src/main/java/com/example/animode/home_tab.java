package com.example.animode;

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
import android.widget.Toast;
import android.widget.VideoView;

import java.util.ArrayList;


public class home_tab extends Fragment {

    VideoView vvVideo;
    View view;

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    ArrayList<MyAnime> list;

    public home_tab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_home_tab,container
                ,false);

        initialize();

        // Inflate the layout for this fragment
        return view;
    }

    private void initialize() {
        vvVideo = view.findViewById(R.id.vvVideo);
        setVideo();



    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //set up the recyler view
        recyclerView = view.findViewById(R.id.recyler);
        recyclerView.setHasFixedSize(true);

        int numColumn = 2;
        layoutManager = new GridLayoutManager(getActivity(),numColumn, GridLayoutManager.VERTICAL,
                false);
        recyclerView.setLayoutManager(layoutManager);
        list = Application.myAnime_list;
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