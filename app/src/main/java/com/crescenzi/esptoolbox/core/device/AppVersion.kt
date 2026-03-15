package com.crescenzi.esptoolbox.core.device

import android.app.Activity
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.UpdateAvailability

fun Activity.checkStoreUpdate(requestCode: Int = 123) {
    val appUpdateManager = AppUpdateManagerFactory.create(this)

    // Check for update
    appUpdateManager.appUpdateInfo.addOnSuccessListener { info ->
        when {
            info.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE &&
                    info.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE) -> {
                try {
                    appUpdateManager.startUpdateFlowForResult(
                        info,
                        AppUpdateType.IMMEDIATE,
                        this,
                        requestCode
                    )
                } catch (e: Exception) { e.printStackTrace() }
            }

            info.updateAvailability() == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS -> {
                // If the update was interrupted
                try {
                    appUpdateManager.startUpdateFlowForResult(
                        info,
                        AppUpdateType.IMMEDIATE,
                        this,
                        requestCode
                    )
                } catch (e: Exception) { e.printStackTrace() }
            }
        }
    }
}
