package id.ypran.search.datasource.local

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import id.ypran.search.datasource.local.entity.SearchItemEntity

@Dao
interface SearchCatalogueDao {
    @Query("SELECT * FROM search_item_entities WHERE title LIKE :query OR name LIKE :query ORDER BY timeStamp asc")
    fun searchCatalogueByTitle(query: String): PagingSource<Int, SearchItemEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllSearchResult(searchResult: List<SearchItemEntity>)

    @Query("DELETE FROM search_item_entities")
    suspend fun clearSearch()
}