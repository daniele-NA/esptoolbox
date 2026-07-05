package com.crescenzi.esptoolbox.presentation.navigation.physical.usb.connection.util

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.crescenzi.esptoolbox.R
import com.crescenzi.esptoolbox.core.values.Constants.STATUS_DEF_CHAR
import com.crescenzi.esp32.params.SerialFormat
import com.crescenzi.esp32.usb.UsbPermission
import com.crescenzi.esptoolbox.presentation.navigation.physical.usb.connection.UsbConnectionViewModel


/**
 * Bottom section with connect button
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun UsbConnectionActionsWidget(
    modifier: Modifier,
    usbConnectionViewModel: UsbConnectionViewModel,
    onClickConnect: () -> Unit
) {
    val connectMessage = stringResource(R.string.btn_connect)
    val grantMessage = stringResource(R.string.grant_permission_tool)

    val deviceSnapshot by usbConnectionViewModel.currentDeviceSnapshot.collectAsStateWithLifecycle()


    val btnEnabled = rememberSaveable { mutableStateOf(false) }

    /**
     * Syncs on one of the values to update the button state
     */
    LaunchedEffect(deviceSnapshot.version) {
        btnEnabled.value = deviceSnapshot.version != STATUS_DEF_CHAR
    }

    val usbPermission by usbConnectionViewModel.usbPermission.collectAsStateWithLifecycle()
    val btnText = if (usbPermission == UsbPermission.GRANTED) connectMessage else grantMessage
    val haptic = LocalHapticFeedback.current

    val connectInteractionSource = remember { MutableInteractionSource() }
    val connectPressed by connectInteractionSource.collectIsPressedAsState()
    val connectScale by animateFloatAsState(
        targetValue = if (connectPressed) 0.95f else 1f,
        label = "connect_btn_scale"
    )

    Button(
        modifier = modifier
            .padding(top = 25.dp)
            .scale(connectScale),
        enabled = btnEnabled.value,
        shape = RoundedCornerShape(24.dp),
        interactionSource = connectInteractionSource,
        onClick = {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            onClickConnect()
        }) {
        Text(
            text = btnText,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

