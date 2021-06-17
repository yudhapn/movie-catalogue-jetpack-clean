package id.ypran.core.movie.ui.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import id.ypran.core.databinding.ItemsMoviesBinding
import id.ypran.core.movie.domain.model.Movie
import id.ypran.core.movie.domain.model.MovieDiffCallback
import id.ypran.core.movie.ui.model.MoviesAdapter.ViewHolder
import id.ypran.core.movie.ui.model.MoviesAdapter.ViewHolder.Companion.from

class MoviesAdapter(
    private val listener: MoviesAdapterListener
) : PagingDataAdapter<Movie, ViewHolder>(MovieDiffCallback) {
    interface MoviesAdapterListener {
        fun onMovieClicked(cardView: View, movie: Movie)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        from(parent, listener)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    class ViewHolder private constructor(
        private val binding: ItemsMoviesBinding,
        private val listener: MoviesAdapterListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.movie = movie
            binding.listener = listener
        }

        companion object {
            fun from(parent: ViewGroup, listener: MoviesAdapterListener) =
                ViewHolder(
                    ItemsMoviesBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    ),
                    listener
                )
        }
    }
}