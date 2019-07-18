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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.farag.hamzamohamed.movieapp.Adapter.MoviesAdapter;
import com.farag.hamzamohamed.movieapp.R;
import com.farag.hamzamohamed.movieapp.model.Genre;
import com.farag.hamzamohamed.movieapp.model.GenreModel;
import com.farag.hamzamohamed.movieapp.model.Movie;
import com.farag.hamzamohamed.movieapp.model.MovieResponse;
import com.farag.hamzamohamed.movieapp.Network.ApiClient;
import com.farag.hamzamohamed.movieapp.Network.ApiInterface;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.farag.hamzamohamed.movieapp.activites.MainActivity.API_KEY;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChooseSearch extends Fragment implements View.OnClickListener,AdapterView.OnItemSelectedListener {


    View view;
    MoviesAdapter mAdapter;
    LinearLayoutManager manager;
    Spinner sortSpinner;
    SharedPreferences preferences;
    String lang, itemSelected;
    TextInputLayout searchInput;
    EditText editTextSearch;
    ImageView bnSearchYear;
    RecyclerView mRecyclerView;
    Vibrator vib;
    Animation animShake;
    AVLoadingIndicatorView loading , avi;
    List<String> spinnerData;
    List<Genre> genres;
    ArrayAdapter<String> spAdapter;
    int currentPage = 1 , moviesYear;
    private boolean isFetchingMovies;

    public ChooseSearch() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_choose_search, container, false);
        initView();
        return view;
    }


    private void initView() {
        sortSpinner = view.findViewById(R.id.sortSpinner);
        sortSpinner.setOnItemSelectedListener(this);
        bnSearchYear = view.findViewById(R.id.btnSearchYear);
        bnSearchYear.setOnClickListener(this);
        searchInput = view.findViewById(R.id.inputSearchYear);
        editTextSearch = view.findViewById(R.id.editTextSearchYear);
        mRecyclerView = view.findViewById(R.id.rv_search_movies);
        manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);
        animShake = AnimationUtils.loadAnimation(getActivity(), R.anim.shake);
        vib = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        loading = view.findViewById(R.id.loadingSearch);
        avi = view.findViewById(R.id.avi);
        preferences = getActivity().getSharedPreferences("myShared", Context.MODE_PRIVATE);
        lang = preferences.getString("lang", "ar");
        genres = new ArrayList<>();


        spinnerData = new ArrayList<>();
        spinnerData.add(String.valueOf(getText(R.string.sort)));
        spinnerData.add(String.valueOf(getText(R.string.sort1)));
        spinnerData.add(String.valueOf(getText(R.string.sort2)));
        spinnerData.add(String.valueOf(getText(R.string.sort3)));


        // Creating adapter for spinner
        spAdapter = new ArrayAdapter<>(getContext(), R.layout.spinner_item, spinnerData);

        // Drop down layout style - list view with radio button
        spAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        sortSpinner.setAdapter(spAdapter);
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnSearchYear:
                searchByName();
                break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
        // On selecting a spinner item

        switch (position) {

            case 1:
                itemSelected = "vote_average.desc";
                break;
            case 0:
            case 2:
                itemSelected = "popularity.desc";
                break;
            case 3:
                itemSelected = "release_date.desc";
                break;
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    private void searchByName() {
        if (!checkMovieEditText()) {
            editTextSearch.setAnimation(animShake);
            editTextSearch.startAnimation(animShake);
            vib.vibrate(120);
            return;
        }
        loading.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<GenreModel> call = apiService.getGenre(API_KEY, lang);
        call.enqueue(new Callback<GenreModel>() {
            @Override
            public void onResponse(Call<GenreModel> call, Response<GenreModel> response) {
                List<Genre> genres = response.body().getGenres();
                getSearch(genres);
                mRecyclerView.setVisibility(View.VISIBLE);
                loading.hide();
                scrollRecycler();
            }

            @Override
            public void onFailure(Call<GenreModel> call, Throwable t) {
                loading.hide();
                Toast.makeText(getContext(), "Connection error", Toast.LENGTH_SHORT).show();
            }
        });
        // to hide keyword after press on button search
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
    }

    private void getSearch(final List<Genre> genres) {

        moviesYear = Integer.parseInt(editTextSearch.getText().toString());

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        Call<MovieResponse> call = apiService.searchByYear(API_KEY, lang, moviesYear, itemSelected,currentPage);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {

                if (response.isSuccessful()) {
                    if (response.body().getResults().size() != 0) {
                        List<Movie> movies = response.body().getResults();
                        mAdapter = new MoviesAdapter(movies, genres, getContext());
                        mRecyclerView.setAdapter(mAdapter);
                    } else {
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

    private boolean checkMovieEditText() {
        if (editTextSearch.getText().toString().isEmpty()) {
            searchInput.setErrorEnabled(true);
            searchInput.setError(getString(R.string.err_msg_year));
            return false;
        }
        searchInput.setErrorEnabled(false);
        return true;
    }

    private void scrollRecycler() {
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int total = manager.getItemCount();
                int visibleItem = manager.getChildCount();
                int firstItem = manager.findFirstVisibleItemPosition();
                if (firstItem + visibleItem == total - 2) {
                    if (!isFetchingMovies) {
                        getMovies(currentPage + 1);
                    }
                }
            }
        });
    }

    private void getMovies(final int page) {
        isFetchingMovies = true;
        if (currentPage < 15) {
            avi.setVisibility(View.VISIBLE);
            ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
            Call<MovieResponse> call = apiService.searchByYear(API_KEY, lang, moviesYear,itemSelected,page);
            call.enqueue(new Callback<MovieResponse>() {
                @Override
                public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
//                        Log.e("<<<<currentPage",String.valueOf(page));
                    Toast.makeText(getContext(), String.valueOf(page), Toast.LENGTH_SHORT).show();
                    List<Movie> movies = response.body().getResults();
                    if (mAdapter == null) {
                        mAdapter = new MoviesAdapter(movies, genres, getContext());
                        mRecyclerView.setAdapter(mAdapter);
                    } else {
                        mAdapter.appendMovies(movies);
                    }
                    currentPage = page;
                    isFetchingMovies = false;
                    avi.hide();
                }

                @Override
                public void onFailure(Call<MovieResponse> call, Throwable t) {
                    avi.hide();
                    Toast.makeText(getContext(), "Connection error", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "The last", Toast.LENGTH_SHORT).show();
        }
    }
}
