package id.ypran.core.series.domain.usecase

import id.ypran.core.series.domain.repository.SeriesRepository

class GetTopRatedSeriesUseCase(private val repository: SeriesRepository) {
    fun execute() = repository.getTopRatedSeriesUseCase()
}