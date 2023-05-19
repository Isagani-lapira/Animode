package com.example.animode;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import pl.droidsonroids.gif.GifImageView;

public class search_activity extends AppCompatActivity {

    private AutoCompleteTextView searchTextView;
    private ImageView ivSearch, ivAnimeImage;
    private TextView tvAnimeName,tvSynopsis, noResult;
    private ScrollView svContainer;
    private ArrayAdapter<String> adapter;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final LinkedBlockingQueue<String> requestQueue = new LinkedBlockingQueue<>();
    private Button btToWatch, btClear;
    StoreUserData userData;
    String anime_name = "";
    String episodes = "";
    String imageLink = "";
    GifImageView loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        // Listen for text changes in the search bar

        initialize();
        listener();
    }

    private void initialize() {
        searchTextView = findViewById(R.id.searchTextView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<>());
        searchTextView.setAdapter(adapter);
        ivSearch = findViewById(R.id.ivSearch);
        tvAnimeName = findViewById(R.id.tvAnimeName);
        tvSynopsis = findViewById(R.id.tvSynopsis);
        ivAnimeImage = findViewById(R.id.ivAnimeImage);
        noResult = findViewById(R.id.noResult);
        svContainer = findViewById(R.id.svContainer);
        btToWatch = findViewById(R.id.btToWatch);
        btClear = findViewById(R.id.btClear);
        loading = findViewById(R.id.loading);
    }

    @SuppressLint("SetTextI18n")
    private void listener() {
        searchTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Do nothing
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Add the search text to the request queue
                requestQueue.offer(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // Start a background thread to handle API requests
        executor.execute(() -> {
            while (true) {
                try {
                    String searchText = requestQueue.take();
                    if (searchText.isEmpty()) {
                        continue;
                    }

                    String url = "https://kitsu.io/api/edge/anime?filter[text]=" + searchText;
                    String response = makeApiRequest(url);

                    // Parse the response and update the adapter on the UI thread
                    runOnUiThread(() -> {
                        try {
                            JSONArray data = new JSONObject(response).getJSONArray("data");
                            ArrayList<String> titles = new ArrayList<>();
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject anime = data.getJSONObject(i);
                                titles.add(anime.getJSONObject("attributes").getString("canonicalTitle"));
                            }
                            adapter.clear();
                            adapter.addAll(titles);
                            adapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        ivSearch.setOnClickListener(v->{
            String animeSearch = searchTextView.getText().toString();
            if(animeSearch.equals(""))
                Toast.makeText(this, "Please input an anime", Toast.LENGTH_SHORT).show();

            else {
                btToWatch.setBackgroundColor(getColor(R.color.accent_color));
                btToWatch.setText("To watch");
                ivAnimeImage.setVisibility(View.GONE);
                svContainer.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE); //show the loading view
                searchResultAPI(animeSearch);
            }

        });

        //save to the to watch list
        btToWatch.setOnClickListener(v->{
            FirebaseFirestore fbStore = FirebaseFirestore.getInstance();
            String userID = FirebaseAuth.getInstance().getUid();

            anime_name = tvAnimeName.getText().toString();
            userData = new StoreUserData(this,userID,anime_name,episodes,fbStore,imageLink); //details to be add on firestore
            userData.insertToWatch();

            btToWatch.setBackgroundColor(getColor(R.color.listed_color));
            btToWatch.setText("Has been added to my list");
        });

        btClear.setOnClickListener(v->{
            svContainer.setVisibility(View.GONE);
            ivAnimeImage.setVisibility(View.GONE);
        });
    }

    private void searchResultAPI(String anime_name) {
        RequestQueue rq = Volley.newRequestQueue(this);
        rq.start();

        String URL = "https://kitsu.io/api/edge/anime?filter[text]="+anime_name;
        JsonObjectRequest obj = new JsonObjectRequest(com.android.volley.Request.Method.GET,
                URL, null,
                response -> {

                //get the response for that search
                    try {
                        JSONArray data = response.getJSONArray("data");

                        loading.setVisibility(View.GONE);
                        //check if existing
                        if(data.length()<=0){
                            noResult.setVisibility(View.VISIBLE);
                            svContainer.setVisibility(View.GONE);
                            noResult.setText(R.string.the_anime_is_not_available_here);
                        }
                        else{
                            loading.setVisibility(View.GONE);
                            noResult.setVisibility(View.GONE);
                            svContainer.setVisibility(View.VISIBLE); //show the container
                            ivAnimeImage.setVisibility(View.VISIBLE);//show image
                            JSONObject attributes = data.getJSONObject(0).getJSONObject("attributes");

                            //get image link
                            JSONObject posterImage = attributes.getJSONObject("posterImage");
                            imageLink = posterImage.getString("medium");

                            //get synopsis
                            String synopsis = attributes.getString("synopsis");

                            //episode
                            episodes = attributes.getString("episodeCount");
                            //set values
                            tvAnimeName.setText(anime_name);
                            tvSynopsis.setText(synopsis);

                            //set up the image
                            Picasso.get()
                                    .load(imageLink)
                                    .fit()
                                    .centerCrop()
                                    .into(ivAnimeImage);

                        }
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }

                }, error -> {

        });

        rq.add(obj);
    }

    private String makeApiRequest(String url) {
        try {
            return new OkHttpClient().newCall(new Request.Builder().url(url).build()).execute().body().string();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }
}
