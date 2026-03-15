package com.crescenzi.esptoolbox.presentation.navigation.physical.usb.connection.util

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.crescenzi.esptoolbox.R
import com.crescenzi.esptoolbox.core.values.Constants.STATUS_DEF_CHAR
import com.crescenzi.esptoolbox.data.core.params.SerialFormat
import com.crescenzi.esptoolbox.data.usb.data.util.UsbPermission
import com.crescenzi.esptoolbox.presentation.navigation.physical.usb.connection.UsbConnectionViewModel


/**
 * Bottom section with connect button
 */
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


    Button(
        modifier = modifier
            .padding(top = 25.dp), enabled = btnEnabled.value,
        onClick = {
            onClickConnect()
        }) {
        Text(
            text = btnText,
            style = MaterialTheme.typography.labelMedium
        )
    }
}

