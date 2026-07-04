package com.crescenzi.esp32.wifi

import android.content.Context
import com.espressif.iot.esptouch.EsptouchTask

/**
 * ESP-Touch (SmartConfig) Wi-Fi provisioning over UDP broadcast.
 */
class EspTouchRepo {

    /**
     * Runs the ESP-Touch task. Returns the connected device BSSID/MAC on success, or null on failure.
     * Blocking call — must be invoked off the main thread.
     */
    fun connect(ssid: String, bssid: String, password: String, context: Context): String? {
        val task = EsptouchTask(ssid, bssid, password, context)
        return task.executeForResults(1)[0].bssid
    }
}
