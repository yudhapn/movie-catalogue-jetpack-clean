package id.ypran.core.datasource.local.entity

import androidx.annotation.VisibleForTesting
import androidx.paging.PagingData
import androidx.paging.map
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import id.ypran.core.series.domain.model.Series

@Entity(tableName = "series_entities")
data class SeriesEntity(
    @PrimaryKey
    @field:SerializedName("id")
    val id: Int,
    @field:SerializedName("name")
    val name: String,
    @field:SerializedName("poster_path")
    val posterPath: String,
    @field:SerializedName("backdrop_path")
    val backdropPath: String,
    @field:SerializedName("release_date")
    val firstAirDate: String,
    @field:SerializedName("vote_average")
    val voteAverage: Double,
    @field:SerializedName("overview")
    val overview: String,
    // this field is used for ordering the movie list from room by ascending
    @field:SerializedName("time_stamp")
    val timeStamp: Long = System.currentTimeMillis()

)

fun SeriesEntity.mapToDomain(): Series =
    Series(
        id,
        name,
        posterPath,
        backdropPath,
        firstAirDate,
        voteAverage,
        overview
    )

fun PagingData<SeriesEntity>.mapToDomain(): PagingData<Series> =
    map { it.mapToDomain() }

@VisibleForTesting
fun List<SeriesEntity>.mapToDomain(): List<Series> =
    map { it.mapToDomain() }
