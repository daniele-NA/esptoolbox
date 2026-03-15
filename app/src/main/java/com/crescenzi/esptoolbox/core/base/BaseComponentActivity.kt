package com.crescenzi.esptoolbox.core.base

import android.content.IntentFilter
import android.content.res.Configuration
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.annotation.CallSuper
import androidx.core.content.ContextCompat
import com.crescenzi.esptoolbox.R
import com.crescenzi.esptoolbox.core.values.Constants.INTENT_ACTION_GRANT_USB
import com.crescenzi.esptoolbox.presentation.navigation.home.HomeViewModel
import com.crescenzi.esptoolbox.presentation.navigation.physical.log.LogViewModel
import com.crescenzi.esptoolbox.presentation.navigation.physical.usb.connection.UsbConnectionViewModel
import com.crescenzi.esptoolbox.presentation.navigation.physical.usb.updater.UsbUpdaterViewModel
import com.crescenzi.esptoolbox.presentation.navigation.physical.wifi.WifiViewModel
import com.crescenzi.esptoolbox.system.receiver.GenericReceiver
import com.crescenzi.esptoolbox.system.receiver.SsidReceiver
import com.crescenzi.esptoolbox.system.receiver.UsbPermissionReceiver
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.getValue

/**
 * Base class for MainActivity
 *
 */
abstract class BaseComponentActivity : ComponentActivity() {

    protected val ssidReceiver = SsidReceiver()
    protected val usbPermissionReceiver = UsbPermissionReceiver()
    protected val genericReceiver = GenericReceiver()


    val usbConnectionViewModel: UsbConnectionViewModel by viewModel()
    val usbUpdaterViewModel: UsbUpdaterViewModel by viewModel()
    val logViewModel: LogViewModel by viewModel()
    val wifiViewModel: WifiViewModel by viewModel()
    val homeViewModel: HomeViewModel by viewModel()


    companion object {
        @Volatile
        var ORIENTATION = Configuration.ORIENTATION_PORTRAIT

        var APP_NAME: String = ""
    }


    @CallSuper
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        APP_NAME = getString(R.string.application_name)

        ContextCompat.registerReceiver(
            this,
            usbPermissionReceiver,
            IntentFilter(INTENT_ACTION_GRANT_USB),
            ContextCompat.RECEIVER_NOT_EXPORTED
        )

        /**
         * Handles data/Wi-Fi connection & location by updating the Repo
         */
        registerReceiver(genericReceiver, IntentFilter().apply {
            addAction(ConnectivityManager.CONNECTIVITY_ACTION)
            addAction(LocationManager.PROVIDERS_CHANGED_ACTION)
        })



        when (resources.configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                ORIENTATION = Configuration.ORIENTATION_LANDSCAPE
            }

            Configuration.ORIENTATION_PORTRAIT -> {
                ORIENTATION = Configuration.ORIENTATION_PORTRAIT
            }

            else -> {
            }
        }

    }


    /**
     * Called at the end of this Activity, cleans up observers etc.
     */
    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(usbPermissionReceiver)
        unregisterReceiver(ssidReceiver)
        unregisterReceiver(genericReceiver)
        usbConnectionViewModel.clearAll()
    }


}