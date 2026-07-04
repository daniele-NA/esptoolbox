package com.crescenzi.esp32.firmware

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