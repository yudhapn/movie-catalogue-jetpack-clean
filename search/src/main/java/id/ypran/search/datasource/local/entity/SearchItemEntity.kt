package id.ypran.search.datasource.local.entity

import androidx.paging.PagingData
import androidx.paging.map
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import id.ypran.search.domain.model.SearchItem

@Entity(tableName = "search_item_entities")
data class SearchItemEntity(
    @PrimaryKey
    @field:SerializedName("id")
    val id: Int,
    @field:SerializedName("title")
    val title: String,
    @field:SerializedName("name")
    val name: String,
    @field:SerializedName("media_type")
    val mediaType: String,
    @field:SerializedName("poster_path")
    val posterPath: String,
    @field:SerializedName("backdrop_path")
    val backdropPath: String,
    @field:SerializedName("vote_average")
    val voteAverage: Double,
    @field:SerializedName("overview")
    val overview: String,
    // this field is used for ordering the movie list from room by ascending
    @field:SerializedName("time_stamp")
    val timeStamp: Long = System.currentTimeMillis()
)

fun SearchItemEntity.mapToDomain(): SearchItem =
    SearchItem(id, title, name, mediaType, posterPath, backdropPath, voteAverage, overview)

fun PagingData<SearchItemEntity>.mapToDomain(): PagingData<SearchItem> =
    map { it.mapToDomain() }
