package com.crescenzi.esptoolbox.presentation.entry

import androidx.lifecycle.ViewModel
import com.crescenzi.esptoolbox.presentation.DeviceHardwareStatus


/**
 * Holds device states and permissions
 */
class EntryViewModel(val deviceHardwareStatus: DeviceHardwareStatus) : ViewModel() {


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