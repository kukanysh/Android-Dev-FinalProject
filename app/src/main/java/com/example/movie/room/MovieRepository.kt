package com.example.movie.room

import com.example.movie.model.Movie
import com.example.movie.network.ApiService

class MovieRepository(private val movieDao: MovieDao, private val apiService: ApiService) {

    suspend fun getMovies(title: String): List<Movie> {
        return try {
            val response = apiService.getMovies("a6a1e977", title)
            val moviesDto = response.body()?.search ?: emptyList()

            // Convert DTO to Entity for caching
            val moviesEntity = moviesDto.map { dto ->
                MovieEntity(
                    imdbID = dto.imdbID,
                    title = dto.title,
                    year = dto.year,
                    type = dto.type,
                    poster = dto.poster,
                    rated = dto.rated,
                    released = dto.released,
                    runtime = dto.runtime,
                    genre = dto.genre,
                    director = dto.director,
                    writer = dto.writer,
                    actors = dto.actors,
                    plot = dto.plot,
                    language = dto.language,
                    country = dto.country
                )
            }

            movieDao.insertMovies(moviesEntity)

            // Return domain model to ViewModel
            moviesEntity.map { entity ->
                Movie(
                    imdbID = entity.imdbID,
                    title = entity.title,
                    year = entity.year,
                    type = entity.type,
                    poster = entity.poster,
                    rated = entity.rated,
                    released = entity.released,
                    runtime = entity.runtime,
                    genre = entity.genre,
                    director = entity.director,
                    writer = entity.writer,
                    actors = entity.actors,
                    plot = entity.plot,
                    language = entity.language,
                    country = entity.country
                )
            }

        } catch (e: Exception) {
            // Offline fallback
            movieDao.getAllMovies().map { entity ->
                Movie(
                    imdbID = entity.imdbID,
                    title = entity.title,
                    year = entity.year,
                    type = entity.type,
                    poster = entity.poster,
                    rated = entity.rated,
                    released = entity.released,
                    runtime = entity.runtime,
                    genre = entity.genre,
                    director = entity.director,
                    writer = entity.writer,
                    actors = entity.actors,
                    plot = entity.plot,
                    language = entity.language,
                    country = entity.country
                )
            }
        }
    }

    suspend fun getMovieById(movieId: String): Movie? {
        return try {
            val response = apiService.getMovieById("a6a1e977", movieId)
            response.body()
        } catch (e: Exception) {
            null
        }
    }
}
