package com.crescenzi.esp32.usb.model


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