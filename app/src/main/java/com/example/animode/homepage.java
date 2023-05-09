package com.example.animode;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;

public class homepage extends AppCompatActivity {

    VideoView video_view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);


        initialize();
    }

    private void initialize() {

        //set video property
        video_view = findViewById(R.id.video_view);

        String path = "android.resource://"+getPackageName()+"/"+R.raw.anime_video;
        video_view.setVideoURI(Uri.parse(path));
        video_view.start();
    }
}