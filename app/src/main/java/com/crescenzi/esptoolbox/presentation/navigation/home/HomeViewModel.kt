package com.crescenzi.esptoolbox.presentation.navigation.home

import androidx.lifecycle.ViewModel
import com.crescenzi.esptoolbox.data.phone.data.DeviceRepo


/**
 * Holds device states and permissions
 */
class HomeViewModel(val deviceRepo: DeviceRepo) : ViewModel() {


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