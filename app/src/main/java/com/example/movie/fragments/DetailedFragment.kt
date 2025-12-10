package com.example.movie.fragments

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.movie.R
import com.example.movie.viewmodel.ViewModel

class DetailedFragment : Fragment(R.layout.detailed_fragment) {

    private lateinit var viewModel: ViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[ViewModel::class.java]

        val movieId = arguments?.getString("movieId") ?: return

        val poster = view.findViewById<ImageView>(R.id.detailPoster)
        val title = view.findViewById<TextView>(R.id.detailTitle)
        val year = view.findViewById<TextView>(R.id.detailYear)
        val runtime = view.findViewById<TextView>(R.id.detailRuntime)
        val genre = view.findViewById<TextView>(R.id.detailGenre)
        val director = view.findViewById<TextView>(R.id.detailDirector)
        val actors = view.findViewById<TextView>(R.id.detailActors)
        val country = view.findViewById<TextView>(R.id.detailCountry)
        val plot = view.findViewById<TextView>(R.id.detailPlot)
//        val rated = view.findViewById<TextView>(R.id.detailRated)
        val released = view.findViewById<TextView>(R.id.detailReleased)

        // Observe movie details
        viewModel.movieDetail.observe(viewLifecycleOwner) { movie ->
            title.text = movie.title
            year.text = movie.year
            runtime.text = movie.runtime
            genre.text = movie.genre
            director.text = movie.director
            actors.text = movie.actors
            country.text = movie.country
            plot.text = movie.plot
//            rated.text = movie.rated
            released.text = movie.released


            Glide.with(this)
                .load(movie.poster)
                .into(poster)
        }

        // Load details
        viewModel.loadMovieDetail(movieId)
    }
}
