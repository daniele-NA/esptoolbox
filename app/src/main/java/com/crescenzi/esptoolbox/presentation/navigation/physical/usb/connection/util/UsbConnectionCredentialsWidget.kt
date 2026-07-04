package com.crescenzi.esptoolbox.presentation.navigation.physical.usb.connection.util

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.crescenzi.esptoolbox.R
import com.crescenzi.esptoolbox.core.presentation.widget.EditText


@Composable
fun UsbConnectionCredentialsWidget(ssidState: MutableState<String>, passwordState: MutableState<String>) {
    EditText(
        modifier = Modifier.padding(top = 10.dp, start = 5.dp, end = 5.dp),
        opt = KeyboardOptions.Default,
        onValueChange = { ssidState.value = it },
        label = stringResource(R.string.ssid_placeholder),
        initialValue = ssidState.value
    )

    EditText(
        modifier = Modifier.padding(top = 15.dp, start = 5.dp, end = 5.dp),
        opt = KeyboardOptions.Default,
        onValueChange = { passwordState.value = it },
        label = stringResource(R.string.pwd_placeholder),
        initialValue = passwordState.value
    )
}
