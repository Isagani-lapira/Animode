package com.example.animode;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ViewHolder> {

    final ArrayList<MyAnime>anime_list;
    static itemClicked activity;
    private int ID;

    //item listener
    public interface itemClicked{
        void onItemClicked(int position,int ID);
    }

    public CustomAdapter(Context context, ArrayList<MyAnime>anime_list){
        this.anime_list = anime_list;
        activity = (itemClicked)context;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView ivImage;
        private final TextView tvAnime, tvEpisodes;

        //where we initialize the views
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivImage = itemView.findViewById(R.id.ivImage);
            tvAnime = itemView.findViewById(R.id.tvAnime);
            tvEpisodes = itemView.findViewById(R.id.tvEpisodes);

            //clicked specific item
            itemView.setOnClickListener(v-> {
                int pos = anime_list.indexOf((MyAnime) v.getTag());
                int ID = anime_list.get(pos).getID();

                activity.onItemClicked(pos,ID);
                anime_list.indexOf((MyAnime) v.getTag());
            });

        }
    }

    @NonNull
    @Override
    public CustomAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflate anime_list layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.anime_list,parent,
                false);

        //pass the anime_list layout to custom viewholder to get the views inside it
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.ViewHolder holder, int position) {

        holder.itemView.setTag(anime_list.get(position)); //to check which one of the item

        //set the image to the holder image
        String imgUrl = anime_list.get(position).getIMG_URL();
        Picasso.get()
                .load(imgUrl)
                .into(holder.ivImage);

        ID = anime_list.get(position).getID();
        holder.tvAnime.setText(anime_list.get(position).getANIME_NAME());
        holder.tvEpisodes.setText("episodes: "+anime_list.get(position).getEPISODES());


    }

    @Override
    public int getItemCount() {
        return anime_list.size();
    }
}
