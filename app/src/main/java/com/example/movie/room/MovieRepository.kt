package com.example.movie.room

import android.util.Log
import com.example.movie.model.Movie
import com.example.movie.network.ApiService

class MovieRepository(private val movieDao: MovieDao, private val apiService: ApiService) {

    suspend fun getMovies(title: String): List<Movie> {
        return try {
            val response = apiService.getMovies("a6a1e977", title)
            val movies = response.body()?.search ?: emptyList()

            Log.d("MovieRepository", "Loaded from network: ${movies.size} movies")

            // Save search results basic info
            val moviesEntity = movies.map { it.toEntity().copy(searchQuery = title) }
            movieDao.insertMovies(moviesEntity)

            // Fetch full details for offline
            movies.forEach { movie ->
                try {
                    val detail = apiService.getMovieById("a6a1e977", movie.imdbID).body()
                    detail?.let {
                        movieDao.insertMovies(listOf(it.toEntity().copy(searchQuery = title)))
                        Log.d("MovieRepository", "Saved full detail: ${it.title}")
                    }
                } catch (e: Exception) {
                    Log.d("MovieRepository", "Failed to fetch detail for ${movie.title}")
                }
            }

            return movies

        } catch (e: Exception) {
            Log.d("MovieRepository", "Network error: $e, loading from Room")
            movieDao.getMoviesByQuery(title).map { it.toMovie() }
        }
    }

    suspend fun getMovieById(movieId: String): Movie? {
        return try {
            val response = apiService.getMovieById("a6a1e977", movieId)
            response.body()?.also {
                Log.d("MovieRepository", "Loaded movie detail from network: ${it.title}")
            }
        } catch (e: Exception) {
            Log.d("MovieRepository", "Network error: $e, loading movie from Room")
            movieDao.getMovieById(movieId)?.run {
                Log.d("MovieRepository", "Loaded movie detail from Room: $title")
                Movie(
                    imdbID = imdbID,
                    title = title,
                    year = year,
                    type = type,
                    poster = poster,
                    rated = rated,
                    released = released,
                    runtime = runtime,
                    genre = genre,
                    director = director,
                    writer = writer,
                    actors = actors,
                    plot = plot,
                    language = language,
                    country = country
                )
            }
        }
    }



}
