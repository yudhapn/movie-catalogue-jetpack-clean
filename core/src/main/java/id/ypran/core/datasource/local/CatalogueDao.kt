package id.ypran.core.datasource.local

import androidx.annotation.VisibleForTesting
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.ypran.core.datasource.local.entity.MovieEntity
import id.ypran.core.datasource.local.entity.SeriesEntity

@Dao
interface CatalogueDao {
    @Query("SELECT * FROM movie_entities ORDER BY timeStamp asc")
    fun getUpcomingMovies(): PagingSource<Int, MovieEntity>

    @VisibleForTesting
    @Query("SELECT * FROM movie_entities ORDER BY timeStamp asc")
    fun getUpcomingMoviesTest(): List<MovieEntity>

    @Query("SELECT * FROM series_entities ORDER BY timeStamp asc")
    fun getTopRatedSeries(): PagingSource<Int, SeriesEntity>

    @VisibleForTesting
    @Query("SELECT * FROM series_entities ORDER BY timeStamp asc")
    fun getTopRatedSeriesTest(): List<SeriesEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<MovieEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSeries(series: List<SeriesEntity>)

    @Query("DELETE FROM movie_entities")
    suspend fun clearMovies()

    @Query("DELETE FROM series_entities")
    suspend fun clearSeries()
}