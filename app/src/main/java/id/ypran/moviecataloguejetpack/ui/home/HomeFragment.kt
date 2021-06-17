package id.ypran.moviecataloguejetpack.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import id.ypran.moviecataloguejetpack.R
import id.ypran.moviecataloguejetpack.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private val currentNavigationFragment: Fragment?
        get() = requireActivity().supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
            ?.childFragmentManager
            ?.fragments
            ?.first()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // TODO: Set up MaterialFadeThrough enterTransition.
        enterTransition = MaterialFadeThrough().apply {
            duration = resources.getInteger(R.integer.motion_duration_large).toLong()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }
        initView()
    }

    private fun initView() {
        val cataloguePagerAdapter = CataloguePager(requireContext(), childFragmentManager)
        with(binding) {
            cataloguePager.adapter = cataloguePagerAdapter
            catalogueTab.setupWithViewPager(cataloguePager)
            searchMenuButton.setOnClickListener { navigateToSearch() }
        }
    }

    private fun navigateToSearch() {
        currentNavigationFragment?.apply {
            exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true).apply {
                duration = resources.getInteger(R.integer.motion_duration_large).toLong()
            }
            reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false).apply {
                duration = resources.getInteger(R.integer.motion_duration_large).toLong()
            }
        }
        val directions = HomeFragmentDirections.actionHomeFragmentToSearchFragment()
        findNavController().navigate(directions)
    }

}