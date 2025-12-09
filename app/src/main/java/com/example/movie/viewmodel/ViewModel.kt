package com.example.movie.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie.model.Movie
import com.example.movie.network.RetrofitInstance
import kotlinx.coroutines.launch

class ViewModel: ViewModel() {

    val popularMovies = MutableLiveData<List<Movie>>()
    val trendingMovies = MutableLiveData<List<Movie>>()
    val recentMovies = MutableLiveData<List<Movie>>()

    val movieDetail = MutableLiveData<Movie>()

    fun loadPopularMovies() = loadMovies("movie", popularMovies)
    fun loadTrendingMovies() = loadMovies("avengers", trendingMovies)
    fun loadRecentMovies() = loadMovies("inc", recentMovies)

    fun loadMovies(title: String, liveData: MutableLiveData<List<Movie>>) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getMovies(
                    apiKey = "a6a1e977",
                    title = title,
                    page = 1
                )

                if (response.isSuccessful) {
                    liveData.value = response.body()?.search ?: emptyList()
                } else {
                    Log.e("MoviesViewModel", "Error: ${response.errorBody()}")
                }
            } catch (e: Exception) {
                Log.e("MoviesViewModel", "Exception: $e")
            }
        }
    }


    fun loadMovieDetail(movieId: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getMovieById(
                    apiKey = "a6a1e977",
                    movieId = movieId
                )

                if (response.isSuccessful) {
                    movieDetail.value = response.body()
                } else {
                    Log.e("MoviesViewModel", "Detail error: ${response.errorBody()}")
                }

            } catch (e: Exception) {
                Log.e("MoviesViewModel", "Detail exception: $e")
            }
        }
    }



}