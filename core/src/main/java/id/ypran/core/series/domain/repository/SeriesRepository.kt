package id.ypran.core.series.domain.repository

import androidx.paging.PagingData
import id.ypran.core.series.domain.model.Series
import kotlinx.coroutines.flow.Flow

interface SeriesRepository {
    fun getTopRatedSeriesUseCase(): Flow<PagingData<Series>>
}