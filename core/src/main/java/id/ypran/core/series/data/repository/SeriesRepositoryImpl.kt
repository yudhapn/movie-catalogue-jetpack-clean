package id.ypran.core.series.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import id.ypran.core.datasource.local.CatalogueDb
import id.ypran.core.datasource.local.entity.mapToDomain
import id.ypran.core.datasource.mediator.SeriesRemoteMediator
import id.ypran.core.series.domain.model.Series
import id.ypran.core.series.domain.repository.SeriesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SeriesRepositoryImpl(
    private val seriesRemoteMediator: SeriesRemoteMediator,
    private val catalogueDb: CatalogueDb
) : SeriesRepository {
    companion object {
        const val NETWORK_PAGE_SIZE = 8
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getTopRatedSeriesUseCase(): Flow<PagingData<Series>> =
        Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = seriesRemoteMediator,
            pagingSourceFactory = { catalogueDb.catalogueDao.getTopRatedSeries() }
        ).flow
            .map {
                it.mapToDomain()
            }
}