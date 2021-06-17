package id.ypran.movie.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.ypran.core.movie.domain.model.Movie

class MovieDetailViewModel : ViewModel() {
    private val _movie = MutableLiveData<Movie>()
    val movie: LiveData<Movie>
        get() = _movie

    fun setMovie(movie: Movie) = movie.also { _movie.value = it }
}