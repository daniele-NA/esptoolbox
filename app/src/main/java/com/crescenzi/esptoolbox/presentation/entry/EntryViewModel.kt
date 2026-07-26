package com.crescenzi.esptoolbox.presentation.entry

import androidx.lifecycle.ViewModel
import com.crescenzi.esptoolbox.data.phone.data.DeviceRepo


/**
 * Holds device states and permissions
 */
class EntryViewModel(val deviceRepo: DeviceRepo) : ViewModel() {


    /**
     * Set by the UI (Activity or Composable) to request permissions
     */
    var onReqPermissionCallback: () -> Unit = {}

    /**
     * Called when you actually want to request permissions
     */
    fun callReqPermission() {
        onReqPermissionCallback()
    }

}