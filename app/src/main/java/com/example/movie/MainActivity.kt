package com.example.movie

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movie.adapter.Adapter
import com.example.movie.model.Movie
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {


    private val movieList = mutableListOf<Movie>()

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = Adapter(
            movieList, isTopList = false
        ) { movie ->
            val bundle = Bundle().apply {
                putString("movieId", movie.imdbID)
            }
            findNavController(R.id.nav_host_fragment).navigate(R.id.detailFragment, bundle)

        }





//    private fun loadMovies() {
//
//        lifecycleScope.launch {
//            val response = RetrofitInstance.api.getMovies(
//                apiKey = "a6a1e977",
//                title = "iron man",
//                page = 1
//            )
//
//            if (response.isSuccessful) {
//                val movies = response.body()?.search ?: emptyList()
//                recyclerView.adapter = Adapter(movies)
//
//                movies?.forEach {
//                    Log.d("MOVIES", "${it.title} (${it.year}) - ${it.imdbID}")
//                }
//            } else {
//                println("Request failed: ${response.errorBody()}")
//            }
//        }
//    }


    }
}

