package com.example.animode;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class AnimeWatchAdapter extends RecyclerView.Adapter <AnimeWatchAdapter.ViewHolder>{

    private final ArrayList<MyAnime>anime;
    public AnimeWatchAdapter(ArrayList<MyAnime> anime){
        this.anime = anime;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

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
        Picasso.get()
                .load(imgUrl)
                .into(holder.ivAnimeImage);

        holder.tvAnimeName.setText(anime.get(position).getANIME_NAME());
        holder.tvEpisodes.setText("episodes: "+anime.get(position).getEPISODES());
    }

    @Override
    public int getItemCount() {
        return anime.size();
    }
}
