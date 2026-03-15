package com.crescenzi.esptoolbox.data.usb.data.util

import com.hoho.android.usbserial.driver.FtdiSerialDriver
import com.hoho.android.usbserial.driver.ProbeTable
import com.hoho.android.usbserial.driver.UsbSerialProber

/**
 * Custom parameters can be added for manufacturers not present in DefaultProber
 */
fun getCustomProber() =
    UsbSerialProber(ProbeTable().apply {
        addProduct(
            0x1234,
            0x0001,
            FtdiSerialDriver::class.java
        )
        addProduct(
            0x1234,
            0x0002,
            FtdiSerialDriver::class.java
        )
    })

/**
 * Permission state
 */
enum class UsbPermission {
    GRANTED, NOT_GRANTED
}
