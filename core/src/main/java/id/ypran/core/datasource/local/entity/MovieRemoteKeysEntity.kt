package id.ypran.core.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movie_remote_keys_entities")
data class MovieRemoteKeysEntity(
    @PrimaryKey
    val movieId: Int,
    val prevKey: Int?,
    val nextKey: Int?,
)