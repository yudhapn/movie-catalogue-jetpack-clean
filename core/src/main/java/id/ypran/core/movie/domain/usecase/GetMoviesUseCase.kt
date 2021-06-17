package id.ypran.core.movie.domain.usecase

import id.ypran.core.movie.domain.repository.MoviesRepository

class GetMoviesUseCase(private val repository: MoviesRepository) {
    fun execute() = repository.getUpcomingMovies()
}