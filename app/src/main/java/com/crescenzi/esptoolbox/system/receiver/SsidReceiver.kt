package com.crescenzi.esptoolbox.system.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.wifi.WifiManager
import android.net.wifi.WifiManager.NETWORK_STATE_CHANGED_ACTION
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import com.crescenzi.esptoolbox.data.phone.data.DeviceRepo
import org.koin.mp.KoinPlatform.getKoin

/**
 * Always monitors the current SSID
 * REGISTERED ONLY ONCE PERMISSIONS ARE GRANTED FROM THE ACTIVITY
 */
class SsidReceiver : BroadcastReceiver() {

    private val deviceRepo = getKoin().get<DeviceRepo>()


    /**
     * The SSID is saved only when location is enabled because
     * with location OFF, incorrect values are received (no Exception thrown)
     */
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent?.action == NETWORK_STATE_CHANGED_ACTION) {
            if (isLocationEnabled(context?.getSystemService(LocationManager::class.java) as LocationManager)) {
                val wifiInfo =
                    (context.applicationContext?.getSystemService(Context.WIFI_SERVICE) as? WifiManager)?.connectionInfo

                val rawSsid =
                    wifiInfo?.ssid?.takeIf { it != "<unknown ssid>" }.toString()
                deviceRepo.changeNetworkInfo(rawSsid = rawSsid, bssid = wifiInfo?.bssid.toString())
            }
        }
    }
}