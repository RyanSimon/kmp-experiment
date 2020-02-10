package co.touchlab.kampstarter.android.extension

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

/**
 * @author Ryan Simon
 */
val Context.networkInfo: NetworkInfo
    get() = (this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo