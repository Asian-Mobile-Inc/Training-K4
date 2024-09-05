package com.example.asian.retrofit.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.example.asian.retrofit.utils.Constant
import java.util.Objects

class InternetBroadcast : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Objects.equals(intent.action, ConnectivityManager.CONNECTIVITY_ACTION)) {
            putData(context, isInternetAvailable(context))
        }
    }

    private fun putData(context: Context, isInternetAvailable: Boolean) {
        Intent().apply {
            setAction(Constant.ACTION_INTERNET_CHANGE)
            putExtra(Constant.KEY_INTERNET_CHANGE, isInternetAvailable)
            context.sendBroadcast(this)
        }
    }

    private fun isInternetAvailable(context: Context): Boolean {
        var result = false
        val connectivityManager =
            context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }
                }
            }
        }
        return result
    }
}