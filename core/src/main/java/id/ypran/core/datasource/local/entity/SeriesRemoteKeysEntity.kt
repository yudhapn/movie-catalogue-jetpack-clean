package id.ypran.core.datasource.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "series_remote_keys_entities")
data class SeriesRemoteKeysEntity(
    @PrimaryKey
    val seriesId: Int,
    val prevKey: Int?,
    val nextKey: Int?,
)