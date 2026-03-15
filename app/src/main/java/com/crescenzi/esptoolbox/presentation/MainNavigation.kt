package com.crescenzi.esptoolbox.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.crescenzi.esptoolbox.presentation.navigation.home.HomeScreen
import com.crescenzi.esptoolbox.presentation.navigation.home.HomeViewModel
import com.crescenzi.esptoolbox.presentation.navigation.physical.log.LogViewModel
import com.crescenzi.esptoolbox.presentation.navigation.physical.navigation.PhysicalNavigation
import com.crescenzi.esptoolbox.presentation.navigation.physical.navigation.PhysicalNavigationParams
import com.crescenzi.esptoolbox.presentation.navigation.physical.usb.connection.UsbConnectionViewModel
import com.crescenzi.esptoolbox.presentation.navigation.physical.usb.updater.UsbUpdaterViewModel
import com.crescenzi.esptoolbox.presentation.navigation.physical.wifi.WifiViewModel
import kotlinx.serialization.Serializable


/**
 * Screen navigation for ConnectionActivity
 */

@Serializable
object HomePage

@Serializable
object PhysicalSection


/**
 * Connection Navigation: First page MacScreen
 */
@Composable
fun MainNavigation(
    usbConnectionViewModel: UsbConnectionViewModel,
    usbUpdaterViewModel: UsbUpdaterViewModel,
    logViewModel: LogViewModel,
    wifiViewModel: WifiViewModel,
    homeViewModel: HomeViewModel,
    onReqUsbPermission: () -> Unit
) {

    val navController = rememberNavController()



    /**
     * Prevents crash in OnResume()
     */
    LaunchedEffect(Unit) {
        val current = navController.currentDestination
        if (current == null || current.route == null) {
            navController.navigate(HomePage) {
                popUpTo(0)
            }
        }
    }

    val homeScreen = remember {
        @Composable
        {
            HomeScreen(homeViewModel) {
                navController.navigate(PhysicalSection)
            }
        }
    }

    val physicalNavigation = remember {
        @Composable
        {
            PhysicalNavigation(
                physicalNavigationParams = PhysicalNavigationParams(
                    usbConnectionViewModel = usbConnectionViewModel,
                    usbUpdaterViewModel = usbUpdaterViewModel,
                    logViewModel = logViewModel,
                    wifiViewModel = wifiViewModel,
                    onReqUsbPermission = onReqUsbPermission
                )
            )
        }
    }

    NavHost(
        navController = navController,
        startDestination = HomePage,
    ) {
        composable<HomePage> {
            homeScreen.invoke()
        }

        composable<PhysicalSection> {
            physicalNavigation.invoke()
        }

    }

}