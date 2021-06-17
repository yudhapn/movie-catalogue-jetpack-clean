package id.ypran.core.datasource.local.entity

import androidx.annotation.VisibleForTesting
import androidx.paging.PagingData
import androidx.paging.map
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import id.ypran.core.movie.domain.model.Movie

@Entity(tableName = "movie_entities")
data class MovieEntity(
    @PrimaryKey
    @field:SerializedName("id")
    val id: Int,
    @field:SerializedName("title")
    val title: String,
    @field:SerializedName("poster_path")
    val posterPath: String,
    @field:SerializedName("backdrop_path")
    val backdropPath: String,
    @field:SerializedName("release_date")
    val releaseDate: String,
    @field:SerializedName("vote_average")
    val voteAverage: Double,
    @field:SerializedName("overview")
    val overview: String,
    // this field is used for ordering the movie list from room by ascending
    @field:SerializedName("time_stamp")
    val timeStamp: Long = System.currentTimeMillis()
)

fun MovieEntity.mapToDomain(): Movie =
    Movie(id, title, posterPath, backdropPath, releaseDate, voteAverage, overview)

fun PagingData<MovieEntity>.mapToDomain(): PagingData<Movie> =
    map { it.mapToDomain() }

@VisibleForTesting
fun List<MovieEntity>.mapToDomain(): List<Movie> =
    map { it.mapToDomain() }
