package com.example.movie.room

import com.example.movie.model.Movie

fun Movie.toEntity(searchQuery: String = ""): MovieEntity {
    return MovieEntity(
        imdbID = this.imdbID,
        title = this.title,
        year = this.year,
        type = this.type,
        poster = this.poster,
        rated = this.rated,
        released = this.released,
        runtime = this.runtime,
        genre = this.genre,
        director = this.director,
        writer = this.writer,
        actors = this.actors,
        plot = this.plot,
        language = this.language,
        country = this.country,
        searchQuery = searchQuery
    )
}

fun MovieEntity.toMovie(): Movie {
    return Movie(
        imdbID = this.imdbID,
        title = this.title,
        year = this.year,
        type = this.type,
        poster = this.poster,
        rated = this.rated,
        released = this.released,
        runtime = this.runtime,
        genre = this.genre,
        director = this.director,
        writer = this.writer,
        actors = this.actors,
        plot = this.plot,
        language = this.language,
        country = this.country
    )
}

