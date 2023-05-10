package com.example.animode;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.VideoView;

public class home_tab extends Fragment {

    VideoView vvVideo;
    View view;
    public home_tab() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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

    //display the video coming from resource
    private void setVideo() {
        String url = "android.resource://"+getContext().getPackageName()+"/"+R.raw.anime_video;
        vvVideo.setVideoURI(Uri.parse(url));
        vvVideo.start();
    }
}