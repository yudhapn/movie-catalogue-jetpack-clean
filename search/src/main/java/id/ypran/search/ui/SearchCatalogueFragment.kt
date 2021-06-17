package id.ypran.search.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDirections
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import com.google.android.material.transition.MaterialSharedAxis
import id.ypran.core.movie.domain.model.Movie
import id.ypran.core.movie.ui.model.MoviesLoadStateAdapter
import id.ypran.core.series.domain.model.Series
import id.ypran.core.util.hideSoftKeyboard
import id.ypran.core.util.showSoftKeyboard
import id.ypran.moviecataloguejetpack.R
import id.ypran.search.databinding.FragmentSearchCatalogueBinding
import id.ypran.search.di.injectFeature
import id.ypran.search.domain.model.SearchItem
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchCatalogueFragment : Fragment(), SearchAdapter.SearchAdapterListener {
    private lateinit var binding: FragmentSearchCatalogueBinding
    private val searchViewModel: SearchCatalogueViewModel by viewModel()
    private var searchJob: Job? = null
    private val searchAdapter = SearchAdapter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
        injectFeature()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchCatalogueBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
        initView()
        initAdapter()
    }

    private fun initAdapter() {
        binding.searchRecyclerView.adapter =
            searchAdapter.withLoadStateFooter(MoviesLoadStateAdapter { searchAdapter.retry() })
        searchAdapter.addLoadStateListener { loadState ->
            binding.searchProgressBar.isVisible = loadState.mediator?.refresh is LoadState.Loading
            val errorState =
                loadState.mediator?.append as? LoadState.Error
                    ?: loadState.mediator?.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error

            errorState?.let {
                Toast.makeText(requireContext(), it.error.message, Toast.LENGTH_LONG).show()
            }
        }

    }

    private fun initView() {
        with(binding) {
//            searchEditText.postDelayed(Runnable {
            searchEditText.requestFocus()
//            }, 100)
            if (searchAdapter.itemCount == 0) {
                requireContext().showSoftKeyboard(searchEditText)
            }

            navigateUpButton.setOnClickListener { findNavController().navigateUp() }
            searchEditText.setOnEditorActionListener { _, actionId, _ ->
                var handled = false
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    val query = searchEditText.text.toString()
                    search(query)
                    requireContext().hideSoftKeyboard(requireActivity().currentFocus?.windowToken)
                    handled = true
                }
                handled
            }
        }
    }

    private fun search(query: String) {
        searchJob?.cancel()
        searchJob = lifecycleScope.launch {
            searchViewModel.searchCatalogue(query).collectLatest {
                searchAdapter.submitData(it)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        requireContext().hideSoftKeyboard(requireActivity().currentFocus?.windowToken)
    }

    override fun onSearchItemClicked(cardView: View, searchItem: SearchItem) {
        searchItem.let {
            val directions: NavDirections
            val detailTransitionName: String
            when (searchItem.mediaType) {
                "tv" -> {
                    directions =
                        SearchCatalogueFragmentDirections.actionSearchFragmentToSeriesDetailFragment(
                            Series(
                                it.id,
                                it.name,
                                it.posterPath,
                                it.backdropPath,
                                "",
                                it.voteAverage,
                                it.overview
                            )
                        )
                    detailTransitionName = getString(R.string.series_detail_card_transition_name)
                }
                else -> {
                    directions =
                        SearchCatalogueFragmentDirections.actionSearchFragmentToMovieDetailFragment(
                            Movie(
                                it.id,
                                it.title,
                                it.posterPath,
                                it.backdropPath,
                                "",
                                it.voteAverage,
                                it.overview
                            )
                        )
                    detailTransitionName = getString(R.string.movie_detail_card_transition_name)
                }
            }
            val extras = FragmentNavigatorExtras(cardView to detailTransitionName)
            findNavController().navigate(directions, extras)
        }
    }
}