package com.example.animode;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class individual_anime extends AppCompatActivity {
    Intent intent;

    ImageView ivPoster;
    TextView tvAnimeName,tvSynopsis,tvEpisode;
    Button btBack, btToWatch;
    Context context = individual_anime.this;

    //anime attributes
    String anime_name = "";
    String episodes = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_anime);


        initialize();
        listener();
        intent = getIntent();
        int position = intent.getIntExtra("position",0);

        //get the selected anime name and attribute
        anime_name = Application.myAnime_list.get(position).getANIME_NAME();
        episodes =  Application.myAnime_list.get(position).getEPISODES();
        setVal(anime_name, episodes);

    }

    private void listener() {
        //return to the homepage
        btBack.setOnClickListener(v->{
            intent = new Intent(context,homepage.class);
            startActivity(intent);
        });
    }

    private void initialize() {

        ivPoster = findViewById(R.id.ivPoster);
        tvAnimeName = findViewById(R.id.tvAnimeName);
        tvSynopsis = findViewById(R.id.tvSynopsis);
        tvEpisode = findViewById(R.id.tvEpisodes);
        btBack = findViewById(R.id.btBack);
        btToWatch = findViewById(R.id.btToWatch);


    }

    private void setVal(String anime_name, String episodes){
        tvAnimeName.setText(anime_name);
        String anime_ep =  tvEpisode.getText()+episodes;
        tvEpisode.setText(anime_ep);


        //get the search  anime information
        RequestQueue rq = Volley.newRequestQueue(this);
        rq.start();

        String URL = "https://kitsu.io/api/edge/anime?filter[text]="+anime_name;
        JsonObjectRequest obj = new JsonObjectRequest(Request.Method.GET, URL, null,
                response -> {

                    try {
                        JSONArray data = response.getJSONArray("data");

                        if(data.length()>0){
                            JSONObject jsonObject = data.getJSONObject(0);
                            String synopysis = jsonObject.getJSONObject("attributes")
                                    .getString("synopsis");
                            String imagePath = jsonObject.getJSONObject("attributes")
                                    .getJSONObject("posterImage").getString("medium");

                            //set up the details
                            //to show image
                            Picasso.get()
                                    .load(imagePath)
                                    .fit()
                                    .centerCrop()
                                    .into(ivPoster);

                            //for synopsis
                            String synopsisVal = tvSynopsis.getText()+synopysis;
                            tvSynopsis.setText(synopsisVal);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> {
            Toast.makeText(context, "Something is wrong. Try again", Toast.LENGTH_SHORT).show();
        });

        rq.add(obj);
    }

}