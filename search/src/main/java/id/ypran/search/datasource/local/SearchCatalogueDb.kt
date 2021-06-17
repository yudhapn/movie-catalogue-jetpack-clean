package id.ypran.search.datasource.local

import androidx.room.Database
import androidx.room.RoomDatabase
import id.ypran.search.datasource.local.entity.SearchItemEntity
import id.ypran.search.datasource.local.entity.SearchRemoteKeysDao
import id.ypran.search.datasource.local.entity.SearchRemoteKeysEntity

@Database(
    entities = [SearchItemEntity::class, SearchRemoteKeysEntity::class],
    version = 1,
    exportSchema = false
)
abstract class SearchCatalogueDb : RoomDatabase() {
    abstract val searchCatalogueDao: SearchCatalogueDao
    abstract val searchRemoteKeysDao: SearchRemoteKeysDao
}