package com.crescenzi.esptoolbox.presentation.navigation.physical.navigation

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.crescenzi.esptoolbox.R
import com.crescenzi.esptoolbox.core.base.BaseComponentActivity
import com.crescenzi.esptoolbox.core.debug.LOG
import com.crescenzi.esptoolbox.core.presentation.widget.CenterHeroTitle
import com.crescenzi.esptoolbox.core.presentation.widget.WavyProgress
import com.crescenzi.esptoolbox.core.values.Constants.HORIZONTAL_PADDING
import com.crescenzi.esptoolbox.core.values.Constants.TOP_PADDING
import com.crescenzi.esptoolbox.presentation.navigation.physical.log.LogScreen
import com.crescenzi.esptoolbox.presentation.navigation.physical.log.LogViewModel
import com.crescenzi.esptoolbox.presentation.navigation.physical.usb.navigation.UsbNavigation
import com.crescenzi.esptoolbox.presentation.navigation.physical.usb.connection.UsbConnectionViewModel
import com.crescenzi.esptoolbox.presentation.navigation.physical.usb.updater.UsbUpdaterViewModel
import com.crescenzi.esptoolbox.presentation.navigation.physical.wifi.WifiScreen
import com.crescenzi.esptoolbox.presentation.navigation.physical.wifi.WifiViewModel


private enum class Destination(val route: String) {
    USB("usb"),
    LOG("log"),
    WIFI("wifi");

    /**
     * To always have the label based on the current language
     */
    @Composable
    fun label(): String = when (this) {
        USB -> stringResource(R.string.usb_label)
        LOG -> stringResource(R.string.log_label)
        WIFI -> stringResource(R.string.wifi_label)
    }
}


data class PhysicalNavigationParams(
    val usbConnectionViewModel: UsbConnectionViewModel,
    val usbUpdaterViewModel: UsbUpdaterViewModel,
    val logViewModel: LogViewModel,
    val wifiViewModel: WifiViewModel,
    val onReqUsbPermission: () -> Unit,
)

/**
 * Navigation page management
 * DOES NOT SCROLL HORIZONTALLY
 */
@Composable
fun PhysicalNavigation(
    physicalNavigationParams: PhysicalNavigationParams,
) {

    /**
     * Monitors the top progress bar, triggered when loading state is set to True or False
     */
    val progressBarManager = rememberSaveable { mutableStateOf(false) }
    val loadingState by physicalNavigationParams.logViewModel.baseRepo.loadingState.collectAsState()
    LaunchedEffect(loadingState) {
        progressBarManager.value = physicalNavigationParams.logViewModel.baseRepo.loadingState.value
    }


    val startDestination = Destination.USB
    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }
    rememberPagerState(
        initialPage = selectedDestination,
        pageCount = { Destination.entries.size }
    )


    val usbNavigation = remember {
        @Composable
        {
            UsbNavigation(physicalNavigationParams)
        }
    }

    val logScreen = remember {
        @Composable
        {
            LogScreen(physicalNavigationParams.logViewModel)
        }
    }

    val wifiScreen = remember {
        @Composable
        {
            WifiScreen(physicalNavigationParams.wifiViewModel)
        }
    }


    /**
     * If we are on the Log page, hide the badge; otherwise show it on each new log
     */
    val showBadge = rememberSaveable { mutableStateOf(false) }
    val logs by physicalNavigationParams.logViewModel.baseRepo.logs.collectAsStateWithLifecycle()
    LaunchedEffect(logs.size) {
        if (selectedDestination != Destination.LOG.ordinal) {
            showBadge.value = true
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        /**
         * Portrait MODE only
         */
        if (BaseComponentActivity.ORIENTATION == Configuration.ORIENTATION_PORTRAIT) {
            CenterHeroTitle(
                modifier = Modifier.padding(
                    start = HORIZONTAL_PADDING,
                    end = HORIZONTAL_PADDING,
                    top = if (BaseComponentActivity.ORIENTATION == Configuration.ORIENTATION_PORTRAIT) TOP_PADDING else 0.dp,
                    bottom = if (BaseComponentActivity.ORIENTATION == Configuration.ORIENTATION_PORTRAIT) 10.dp else 5.dp
                ),
                txt = stringResource(R.string.application_name),
                textStyle = MaterialTheme.typography.titleLarge
            )
        }


        PrimaryTabRow(
            selectedTabIndex = selectedDestination,
            containerColor = MaterialTheme.colorScheme.background, divider = {
                WavyProgress(
                    isVisible = progressBarManager
                )
            }
        ) {
            Destination.entries.forEachIndexed { index, destination ->
                Tab(
                    selected = selectedDestination == index,
                    onClick = {
                        selectedDestination = index
                        if (destination == Destination.LOG) {
                            showBadge.value = false // hides the badge as soon as we enter the LOG page
                        }
                    },
                    text = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = destination.label(),
                                maxLines = 1,
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.onBackground
                            )

                            if (destination == Destination.LOG && showBadge.value) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .background(
                                            color = MaterialTheme.colorScheme.error,
                                            shape = CircleShape
                                        )
                                )
                            }
                        }
                    }
                )
            }
        }

        // Show the selected screen
        when (Destination.entries[selectedDestination]) {
            Destination.USB -> usbNavigation.invoke()
            Destination.LOG -> logScreen.invoke()
            Destination.WIFI -> wifiScreen.invoke()
        }

    }
}
