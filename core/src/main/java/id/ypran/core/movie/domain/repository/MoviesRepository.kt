package id.ypran.core.movie.domain.repository

import androidx.paging.PagingData
import id.ypran.core.movie.domain.model.Movie
import kotlinx.coroutines.flow.Flow

interface MoviesRepository {
    fun getUpcomingMovies(): Flow<PagingData<Movie>>
}