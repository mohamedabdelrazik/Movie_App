package com.farag.hamzamohamed.movieapp.activites;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.farag.hamzamohamed.movieapp.Adapter.MoviesAdapter;
import com.farag.hamzamohamed.movieapp.Listeners.OnItemClickListener;
import com.farag.hamzamohamed.movieapp.R;
import com.farag.hamzamohamed.movieapp.RoomDataBase.DataBaseClient;
import com.farag.hamzamohamed.movieapp.RoomDataBase.Movies;
import com.farag.hamzamohamed.movieapp.model.Genre;
import com.farag.hamzamohamed.movieapp.model.GenreModel;
import com.farag.hamzamohamed.movieapp.model.Movie;
import com.farag.hamzamohamed.movieapp.model.MovieResponse;
import com.farag.hamzamohamed.movieapp.Network.ApiClient;
import com.farag.hamzamohamed.movieapp.Network.ApiInterface;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {

    public final static String API_KEY = "069522d9d43e38f343e8fc83d04e4a8a";
    private RecyclerView mRecyclerView ;
    private MoviesAdapter mAdapter;
    LinearLayoutManager manager;
    SharedPreferences preferences ;
    Button topRateMovie , popularMovie , upcomingMovies , playingMovies;
    ImageView openDrawer;
    DrawerLayout drawerLayout;
    TextView mainTitle;
    AVLoadingIndicatorView avi  , loading;
    String lang;
    Dialog dialog;
    List<Genre> genres;
    List<Movie> movies;
    List<String> getAll;
    int currentPage = 1 , currentSection ;
    private boolean isFetchingMovies;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        defaultDisplay();
        exeAction();
    }

    private void initView(){
        preferences = getSharedPreferences("myShared",Context.MODE_PRIVATE);
        lang = preferences.getString("lang","ar");
        Log.e("<<<<lang",lang);
        topRateMovie =  findViewById(R.id.top);
        popularMovie = findViewById(R.id.pop);
        openDrawer =  findViewById(R.id.openBar);
        openDrawer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        mRecyclerView = findViewById(R.id.rv_movies);
        drawerLayout = findViewById(R.id.drawer);
        upcomingMovies = findViewById(R.id.upComing);
        playingMovies = findViewById(R.id.playingMovies);
        mainTitle = findViewById(R.id.mainTitle);
        avi = findViewById(R.id.avi);
        loading = findViewById(R.id.loading);
        genres = new ArrayList<>();
        movies = new ArrayList<>();
    }

    private void exeAction(){
        buttonClick(popularMovie,1);
        buttonClick(topRateMovie,2);
        buttonClick(upcomingMovies,3);
        buttonClick(playingMovies,4);
    }


    private void defaultDisplay(){
        regular(mainTitle, String.valueOf(getText(R.string.pop)));
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<GenreModel> call = apiService.getGenre(API_KEY,lang);
        call.enqueue(new Callback<GenreModel>() {
            @Override
            public void onResponse(Call<GenreModel> call, Response<GenreModel> response) {
                genres = response.body().getGenres();
                getPopulatMovies(genres);
                scrollRecycler();
            }
            @Override
            public void onFailure(Call<GenreModel> call, Throwable t) {
                loading.hide();
                Toast.makeText(MainActivity.this, "Connection error", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void scrollRecycler(){
        manager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(manager);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int total = manager.getItemCount();
                int visibleItem = manager.getChildCount();
                int firstItem = manager.findFirstVisibleItemPosition();
                if (firstItem + visibleItem == total-2){
                    if (!isFetchingMovies){
                        getMovies(currentPage+1);
                    }
                }
            }
        });
    }

    private void getMovies(final int page){
        isFetchingMovies = true;
        if (currentSection == 1){
            if (currentPage<15){
                avi.setVisibility(View.VISIBLE);
                ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                Call<MovieResponse> call = apiService.getPopularMovies(API_KEY,lang,page);
                call.enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
//                        Log.e("<<<<currentPage",String.valueOf(page));
                        Toast.makeText(MainActivity.this, String.valueOf(page), Toast.LENGTH_SHORT).show();
                        movies = response.body().getResults();
//                        for (int x = 0 ; x<movies.size();x++){
//                            getAll.add(x, String.valueOf(movies.get(x).getId()));
//                        }
                        if (movies.isEmpty()) {
                            movies = response.body().getResults();
                        }else{
                            movies.addAll(response.body().getResults());
                        }
                        if (mAdapter == null){
                            mAdapter = new MoviesAdapter(movies,genres,MainActivity.this);
                            mAdapter.setOnItemClickListener(MainActivity.this);
                            mRecyclerView.setAdapter(mAdapter);
                        }else {
                            mAdapter.appendMovies(movies);
                        }
                        currentPage = page;
                        isFetchingMovies =false;
                        avi.hide();
                    }

                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {
                        loading.hide();
                        Toast.makeText(MainActivity.this, "Connection error", Toast.LENGTH_SHORT).show();
                    }
                });
            }else {
                Toast.makeText(this, "The last", Toast.LENGTH_SHORT).show();
            }
        }else if (currentSection == 2){
            if (currentPage<15){
                avi.setVisibility(View.VISIBLE);
                ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                Call<MovieResponse> call = apiService.getTopRatedMovies(API_KEY,lang,page);
                call.enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
//                        Log.e("<<<<currentPage",String.valueOf(page));
                        Toast.makeText(MainActivity.this, String.valueOf(page), Toast.LENGTH_SHORT).show();
                        if (movies.isEmpty()) {
                            movies = response.body().getResults();
                        }else {
                            movies.addAll(response.body().getResults());
                        }
                        if (mAdapter == null){
                            mAdapter = new MoviesAdapter(movies,genres,MainActivity.this);
                            mAdapter.setOnItemClickListener(MainActivity.this);
                            mRecyclerView.setAdapter(mAdapter);
                        }else {
                            mAdapter.appendMovies(movies);
                        }
                        currentPage = page;
                        isFetchingMovies =false;
                        avi.hide();
                    }
                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {
                        loading.hide();
                        Toast.makeText(MainActivity.this, "Connection error", Toast.LENGTH_SHORT).show();
                    }
                });
            }else {
                Toast.makeText(this, "The last", Toast.LENGTH_SHORT).show();
            }
        }else if (currentSection == 3){
            if (currentPage<15){
                avi.setVisibility(View.VISIBLE);
                ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                Call<MovieResponse> call = apiService.getUpcomingMovies(API_KEY,lang,page);
                call.enqueue(new Callback<MovieResponse>() {
                    @Override
                    public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
//                        Log.e("<<<<currentPage",String.valueOf(page));
                        Toast.makeText(MainActivity.this, String.valueOf(page), Toast.LENGTH_SHORT).show();
                        if (movies.isEmpty()) {
                            movies = response.body().getResults();
                        }else {
                            movies.addAll(response.body().getResults());
                        }
                        if (mAdapter == null){
                            mAdapter = new MoviesAdapter(movies,genres,MainActivity.this);
                            mAdapter.setOnItemClickListener(MainActivity.this);
                            mRecyclerView.setAdapter(mAdapter);
                        }else {
                            mAdapter.appendMovies(movies);
                        }
                        currentPage = page;
                        isFetchingMovies =false;
                        avi.hide();
                    }
                    @Override
                    public void onFailure(Call<MovieResponse> call, Throwable t) {
                        loading.hide();
                        Toast.makeText(MainActivity.this, "Connection error", Toast.LENGTH_SHORT).show();
                    }
                });
            }else {
                Toast.makeText(this, "The last", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getPopulatMovies(final List<Genre> genres){
        currentSection = 1;
        isFetchingMovies = false;
        regular(mainTitle, String.valueOf(getText(R.string.pop)));
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MovieResponse> call = apiService.getPopularMovies(API_KEY,lang,currentPage);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                movies = response.body().getResults();
                mAdapter = new MoviesAdapter(movies,genres,getApplicationContext());
                mAdapter.notifyDataSetChanged();
                mAdapter.setOnItemClickListener(MainActivity.this);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setVisibility(View.VISIBLE);
                loading.hide();
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                loading.hide();
                Toast.makeText(MainActivity.this, "Connection error", Toast.LENGTH_SHORT).show();
            }
        });
//        Log.e("<<<<currentPage",String.valueOf(currentPage));
//        scrollRecycler();
    }

    private void getTopRate(final List<Genre> genres){
        currentSection = 2;
        isFetchingMovies = false;
        regular(mainTitle, String.valueOf(getText(R.string.topRated)));
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MovieResponse> call = apiService.getTopRatedMovies(API_KEY,lang,currentPage);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                movies = response.body().getResults();
                mAdapter = new MoviesAdapter(movies,genres,getApplicationContext());
                mAdapter.setOnItemClickListener(MainActivity.this);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setVisibility(View.VISIBLE);
                loading.hide();
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                loading.hide();
                Toast.makeText(MainActivity.this, "Connection error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getUpcoming(final List<Genre> genres){
        currentSection = 3;
        isFetchingMovies = false;
        regular(mainTitle, String.valueOf(getText(R.string.upComing)));
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MovieResponse> call = apiService.getUpcomingMovies(API_KEY,lang,currentPage);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                movies = response.body().getResults();
                mAdapter = new MoviesAdapter(movies,genres,getApplicationContext());
                mAdapter.setOnItemClickListener(MainActivity.this);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setVisibility(View.VISIBLE);
                loading.hide();
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                loading.hide();
                Toast.makeText(MainActivity.this, "Connection error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void getplaying(final List<Genre> genres){
        regular(mainTitle, String.valueOf(getText(R.string.playing)));
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MovieResponse> call = apiService.getPlayingMovies(API_KEY,lang);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
                movies = response.body().getResults();
                mAdapter = new MoviesAdapter(movies,genres,getApplicationContext());
                mAdapter.setOnItemClickListener(MainActivity.this);
                mRecyclerView.setAdapter(mAdapter);
                mRecyclerView.setVisibility(View.VISIBLE);
                loading.hide();
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {
                loading.hide();
                Toast.makeText(MainActivity.this, "Connection error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void buttonClick(Button button , final int section){

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        isFetchingMovies = true;
                        currentPage = 1;
                        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
                            drawerLayout.closeDrawer(GravityCompat.START);
                        }
                        mRecyclerView.setVisibility(View.GONE);
                        loading.setVisibility(View.VISIBLE);
                        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
                                Call<GenreModel> call = apiService.getGenre(API_KEY, lang);
                                call.enqueue(new Callback<GenreModel>() {
                                    @Override
                                    public void onResponse(Call<GenreModel> call, Response<GenreModel> response) {
                                        List<Genre> genres = response.body().getGenres();
                                        if (section == 1) {
                                            getPopulatMovies(genres);
                                            scrollRecycler();
                                            Log.e("<<<<sectionNumber1",String.valueOf(currentSection));
                                            Log.e("<<<<currentPage1", String.valueOf(currentPage));
                                        } else if (section == 2) {
                                            getTopRate(genres);
                                            scrollRecycler();
                                            Log.e("<<<<sectionNumber2",String.valueOf(currentSection));
                                            Log.e("<<<<currentPage2", String.valueOf(currentPage));
                                        } else if (section == 3) {
                                            getUpcoming(genres);
                                            scrollRecycler();
                                            Log.e("<<<<sectionNumber2",String.valueOf(currentSection));
                                            Log.e("<<<<currentPage2", String.valueOf(currentPage));
                                        } else if (section == 4) {
                                            getplaying(genres);
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<GenreModel> call, Throwable t) {
                                        loading.hide();
                                        Toast.makeText(MainActivity.this, "Connection error", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                });
    }

    private void regular(TextView textView,String headTitle){

        textView.setText(headTitle);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        loading.setVisibility(View.VISIBLE);
    }

    private void exitDialog(){
        dialog = new Dialog(this,android.R.style.Theme_Black_NoTitleBar);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.back_dialog);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        Button ok = dialog.findViewById(R.id.ok_dialog);
        Button cancel = dialog.findViewById(R.id.cancel_dialog);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
                MainActivity.this.finish();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

    }

    public void changeLanguage(View view) {
        dialog = new Dialog(this,android.R.style.Theme_Black_NoTitleBar);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.setContentView(R.layout.language_dialog);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();

        final RadioButton arab = dialog.findViewById(R.id.arabic);
        final RadioButton eng = dialog.findViewById(R.id.english);
        Button cancel = dialog.findViewById(R.id.cancel_dialog_language);
        Button ok = dialog.findViewById(R.id.ok_dialog_language);

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (arab.isChecked()){
                    changed("ar");
                    dialog.cancel();
                    defaultDisplay();
                }else if (eng.isChecked()){
                    changed("en");
                    dialog.cancel();
                    defaultDisplay();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
    }

    private void changed (String language ){
        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        getResources().updateConfiguration(configuration,getResources().getDisplayMetrics());
        recreate();
//        finish();
//        startActivity(getIntent());



        preferences = getSharedPreferences("myShared", Context.MODE_PRIVATE);
        SharedPreferences.Editor myEdit = preferences.edit();
        myEdit.putString("lang", language);
        myEdit.commit();
    }

    public void exit(View view) {
        exitDialog();
    }

    public void searchForMovies(View view) {
        drawerLayout.closeDrawer(GravityCompat.START);
        startActivity(new Intent(MainActivity.this,SearchForMovies.class));
    }

    public void myFavorites(View view) {
        drawerLayout.closeDrawer(GravityCompat.START);
        startActivity(new Intent(MainActivity.this,FavoriteRoomActivity.class));
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else {
            exitDialog();
        }
    }

    @Override
    public void onItemClicked(View view, int position) {

        if (view.getId() ==R.id.addToFavorite){
            saveToDataBase(position);
        }
    }


    private void saveToDataBase(final int position ){
        class SaveMovie extends AsyncTask<Void,Void,Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                Movies movie = new Movies();
                movie.setId(movies.get(position).getId());
                movie.setReleaseDate(movies.get(position).getReleaseDate());
                movie.setTitle(movies.get(position).getTitle());
                movie.setPoster(movies.get(position).getPosterPath());
                movie.setVoteAverage(String.valueOf(movies.get(position).getVoteAverage()));
                String name = movie.getTitle();
                List<Movies> allDataBase = DataBaseClient.getInstance(getApplicationContext()).getMoviesDataBase().daoMovies().getAll();
                List<String> allTitle = new ArrayList<>();

                for (int x = 0  ; x <allDataBase.size(); x++){ allTitle.add(x,allDataBase.get(x).getTitle()); }

                if (allTitle.contains(name)){
                   makeToastInAsyncTask(getString(R.string.item_exist));
                }else {
                    DataBaseClient.getInstance(getApplicationContext()).getMoviesDataBase().daoMovies().insert(movie);
                    makeToastInAsyncTask(getString(R.string.save));
                }


                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }

        SaveMovie saveMovie = new SaveMovie();
        saveMovie.execute();
    }

    void makeToastInAsyncTask(final String message){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }
}
