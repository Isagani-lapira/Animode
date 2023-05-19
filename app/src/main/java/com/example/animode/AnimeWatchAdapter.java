package com.example.animode;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AnimeWatchAdapter extends RecyclerView.Adapter <AnimeWatchAdapter.ViewHolder>{

    private final ArrayList<MyAnime>anime;
    private final FirebaseFirestore FBSTORE;
    private final String USER_ID;
    String synopsis;
    private final Context CONTEXT;
    public AnimeWatchAdapter(Context context, ArrayList<MyAnime> anime, FirebaseFirestore fbstore,
                             String user_id){
        this.anime = anime;
        FBSTORE = fbstore;
        USER_ID = user_id;
        CONTEXT = context;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivAnimeImage;
        TextView tvAnimeName,tvEpisodes;
        Button btDone;


        //connect views of the list_item layout
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivAnimeImage = itemView.findViewById(R.id.ivAnimeImage);
            tvAnimeName = itemView.findViewById(R.id.tvAnimeName);
            tvEpisodes = itemView.findViewById(R.id.tvEpisodes);
            btDone = itemView.findViewById(R.id.btDone);

        }
    }

    public void removeItem(int position) {

        if(position!=-1){
            anime.remove(position);
            notifyItemRemoved(position);
        }
        else
            Toast.makeText(CONTEXT, "Try again", Toast.LENGTH_SHORT).show();

    }

    @NonNull
    @Override
    public AnimeWatchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //set up the layout that will be using as recyclerview desing
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        return new ViewHolder(view);
    }



    //add values for each item in the list
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AnimeWatchAdapter.ViewHolder holder, int position) {

        //set the image to the holder image
        String imgUrl = anime.get(position).getIMG_URL();

        //set up the image
        Picasso.get()
                .load(imgUrl)
                .fit()
                .centerCrop()
                .into(holder.ivAnimeImage);

        holder.tvAnimeName.setText(anime.get(position).getANIME_NAME());
        holder.tvEpisodes.setText("Episodes: "+anime.get(position).getEPISODES());


        //find the one need to be remove using query (whereEqualTo)
        holder.btDone.setOnClickListener(v->
                FBSTORE.collection("userAnime")
                .document(USER_ID)
                .collection("toWatch")
                .whereEqualTo("Title",anime.get(position).getANIME_NAME())
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        for (QueryDocumentSnapshot document: task.getResult()){
                            String documentID = document.getId();

                            //remove the item
                            FBSTORE.collection("userAnime")
                                    .document(USER_ID)
                                    .collection("toWatch")
                                    .document(documentID)
                                    .delete()
                                    .addOnSuccessListener(unused ->{
                                        removeItem(holder.getAdapterPosition());
                                        notifyDataSetChanged();
                                    })
                                    .addOnFailureListener(e -> Toast.makeText(CONTEXT, e.getMessage(), Toast.LENGTH_SHORT).show());
                        }
                    }
                    else{
                        Toast.makeText(v.getContext(), "Try again", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> Log.d("Sheesh", e.getMessage())));

        holder.itemView.setOnClickListener(v->{
            String title = holder.tvAnimeName.getText().toString();
            String url = anime.get(position).getIMG_URL();
            getSynopsis(title,url);
        });

    }

    @Override
    public int getItemCount() {
        return anime.size();
    }

    private void getSynopsis(String title,String imgURL) {
        RequestQueue rq = Volley.newRequestQueue(CONTEXT);
        rq.start();

        String url = "https://kitsu.io/api/edge/anime?filter[text]="+title;
        //get json object
        JsonObjectRequest jsonObj = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        JSONArray array = response.getJSONArray("data");

                        JSONObject obj = array.getJSONObject(0);
                        synopsis = obj.getJSONObject("attributes").getString("synopsis");

                        //show alert dialog
                        showDialog(title,synopsis,imgURL);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }, error -> {

        });

        rq.add(jsonObj);
    }

    private void showDialog(String title, String synopsis,String imgURL) {

        //layout that will be using
        LayoutInflater inflate = LayoutInflater.from(CONTEXT);
        View view = inflate.inflate(R.layout.alert_dial_watch_item,null);

        AlertDialog.Builder builder = new AlertDialog.Builder(CONTEXT,R.style.TransparentAlertDialogTheme);
        builder.setView(view);

        //set up the view's data in the custom layout
        TextView tvAnimeName = view.findViewById(R.id.tvAnimeName);
        TextView tvSynopsis = view.findViewById(R.id.tvSynopsis);
        ImageView ivAnimeImage = view.findViewById(R.id.ivAnimeImage);

        //set the text based on the one has been clicked
        tvAnimeName.setText(title);
        tvSynopsis.setText(synopsis);

        //add the image
        Picasso.get()
                .load(imgURL)
                .fit()
                .into(ivAnimeImage);

        //display dialog
        Dialog dialog = builder.create();
        dialog.show();
    }
}
