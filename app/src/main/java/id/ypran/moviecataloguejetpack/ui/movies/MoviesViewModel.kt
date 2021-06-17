package id.ypran.moviecataloguejetpack.ui.movies

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import id.ypran.core.movie.domain.model.Movie
import id.ypran.core.movie.domain.usecase.GetMoviesUseCase
import kotlinx.coroutines.flow.Flow

class MoviesViewModel(private val getMoviesUseCase: GetMoviesUseCase) : ViewModel() {
    var upcomingMovies: Flow<PagingData<Movie>>? = null

    init {
        getMovies()
    }

    private fun getMovies() {
        upcomingMovies = getMoviesUseCase.execute()
    }
}
