package id.ypran.moviecataloguejetpack.ui.series

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.google.android.material.transition.MaterialElevationScale
import id.ypran.core.series.domain.model.Series
import id.ypran.core.series.ui.model.SeriesAdapter
import id.ypran.core.series.ui.model.SeriesAdapter.SeriesAdapterListener
import id.ypran.core.series.ui.model.SeriesLoadStateAdapter
import id.ypran.moviecataloguejetpack.R
import id.ypran.moviecataloguejetpack.databinding.FragmentSeriesBinding
import id.ypran.moviecataloguejetpack.ui.home.HomeFragmentDirections
import id.ypran.moviecataloguejetpack.ui.home.HomeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SeriesFragment : Fragment(), SeriesAdapterListener {
    private lateinit var binding: FragmentSeriesBinding
    private val seriesViewModel: SeriesViewModel by viewModel()
    private val homeViewModel: HomeViewModel by sharedViewModel()
    private var job: Job? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSeriesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
    }

    private fun initAdapter() {
        val seriesAdapter = SeriesAdapter(this)
        binding.seriesRecyclerView.adapter =
            seriesAdapter.withLoadStateFooter(SeriesLoadStateAdapter { seriesAdapter.retry() })
        seriesAdapter.addLoadStateListener { loadState ->
            binding.seriesProgressBar.isVisible = loadState.mediator?.refresh is LoadState.Loading
            val errorState =
                loadState.mediator?.append as? LoadState.Error
                    ?: loadState.mediator?.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error

            errorState?.let {
                Toast.makeText(requireContext(), it.error.message, Toast.LENGTH_LONG).show()
            }
        }
        job?.cancel()
        job = lifecycleScope.launch {
            homeViewModel.topRatedSeries?.collectLatest {
                seriesAdapter.submitData(lifecycle, it)
            }
        }
//        homeViewModel.isConnected.observe(viewLifecycleOwner, {
//            if (it) {
//                if (seriesAdapter.itemCount == 0) {
//                    seriesAdapter.refresh()
//                }
//            } else {
//                Toast.makeText(requireContext(), "You\'re disconnected", Toast.LENGTH_LONG)
//                    .show()
//            }
//        })
    }

    override fun onSeriesClicked(cardView: View, series: Series) {
        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }

        val movieCardDetailTransitionName = getString(R.string.series_detail_card_transition_name)
        val extras = FragmentNavigatorExtras(cardView to movieCardDetailTransitionName)
        val directions = HomeFragmentDirections.actionHomeFragmentToSeriesDetailFragment(series)
        findNavController().navigate(directions, extras)

    }
}