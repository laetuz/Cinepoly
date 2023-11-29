package com.neotica.cinepoly.ui.fragment.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.neotica.cinepoly.databinding.ItemMovieBinding
import com.neotica.core.data.remote.model.MovieModel

class MovieAdapter(private var listMovies: List<MovieModel>, private val listener: MoviesListener) :
    RecyclerView.Adapter<MovieAdapter.MoviesViewHolder>() {

    class MoviesViewHolder(val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root)

    interface MoviesListener {
        fun onMovieClick(movie: MovieModel)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MoviesViewHolder =
        MoviesViewHolder(ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int {
        return listMovies.size
    }

    override fun onBindViewHolder(holder: MoviesViewHolder, position: Int) {
        val context = holder.itemView.context
        holder.binding.apply {
            val itemPosition = listMovies[position]

            tvTitle.text = itemPosition.title
            Glide
                .with(context)
                .load("https://www.themoviedb.org/t/p/w600_and_h900_bestv2${itemPosition.posterPath}")
                .into(ivPoster)
            cvMovieChildren.setOnClickListener {
                listener.onMovieClick(itemPosition)
            }
        }
    }
}