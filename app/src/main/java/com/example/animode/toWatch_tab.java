package com.example.animode;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class toWatch_tab extends Fragment {
    View view;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter adapter;
    ArrayList<MyAnime> list;
    private int flag;
    private LinearLayout llNoWatchList;
    FirebaseFirestore firestore;
    String userId;


    public toWatch_tab() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_to_watch_tab, container, false);

        //set up recycler view
        recyclerView = view.findViewById(R.id.rvToWatch);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AnimeWatchAdapter(list, firestore, userId);
        llNoWatchList = view.findViewById(R.id.llNoWatchList);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        firestore = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        recyclerView = view.findViewById(R.id.rvToWatch);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
        list = new ArrayList<>();
        getAnimeList(); //fetch save anime data

    }

    private void getAnimeList() {

        flag=0; //checking for existing
        firestore.collection("userAnime")
                .document(userId)
                .collection("toWatch")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot: queryDocumentSnapshots){
                        if(documentSnapshot.exists()){
                            flag=1;

                            //fetch data
                            String title = documentSnapshot.getString("Title");
                            String episode = documentSnapshot.getString("Episode");
                            String img_Url = documentSnapshot.getString("Image");
                            list.add(new MyAnime(img_Url,title,episode));
                        }

                    }

                    //add to the recycler if there's a record
                    if(flag==1){
                        llNoWatchList.setVisibility(View.GONE);
                        //add anime list in the recyclerview
                        adapter = new AnimeWatchAdapter(list, firestore, userId);
                        recyclerView.setAdapter(adapter);
                    }

                });
    }


}