package com.example.movie.fragments.search

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movie.R
import com.example.movie.adapter.Adapter
import com.example.movie.network.RetrofitInstance
import com.example.movie.room.MovieDatabase
import com.example.movie.room.MovieRepository
import kotlinx.coroutines.launch



class SearchFragment : Fragment(R.layout.fragment_search) {

    private lateinit var repository: MovieRepository

    private val adapter = Adapter(isTopList = false) { movie ->
            // handle click here (you can navigate)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val etSearch = view.findViewById<EditText>(R.id.etSearch)
        val rvMovies = view.findViewById<RecyclerView>(R.id.rvMovies)

        rvMovies.adapter = adapter
        rvMovies.layoutManager = LinearLayoutManager(requireContext())

        val db = MovieDatabase.getDatabase(requireContext())
        val dao = db.movieDao()
        val api = RetrofitInstance.api
        repository = MovieRepository(dao, api)

        etSearch.addTextChangedListener { text ->
            val query = text.toString().trim()
            if (query.length > 2) {
                search(query)
            }
        }
    }

    private fun search(query: String) {
        lifecycleScope.launch {
            val movies = repository.getMovies(query)
            adapter.updateList(movies)
        }
    }

}
