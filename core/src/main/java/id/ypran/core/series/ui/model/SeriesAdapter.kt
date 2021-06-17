package id.ypran.core.series.ui.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import id.ypran.core.databinding.ItemsSeriesBinding
import id.ypran.core.series.domain.model.Series
import id.ypran.core.series.domain.model.SeriesDiffCallback
import id.ypran.core.series.ui.model.SeriesAdapter.ViewHolder.Companion.from

class SeriesAdapter(
    private val listener: SeriesAdapterListener
) : PagingDataAdapter<Series, SeriesAdapter.ViewHolder>(SeriesDiffCallback) {
    interface SeriesAdapterListener {
        fun onSeriesClicked(cardView: View, series: Series)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        from(parent, listener)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    class ViewHolder private constructor(
        private val binding: ItemsSeriesBinding, private val listener: SeriesAdapterListener
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(series: Series) {
            binding.series = series
            binding.listener = listener
        }

        companion object {
            fun from(parent: ViewGroup, listener: SeriesAdapterListener) =
                ViewHolder(
                    ItemsSeriesBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    ),
                    listener
                )
        }
    }
}