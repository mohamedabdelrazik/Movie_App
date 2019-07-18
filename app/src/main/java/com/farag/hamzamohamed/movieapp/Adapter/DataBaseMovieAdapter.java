package com.farag.hamzamohamed.movieapp.Adapter;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.farag.hamzamohamed.movieapp.R;
import com.farag.hamzamohamed.movieapp.RoomDataBase.DataBaseClient;
import com.farag.hamzamohamed.movieapp.RoomDataBase.Movies;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Mohamed on 17/6/2018.
 */

public class DataBaseMovieAdapter extends RecyclerView.Adapter<DataBaseMovieAdapter.MoviesDBaseViewHolder> {

    private Context context;
    private List<Movies> moviesList;

    public DataBaseMovieAdapter(Context context , List<Movies> moviesList){
        this.context = context;
        this.moviesList = moviesList;
    }

    @Override
    public MoviesDBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MoviesDBaseViewHolder(LayoutInflater.from(context).inflate(R.layout.list_item_data_base_movies,parent,false));
    }

    @Override
    public void onBindViewHolder(MoviesDBaseViewHolder holder, int position) {

        holder.movieTitle.setText(moviesList.get(position).getTitle());
        holder.year.setText(moviesList.get(position).getReleaseDate());
        holder.rating.setText(moviesList.get(position).getVoteAverage());
        Picasso.get().load(moviesList.get(position).getPoster()).into(holder.poster);

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

    public class MoviesDBaseViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout viewBackground, viewForeground;
        TextView movieTitle , rating , genreMovie , year;
        ImageView poster ;
        public MoviesDBaseViewHolder(View itemView) {
            super(itemView);
            movieTitle = itemView.findViewById(R.id.titleOfMovie);
            rating = itemView.findViewById(R.id.rating);
            poster = itemView.findViewById(R.id.poster);
            genreMovie = itemView.findViewById(R.id.genreOfMovie);
            year = itemView.findViewById(R.id.yearOfMovie);
            viewBackground = itemView.findViewById(R.id.view_background);
            viewForeground = itemView.findViewById(R.id.view_foreground);
        }
    }

    public void removeItemFromDataBase (int position){
        Movies movies = moviesList.get(position);
        deleteFromDataBase(movies);
        moviesList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations

        notifyItemRemoved(position);


    }

    private void deleteFromDataBase(final Movies movies){

        class DeleteMovies extends AsyncTask<Void,Void,Void>{
            @Override
            protected Void doInBackground(Void... voids) {

               DataBaseClient.getInstance(context).getMoviesDataBase().daoMovies().delete(movies);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Toast.makeText(context, context.getString(R.string.delete), Toast.LENGTH_SHORT).show();
            }
        }

        DeleteMovies deleteMovies = new DeleteMovies();
        deleteMovies.execute();
    }
}
