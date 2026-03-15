package com.crescenzi.esptoolbox

import android.app.PendingIntent
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.hardware.usb.UsbManager
import android.location.LocationManager
import android.net.Uri
import android.net.wifi.WifiManager.NETWORK_STATE_CHANGED_ACTION
import android.os.Bundle
import android.provider.Settings
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.crescenzi.esptoolbox.core.base.BaseComponentActivity
import com.crescenzi.esptoolbox.core.values.Constants.INTENT_ACTION_GRANT_USB
import com.crescenzi.esptoolbox.core.values.Constants.permissions
import com.crescenzi.esptoolbox.presentation.MainNavigation
import com.crescenzi.esptoolbox.xml.AppTheme

/**
 * Device Connection Activity
 */
class MainActivity : BaseComponentActivity() {


    /**
     * Once location permissions are granted, we start observing the SSID
     */
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissionsMap: Map<String, Boolean> ->

        val coarseGranted = permissionsMap[permissions[0]] == true
        val fineGranted = permissionsMap[permissions[1]] == true


        /**
         * If all location permissions are granted
         */
        if (coarseGranted && fineGranted) {
            registerReceiver(
                ssidReceiver,
                IntentFilter(NETWORK_STATE_CHANGED_ACTION)
            )
            homeViewModel.deviceRepo.changeLocationPermissionStatus(true)
        } else
            homeViewModel.deviceRepo.changeLocationPermissionStatus(false)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        /**
         * Location init (the first value is not received)
         */

        homeViewModel.deviceRepo.changeLocationStatus(
            (getSystemService(LOCATION_SERVICE) as LocationManager).isProviderEnabled(
                LocationManager.GPS_PROVIDER
            )
        )

        /**
         * Permission callback handling
         */
        homeViewModel.onReqPermissionCallback = {
            startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = Uri.fromParts("package", packageName, null)
            })
        }

        setContent {
            AppTheme(this) {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    content = { safePadding ->
                        Column(
                            modifier = Modifier
                                .padding(safePadding)
                                .fillMaxSize()
                        ) {
                            MainNavigation(
                                usbConnectionViewModel = super.usbConnectionViewModel,
                                usbUpdaterViewModel = super.usbUpdaterViewModel,
                                logViewModel = super.logViewModel,
                                wifiViewModel = super.wifiViewModel,
                                homeViewModel = super.homeViewModel,
                                onReqUsbPermission = this@MainActivity::requestUsbPermission
                            )
                        }
                    })
            }
        }



        requestPermissionLauncher.launch(permissions.toTypedArray())


    }


    /**
     * Permission request for each different device, WORKING VERSION FOR ALL API LEVELS
     */
    fun requestUsbPermission() {
        usbConnectionViewModel.currentDevice.value?.let {
            val usbManager = getSystemService(USB_SERVICE) as UsbManager
            val intent = Intent(INTENT_ACTION_GRANT_USB).apply {
                setPackage(packageName)
            }
            usbManager.requestPermission(
                it.device, PendingIntent.getBroadcast(
                    this,
                    0,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
                )
            )
        }
    }


    private fun checkPermission(permission: String) =
        ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED

    override fun onResume() {
        super.onResume()

        /**
         * If all location permissions are granted
         */
        if (checkPermission(permissions[0]) && checkPermission(permissions[1])) {
            homeViewModel.deviceRepo.changeLocationPermissionStatus(true)
        } else
            homeViewModel.deviceRepo.changeLocationPermissionStatus(false)
    }


}
