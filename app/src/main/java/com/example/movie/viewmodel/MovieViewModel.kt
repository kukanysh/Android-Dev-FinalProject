package com.example.movie.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movie.model.Movie
import com.example.movie.room.MovieRepository
import kotlinx.coroutines.launch

class MovieViewModel(private val repository: MovieRepository): ViewModel() {

    val popularMovies = MutableLiveData<List<Movie>>()
    val trendingMovies = MutableLiveData<List<Movie>>()
    val recentMovies = MutableLiveData<List<Movie>>()
    val movieDetail = MutableLiveData<Movie?>()

    fun loadPopularMovies() {
        loadMovies("jump", popularMovies)
    }

    fun loadTrendingMovies() {
        loadMovies("avengers", trendingMovies)
    }

    fun loadRecentMovies() {
        loadMovies("war", recentMovies)
    }

    fun loadMovies(title: String, liveData: MutableLiveData<List<Movie>>) {
        viewModelScope.launch {
            val movies = repository.getMovies(title)
            liveData.value = movies

        }
    }


    fun loadMovieDetail(movieId: String) {
        viewModelScope.launch {
            val movie = repository.getMovieById(movieId)
            movieDetail.value = movie
        }
    }



}