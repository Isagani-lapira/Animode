package com.example.animode;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class toWatch_tab extends Fragment {
    View view;

    public toWatch_tab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

         view = inflater.inflate(R.layout.fragment_to_watch_tab, container, false);

         //initialize


        // Inflate the layout for this fragment
        return view;
    }
}