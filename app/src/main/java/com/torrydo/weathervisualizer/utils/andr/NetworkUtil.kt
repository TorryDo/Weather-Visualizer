package com.torrydo.weathervisualizer.utils.andr

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

object NetworkUtil {

    inline fun listenStatus(
        context: Context,
        crossinline onAvailable: (Network) -> Unit,
        crossinline onLost: (Network) -> Unit
    ) {
        val conMa = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val networkCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                onAvailable(network)
            }

            override fun onLost(network: Network) {
                onLost(network)
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            conMa.registerDefaultNetworkCallback(networkCallback)
        } else {
            val request = NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build()
            conMa.registerNetworkCallback(request, networkCallback)
        }

    }

}

class NetworkStateHolder(context: Context) {
    private val networkState: MutableStateFlow<Boolean> = MutableStateFlow(false)

    init {
        NetworkUtil.listenStatus(
            context,
            onAvailable = { networkState.value = true },
            onLost = { networkState.value = false })
    }

    fun isAvailable(): Boolean {
        return networkState.value
    }

    fun listen(): Flow<Boolean> = networkState

}