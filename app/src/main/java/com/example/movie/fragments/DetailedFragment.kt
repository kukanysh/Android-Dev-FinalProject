package com.example.movie.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.movie.R
import com.example.movie.viewmodel.ViewModel

class DetailedFragment : Fragment(R.layout.detailed_fragment) {

    private lateinit var viewModel: ViewModel

    private var isLiked = false
    private var isSaved = false

    private lateinit var btnLike: ImageButton
    private lateinit var btnSave: ImageButton

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
        val released = view.findViewById<TextView>(R.id.detailReleased)

        btnLike = view.findViewById(R.id.btnLike)
        btnSave = view.findViewById(R.id.btnSave)

        val prefs = requireContext().getSharedPreferences("movie_prefs", Context.MODE_PRIVATE)

        // Загружаем сохранённые состояния для этого фильма
        isLiked = prefs.getBoolean("${movieId}_liked", false)
        isSaved = prefs.getBoolean("${movieId}_saved", false)

        // Устанавливаем иконки при загрузке
        btnLike.setImageResource(if (isLiked) R.drawable.ic_heart_filled else R.drawable.ic_heart_outline)
        btnSave.setImageResource(if (isSaved) R.drawable.ic_bookmark_filled else R.drawable.ic_bookmark_outline)

        fun animatePop(button: ImageButton) {
            button.animate().scaleX(1.2f).scaleY(1.2f).setDuration(100).withEndAction {
                button.animate().scaleX(1f).scaleY(1f).setDuration(100).start()
            }.start()
        }

        btnLike.setOnClickListener {
            isLiked = !isLiked
            btnLike.setImageResource(if (isLiked) R.drawable.ic_heart_filled else R.drawable.ic_heart_outline)
            animatePop(btnLike)

            // Сохраняем состояние
            prefs.edit().putBoolean("${movieId}_liked", isLiked).apply()
        }

        btnSave.setOnClickListener {
            isSaved = !isSaved
            btnSave.setImageResource(if (isSaved) R.drawable.ic_bookmark_filled else R.drawable.ic_bookmark_outline)
            animatePop(btnSave)

            // Сохраняем состояние
            prefs.edit().putBoolean("${movieId}_saved", isSaved).apply()
        }

        viewModel.movieDetail.observe(viewLifecycleOwner) { movie ->
            title.text = movie.title
            year.text = movie.year
            runtime.text = movie.runtime
            genre.text = movie.genre
            director.text = movie.director
            actors.text = movie.actors
            country.text = movie.country
            plot.text = movie.plot
            released.text = movie.released

            Glide.with(this)
                .load(movie.poster)
                .into(poster)
        }

        viewModel.loadMovieDetail(movieId)
    }
}

