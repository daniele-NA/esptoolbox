package com.crescenzi.esp32.internal

import android.util.Log
import com.crescenzi.esp32.BuildConfig

/* ==

Self-contained utilities for the esp32 module, so it stays agnostic from the app.

== */

const val STATUS_DEF_CHAR = "—"
const val WRITE_WAIT_MILLIS = 2000

/**
 * Debug logging
 */
fun LOG(value: String) {
    if (BuildConfig.DEBUG) {
        Log.e("MY-LOG", value)
    }
}

/**
 * Let for multiple parameters
 */
inline fun <A, B, R> safeLet(a: A?, b: B?, block: (A, B) -> R): R? {
    return if (a != null && b != null) block(a, b) else null
}
