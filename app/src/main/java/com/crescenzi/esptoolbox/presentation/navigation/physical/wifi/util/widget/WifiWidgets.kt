package com.crescenzi.esptoolbox.presentation.navigation.physical.wifi.util.widget

import android.location.LocationManager
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.crescenzi.esptoolbox.R
import com.crescenzi.esptoolbox.core.base.BaseComponentActivity.Companion.APP_NAME
import com.crescenzi.esptoolbox.presentation.navigation.physical.wifi.WifiViewModel


/**
 * Data input section
 */
@Composable
fun DataSection(wifiViewModel: WifiViewModel, onButtonClick: (pwd: String) -> Unit) {
    val pwdState = rememberTextFieldState()

    val ssid = wifiViewModel.ssid.collectAsStateWithLifecycle()
    val bssid = wifiViewModel.bssid.collectAsStateWithLifecycle()

    /**
     * SSID
     */
    OutlinedTextField(
        enabled = false,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 25.dp),
        value = ssid.value.ifEmpty { stringResource(R.string.permission_required) },
        onValueChange = {},
        textStyle = MaterialTheme.typography.labelMedium,
        label = {
            Text(
                text = "SSID",
                style = MaterialTheme.typography.labelMedium
            )
        })

    /**
     * BSSID
     */
    OutlinedTextField(
        enabled = false,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 25.dp),
        value = bssid.value.ifEmpty { stringResource(R.string.permission_required) },
        onValueChange = {},
        textStyle = MaterialTheme.typography.labelMedium,
        label = {
            Text(
                text = "BSSID",
                style = MaterialTheme.typography.labelMedium
            )
        })


    /**
     * Password
     */
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 25.dp),
        state = pwdState,
        textStyle = MaterialTheme.typography.labelMedium,
        label = {
            Text(
                text = stringResource(R.string.wifi_pwd_hint),
                style = MaterialTheme.typography.labelMedium
            )
        })

    /**
     * Warns that location is disabled
     */
    if (isLocationEnabled(LocalContext.current.getSystemService(LocationManager::class.java) as LocationManager)==false) {
        Text(
            modifier = Modifier.padding(vertical = 10.dp),
            text = stringResource(R.string.position_warning,APP_NAME),
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.Bold
            ), color = MaterialTheme.colorScheme.error

        )
    }

    /**
     * Start connection
     */
    Button(
        modifier = Modifier.padding(bottom = 10.dp),
        onClick = {
            onButtonClick.invoke(pwdState.text.toString())
        }) {
        Text(
            text = stringResource(
                R.string.btn_connect
            ), style = MaterialTheme.typography.labelMedium
        )
    }

}