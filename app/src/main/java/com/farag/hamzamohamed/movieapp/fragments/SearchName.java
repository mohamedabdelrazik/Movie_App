package com.farag.hamzamohamed.movieapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.farag.hamzamohamed.movieapp.MoviesAdapter;
import com.farag.hamzamohamed.movieapp.R;
import com.farag.hamzamohamed.movieapp.model.Genre;
import com.farag.hamzamohamed.movieapp.model.GenreModel;
import com.farag.hamzamohamed.movieapp.model.Movie;
import com.farag.hamzamohamed.movieapp.model.MovieResponse;
import com.farag.hamzamohamed.movieapp.rest.ApiClient;
import com.farag.hamzamohamed.movieapp.rest.ApiInterface;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.farag.hamzamohamed.movieapp.activites.MainActivity.API_KEY;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchName extends Fragment implements View.OnClickListener {

    View view;
    SharedPreferences preferences;
    String lang;
    TextInputLayout searchInput;
    EditText editTextSearch;
    ImageView  bnSearch;
    RecyclerView mRecyclerView;
    Vibrator vib;
    Animation animShake;
    AVLoadingIndicatorView loading;

    public SearchName() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search_name, container, false);
        initView();
        return view;
    }

    private void initView(){
        bnSearch = view.findViewById(R.id.btnSearch);
        bnSearch.setOnClickListener(this);
        searchInput = view.findViewById(R.id.inputSearch);
        editTextSearch = view.findViewById(R.id.editTextSearch);
        mRecyclerView = view.findViewById(R.id.rv_search_movies);
        animShake = AnimationUtils.loadAnimation(getActivity(),R.anim.shake);
        vib = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        loading = view.findViewById(R.id.loadingSearch);
        preferences = getActivity().getSharedPreferences("myShared",Context.MODE_PRIVATE);
        lang = preferences.getString("lang","ar");
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()){
            case R.id.btnSearch:
                searchByName();
                break;

        }
    }

    private void searchByName(){
        if (!checkMovieEditText()){
            editTextSearch.setAnimation(animShake);
            editTextSearch.startAnimation(animShake);
            vib.vibrate(120);
            return;
        }
        loading.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<GenreModel> call = apiService.getGenre(API_KEY,lang);
        call.enqueue(new Callback<GenreModel>() {
            @Override
            public void onResponse(Call<GenreModel> call, Response<GenreModel> response) {
                List<Genre> genres = response.body().getGenres();
                getSearch(genres);
                mRecyclerView.setVisibility(View.VISIBLE);
                loading.hide();
            }
            @Override
            public void onFailure(Call<GenreModel> call, Throwable t) {
                loading.hide();
                Toast.makeText(getContext(), "Connection error", Toast.LENGTH_SHORT).show();
            }
        });
        // to hide keyword after press on button search
        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }

    private void getSearch(final List<Genre> genres){

        String movieName = editTextSearch.getText().toString();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MovieResponse> call = apiService.searchMovies(API_KEY,movieName);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {

                if (response.isSuccessful()){
                    if (response.body().getResults().size() != 0 ){
                        List<Movie> movies = response.body().getResults();
                        mRecyclerView.setAdapter(new MoviesAdapter(movies,genres,getContext()));
                    }else {
                        Toast.makeText(getContext(), getText((R.string.correct_name)), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<MovieResponse> call, Throwable t) {

                Toast.makeText(getContext(), "Connection error", Toast.LENGTH_SHORT).show();
            }
        });
    }

        private boolean checkMovieEditText(){
        if (editTextSearch.getText().toString().isEmpty()){
            searchInput.setErrorEnabled(true);
            searchInput.setError(getString(R.string.err_msg_name));
            return false;
        }
        searchInput.setErrorEnabled(false);
        return true;
    }
}
