package com.example.movie.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movie.R
import com.example.movie.adapter.Adapter
import com.example.movie.network.RetrofitInstance
import com.example.movie.room.MovieDatabase
import com.example.movie.room.MovieRepository
import com.example.movie.viewmodel.MovieViewModel

class MovieFragment : Fragment(R.layout.fragment_movie) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val repository by lazy {
            val movieDao = MovieDatabase.getDatabase(requireContext()).movieDao()
            MovieRepository(movieDao, RetrofitInstance.api)
        }

        val viewModel: MovieViewModel by viewModels {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    @Suppress("UNCHECKED_CAST")
                    return MovieViewModel(repository) as T
                }
            }
        }

        // RecyclerViews
        val popularRv = view.findViewById<RecyclerView>(R.id.popularRecyclerView)
        val trendingRv = view.findViewById<RecyclerView>(R.id.trendingRecyclerView)
        val recentRv = view.findViewById<RecyclerView>(R.id.recentRecyclerView)


        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        popularRv.layoutManager = layoutManager

        popularRv.post {
            val offset = 100 // negative to show part of first item
            layoutManager.scrollToPositionWithOffset(1, offset)
        }

        applyCenterScaling(popularRv)

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

    fun applyCenterScaling(recyclerView: RecyclerView) {
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(rv, dx, dy)

                val mid = rv.width / 2f

                for (i in 0 until rv.childCount) {
                    val child = rv.getChildAt(i)
                    val childMid = (child.x + child.width) / 2f
                    val distance = kotlin.math.abs(mid - childMid)

                    // scale main item to 1.0, neighbors smaller (0.7-0.85)
                    val scale = 0.7f + (1 - (distance / rv.width).coerceIn(0f, 1f)) * 0.3f

                    child.scaleX = scale
                    child.scaleY = scale
                    child.alpha = 0.5f + (scale - 0.7f) / 0.3f * 0.5f // optional: fade neighbors
                }
            }
        })
    }


}
