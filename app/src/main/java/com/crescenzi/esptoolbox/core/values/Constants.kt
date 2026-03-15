package com.crescenzi.esptoolbox.core.values

import android.Manifest
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.crescenzi.esptoolbox.BuildConfig


/**
 * All app-wide variables
 */
object Constants {

    /**
     * Business
     */
    const val STATUS_DEF_CHAR = "—"

    const val WRITE_WAIT_MILLIS = 2000  //For the serial port
    const val INTENT_ACTION_GRANT_USB = BuildConfig.APPLICATION_ID + ".GRANT_USB"
    const val OBSERVE_STATUS_DELAY = 3000L

    val permissions = mutableListOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    /**
     * Ui
     */

    val HORIZONTAL_PADDING = 18.dp
    val TOP_PADDING = 32.dp
    val CARD_CORNER = 22.dp
    val CARD_PADDING = 15.dp

    const val PICK_MIME_TYPE="application/octet-stream"
    const val WIFI_ANIM="lottie/wifi_anim.json"
    const val GITHUB_URL = "https://github.com/daniele-NA"
}
