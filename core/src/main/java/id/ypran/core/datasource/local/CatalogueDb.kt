package id.ypran.core.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import id.ypran.core.datasource.local.entity.*

@Database(
    entities = [MovieEntity::class, SeriesEntity::class, MovieRemoteKeysEntity::class, SeriesRemoteKeysEntity::class],
    version = 2,
    exportSchema = false
)
abstract class CatalogueDb : RoomDatabase() {
    abstract val catalogueDao: CatalogueDao
    abstract val movieRemoteKeysDao: MovieRemoteKeysDao
    abstract val seriesRemoteKeysDao: SeriesRemoteKeysDao
}