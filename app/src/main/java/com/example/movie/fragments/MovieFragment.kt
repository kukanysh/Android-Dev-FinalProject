package com.example.movie.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movie.R
import com.example.movie.adapter.Adapter

class MovieFragment : Fragment(R.layout.fragment_movie) {

    private lateinit var viewModel: com.example.movie.viewmodel.ViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // ViewModel
        viewModel = ViewModelProvider(this)[com.example.movie.viewmodel.ViewModel::class.java]

        // RecyclerViews
        val popularRv = view.findViewById<RecyclerView>(R.id.popularRecyclerView)
        val trendingRv = view.findViewById<RecyclerView>(R.id.trendingRecyclerView)
        val recentRv = view.findViewById<RecyclerView>(R.id.recentRecyclerView)

        popularRv.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        trendingRv.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        recentRv.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

        // Observe LiveData and display items
        viewModel.popularMovies.observe(viewLifecycleOwner) { movies ->
            popularRv.adapter = Adapter(movies, isTopList = true) { movie ->
                val bundle = Bundle()
                bundle.putString("movieId", movie.imdbID)
                findNavController().navigate(R.id.detailFragment, bundle)

            }
        }

        viewModel.trendingMovies.observe(viewLifecycleOwner) { movies ->
            trendingRv.adapter = Adapter(movies, isTopList = false) { movie ->
                val bundle = Bundle()
                bundle.putString("movieId", movie.imdbID)
                findNavController().navigate(R.id.detailFragment, bundle)

            }
        }

        viewModel.recentMovies.observe(viewLifecycleOwner) { movies ->
            recentRv.adapter = Adapter(movies, isTopList = false) { movie ->
                val bundle = Bundle()
                bundle.putString("movieId", movie.imdbID)
                findNavController().navigate(R.id.detailFragment, bundle)

            }
        }

        // Load data
        viewModel.loadPopularMovies()
        viewModel.loadTrendingMovies()
        viewModel.loadRecentMovies()
    }
}
