package com.crescenzi.esptoolbox.presentation.util

import android.content.Context
import com.crescenzi.esptoolbox.R
import com.crescenzi.esp32.exception.UsbConnectionException
import com.crescenzi.esp32.exception.WifiConnectionException


/**
 * Maps Exceptions to user-facing messages
 */
fun getMessage(cnt:Context,e: Throwable): String {
    return when (e) {
        is UsbConnectionException -> cnt.getString(R.string.error_usb_connection)
        is WifiConnectionException ->cnt.getString(R.string.error_wifi_connection)
        is java.io.FileNotFoundException -> cnt.getString(R.string.error_file_not_found)
        is java.io.IOException -> cnt.getString(R.string.error_io_exception)
        is SecurityException -> cnt.getString(R.string.error_file_permission)
        is Exception -> cnt.getString(R.string.error_generic)
        else -> cnt.getString(R.string.error_generic)
    }
}
