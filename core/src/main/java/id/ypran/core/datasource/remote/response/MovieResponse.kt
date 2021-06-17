package id.ypran.core.datasource.remote.response

import com.google.gson.annotations.SerializedName
import id.ypran.core.datasource.local.entity.MovieEntity

data class MovieListResponse(
    @SerializedName("results")
    val results: List<MovieResponse> = emptyList(),
    @SerializedName("page")
    val page: Int = 0
)

data class MovieResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("overview")
    val overview: String
)

fun MovieResponse.mapToEntity(): MovieEntity = MovieEntity(
    id,
    title,
    posterPath ?: "",
    backdropPath ?: "",
    releaseDate,
    voteAverage,
    overview
)

fun List<MovieResponse>.mapToEntity(): List<MovieEntity> = map { it.mapToEntity() }
