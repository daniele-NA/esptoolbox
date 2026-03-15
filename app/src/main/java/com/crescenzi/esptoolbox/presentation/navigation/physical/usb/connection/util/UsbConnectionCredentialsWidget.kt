package com.crescenzi.esptoolbox.presentation.navigation.physical.usb.connection.util

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.crescenzi.esptoolbox.R


@Composable
fun UsbConnectionCredentialsWidget(ssidState: MutableState<String>, passwordState: MutableState<String>) {
    TextField(
        singleLine = true,
        modifier = Modifier.padding(top = 10.dp, start = 5.dp, end = 5.dp),
        value = ssidState.value,
        onValueChange = {
            ssidState.value = it
        },
        placeholder = {
            Text(
                text = stringResource(R.string.ssid_placeholder),
                style = MaterialTheme.typography.labelMedium
            )
        },
        textStyle = MaterialTheme.typography.labelMedium,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )

    TextField(
        singleLine = true,
        modifier = Modifier.padding(top = 15.dp, start = 5.dp, end = 5.dp),
        value = passwordState.value,
        onValueChange = {
            passwordState.value = it
        },
        placeholder = {
            Text(
                text = stringResource(R.string.pwd_placeholder),
                style = MaterialTheme.typography.labelMedium
            )
        },
        textStyle = MaterialTheme.typography.labelMedium,
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = MaterialTheme.colorScheme.primary,
            unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )
}
