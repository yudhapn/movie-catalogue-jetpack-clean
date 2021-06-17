package id.ypran.search.datasource.local.entity

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SearchRemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchRemoteKeys(searchKeyEntities: List<SearchRemoteKeysEntity>)

    @Query("SELECT * FROM search_remote_keys_entities WHERE searchId = :searchId")
    suspend fun remoteKeysSearchId(searchId: Int): SearchRemoteKeysEntity?

    @Query("DELETE FROM search_remote_keys_entities")
    suspend fun clearSearchRemoteKeys()
}