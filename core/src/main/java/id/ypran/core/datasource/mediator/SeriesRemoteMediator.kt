package id.ypran.core.datasource.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import id.ypran.core.datasource.local.CatalogueDb
import id.ypran.core.datasource.local.entity.SeriesEntity
import id.ypran.core.datasource.local.entity.SeriesRemoteKeysEntity
import id.ypran.core.datasource.remote.SeriesApi
import id.ypran.core.datasource.remote.response.mapToEntity
import retrofit2.HttpException
import java.io.IOException

private const val SERIES_STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class SeriesRemoteMediator(
    private val seriesApi: SeriesApi,
    private val catalogueDb: CatalogueDb,
    private val key: String
) : RemoteMediator<Int, SeriesEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, SeriesEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.APPEND -> {
                val seriesRemoteKeys = getSeriesRemoteKeyForLastItem(state)
                val nextKey = seriesRemoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = seriesRemoteKeys != null)
                nextKey
            }
            LoadType.PREPEND -> {
                val seriesRemoteKeys = getSeriesRemoteKeyForFirstItem(state)
                val prevKey = seriesRemoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = seriesRemoteKeys != null)
                prevKey
            }
            LoadType.REFRESH -> {
                val seriesRemoteKeys = getSeriesRemoteKeyClosestToCurrentPosition(state)
                seriesRemoteKeys?.nextKey?.minus(1) ?: SERIES_STARTING_PAGE_INDEX
            }
        }

        return try {
            val response = seriesApi.getTopRatedSeries(key, page)
            val series = response.results.mapToEntity()
            val isEndOfPagination = series.isEmpty()

            catalogueDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    catalogueDb.seriesRemoteKeysDao.clearSeriesRemoteKeys()
                    catalogueDb.catalogueDao.clearSeries()
                }
                val prevKey = if (page == SERIES_STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (isEndOfPagination) null else page + 1
                val keys = series.map {
                    SeriesRemoteKeysEntity(seriesId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                catalogueDb.catalogueDao.insertSeries(series)
                catalogueDb.seriesRemoteKeysDao.insertSeriesRemoteKeys(keys)
            }
            MediatorResult.Success(endOfPaginationReached = isEndOfPagination)
        } catch (exception: IOException) {
            MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            MediatorResult.Error(exception)
        }
    }

    private suspend fun getSeriesRemoteKeyForLastItem(state: PagingState<Int, SeriesEntity>): SeriesRemoteKeysEntity? {
        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { series ->
            catalogueDb.seriesRemoteKeysDao.remoteKeysSeriesId(series.id)
        }
    }

    private suspend fun getSeriesRemoteKeyForFirstItem(state: PagingState<Int, SeriesEntity>): SeriesRemoteKeysEntity? {
        return state.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { series ->
            catalogueDb.seriesRemoteKeysDao.remoteKeysSeriesId(series.id)
        }
    }

    private suspend fun getSeriesRemoteKeyClosestToCurrentPosition(state: PagingState<Int, SeriesEntity>): SeriesRemoteKeysEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { seriesId ->
                catalogueDb.seriesRemoteKeysDao.remoteKeysSeriesId(seriesId)
            }
        }
    }
}