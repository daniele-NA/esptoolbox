package com.crescenzi.esptoolbox.presentation

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Hardware, Permissions, Various states
 */
class DeviceHardwareStatus {

    private val _internet = MutableStateFlow(false)
    val internet = _internet.asStateFlow()

    private val _location = MutableStateFlow(false)
    val location = _location.asStateFlow()

    private val _locationPermission = MutableStateFlow(false)
    val locationPermission = _locationPermission.asStateFlow()

    private val _ssid = MutableStateFlow("")
    val ssid = _ssid.asStateFlow()

    private val _bssid = MutableStateFlow("")
    val bssid = _bssid.asStateFlow()



    fun changeInternetStatus(value: Boolean) {
        _internet.value = value
    }

    fun changeLocationStatus(value: Boolean){
        _location.value=value
    }

    fun changeLocationPermissionStatus(value: Boolean){
        _locationPermission.value=value
    }

    fun changeNetworkInfo(rawSsid: String,bssid: String){
        val cleanedSsid=rawSsid.removePrefix("\"").removeSuffix("\"").trim()

        _ssid.value=cleanedSsid
        if(bssid.trim().isNotEmpty()) _bssid.value=bssid.trim()
    }
}