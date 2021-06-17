package id.ypran.moviecataloguejetpack.ui.home

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.ypran.moviecataloguejetpack.R
import id.ypran.moviecataloguejetpack.databinding.ActivityHomeBinding
import id.ypran.moviecataloguejetpack.ui.contentView
import id.ypran.moviecataloguejetpack.ui.util.ConnectivityTracker
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class HomeActivity : AppCompatActivity() {
    private val homeViewModel: HomeViewModel by viewModel()
    private val binding: ActivityHomeBinding by contentView(R.layout.activity_home)
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_MovieCatalogueJetpack)
        super.onCreate(savedInstanceState)
        binding.navHostFragment
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val connectivityTracker: ConnectivityTracker by inject {
            parametersOf(connectivityManager)
        }

        with(connectivityTracker) {
            lifecycle.addObserver(this)
            isConnected.observe(this@HomeActivity, {
                homeViewModel.setConnectivity(it)
            })
        }
    }
}