package com.crescenzi.esptoolbox.data.usb.data.model

import android.hardware.usb.UsbDevice
import com.crescenzi.esptoolbox.core.values.Constants.STATUS_DEF_CHAR
import com.hoho.android.usbserial.driver.UsbSerialDriver


/**
 * Represents the device in a USB connection
 */
data class UsbStatus(
    val device: UsbDevice,
    val port: Int,
    val driver: UsbSerialDriver?
) {

    fun toSnapshotUsbDevice() = SnapshotUsb(
        version = device.version,
        manufacturerName = device.manufacturerName ?: STATUS_DEF_CHAR,
        vendorId = device.vendorId,
        productId = device.productId,
        port = port
    )

    data class SnapshotUsb(
        val version: String = STATUS_DEF_CHAR,
        val manufacturerName: String = STATUS_DEF_CHAR,
        val vendorId: Int = -1,
        val productId: Int = -1,
        val port: Int = -1
    )


}
