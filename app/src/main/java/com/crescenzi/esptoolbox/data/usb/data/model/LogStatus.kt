package com.crescenzi.esptoolbox.data.usb.data.model


/**
 * Represents logs from serial connections
 */
data class LogStatus(
    val line: String,
    val logLevel: LogLevel
)

enum class LogLevel {
    INFO, ERROR,WARNING
}