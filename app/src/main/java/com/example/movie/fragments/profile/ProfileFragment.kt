package com.example.movie.fragments.profile

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.movie.R
import com.google.android.material.button.MaterialButton

class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private lateinit var profileUsername: TextView
    private lateinit var likedCount: TextView
    private lateinit var savedCount: TextView
    private lateinit var likedMoviesRecyclerView: RecyclerView
    private lateinit var savedMoviesRecyclerView: RecyclerView
    private lateinit var noLikedMovies: TextView
    private lateinit var noSavedMovies: TextView
    private lateinit var btnEditProfile: MaterialButton

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileUsername = view.findViewById(R.id.profileUsername)
        likedCount = view.findViewById(R.id.likedCount)
        savedCount = view.findViewById(R.id.savedCount)
        likedMoviesRecyclerView = view.findViewById(R.id.likedMoviesRecyclerView)
        savedMoviesRecyclerView = view.findViewById(R.id.savedMoviesRecyclerView)
        noLikedMovies = view.findViewById(R.id.noLikedMovies)
        noSavedMovies = view.findViewById(R.id.noSavedMovies)
        btnEditProfile = view.findViewById(R.id.btnEditProfile)

        // Set username (you can get this from SharedPreferences or a database)
        profileUsername.text = "Leo Messi"

        // Setup RecyclerViews
        likedMoviesRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        savedMoviesRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        // Load liked and saved movies
        loadLikedMovies()
        loadSavedMovies()

        // Edit Profile button
        btnEditProfile.setOnClickListener {
            // TODO: Open edit profile dialog or screen
        }
    }

    private fun loadLikedMovies() {
        val prefs = requireContext().getSharedPreferences("movie_prefs", Context.MODE_PRIVATE)
        val allPrefs = prefs.all

        // Count liked movies
        val likedMovieIds = allPrefs.filter { it.key.endsWith("_liked") && it.value == true }
        val count = likedMovieIds.size

        likedCount.text = count.toString()

        if (count == 0) {
            noLikedMovies.visibility = View.VISIBLE
            likedMoviesRecyclerView.visibility = View.GONE
        } else {
            noLikedMovies.visibility = View.GONE
            likedMoviesRecyclerView.visibility = View.VISIBLE

            // TODO: Load actual movie data and set adapter
            // For now we just show the count
        }
    }

    private fun loadSavedMovies() {
        val prefs = requireContext().getSharedPreferences("movie_prefs", Context.MODE_PRIVATE)
        val allPrefs = prefs.all

        // Count saved movies
        val savedMovieIds = allPrefs.filter { it.key.endsWith("_saved") && it.value == true }
        val count = savedMovieIds.size

        savedCount.text = count.toString()

        if (count == 0) {
            noSavedMovies.visibility = View.VISIBLE
            savedMoviesRecyclerView.visibility = View.GONE
        } else {
            noSavedMovies.visibility = View.GONE
            savedMoviesRecyclerView.visibility = View.VISIBLE

            // TODO: Load actual movie data and set adapter
            // For now we just show the count
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh counts when returning to profile
        loadLikedMovies()
        loadSavedMovies()
    }
}