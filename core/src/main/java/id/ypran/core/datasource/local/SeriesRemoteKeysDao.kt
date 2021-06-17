package id.ypran.core.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.ypran.core.datasource.local.entity.SeriesRemoteKeysEntity

@Dao
interface SeriesRemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeriesRemoteKeys(seriesKeyEntities: List<SeriesRemoteKeysEntity>)

    @Query("SELECT * FROM series_remote_keys_entities WHERE seriesId = :seriesId")
    suspend fun remoteKeysSeriesId(seriesId: Int): SeriesRemoteKeysEntity?

    @Query("DELETE FROM series_remote_keys_entities")
    suspend fun clearSeriesRemoteKeys()

}