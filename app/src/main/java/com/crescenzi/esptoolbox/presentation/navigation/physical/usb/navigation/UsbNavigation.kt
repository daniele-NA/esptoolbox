package com.crescenzi.esptoolbox.presentation.navigation.physical.usb.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.crescenzi.esptoolbox.R
import com.crescenzi.esptoolbox.core.LOG
import com.crescenzi.esptoolbox.presentation.navigation.physical.usb.connection.UsbConnectionScreen
import com.crescenzi.esptoolbox.presentation.navigation.physical.usb.connection.UsbConnectionViewModel
import com.crescenzi.esptoolbox.presentation.navigation.physical.usb.updater.UsbUpdaterScreen
import com.crescenzi.esptoolbox.presentation.navigation.physical.usb.updater.UsbUpdaterViewModel
import com.crescenzi.esptoolbox.xml.titlesFont
import kotlinx.coroutines.launch

/**
 * USB/Log page navigation
 */

private enum class Destination(val route: String) {
    CONNECTION("connection"),
    UPDATER("updater");

    /**
     * To always have the label based on the current language
     */
    @Composable
    fun label(): String = when (this) {
        CONNECTION -> stringResource(R.string.connection_label)
        UPDATER -> stringResource(R.string.updater_label)
    }
}



/**
 * Navigation page management
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun UsbNavigation(
    usbConnectionViewModel: UsbConnectionViewModel,
    usbUpdaterViewModel: UsbUpdaterViewModel,
    onReqUsbPermission: () -> Unit
) {



    val coroutineScope = rememberCoroutineScope()
    val startDestination = Destination.CONNECTION
    var selectedDestination by rememberSaveable { mutableIntStateOf(startDestination.ordinal) }
    val pagerState = rememberPagerState(
        initialPage = selectedDestination,
        pageCount = { Destination.entries.size }
    )

    val usbConnectionScreen = remember {
        @Composable
        {
            UsbConnectionScreen(
                usbConnectionViewModel = usbConnectionViewModel,
                onReqUsbPermission = onReqUsbPermission
            )
        }
    }

    val usbUpdaterScreen = remember {
        @Composable
        {
            UsbUpdaterScreen(usbUpdaterViewModel)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(top = 12.dp)
        ) {
            Destination.entries.forEachIndexed { index, destination ->
                ToggleButton(
                    checked = selectedDestination == index,
                    onCheckedChange = {
                        selectedDestination = index
                        coroutineScope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    }
                ) {
                    Text(
                        text = destination.label(),
                        style = MaterialTheme.typography.labelMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily(titlesFont)
                        )
                    )
                }
            }
        }

        HorizontalPager(
            state = pagerState,
            modifier = Modifier.fillMaxSize(),
            beyondViewportPageCount = 1
        ) { page ->
            when (Destination.entries[page]) {
                Destination.CONNECTION -> usbConnectionScreen.invoke()
                Destination.UPDATER -> usbUpdaterScreen.invoke()
            }
        }

        // Sync tab selection with pager page
        LaunchedEffect(pagerState.currentPage) {
            selectedDestination = pagerState.currentPage
        }
    }
}
