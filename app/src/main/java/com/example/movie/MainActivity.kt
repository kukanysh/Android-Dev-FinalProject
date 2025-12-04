package com.example.movie

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.movie.model.Movie
import com.example.movie.network.RetrofitInstance
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {


    private val movieList = mutableListOf<Movie>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        loadMovies()

    }

    private fun loadMovies() {

        lifecycleScope.launch {
            val response = RetrofitInstance.api.getMovies(
                apiKey = "a6a1e977",
                title = "bat",
                page = 1
            )

            if (response.isSuccessful) {
                val movies = response.body()?.search
                movies?.forEach {
                    Log.d("MOVIES", "${it.title} (${it.year}) - ${it.imdbID}")
                }
            } else {
                println("Request failed: ${response.errorBody()}")
            }
        }
    }



//    fun loadMovies() {
//
//        lifecycleScope.launch {
//            try {
//
//
//
//
//            } catch(e: Exception) {
//
//            }
//        }
//    }

}

