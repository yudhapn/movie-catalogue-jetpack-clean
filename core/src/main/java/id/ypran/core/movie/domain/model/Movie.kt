package id.ypran.core.movie.domain.model

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val id: Int,
    val title: String,
    val posterPath: String,
    val backdropPath: String,
    val releaseDate: String,
    val voteAverage: Double,
    val overview: String,
) : Parcelable {
    val yearReleaseDate: String = releaseDate.take(4)
}

object MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun areItemsTheSame(
        oldItem: Movie,
        newItem: Movie
    ): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: Movie,
        newItem: Movie
    ): Boolean =
        oldItem == newItem
}