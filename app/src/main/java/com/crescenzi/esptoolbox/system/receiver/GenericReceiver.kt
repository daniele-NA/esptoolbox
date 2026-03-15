package com.crescenzi.esptoolbox.system.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Intent
import android.location.LocationManager
import android.net.ConnectivityManager
import com.crescenzi.esptoolbox.data.phone.data.DeviceRepo
import org.koin.mp.KoinPlatform.getKoin

/**
 * Manages internet and location state
 */
class GenericReceiver : BroadcastReceiver() {

    private val deviceRepo = getKoin().get<DeviceRepo>()

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == ConnectivityManager.CONNECTIVITY_ACTION) {
            val isConnected =
                (context?.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo?.isConnectedOrConnecting == true

            deviceRepo.changeInternetStatus(isConnected)
        }

        if (intent?.action == LocationManager.PROVIDERS_CHANGED_ACTION) {
            val locationManager = context?.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isLocationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            deviceRepo.changeLocationStatus(isLocationEnabled)
        }
    }

}