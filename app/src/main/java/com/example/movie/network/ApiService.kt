package com.example.movie.network

import com.example.movie.model.Movie
import com.example.movie.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/")
    suspend fun getMovies(
        @Query("apikey") apiKey: String,
        @Query("s") title: String,
        @Query("type") type: String? = "movie", //movie, series, episode
        @Query("page") page: Int? = 1
    ): retrofit2.Response<MovieResponse>

    @GET("/")
    suspend fun getMovieById(
        @Query("apikey") apiKey: String,
        @Query("i") movieId: String
    ): retrofit2.Response<Movie>






}