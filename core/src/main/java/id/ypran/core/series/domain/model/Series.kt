package id.ypran.core.series.domain.model

import android.os.Parcelable
import androidx.recyclerview.widget.DiffUtil
import kotlinx.parcelize.Parcelize

@Parcelize
data class Series(
    val id: Int,
    val name: String,
    val posterPath: String,
    val backdropPath: String,
    val firstAirDate: String,
    val voteAverage: Double,
    val overview: String
) : Parcelable {
    var yearFirstAirDate = firstAirDate.take(4)
}

object SeriesDiffCallback : DiffUtil.ItemCallback<Series>() {
    override fun areItemsTheSame(
        oldItem: Series,
        newItem: Series
    ): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: Series,
        newItem: Series
    ): Boolean =
        oldItem == newItem
}