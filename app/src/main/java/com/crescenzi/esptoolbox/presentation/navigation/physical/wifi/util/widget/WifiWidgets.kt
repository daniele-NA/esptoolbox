package com.crescenzi.esptoolbox.presentation.navigation.physical.wifi.util.widget

import android.location.LocationManager
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.crescenzi.esptoolbox.R
import com.crescenzi.esptoolbox.presentation.widget.EditText
import com.crescenzi.esptoolbox.presentation.navigation.physical.wifi.WifiViewModel


/**
 * Data input section
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun DataSection(wifiViewModel: WifiViewModel, onButtonClick: (pwd: String) -> Unit) {
    val pwdState = rememberSaveable { mutableStateOf("") }

    val ssid = wifiViewModel.ssid.collectAsStateWithLifecycle()
    val bssid = wifiViewModel.bssid.collectAsStateWithLifecycle()

    /**
     * SSID
     */
    key(ssid.value) {
        EditText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 25.dp),
            opt = KeyboardOptions.Default,
            onValueChange = {},
            label = "SSID",
            initialValue = ssid.value.ifEmpty { stringResource(R.string.permission_required) },
            enabled = false
        )
    }

    /**
     * BSSID
     */
    key(bssid.value) {
        EditText(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 25.dp),
            opt = KeyboardOptions.Default,
            onValueChange = {},
            label = "BSSID",
            initialValue = bssid.value.ifEmpty { stringResource(R.string.permission_required) },
            enabled = false
        )
    }


    /**
     * Password
     */
    EditText(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 25.dp),
        opt = KeyboardOptions.Default,
        onValueChange = { pwdState.value = it },
        label = stringResource(R.string.wifi_pwd_hint),
        initialValue = pwdState.value
    )

    /**
     * Warns that location is disabled
     */
    if (isLocationEnabled(LocalContext.current.getSystemService(LocationManager::class.java) as LocationManager)==false) {
        Text(
            modifier = Modifier.padding(vertical = 10.dp),
            text = stringResource(R.string.position_warning, stringResource(R.string.application_name)),
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
        shapes = ButtonDefaults.shapes(),
        onClick = {
            onButtonClick.invoke(pwdState.value)
        }) {
        Text(
            text = stringResource(
                R.string.btn_connect
            ), style = MaterialTheme.typography.labelMedium
        )
    }

}