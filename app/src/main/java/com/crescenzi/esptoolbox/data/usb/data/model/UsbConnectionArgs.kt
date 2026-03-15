package com.crescenzi.esptoolbox.data.usb.data.model

import com.crescenzi.esptoolbox.data.core.params.BaudRateFormat
import com.crescenzi.esptoolbox.data.core.params.SerialFormat

/**
 * Required arguments for the connection
 */
data class UsbConnectionArgs(
    val ssid: String,
    val pwd: String,
    val serialFormat: SerialFormat,
    val baudRateFormat: BaudRateFormat
)