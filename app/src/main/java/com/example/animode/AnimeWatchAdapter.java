package com.example.animode;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AnimeWatchAdapter extends RecyclerView.Adapter <AnimeWatchAdapter.ViewHolder>{

    private final ArrayList<MyAnime>anime;
    private final FirebaseFirestore FBSTORE;
    private final String USER_ID;
    private View fragment;
    public AnimeWatchAdapter(ArrayList<MyAnime> anime, FirebaseFirestore fbstore, String user_id){
        this.anime = anime;
        FBSTORE = fbstore;
        USER_ID = user_id;
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
        anime.remove(position);
        notifyItemRemoved(position);
    }

    @NonNull
    @Override
    public AnimeWatchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        //set up the layout that will be using as recyclerview desing
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item,parent,false);
        fragment = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_to_watch_tab,
                parent,false);
        return new ViewHolder(view);
    }



    //add values for each item in the list
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull AnimeWatchAdapter.ViewHolder holder, int position) {

        //set the image to the holder image
        String imgUrl = anime.get(position).getIMG_URL();

        Picasso.get()
                .load(imgUrl)
                .fit()
                .centerCrop()
                .into(holder.ivAnimeImage);

        holder.tvAnimeName.setText(anime.get(position).getANIME_NAME());
        holder.tvEpisodes.setText("Episodes: "+anime.get(position).getEPISODES());

        //remove the item has been set to done
        holder.btDone.setOnClickListener(v->{

            FBSTORE.collection("userAnime")
                    .document(USER_ID)
                    .collection("toWatch")
                    .whereEqualTo("Title",anime.get(position).getANIME_NAME())
                    .get()
                    .addOnCompleteListener(task -> {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document: task.getResult()){
                                String documentID = document.getId();

                                FBSTORE.collection("userAnime")
                                        .document(USER_ID)
                                        .collection("toWatch")
                                        .document(documentID)
                                        .delete()
                                        .addOnSuccessListener(unused ->removeItem(holder.getAdapterPosition()))
                                        .addOnFailureListener(e -> Log.d("Sheesh", e.getMessage()));
                            }
                        }
                    })
                    .addOnFailureListener(e -> Log.d("Sheesh", e.getMessage()));
        });
    }

    @Override
    public int getItemCount() {
        return anime.size();
    }
}
