package com.crescenzi.esptoolbox.presentation.navigation.physical.usb.updater.viewmodel.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize


/**
 * Represents each unit for flashing
 */
@Parcelize
data class FlashFileEntry(
    val label: String=".bin",       // es: "Bootloader", "Firmware", ecc.
    val address: Int, // es: 0x1000
    var uri: Uri?=null
): Parcelable
