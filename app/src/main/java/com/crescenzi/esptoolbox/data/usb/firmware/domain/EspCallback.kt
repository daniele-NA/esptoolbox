package com.crescenzi.esptoolbox.data.usb.firmware.domain

/**
 * Callback interface
 */
interface EspCallback {
    /*
        * Callback methods
        */
    fun onInfo(line: String)
    fun onFlashLoading(percentage: Int)

    @Throws(Exception::class)
    fun onError(e: Throwable)
}