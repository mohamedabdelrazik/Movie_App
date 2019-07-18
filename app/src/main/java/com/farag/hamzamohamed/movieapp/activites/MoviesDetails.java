package com.farag.hamzamohamed.movieapp.activites;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.farag.hamzamohamed.movieapp.R;
import com.farag.hamzamohamed.movieapp.model.MovieDetails;
import com.farag.hamzamohamed.movieapp.Network.ApiClient;
import com.farag.hamzamohamed.movieapp.Network.ApiInterface;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.farag.hamzamohamed.movieapp.activites.MainActivity.API_KEY;

public class MoviesDetails extends AppCompatActivity {
    String   title , date , homePage , lang , genre;
    int id;
    TextView titleText , tagText , dateText , webText , detailsText ,movieGenre;
    RatingBar rate;
    ImageView movieImage , back;
    ProgressDialog progressDialog;
    SharedPreferences preferences;
    LinearLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movies_details);
        initView();
        action();
    }

    private void initView(){

        preferences = getSharedPreferences("myShared", Context.MODE_PRIVATE);
        lang=preferences.getString("lang","ar");
        id = getIntent().getIntExtra("id",0);
        Log.e("<<<<<id",String.valueOf(id));
        title = getIntent().getStringExtra("title");
        date = getIntent().getStringExtra("date");
        genre = getIntent().getStringExtra("genre");
        movieGenre = findViewById(R.id.movieGenre);
        movieGenre.setText(genre);
        titleText =  findViewById(R.id.title);
        titleText.setText(title);
        tagText =  findViewById(R.id.tag);
        dateText =  findViewById(R.id.dateOfMovies);
        back =  findViewById(R.id.back);
        if (lang.equals("ar")){
            //  back.setImageDrawable(getResources().getDrawable(R.drawable.arrow_2));
            back.setImageResource(R.drawable.arrow_2);
        }
        dateText.setText(date);
        webText =  findViewById(R.id.webPage);
        detailsText =  findViewById(R.id.details);
        rate =  findViewById(R.id.rateOfMovie);
        movieImage =  findViewById(R.id.imageOfMovie);
        progressDialog = new ProgressDialog(this);
        layout = findViewById(R.id.tagLine);
    }

    private void action(){

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Wait while loading...");
        progressDialog.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progressDialog.show();
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<MovieDetails> call = apiInterface.getMoviesDetails(id,API_KEY,lang);
        call.enqueue(new Callback<MovieDetails>() {
            @Override
            public void onResponse(Call<MovieDetails> call, Response<MovieDetails> response) {
                if (response.isSuccessful()){
                    Picasso.get()
                            .load(response.body().getPosterPathMovie())
                            .resize(300,300)
                            .into(movieImage);

                    rate.setRating(response.body().getVote_average());
                    if (response.body().getTagline().isEmpty()){
                        layout.setVisibility(View.GONE);
                    }else {
                        tagText.setText(response.body().getTagline());
                    }
                    detailsText.setText(response.body().getOverview());

                    homePage = response.body().getHomepage();
                    if (homePage == null){
                        webText.setVisibility(View.GONE);
                    }
                    if (homePage!=null){Log.e("<<<web",homePage);}
                    progressDialog.cancel();

                }
            }

            @Override
            public void onFailure(Call<MovieDetails> call, Throwable throwable) {
                progressDialog.cancel();
                Toast.makeText(MoviesDetails.this, "Connection error", Toast.LENGTH_SHORT).show();
            }
        });

        webText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (homePage!= null){
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(homePage));
                startActivity(i);
                }else {
                    Toast.makeText(MoviesDetails.this, "The service is currently unavailable", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
