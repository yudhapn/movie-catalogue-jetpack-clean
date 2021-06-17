package id.ypran.moviecataloguejetpack.ui.series

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import id.ypran.core.series.domain.model.Series
import id.ypran.core.series.domain.usecase.GetTopRatedSeriesUseCase
import kotlinx.coroutines.flow.Flow

class SeriesViewModel(private val getTopRatedSeriesUseCase: GetTopRatedSeriesUseCase) :
    ViewModel() {
    var topRatedSeries: Flow<PagingData<Series>>? = null

    init {
        getSeries()
    }

    private fun getSeries() {
        topRatedSeries = getTopRatedSeriesUseCase.execute()
    }
}