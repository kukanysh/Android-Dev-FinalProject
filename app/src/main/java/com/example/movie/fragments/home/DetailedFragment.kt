package com.example.movie.fragments.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.edit
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.movie.R
import com.example.movie.network.RetrofitInstance
import com.example.movie.room.MovieDatabase
import com.example.movie.room.MovieRepository
import com.example.movie.viewmodel.MovieViewModel

class DetailedFragment : Fragment(R.layout.detailed_fragment) {

    private val repository by lazy {
        val movieDao = MovieDatabase.getDatabase(requireContext()).movieDao()
        MovieRepository(movieDao, RetrofitInstance.api)
    }

    private val viewModel: MovieViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MovieViewModel(repository) as T
            }
        }
    }

    private var isLiked = false
    private var isSaved = false

    private lateinit var btnLike: LinearLayout
    private lateinit var btnSave: LinearLayout
    private lateinit var btnSend: LinearLayout
    private lateinit var likeIcon: ImageView
    private lateinit var listIcon: ImageView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
        val released = view.findViewById<TextView>(R.id.detailReleased)

        btnLike = view.findViewById(R.id.btnLike)
        btnSave = view.findViewById(R.id.btnList)
        btnSend = view.findViewById(R.id.btnSend)
        likeIcon = view.findViewById(R.id.likeIcon)
        listIcon = view.findViewById(R.id.listIcon)

        val prefs = requireContext().getSharedPreferences("movie_prefs", Context.MODE_PRIVATE)


        isLiked = prefs.getBoolean("${movieId}_liked", false)
        isSaved = prefs.getBoolean("${movieId}_saved", false)


        likeIcon.setImageResource(if (isLiked) R.drawable.ic_like_filled else R.drawable.ic_like)
        listIcon.setImageResource(if (isSaved) R.drawable.ic_list_filled else R.drawable.ic_list)

        fun animatePop(icon: ImageView) {
            icon.animate().scaleX(1.2f).scaleY(1.2f).setDuration(100).withEndAction {
                icon.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
            }.start()
        }

        btnLike.setOnClickListener {
            isLiked = !isLiked
            likeIcon.setImageResource(if (isLiked) R.drawable.ic_like_filled else R.drawable.ic_like)
            animatePop(likeIcon)


            prefs.edit { putBoolean("${movieId}_liked", isLiked) }
        }

        btnSave.setOnClickListener {
            isSaved = !isSaved
            listIcon.setImageResource(if (isSaved) R.drawable.ic_list_filled else R.drawable.ic_list)
            animatePop(listIcon)


            prefs.edit { putBoolean("${movieId}_saved", isSaved) }
        }

        btnSend.setOnClickListener {

            viewModel.movieDetail.value?.let { movie ->
                val shareIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    type = "text/plain"
                    putExtra(
                        Intent.EXTRA_TEXT,
                        "Check out this movie: ${movie.title} (${movie.year})"
                    )
                }
                startActivity(Intent.createChooser(shareIntent, "Share via"))
            }
        }

        view.findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            findNavController().popBackStack()
        }


        viewModel.movieDetail.observe(viewLifecycleOwner) { movie ->
            title.text = movie?.title
            year.text = movie?.year
            runtime.text = movie?.runtime
            genre.text = movie?.genre
            director.text = movie?.director
            actors.text = movie?.actors
            country.text = movie?.country
            plot.text = movie?.plot
            released.text = movie?.released

            Glide.with(this)
                .load(movie?.poster)
                .into(poster)
        }

        viewModel.loadMovieDetail(movieId)
    }
}