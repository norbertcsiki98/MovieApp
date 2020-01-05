package com.example.movieapp.interfaces;


import com.example.movieapp.Movie;
import com.example.movieapp.MovieResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieInterface {

    public static final String BASE_URL = "https://api.themoviedb.org/3/movie/";
    public static final String API_KEY = "fc8910d3f139f6efd432899524d75f3f";
    @GET("top_rated")
    Call<MovieResponse> getMovies(@Query("api_key") String api_key);

}
