package com.crescenzi.esptoolbox.data.core

import com.crescenzi.esptoolbox.data.usb.data.model.LogLevel
import com.crescenzi.esptoolbox.data.usb.data.model.LogStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.collections.plus

/**
 * Holds all logs (used both for connections and for notifying the user)
 * Also holds the operation state
 */
class BaseRepo {

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