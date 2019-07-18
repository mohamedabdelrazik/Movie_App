package com.farag.hamzamohamed.movieapp.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.farag.hamzamohamed.movieapp.Listeners.OnItemClickListener;
import com.farag.hamzamohamed.movieapp.R;
import com.farag.hamzamohamed.movieapp.activites.MoviesDetails;
import com.farag.hamzamohamed.movieapp.model.Genre;
import com.farag.hamzamohamed.movieapp.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mohamed on 04/12/2018.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MovieViewHolder> {

    String gener ;

    private List<Movie> movieslist;
//    private int rowLayout;
    private Context context;
    private List<Genre> genresList;
    private OnItemClickListener onItemClickListener;

    public MoviesAdapter( List<Movie> movieslist  , Context context){

        this.movieslist = movieslist;
//        this.rowLayout = rowLayout;
        this.context = context;

    }

    public MoviesAdapter( List<Movie> movies ,List<Genre> genresList , Context context){

        this.movieslist = movies;
        this.genresList = genresList;
        this.context = context;

    }

    public class MovieViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView movieTitle , rating , genreMovie , year;
        ImageView poster ,addToFavorite ;

        public MovieViewHolder(View itemView) {
            super(itemView);
            movieTitle = itemView.findViewById(R.id.titleOfMovie);
            rating = itemView.findViewById(R.id.rating);
            poster = itemView.findViewById(R.id.poster);
            genreMovie = itemView.findViewById(R.id.genreOfMovie);
            year = itemView.findViewById(R.id.yearOfMovie);
            addToFavorite = itemView.findViewById(R.id.addToFavorite);
            poster.setOnClickListener(this);
            addToFavorite.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {

            if (onItemClickListener != null) onItemClickListener.onItemClicked(view,getAdapterPosition());
//            if (view.getId()==R.id.poster){
//                Intent intent = new Intent(context, MoviesDetails.class);
//                intent.putExtra("id",movieslist.get(getAdapterPosition()).getId());
//                intent.putExtra("title",movieslist.get(getAdapterPosition()).getTitle());
//                intent.putExtra("date",movieslist.get(getAdapterPosition()).getReleaseDate());
//                intent.putExtra("genre",gener);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                context.startActivity(intent);
//            }else {
//                Movie movie = movieslist.get(getAdapterPosition());
//
//            }
        }
    }



    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.list_item_movie,parent,false);
        return new MovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {

        holder.movieTitle.setText(movieslist.get(position).getTitle());
        holder.rating.setText(movieslist.get(position).getVoteAverage().toString());
        holder.year.setText(movieslist.get(position).getReleaseDate().split("-")[0]);
        holder.genreMovie.setText(getGenersForMovies(movieslist.get(position).getGenreIds()));
        gener = getGenersForMovies(movieslist.get(position).getGenreIds());
        Picasso.get()
                .load(movieslist.get(position).getPosterPath())
                .placeholder(R.color.backCard)
                .resize(150,150)
                .into(holder.poster);

        holder.poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MoviesDetails.class);
                intent.putExtra("id",movieslist.get(position).getId());
                intent.putExtra("title",movieslist.get(position).getTitle());
                intent.putExtra("date",movieslist.get(position).getReleaseDate());
                intent.putExtra("genre",gener);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieslist.size();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    public void appendMovies(List<Movie> moviesToAppend){
        movieslist.addAll(moviesToAppend);
            notifyDataSetChanged();
    }

    private String getGenersForMovies (List<Integer> genreIds){
        List<String> moviesGenre = new ArrayList<>();
        for (Integer genreId : genreIds){
            for (Genre genre : genresList ){
                if (genre.getId() == genreId){
                    moviesGenre.add(genre.getName());
                    break;
                }
            }
        }
        return TextUtils.join("-",moviesGenre);
    }




}
