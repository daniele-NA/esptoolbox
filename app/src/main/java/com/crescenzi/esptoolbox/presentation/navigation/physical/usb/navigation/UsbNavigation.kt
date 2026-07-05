package com.crescenzi.esptoolbox.presentation.navigation.physical.usb.navigation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ToggleButton
import androidx.compose.material3.ToggleButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import com.crescenzi.esptoolbox.R
import androidx.compose.foundation.shape.RoundedCornerShape
import com.crescenzi.esptoolbox.core.values.Constants.HORIZONTAL_PADDING
import com.crescenzi.esptoolbox.presentation.navigation.physical.usb.connection.UsbConnectionScreen
import com.crescenzi.esptoolbox.presentation.navigation.physical.usb.connection.UsbConnectionViewModel
import com.crescenzi.esptoolbox.presentation.navigation.physical.usb.updater.UsbUpdaterScreen
import com.crescenzi.esptoolbox.presentation.navigation.physical.usb.updater.UsbUpdaterViewModel
import com.crescenzi.esptoolbox.presentation.widget.PageHeader
import com.crescenzi.esptoolbox.theme.titlesFont


private enum class Destination {
    CONNECTION, UPDATER;

    @Composable
    fun label(): String = when (this) {
        CONNECTION -> stringResource(R.string.connection_label)
        UPDATER -> stringResource(R.string.updater_label)
    }
}


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun UsbNavigation(
    usbConnectionViewModel: UsbConnectionViewModel,
    usbUpdaterViewModel: UsbUpdaterViewModel,
    onReqUsbPermission: () -> Unit
) {

    var selected by rememberSaveable { mutableIntStateOf(0) }

    Column(modifier = Modifier.fillMaxSize()) {

        /**
         * Fixed header + Connection/Updater switch (below the status bar)
         */
        Column(
            modifier = Modifier
                .padding(WindowInsets.statusBars.asPaddingValues())
                .padding(horizontal = HORIZONTAL_PADDING)
                .padding(top = HORIZONTAL_PADDING)
        ) {

            PageHeader(
                upTitle = stringResource(R.string.usb_up_title),
                title = stringResource(R.string.usb_title)
            )

            val haptic = LocalHapticFeedback.current

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Destination.entries.forEachIndexed { index, destination ->
                    val isSelected = selected == index
                    // Selected button is bigger; unselected is smaller
                    val weight by androidx.compose.animation.core.animateFloatAsState(
                        targetValue = if (isSelected) 1.6f else 1f,
                        animationSpec = androidx.compose.animation.core.tween(
                            durationMillis = 400,
                            easing = androidx.compose.animation.core.CubicBezierEasing(0.2f, 0.0f, 0.0f, 1.0f)
                        ),
                        label = "toggle-weight"
                    )
                    ToggleButton(
                        checked = isSelected,
                        onCheckedChange = {
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                            selected = index
                        },
                        modifier = Modifier.weight(weight),
                        shapes = ToggleButtonDefaults.shapes()
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
        }

        /**
         * Selected page (each one scrolls on its own)
         */
        Box(modifier = Modifier.weight(1f)) {
            when (Destination.entries[selected]) {
                Destination.CONNECTION -> UsbConnectionScreen(
                    usbConnectionViewModel = usbConnectionViewModel,
                    onReqUsbPermission = onReqUsbPermission
                )

                Destination.UPDATER -> UsbUpdaterScreen(usbUpdaterViewModel)
            }
        }
    }
}
