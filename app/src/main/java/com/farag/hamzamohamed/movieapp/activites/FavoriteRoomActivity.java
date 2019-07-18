package com.farag.hamzamohamed.movieapp.activites;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;

import com.farag.hamzamohamed.movieapp.Adapter.DataBaseMovieAdapter;
import com.farag.hamzamohamed.movieapp.Adapter.MoviesAdapter;
import com.farag.hamzamohamed.movieapp.Listeners.RecyclerItemTouchHelperListener;
import com.farag.hamzamohamed.movieapp.R;
import com.farag.hamzamohamed.movieapp.RecyclerItemTouchHelper;
import com.farag.hamzamohamed.movieapp.RoomDataBase.DataBaseClient;
import com.farag.hamzamohamed.movieapp.RoomDataBase.Movies;

import java.util.List;

/**
 * Created by Mohamed on 17/6/2019.
 */

public class FavoriteRoomActivity extends AppCompatActivity implements RecyclerItemTouchHelperListener {

    RecyclerView rvRoom;
    DataBaseMovieAdapter mAdapter;

    ProgressDialog progressDialog;
    SharedPreferences preferences;
    ImageView  back;

    String lang;
    List<Movies> moviesList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_room);

        initView();
        getMyFavoriteFilmDataBase();

        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerItemTouchHelper(0,ItemTouchHelper.LEFT,this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(rvRoom);
    }


    void initView(){
        preferences = getSharedPreferences("myShared", Context.MODE_PRIVATE);
        lang=preferences.getString("lang","ar");
        back =  findViewById(R.id.back);
        if (lang.equals("ar")){
            //  back.setImageDrawable(getResources().getDrawable(R.drawable.arrow_2));
            back.setImageResource(R.drawable.arrow_2);
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        rvRoom = findViewById(R.id.rv_room);
        rvRoom.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
    }

    private void getMyFavoriteFilmDataBase(){

        class GetFavorite extends AsyncTask<Void,Void, List<Movies>>{
            @Override
            protected List<Movies> doInBackground(Void... voids) {

                moviesList = DataBaseClient.getInstance(getApplicationContext())
                        .getMoviesDataBase()
                        .daoMovies().getAll();

                return moviesList;
            }

            @Override
            protected void onPostExecute(List<Movies> movies) {
                super.onPostExecute(movies);

                mAdapter = new DataBaseMovieAdapter(getApplicationContext(),moviesList);
                rvRoom.setAdapter(mAdapter);
            }
        }
        GetFavorite getFavorite = new GetFavorite();
        getFavorite.execute();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {

//        String name = moviesList.get(viewHolder.getAdapterPosition()).getTitle();

        mAdapter.removeItemFromDataBase(viewHolder.getAdapterPosition());


    }
}
