package com.example.movie.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.movie.R
import com.example.movie.model.Movie

class Adapter(
    private val movies: List<Movie>,
    private val isTopList: Boolean,
    private val onItemClick: (Movie) -> Unit,
) : RecyclerView.Adapter<Adapter.MovieViewHolder>() {



    inner class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val poster: ImageView = view.findViewById(R.id.posterImageView)
        val title: TextView? = view.findViewById(R.id.titleTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val layoutId = if (isTopList) R.layout.top_item_layout else R.layout.item_layout
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return MovieViewHolder(view)
    }

    override fun getItemCount() = movies.size

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]

        Glide.with(holder.itemView.context)
            .load(movie.poster)
            .transform(CenterCrop(), RoundedCorners(30))
            .placeholder(Color.LTGRAY.toDrawable())
            .error(Color.DKGRAY.toDrawable())
            .into(holder.poster)


        holder.title?.let {
            it.visibility = View.VISIBLE
            it.text = movie.title
        }

        holder.itemView.setOnClickListener {
            onItemClick(movie)
        }

    }
}
