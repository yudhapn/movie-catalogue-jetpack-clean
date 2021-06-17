package id.ypran.core.datasource.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import id.ypran.core.datasource.local.CatalogueDb
import id.ypran.core.datasource.local.entity.MovieEntity
import id.ypran.core.datasource.local.entity.MovieRemoteKeysEntity
import id.ypran.core.datasource.remote.MovieApi
import id.ypran.core.datasource.remote.response.mapToEntity
import id.ypran.core.util.EspressoIdlingResource
import retrofit2.HttpException
import java.io.IOException

private const val MOVIE_STARTING_PAGE_INDEX = 1

@OptIn(ExperimentalPagingApi::class)
class MoviesRemoteMediator(
    private val movieApi: MovieApi,
    private val catalogueDb: CatalogueDb,
    private val key: String
) : RemoteMediator<Int, MovieEntity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, MovieEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.APPEND -> {
                val movieRemoteKeys = getMovieRemoteKeyForLastItem(state)
                val nextKey = movieRemoteKeys?.nextKey
                    ?: return MediatorResult.Success(endOfPaginationReached = movieRemoteKeys != null)
                nextKey
            }
            LoadType.PREPEND -> {
                val movieRemoteKeys = getMovieRemoteKeyForFirstItem(state)
                val prevKey = movieRemoteKeys?.prevKey
                    ?: return MediatorResult.Success(endOfPaginationReached = movieRemoteKeys != null)
                prevKey
            }
            LoadType.REFRESH -> {
                val movieRemoteKeys = getMovieRemoteKeyClosestToCurrentPosition(state)
                movieRemoteKeys?.nextKey?.minus(1) ?: MOVIE_STARTING_PAGE_INDEX
            }
        }

        return try {
            EspressoIdlingResource.increment()
            val response = movieApi.getUpcomingMovies(key, page)
            val movies = response.results.mapToEntity()
            val isEndOfPagination = movies.isEmpty()

            catalogueDb.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    catalogueDb.movieRemoteKeysDao.clearMovieRemoteKeys()
                    catalogueDb.catalogueDao.clearMovies()
                }
                val prevKey = if (page == MOVIE_STARTING_PAGE_INDEX) null else page - 1
                val nextKey = if (isEndOfPagination) null else page + 1
                val keys = movies.map {
                    MovieRemoteKeysEntity(movieId = it.id, prevKey = prevKey, nextKey = nextKey)
                }
                catalogueDb.catalogueDao.insertMovies(movies)
                catalogueDb.movieRemoteKeysDao.insertMovieRemoteKeys(keys)
            }
            EspressoIdlingResource.decrement()
            MediatorResult.Success(endOfPaginationReached = isEndOfPagination)
        } catch (exception: IOException) {
            MediatorResult.Error(exception)
        } catch (exception: HttpException) {
            MediatorResult.Error(exception)
        }
    }

    private suspend fun getMovieRemoteKeyForLastItem(state: PagingState<Int, MovieEntity>): MovieRemoteKeysEntity? {
        return state.pages.lastOrNull {
            it.data.isNotEmpty()
        }?.data?.lastOrNull()?.let { movie ->
            catalogueDb.movieRemoteKeysDao.remoteKeysMovieId(movie.id)
        }
    }

    private suspend fun getMovieRemoteKeyForFirstItem(state: PagingState<Int, MovieEntity>): MovieRemoteKeysEntity? {
        return state.pages.firstOrNull {
            it.data.isNotEmpty()
        }?.data?.firstOrNull()?.let { movie ->
            catalogueDb.movieRemoteKeysDao.remoteKeysMovieId(movie.id)
        }
    }

    private suspend fun getMovieRemoteKeyClosestToCurrentPosition(state: PagingState<Int, MovieEntity>): MovieRemoteKeysEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { movieId ->
                catalogueDb.movieRemoteKeysDao.remoteKeysMovieId(movieId)
            }
        }
    }
}