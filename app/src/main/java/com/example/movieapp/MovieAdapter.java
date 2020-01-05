package com.example.movieapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;


import java.util.List;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MyViewHolder> {

    List<Movie> movies;
    Context ctx;
    String baseImageUrl="https://image.tmdb.org/t/p/w500";


    public MovieAdapter(List<Movie> movies) {
        this.movies = movies;
    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ctx = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.one_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Movie movie = movies.get(position);
         String k = movie.getPoster_path();
        Log.d("HELLO", "Path: " + k);
        String url = baseImageUrl + k;
        Glide.with(ctx).load(url).placeholder(R.drawable.baseline_star_black_18dp).into(holder.image);
        holder.title.setText(movie.getTitle());


        holder.relative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (ctx,DetailsActivity.class);
                intent.putExtra("title",movie.getTitle());
                intent.putExtra("picture",movie.getPoster_path());
                intent.putExtra("popularity",movie.getPopularity());
                intent.putExtra("originallanguage",movie.getOriginal_language());
                intent.putExtra("average",movie.getVote_average());
                intent.putExtra("vote",movie.getVote_count());
                intent.putExtra("overview",movie.getOverview());
                intent.putExtra("posterpath",movie.getPoster_path());




                ctx.startActivity(intent);
            }
        });
    }



    @Override
    public int getItemCount() {
        return movies.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private ImageView image;
        private TextView title;
        RelativeLayout relative;
        private Switch aSwitch;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            image = itemView.findViewById(R.id.image);
            title = itemView.findViewById(R.id.title);
            relative = itemView.findViewById(R.id.relative);
            aSwitch = itemView.findViewById(R.id.switch1);

        }
    }
}
