package id.ypran.moviecataloguejetpack.ui.util

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.util.Log
import androidx.lifecycle.*

class ConnectivityTracker(
    private val connectivityManager: ConnectivityManager
) : LifecycleObserver {
    private var isMonitoring = false
    private val _isConnected = MutableLiveData<Boolean>()
    val isConnected: LiveData<Boolean>
        get() = _isConnected

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            _isConnected.postValue(true)
            connectivityManager.unregisterNetworkCallback(this)
            isMonitoring = false
        }

        override fun onLost(network: Network) {
            _isConnected.postValue(false)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
    fun startMonitoring() {
        Log.d("ConnectivityTracker", "startMonitoring run")
        val activeNetworkInfo = connectivityManager.activeNetworkInfo
        val connected = activeNetworkInfo != null && activeNetworkInfo.isConnected
        _isConnected.postValue(connected)
        if (!connected) {
            Log.d("ConnectivityTracker", "there is no connection")
            connectivityManager.registerNetworkCallback(
                NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build(),
                networkCallback
            )
            isMonitoring = true
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
    fun stopMonitoring() {
        Log.d("ConnectivityTracker", "stopMonitoring run")
        if (isMonitoring) {
            Log.d("ConnectivityTracker", "There is connection")
            connectivityManager.unregisterNetworkCallback(networkCallback)
            isMonitoring = false
        }

    }
}