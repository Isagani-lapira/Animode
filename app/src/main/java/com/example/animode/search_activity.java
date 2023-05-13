package com.example.animode;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import androidx.appcompat.app.AppCompatActivity;
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

public class search_activity extends AppCompatActivity {

    private AutoCompleteTextView searchTextView;
    private ArrayAdapter<String> adapter;
    private final Executor executor = Executors.newSingleThreadExecutor();
    private final LinkedBlockingQueue<String> requestQueue = new LinkedBlockingQueue<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchTextView = findViewById(R.id.searchTextView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, new ArrayList<>());
        searchTextView.setAdapter(adapter);

        // Listen for text changes in the search bar
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
