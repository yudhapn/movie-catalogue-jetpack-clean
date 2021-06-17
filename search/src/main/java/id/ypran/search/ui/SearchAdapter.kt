package id.ypran.search.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import id.ypran.search.databinding.ItemsSearchBinding
import id.ypran.search.domain.model.SearchItem
import id.ypran.search.domain.model.SearchItemDiffCallback
import id.ypran.search.ui.SearchAdapter.ViewHolder
import id.ypran.search.ui.SearchAdapter.ViewHolder.Companion.from

class SearchAdapter(
    private val listener: SearchAdapterListener
) : PagingDataAdapter<SearchItem, ViewHolder>(SearchItemDiffCallback) {
    interface SearchAdapterListener {
        fun onSearchItemClicked(cardView: View, searchItem: SearchItem)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        from(parent, listener)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    class ViewHolder private constructor(
        private val binding: ItemsSearchBinding,
        private val listener: SearchAdapterListener
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(searchItem: SearchItem) {
            binding.searchItem = searchItem
            binding.listener = listener
        }

        companion object {
            fun from(parent: ViewGroup, listener: SearchAdapterListener) =
                ViewHolder(
                    ItemsSearchBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    ),
                    listener
                )
        }
    }
}