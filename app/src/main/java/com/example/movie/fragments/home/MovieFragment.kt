package com.example.movie.fragments.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movie.R
import com.example.movie.adapter.Adapter
import com.example.movie.viewmodel.ViewModel
import kotlin.math.abs

class MovieFragment : Fragment(R.layout.fragment_movie) {

    private lateinit var viewModel: ViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[ViewModel::class.java]

        val popularRv = view.findViewById<RecyclerView>(R.id.popularRecyclerView)
        val trendingRv = view.findViewById<RecyclerView>(R.id.trendingRecyclerView)
        val recentRv = view.findViewById<RecyclerView>(R.id.recentRecyclerView)


        val layoutManager = LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        popularRv.layoutManager = layoutManager

        popularRv.post {
            val offset = 100 // negative to show part of first item
            layoutManager.scrollToPositionWithOffset(1, offset)
        }

        trendingRv.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        recentRv.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)


        fun applyCenterScaling(recyclerView: RecyclerView) {
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager

            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(rv, dx, dy)

                    val mid = rv.width / 2f

                    for (i in 0 until rv.childCount) {
                        val child = rv.getChildAt(i)
                        val childMid = (child.x + child.width) / 2f
                        val distance = abs(mid - childMid)

                        // scale main item to 1.0, neighbors smaller
                        val scale = 0.7f + (1 - (distance / rv.width).coerceIn(0f, 1f)) * 0.4f

                        child.scaleX = scale
                        child.scaleY = scale

                    }
                }
            })
        }

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


        applyCenterScaling(popularRv)

        // Load data
        viewModel.loadPopularMovies()
        viewModel.loadTrendingMovies()
        viewModel.loadRecentMovies()

    }
}