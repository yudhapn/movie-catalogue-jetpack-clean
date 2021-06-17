package id.ypran.core.datasource.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.ypran.core.datasource.local.entity.MovieRemoteKeysEntity

@Dao
interface MovieRemoteKeysDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovieRemoteKeys(movieKeyEntities: List<MovieRemoteKeysEntity>)

    @Query("SELECT * FROM movie_remote_keys_entities WHERE movieId = :movieId")
    suspend fun remoteKeysMovieId(movieId: Int): MovieRemoteKeysEntity?

    @Query("DELETE FROM movie_remote_keys_entities")
    suspend fun clearMovieRemoteKeys()
}