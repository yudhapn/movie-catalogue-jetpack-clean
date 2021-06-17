package id.ypran.moviecataloguejetpack.ui.movies

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
import id.ypran.core.movie.domain.model.Movie
import id.ypran.core.movie.ui.model.MoviesAdapter
import id.ypran.core.movie.ui.model.MoviesAdapter.MoviesAdapterListener
import id.ypran.core.movie.ui.model.MoviesLoadStateAdapter
import id.ypran.moviecataloguejetpack.R
import id.ypran.moviecataloguejetpack.databinding.FragmentMoviesBinding
import id.ypran.moviecataloguejetpack.ui.home.HomeFragmentDirections
import id.ypran.moviecataloguejetpack.ui.home.HomeViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MoviesFragment : Fragment(), MoviesAdapterListener {
    private lateinit var binding: FragmentMoviesBinding
    private val moviesViewModel: MoviesViewModel by viewModel()
    private val homeViewModel: HomeViewModel by sharedViewModel()
    private var job: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMoviesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapter()
    }

    private fun initAdapter() {
        val moviesAdapter = MoviesAdapter(this)
        binding.moviesRecyclerView.adapter =
            moviesAdapter.withLoadStateFooter(MoviesLoadStateAdapter { moviesAdapter.retry() })
        moviesAdapter.addLoadStateListener { loadState ->
            binding.moviesProgressBar.isVisible = loadState.mediator?.refresh is LoadState.Loading
            val errorState =
                loadState.mediator?.append as? LoadState.Error
                    ?: loadState.mediator?.prepend as? LoadState.Error
                    ?: loadState.append as? LoadState.Error
                    ?: loadState.prepend as? LoadState.Error

            errorState?.let {
                Toast.makeText(requireContext(), it.error.message, Toast.LENGTH_LONG).show()
            }
        }

        lifecycleScope.launch {
            homeViewModel.upcomingMovies?.collectLatest {
                moviesAdapter.submitData(lifecycle, it)
            }
        }
//        homeViewModel.isConnected.observe(viewLifecycleOwner, {
////            if (it) {
////                if (moviesAdapter.itemCount == 0) {
////                    moviesAdapter.refresh()
////                }
////            } else {
////                Toast.makeText(requireContext(), "You\'re disconnected", Toast.LENGTH_LONG)
////                    .show()
////            }
////        })
    }

    override fun onMovieClicked(cardView: View, movie: Movie) {
        exitTransition = MaterialElevationScale(false).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
        reenterTransition = MaterialElevationScale(true).apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }

        val movieCardDetailTransitionName = getString(R.string.movie_detail_card_transition_name)
        val extras = FragmentNavigatorExtras(cardView to movieCardDetailTransitionName)
        val directions = HomeFragmentDirections.actionHomeFragmentToMovieDetailFragment(movie)
        findNavController().navigate(directions, extras)
    }
}