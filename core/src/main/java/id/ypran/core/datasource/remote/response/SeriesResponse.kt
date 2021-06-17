package id.ypran.core.datasource.remote.response

import com.google.gson.annotations.SerializedName
import id.ypran.core.datasource.local.entity.SeriesEntity

data class SeriesListResponse(
    @SerializedName("results")
    val results: List<SeriesResponse> = emptyList(),
    @SerializedName("page")
    val page: Int = 0
)

data class SeriesResponse(
    @SerializedName("id")
    val id: Int,
    @SerializedName("name")
    val name: String,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("first_air_date")
    val firstAirDate: String,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("overview")
    val overview: String
)

fun SeriesResponse.mapToEntity(): SeriesEntity = SeriesEntity(
    id,
    name,
    posterPath ?: "",
    backdropPath ?: "",
    firstAirDate,
    voteAverage,
    overview
)

fun List<SeriesResponse>.mapToEntity(): List<SeriesEntity> = map { it.mapToEntity() }
