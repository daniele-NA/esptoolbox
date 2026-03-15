package com.crescenzi.esptoolbox.data.phone.data

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.text.removePrefix

/**
 * Hardware, Permissions, Various states
 */
class DeviceRepo() {

    private val _internet = MutableStateFlow<Boolean>(false)
    val internet = _internet.asStateFlow()

    private val _location = MutableStateFlow<Boolean>(false)
    val location = _location.asStateFlow()

    private val _locationPermission = MutableStateFlow<Boolean>(false)
    val locationPermission = _locationPermission.asStateFlow()

    private val _ssid = MutableStateFlow<String>("")
    val ssid = _ssid.asStateFlow()

    private val _bssid = MutableStateFlow<String>("")
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