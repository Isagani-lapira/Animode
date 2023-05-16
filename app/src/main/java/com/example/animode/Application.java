package com.example.animode;


import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;

public class Application extends android.app.Application {

    public static ArrayList<MyAnime>myAnime_list;
    public static ArrayList<MyAnime>watchList;
    @Override
    public void onCreate() {
        super.onCreate();

        myAnime_list = new ArrayList<>();
        watchList = new ArrayList<>();

        //make a request
        //get trending anime list
        RequestQueue rq = Volley.newRequestQueue(getApplicationContext());
        rq.start();

        String END_POINT = "https://kitsu.io/api/edge/trending/anime";
        JsonObjectRequest objReq = new JsonObjectRequest(Request.Method.GET, END_POINT, null,
                response -> {

                    try {
                        JSONArray array = response.getJSONArray("data");

                        //get the top 10 best anime
                        for(int i = 0; i<10; i++){

                            JSONObject attri = array.getJSONObject(i);
                            JSONObject attributes = attri.getJSONObject("attributes");

                            //image
                            JSONObject posterImage = attributes.getJSONObject("posterImage");
                            String image = posterImage.getString("medium");

                            //for title
                            JSONObject title = attributes.getJSONObject("titles");
                            String enTitle = title.getString("en");
                            String episodes = attributes.getString("episodeCount"); //episodes

                            myAnime_list.add(new MyAnime(image,enTitle,episodes));

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }, error -> {
            Toast.makeText(this, error.getMessage(), Toast.LENGTH_SHORT).show();
        });

        rq.add(objReq);


    }

}