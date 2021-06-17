package id.ypran.search.datasource.remote.response

import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import id.ypran.search.datasource.local.entity.SearchItemEntity

data class SearchListResponse(
    @SerializedName("results")
    val results: List<SearchItemResponse> = emptyList(),
    @SerializedName("page")
    val page: Int = 0

)

data class SearchItemResponse(
    @PrimaryKey
    @SerializedName("id")
    val id: Int,
    @SerializedName("title")
    val title: String?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("media_type")
    val mediaType: String,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("overview")
    val overview: String?
)

fun SearchItemResponse.mapToEntity(): SearchItemEntity =
    SearchItemEntity(
        id,
        title ?: "",
        name ?: "",
        mediaType,
        posterPath ?: "",
        backdropPath ?: "",
        voteAverage,
        overview ?: ""
    )

fun List<SearchItemResponse>.mapToEntity(): List<SearchItemEntity> =
    map { it.mapToEntity() }

