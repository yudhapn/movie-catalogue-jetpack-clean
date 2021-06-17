package id.ypran.core.movie.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import id.ypran.core.datasource.local.CatalogueDb
import id.ypran.core.datasource.local.entity.mapToDomain
import id.ypran.core.datasource.mediator.MoviesRemoteMediator
import id.ypran.core.movie.domain.model.Movie
import id.ypran.core.movie.domain.repository.MoviesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class MoviesRepositoryImpl(
    private val moviesRemoteMediator: MoviesRemoteMediator,
    private val catalogueDb: CatalogueDb
) : MoviesRepository {
    companion object {
        const val NETWORK_PAGE_SIZE = 8
    }

    @OptIn(ExperimentalPagingApi::class)
    override fun getUpcomingMovies(): Flow<PagingData<Movie>> =
        Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator = moviesRemoteMediator,
            pagingSourceFactory = { catalogueDb.catalogueDao.getUpcomingMovies() }
        ).flow
            .map {
                it.mapToDomain()
            }
}