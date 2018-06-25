package com.sparkchicks.popularmovies.network;

import com.sparkchicks.popularmovies.model.MovieResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface GetMovieService {

    @GET("3/movie/{sortBy}")
    Call<MovieResponse> getMovies(@Path("sortBy") String sortBy, @Query("api_key") String apiKey);
}
