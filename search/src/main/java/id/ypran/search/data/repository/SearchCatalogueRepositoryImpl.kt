package id.ypran.search.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import id.ypran.search.datasource.local.SearchCatalogueDb
import id.ypran.search.datasource.local.entity.mapToDomain
import id.ypran.search.datasource.mediator.SearchRemoteMediator
import id.ypran.search.domain.model.SearchItem
import id.ypran.search.domain.repository.SearchCatalogueRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.parameter.parametersOf


class SearchCatalogueRepositoryImpl(
    private val searchCatalogueDb: SearchCatalogueDb
) : SearchCatalogueRepository, KoinComponent {
    companion object {
        const val NETWORK_PAGE_SIZE = 8
    }


    @OptIn(ExperimentalPagingApi::class)
    override fun searchCatalogueByTitle(query: String): Flow<PagingData<SearchItem>> {
        val remoteMediator: SearchRemoteMediator by inject { parametersOf(query) }
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = remoteMediator,
            pagingSourceFactory = {
                searchCatalogueDb.searchCatalogueDao.searchCatalogueByTitle(
                    "%$query%"
                )
            }
        ).flow
            .map {
                it.mapToDomain()
            }
    }
}
