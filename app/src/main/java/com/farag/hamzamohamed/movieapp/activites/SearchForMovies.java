package com.farag.hamzamohamed.movieapp.activites;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.farag.hamzamohamed.movieapp.R;
import com.farag.hamzamohamed.movieapp.Adapter.VPagerAdapter;
import com.farag.hamzamohamed.movieapp.fragments.ChooseSearch;
import com.farag.hamzamohamed.movieapp.fragments.SearchName;

public class SearchForMovies extends AppCompatActivity {

    SharedPreferences preferences;
    String lang;
    ImageView back;
    TabLayout mTabLayout;
    ViewPager mViewPager;
    VPagerAdapter mPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_for_movies);

        initView();
    }

    private void initView(){
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        preferences = getSharedPreferences("myShared",Context.MODE_PRIVATE);
        lang = preferences.getString("lang","ar");
        back = findViewById(R.id.back);
        if (lang.equals("ar")){
            back.setImageResource(R.drawable.arrow_2);
        }
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mTabLayout = findViewById(R.id.tablayout);
        mViewPager = findViewById(R.id.vPager);

        mPagerAdapter = new VPagerAdapter(getSupportFragmentManager());
        mPagerAdapter.addFragment(new SearchName(),String.valueOf(getText(R.string.tab1)));
        mPagerAdapter.addFragment(new ChooseSearch(),String.valueOf(getText(R.string.tab2)));

        mViewPager.setAdapter(mPagerAdapter);

        mTabLayout.setupWithViewPager(mViewPager);
//        searchInput = findViewById(R.id.inputSearch);
//        editTextSearch = findViewById(R.id.editTextSearch);
//        mRecyclerView = findViewById(R.id.rv_search_movies);
//        animShake = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake);
//        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
//        loading = findViewById(R.id.loadingSearch);
//    }
//
//    public void butt_search(View view) {
//        searchByName();
//    }
//
//
//
//    private void searchByName(){
//        if (!checkMovieEditText()){
//            editTextSearch.setAnimation(animShake);
//            editTextSearch.startAnimation(animShake);
//            vib.vibrate(120);
//            return;
//        }
//        loading.setVisibility(View.VISIBLE);
//        mRecyclerView.setVisibility(View.GONE);
//        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
//        Call<GenreModel> call = apiService.getGenre(API_KEY,lang);
//        call.enqueue(new Callback<GenreModel>() {
//            @Override
//            public void onResponse(Call<GenreModel> call, Response<GenreModel> response) {
//                List<Genre> genres = response.body().getGenres();
//                getSearch(genres);
//                mRecyclerView.setVisibility(View.VISIBLE);
//                loading.hide();
//            }
//            @Override
//            public void onFailure(Call<GenreModel> call, Throwable t) {
//                loading.hide();
//                Toast.makeText(SearchForMovies.this, "Connection error", Toast.LENGTH_SHORT).show();
//            }
//        });
//        // to hide keyword after press on button search
//        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
//    }
//
//    private void getSearch(final List<Genre> genres){
//
//        String movieName = editTextSearch.getText().toString();
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(SearchForMovies.this));
//
//        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
//        Call<MovieResponse> call = apiService.searchMovies(API_KEY,movieName);
//        call.enqueue(new Callback<MovieResponse>() {
//            @Override
//            public void onResponse(Call<MovieResponse> call, Response<MovieResponse> response) {
//
//                if (response.isSuccessful()){
//                    if (response.body().getResults().size() != 0 ){
//                        List<Movie> movies = response.body().getResults();
//                        mRecyclerView.setAdapter(new MoviesAdapter(movies,genres,getApplicationContext()));
//                    }else {
//                        Toast.makeText(SearchForMovies.this, getText((R.string.correct_name)), Toast.LENGTH_SHORT).show();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MovieResponse> call, Throwable t) {
//                progressDialog.cancel();
//                Toast.makeText(SearchForMovies.this, "Connection error", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private boolean checkMovieEditText(){
//        if (editTextSearch.getText().toString().isEmpty()){
//            searchInput.setErrorEnabled(true);
//            searchInput.setError(getString(R.string.err_msg_name));
//            return false;
//        }
//        searchInput.setErrorEnabled(false);
//        return true;
    }
}
