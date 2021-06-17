package id.ypran.core.movie.ui.model

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import id.ypran.core.databinding.ItemsMoviesLoadStateFooterBinding
import id.ypran.core.movie.ui.model.MoviesLoadStateAdapter.ViewHolder

class MoviesLoadStateAdapter(private val retry: () -> Unit) :
    LoadStateAdapter<ViewHolder>() {
    override fun onBindViewHolder(
        holder: ViewHolder,
        loadState: LoadState
    ) = holder.bind(loadState)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): ViewHolder = ViewHolder.from(parent, retry)

    class ViewHolder(
        private val binding: ItemsMoviesLoadStateFooterBinding,
        retry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryButton.setOnClickListener { retry.invoke() }
        }

        fun bind(loadState: LoadState) {
            with(binding) {
                moviesProgressBar.isVisible = loadState is LoadState.Loading
                retryButton.isVisible = loadState is LoadState.Error
            }
        }

        companion object {
            fun from(parent: ViewGroup, retry: () -> Unit) =
                ViewHolder(
                    ItemsMoviesLoadStateFooterBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    ),
                    retry
                )
        }
    }
}