package com.farag.hamzamohamed.movieapp.rest;

import com.farag.hamzamohamed.movieapp.model.GenreModel;
import com.farag.hamzamohamed.movieapp.model.MovieDetails;
import com.farag.hamzamohamed.movieapp.model.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("movie/top_rated")
    Call<MovieResponse> getTopRatedMovies (@Query("api_key")String apiKey,
                                           @Query("language")String language,
                                           @Query("page")int page);

    @GET("movie/popular")
    Call<MovieResponse> getPopularMovies (@Query("api_key")String apiKey,
                                          @Query("language")String language,
                                          @Query("page")int page);

    @GET("movie/upcoming")
    Call<MovieResponse> getUpcomingMovies (@Query("api_key")String apiKey,
                                           @Query("language")String language,
                                           @Query("page")int page);

    @GET("movie/now_playing")
    Call<MovieResponse> getPlayingMovies (@Query("api_key")String apiKey,
                                           @Query("language")String language);

    @GET("movie/{id}")
    Call<MovieDetails> getMoviesDetails (@Path("id") int id ,
                                         @Query("api_key") String apiKey,
                                         @Query("language")String language);

    @GET("search/movie")
    Call<MovieResponse> searchMovies (@Query("api_key")String apiKey,
                                      @Query("query")String query);

    @GET("genre/movie/list")
    Call<GenreModel> getGenre (@Query("api_key")String apiKey,
                               @Query("language")String language);

    @GET("discover/movie")
    Call<MovieResponse> searchByYear (@Query("api_key")String apiKey,
                                      @Query("language")String language,
                                      @Query("year")int year,
                                      @Query("sort_by")String sort_by,
                                      @Query("page")int page);

}
