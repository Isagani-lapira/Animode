package com.example.animode;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class profile_tab extends Fragment {

    private TextView tvFname, tvLname, tvUname, tvEmail, tvPass;
    private Button edit;

    public profile_tab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile_tab, container, false);

        i();
        l();

        return view;
    }

    private void i() {

    }

    private void l() {

    }
}