package com.example.animode;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class toWatch_tab extends Fragment {
    View view;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    ArrayList<MyAnime> list;
    private int flag;
    private LinearLayout llNoWatchList;

    public toWatch_tab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_to_watch_tab, container, false);

        recyclerView = view.findViewById(R.id.rvToWatch);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AnimeWatchAdapter(list);
        llNoWatchList = view.findViewById(R.id.llNoWatchList);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        recyclerView = view.findViewById(R.id.rvToWatch);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        getAnimeList();

    }

    private void getAnimeList() {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        flag=0;
        firestore.collection("userAnime")
                .document(userId)
                .collection("toWatch")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        if(documentSnapshot.exists()){
                            flag=1;
                            String title = documentSnapshot.getString("Title");
                            String episode = documentSnapshot.getString("Episode");
                            String img_Url = documentSnapshot.getString("Image");
                            list.add(new MyAnime(img_Url,title,episode));
                        }

                    }

                    if(flag==1){
                        llNoWatchList.setVisibility(View.GONE);
                        adapter = new AnimeWatchAdapter(list);
                        recyclerView.setAdapter(adapter);
                    }

                });
    }


}