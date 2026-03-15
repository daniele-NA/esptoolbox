package com.crescenzi.esptoolbox.core.device


import android.os.Build

/**
 * Version checks for Runtime Permissions
 */
object VersionManager {

    /**
     * Android 11 or below
     */
    inline fun <T> isAndroid11OrBelow(onAndroid11OrBelow: () -> T): T? {
        return if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.R) { // R = 30 (Android 11)
            onAndroid11OrBelow()
        } else null
    }

    // >=13
    inline fun <T> isAndroid13OrAbove(onAndroid13OrAbove: () -> T): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            onAndroid13OrAbove()
        } else null
    }

    // >=14
    inline fun <T> isAndroid14OrAbove(action: () -> T): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            action()
        } else null
    }


}