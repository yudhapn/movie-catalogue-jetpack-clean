package id.ypran.search.domain.repository

import androidx.paging.PagingData
import id.ypran.search.domain.model.SearchItem
import kotlinx.coroutines.flow.Flow

interface SearchCatalogueRepository {
    fun searchCatalogueByTitle(query: String): Flow<PagingData<SearchItem>>
}