package com.crescenzi.esp32

import com.crescenzi.esp32.usb.model.LogLevel
import com.crescenzi.esp32.usb.model.LogStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.collections.plus

/**
 * Holds all logs (used both for connections and for notifying the user)
 * Also holds the operation state
 */
class LogRepo {

    private val _logs = MutableStateFlow<List<LogStatus>>(emptyList())
    val logs = _logs.asStateFlow()


    fun plusLog(line: String, logLevel: LogLevel = LogLevel.INFO) {
        _logs.value = _logs.value + LogStatus(line.trim(), logLevel)
    }

    fun cleanLog() {
        _logs.value = emptyList()
    }

    /**
     * Returns all logs
     */
    override fun toString(): String {
        return _logs.value.joinToString(separator = "\n\n")
    }
}