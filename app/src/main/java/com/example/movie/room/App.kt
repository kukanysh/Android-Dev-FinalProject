package com.example.movie.room

import android.app.Application
import androidx.room.Room

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