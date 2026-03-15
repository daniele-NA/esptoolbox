package com.crescenzi.esptoolbox.presentation.navigation.physical.wifi

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.crescenzi.esptoolbox.R
import com.crescenzi.esptoolbox.core.presentation.util.getMessage
import com.crescenzi.esptoolbox.core.values.ResultState
import com.crescenzi.esptoolbox.data.core.BaseRepo
import com.crescenzi.esptoolbox.data.phone.data.DeviceRepo
import com.crescenzi.esptoolbox.data.phone.domain.exception.WifiConnectionException
import com.crescenzi.esptoolbox.data.usb.data.model.LogLevel
import com.espressif.iot.esptouch.EsptouchTask


/**
 * WiFi connection management for the board (UDP)
 */
class WifiViewModel(
    application: Application,
    private val deviceRepo: DeviceRepo,
    val baseRepo: BaseRepo
) : AndroidViewModel(application = application) {

    val bssid = deviceRepo.bssid
    val ssid = deviceRepo.ssid


    /**
     *
     * If it connects, the MAC is immediately stored in preferences
     */
    fun sendBroadcast(pwd: String) {
        if (pwd.trim().isEmpty()) return
        if (ssid.value.isEmpty() || bssid.value.isEmpty()) return   // If empty, either permissions are missing or an error occurred

        baseRepo.plusLog(line = getApplication<Application>().getString(R.string.broadcast_warning), logLevel = LogLevel.WARNING)
        Thread {
            baseRepo.notifyLoadingState(true)
            try {
                val task =
                    EsptouchTask(
                        ssid.value,
                        bssid.value,
                        pwd.trim(),
                        getApplication<Application>()
                    )

                /**
                 * Mac == null -> Connection failed
                 */
                task.executeForResults(1)[0].bssid?.let { mac ->
                    baseRepo.notifyLoadingState(false)
                    baseRepo.plusLog(line = getApplication<Application>().getString(R.string.connection_successfully))
                } ?: run {
                    disableLoadingForException()
                }

            } catch (e: Exception) {
                disableLoadingForException(e)
            }
        }.start()
    }

    /**
     * Disables loading and logs the exception
     */
    private fun disableLoadingForException(e: Throwable= WifiConnectionException()){
        baseRepo.notifyLoadingState(false)
        getApplication<Application>().baseContext?.let { context ->
            baseRepo.plusLog(
                line = getMessage(context, e)
                    .toString(), logLevel = LogLevel.ERROR
            )
        }
    }

}