package id.ypran.search.datasource.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import id.ypran.search.datasource.local.SearchCatalogueDb
import id.ypran.search.datasource.local.entity.SearchItemEntity
import id.ypran.search.datasource.local.entity.SearchRemoteKeysEntity
import id.ypran.search.datasource.remote.SearchApi
import id.ypran.search.datasource.remote.response.mapToEntity
import retrofit2.HttpException
import java.io.IOException

private const val SEARCH_STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class SearchRemoteMediator(
    private val searchApi: SearchApi,
    private val searchCatalogueDb: SearchCatalogueDb,
    private val key: String,
    private val query: String,
) : RemoteMediator<Int, SearchItemEntity>() {
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, SearchItemEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.APPEND -> {
                val searchRemoteKeys = getSearchRemoteKeyForLastItem(state)
                val nextKey = searchRemoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = searchRemoteKeys != null)
                nextKey
            }
            LoadType.PREPEND -> {
                val searchRemoteKeys = getSearchRemoteKeyForFirstItem(state)
                val prevKey = searchRemoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = searchRemoteKeys != null)
                prevKey
            }
            LoadType.REFRESH -> {
                val searchRemoteKeys = getSearchRemoteKeyClosestToCurrentPosition(state)
                searchRemoteKeys?.nextKey?.minus(1) ?: SEARCH_STARTING_PAGE_INDEX
            }
        }

        return try {
            val response = searchApi.getSearchResult(query, key, page)
            val searchResult = response.results.mapToEntity()
            val isEndOfPagination = searchResult.isEmpty()

            searchCatalogueDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    searchCatalogueDb.searchCatalogueDao.clearSearch()
                    searchCatalogueDb.searchRemoteKeysDao.clearSearchRemoteKeys()
                }
                val prevKey = if (page == SEARCH_STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (isEndOfPagination) null else page + 1
                val keys = searchResult.map {
                    SearchRemoteKeysEntity(
                        searchId = it.id,
                        prevKey = prevKey,
                        nextKey = nextKey
                    )
                }
                searchCatalogueDb.searchCatalogueDao.insertAllSearchResult(searchResult)
                searchCatalogueDb.searchRemoteKeysDao.insertSearchRemoteKeys(keys)
            }
            MediatorResult.Success(endOfPaginationReached = isEndOfPagination)
        } catch (exception: IOException) {
            MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            MediatorResult.Error(exception)
        }
    }

    private suspend fun getSearchRemoteKeyForLastItem(state: PagingState<Int, SearchItemEntity>): SearchRemoteKeysEntity? {
        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { searchItem ->
            searchCatalogueDb.searchRemoteKeysDao.remoteKeysSearchId(searchItem.id)
        }
    }

    private suspend fun getSearchRemoteKeyForFirstItem(state: PagingState<Int, SearchItemEntity>): SearchRemoteKeysEntity? {
        return state.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { searchItem ->
            searchCatalogueDb.searchRemoteKeysDao.remoteKeysSearchId(searchItem.id)
        }
    }

    private suspend fun getSearchRemoteKeyClosestToCurrentPosition(state: PagingState<Int, SearchItemEntity>): SearchRemoteKeysEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { searchItemId ->
                searchCatalogueDb.searchRemoteKeysDao.remoteKeysSearchId(searchItemId)
            }
        }
    }
}