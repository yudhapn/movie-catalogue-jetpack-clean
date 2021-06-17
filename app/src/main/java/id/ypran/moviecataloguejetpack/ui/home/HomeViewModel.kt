package id.ypran.moviecataloguejetpack.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import id.ypran.core.movie.domain.model.Movie
import id.ypran.core.movie.domain.usecase.GetMoviesUseCase
import id.ypran.core.series.domain.model.Series
import id.ypran.core.series.domain.usecase.GetTopRatedSeriesUseCase
import kotlinx.coroutines.flow.Flow

class HomeViewModel(
    private val getMoviesUseCase: GetMoviesUseCase,
    private val getTopRatedSeriesUseCase: GetTopRatedSeriesUseCase
) : ViewModel() {
    private val _isConnected = MutableLiveData<Boolean>()
    val isConnected: LiveData<Boolean>
        get() = _isConnected

    var upcomingMovies: Flow<PagingData<Movie>>? = null
    var topRatedSeries: Flow<PagingData<Series>>? = null

    init {
        getMovies()
        getSeries()
    }

    private fun getSeries() {
        topRatedSeries = getTopRatedSeriesUseCase.execute()
    }

    private fun getMovies() {
        upcomingMovies = getMoviesUseCase.execute()
    }

    fun setConnectivity(isConnected: Boolean) = _isConnected.postValue(isConnected)
}