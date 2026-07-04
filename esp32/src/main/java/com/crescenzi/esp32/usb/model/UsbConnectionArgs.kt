package com.crescenzi.esp32.usb.model

import com.crescenzi.esp32.params.BaudRateFormat
import com.crescenzi.esp32.params.SerialFormat

/**
 * Required arguments for the connection
 */
data class UsbConnectionArgs(
    val ssid: String,
    val pwd: String,
    val serialFormat: SerialFormat,
    val baudRateFormat: BaudRateFormat
)