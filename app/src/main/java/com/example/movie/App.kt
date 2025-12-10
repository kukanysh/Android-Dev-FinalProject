package com.example.movie

import android.app.Application
import androidx.room.Room
import com.example.movie.room.MovieDatabase

class App : Application() {

    companion object {
        lateinit var db: MovieDatabase
            private set
    }

    override fun onCreate() {
        super.onCreate()

        db = Room.databaseBuilder(
            applicationContext,
            MovieDatabase::class.java,
            "movie_db"
        ).build()
    }
}