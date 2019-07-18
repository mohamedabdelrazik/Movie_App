package com.farag.hamzamohamed.movieapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.farag.hamzamohamed.movieapp.R;
import com.farag.hamzamohamed.movieapp.activites.MoviesDetails;
import com.farag.hamzamohamed.movieapp.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

public class InfiniteScroll extends RecyclerView.Adapter<InfiniteScroll.MovieViewHolder> {

    private List<Movie> movies;
    private int rowLayout;
    private Context context;

    public class MovieViewHolder extends RecyclerView.ViewHolder {


        TextView movieTitle;
        TextView rating;
        ImageView poster;

        public MovieViewHolder(View itemView) {
            super(itemView);
            movieTitle = itemView.findViewById(R.id.titleOfMovie);
            rating = itemView.findViewById(R.id.rating);
            poster = itemView.findViewById(R.id.poster);
        }
    }

    public InfiniteScroll( List<Movie> movies , int rowLayout , Context context){

        this.movies = movies;
        this.rowLayout = rowLayout;
        this.context = context;

    }

    @Override
    public InfiniteScroll.MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_item_movie,parent,false);
        return new InfiniteScroll.MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InfiniteScroll.MovieViewHolder holder, final int position) {

        holder.movieTitle.setText(movies.get(position).getTitle());
        holder.rating.setText(movies.get(position).getVoteAverage().toString());
        Picasso.get()
                .load(movies.get(position).getPosterPath())
                .placeholder(R.color.backCard)
                .resize(150,150)
                .into(holder.poster);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MoviesDetails.class);
                intent.putExtra("id",movies.get(position).getId());
                intent.putExtra("title",movies.get(position).getTitle());
                intent.putExtra("date",movies.get(position).getReleaseDate());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }




}
