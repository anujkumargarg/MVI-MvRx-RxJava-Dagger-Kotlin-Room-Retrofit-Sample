package com.tmdbclient.mvi.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import com.tmdbclient.mvi.R
import com.tmdbclient.mvi.model.Movie
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_view_popular_movie.*
import javax.inject.Inject

class PopularMoviesAdapter @Inject constructor() :
    RecyclerView.Adapter<PopularMoviesAdapter.PopularMoviesHolder>() {
    private var moviesList: List<Movie> = mutableListOf()

    fun updateMovieList(newList: List<Movie>) {
        val callback = PopularMoviesDiffCallback(newList, moviesList)
        DiffUtil.calculateDiff(callback).dispatchUpdatesTo(this)
        moviesList = newList
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMoviesHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_view_popular_movie, parent, false)
        return PopularMoviesHolder(view)
    }

    override fun getItemCount(): Int = moviesList.size

    override fun onBindViewHolder(holder: PopularMoviesHolder, position: Int) {
        holder.setup(moviesList[position])
    }

    class PopularMoviesDiffCallback(
        private val newList: List<Movie>,
        private val oldList: List<Movie>
    ) : DiffUtil.Callback() {
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            newList[newItemPosition].id == oldList[oldItemPosition].id

        override fun getOldListSize() = oldList.size

        override fun getNewListSize() = newList.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            newList[newItemPosition] == oldList[oldItemPosition]
    }

    class PopularMoviesHolder(override val containerView: View) :
        RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun setup(movie: Movie) {
            movieNameTextView.text = movie.title
            Glide.with(containerView.context)
                .load(POSTER_PATH_BASE_URL + movie.posterPath)
                .centerCrop()
                .placeholder(R.drawable.ic_query_builder_black_24dp)
                .into(moviePosterImageView)
            voteAverageTextView.text = movie.voteAverage.toString()
        }
    }

    companion object {
        const val POSTER_PATH_BASE_URL = "https://image.tmdb.org/t/p/w185/"
    }
}