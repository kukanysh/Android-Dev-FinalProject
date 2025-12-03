package com.example.movie.network

import com.example.movie.model.MovieResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("/")
    suspend fun getMovies(
        @Query("apikey") apiKey: String,
        @Query("t") title: String
    ): MovieResponse



}