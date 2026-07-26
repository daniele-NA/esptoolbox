package com.crescenzi.esptoolbox.presentation.main_shell.usb_connection.util

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.crescenzi.esptoolbox.R
import com.crescenzi.esptoolbox.core.values.Constants.STATUS_DEF_CHAR
import com.crescenzi.esp32.usb.UsbPermission
import com.crescenzi.esptoolbox.presentation.main_shell.usb_connection.USBConnectionViewModel
import com.crescenzi.esptoolbox.presentation.widget.AppButton
import com.crescenzi.esptoolbox.theme.SPACE_XL


/**
 * Bottom section with connect button
 */
@Composable
fun UsbConnectionActionsWidget(
    modifier: Modifier,
    usbConnectionViewModel: USBConnectionViewModel,
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
    val loading by usbConnectionViewModel.loading.collectAsStateWithLifecycle()
    val btnText = if (usbPermission == UsbPermission.GRANTED) connectMessage else grantMessage
    val haptic = LocalHapticFeedback.current

    AppButton(
        modifier = modifier.padding(top = SPACE_XL),
        txt = btnText,
        enabled = btnEnabled.value && !loading,
        onTap = {
            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
            onClickConnect()
        }
    )
}
