package com.crescenzi.esptoolbox.core

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import android.util.Log
import com.crescenzi.esptoolbox.BuildConfig


/**
 * Let for multiple parameters
 */
inline fun <A, B, R> safeLet(a: A?, b: B?, block: (A, B) -> R): R? {
    return if (a != null && b != null) block(a, b) else null
}

/**
 * File name from Uri
 */
fun Uri.getFileNameWithoutBin(context: Context): String? {
    val fileName = if (scheme == "content") {
        context.contentResolver.query(this, null, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
            } else null
        }
    } else {
        path?.substringAfterLast('/')
    }

    return fileName?.removeSuffix(".bin")
}

/**
 * Debug logging
 */
fun LOG(value: String) {
    if (BuildConfig.DEBUG) {
        Log.e("MY-LOG", value)
    }
}
