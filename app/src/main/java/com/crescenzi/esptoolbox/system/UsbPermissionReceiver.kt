package com.crescenzi.esptoolbox.system

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.hardware.usb.UsbManager
import com.crescenzi.esptoolbox.core.LOG
import com.crescenzi.esptoolbox.core.AppConstants
import com.crescenzi.esp32.usb.UsbPermission
import com.crescenzi.esptoolbox.presentation.main_shell.usb_connection.USBConnectionViewModel
import org.koin.java.KoinJavaComponent

/**
 * Manages permissions for each USB device
 */
class UsbPermissionReceiver : BroadcastReceiver() {


    private val usbViewModel: USBConnectionViewModel by KoinJavaComponent.inject(
        USBConnectionViewModel::class.java)


    override fun onReceive(context: Context?, intent: Intent?) {
        LOG("UsbPermissionReceiver : incoming intent")
        context?.let {
            if (intent?.action == AppConstants.INTENT_ACTION_GRANT_USB) {
                usbViewModel.updateUsbPermission(
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false))
                        UsbPermission.GRANTED else UsbPermission.NOT_GRANTED
                )
            }
        }

    }
}
