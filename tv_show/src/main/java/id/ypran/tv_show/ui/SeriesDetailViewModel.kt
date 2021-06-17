package id.ypran.tv_show.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.ypran.core.series.domain.model.Series

class SeriesDetailViewModel : ViewModel() {
    private val _series = MutableLiveData<Series>()
    val series: LiveData<Series>
        get() = _series

    fun setSeries(series: Series) = series.also { _series.value = it }
}