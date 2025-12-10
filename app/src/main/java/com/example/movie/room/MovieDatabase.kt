package com.example.movie.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.movie.room.MovieEntity

@Database(
    entities = [MovieEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao
}
