package id.ypran.search.domain.model

import androidx.recyclerview.widget.DiffUtil

data class SearchItem(
    val id: Int,
    val title: String,
    val name: String,
    val mediaType: String,
    val posterPath: String,
    val backdropPath: String,
    val voteAverage: Double,
    val overview: String,
)

object SearchItemDiffCallback : DiffUtil.ItemCallback<SearchItem>() {
    override fun areItemsTheSame(
        oldItem: SearchItem,
        newItem: SearchItem
    ): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: SearchItem,
        newItem: SearchItem
    ): Boolean =
        oldItem == newItem
}