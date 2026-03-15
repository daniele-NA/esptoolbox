package com.crescenzi.esptoolbox.core.debug

import android.util.Log
import com.crescenzi.esptoolbox.BuildConfig

/**
 * Debug logging
 */

fun LOG(value: String) {
    if (BuildConfig.DEBUG) {
        Log.e("MY-LOG", value)
    }
}
