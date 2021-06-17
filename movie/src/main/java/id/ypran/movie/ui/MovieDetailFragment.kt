package id.ypran.movie.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialContainerTransform
import id.ypran.core.movie.domain.model.Movie
import id.ypran.movie.databinding.FragmentMovieDetailBinding
import id.ypran.movie.di.injectFeature
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.LazyThreadSafetyMode.NONE
import id.ypran.moviecataloguejetpack.R as appResource

class MovieDetailFragment : Fragment() {
    private lateinit var binding: FragmentMovieDetailBinding
    private val args: MovieDetailFragmentArgs by navArgs()
    private val movie: Movie by lazy(NONE) { args.movie }
    private val movieDetailViewModel: MovieDetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = appResource.id.nav_host_fragment
            duration = resources.getInteger(appResource.integer.motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT
        }
        injectFeature()
        movieDetailViewModel.setMovie(movie)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMovieDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        with(binding) {
            lifecycleOwner = this@MovieDetailFragment
            viewModel = movieDetailViewModel
        }
    }
}