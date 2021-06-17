package id.ypran.tv_show.ui

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.google.android.material.transition.MaterialContainerTransform
import id.ypran.core.series.domain.model.Series
import id.ypran.tv_show.databinding.FragmentSeriesDetailBinding
import id.ypran.tv_show.di.injectFeature
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.LazyThreadSafetyMode.NONE
import id.ypran.moviecataloguejetpack.R as appResource

class SeriesDetailFragment : Fragment() {
    private lateinit var binding: FragmentSeriesDetailBinding
    private val args: SeriesDetailFragmentArgs by navArgs()
    private val series: Series by lazy(NONE) { args.series }
    private val seriesDetailViewModel: SeriesDetailViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = appResource.id.nav_host_fragment
            duration = resources.getInteger(appResource.integer.motion_duration_large).toLong()
            scrimColor = Color.TRANSPARENT
        }
        injectFeature()
        seriesDetailViewModel.setSeries(series)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSeriesDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        with(binding) {
            lifecycleOwner = this@SeriesDetailFragment
            viewModel = seriesDetailViewModel
        }
    }
}