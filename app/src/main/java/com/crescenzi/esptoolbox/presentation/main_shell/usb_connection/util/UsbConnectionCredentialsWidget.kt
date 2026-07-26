package com.crescenzi.esptoolbox.presentation.main_shell.usb_connection.util

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.stringResource
import com.crescenzi.esptoolbox.R
import com.crescenzi.esptoolbox.presentation.widget.EditText


@Composable
fun UsbConnectionCredentialsWidget(ssidState: MutableState<String>, passwordState: MutableState<String>) {
    EditText(
        opt = KeyboardOptions.Default,
        onValueChange = { ssidState.value = it },
        label = stringResource(R.string.ssid_placeholder),
        initialValue = ssidState.value
    )

    EditText(
        opt = KeyboardOptions.Default,
        onValueChange = { passwordState.value = it },
        label = stringResource(R.string.pwd_placeholder),
        initialValue = passwordState.value,
        isPassword = true
    )
}
