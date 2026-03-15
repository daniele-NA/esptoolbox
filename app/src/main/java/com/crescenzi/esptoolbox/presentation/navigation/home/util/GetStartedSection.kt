package com.crescenzi.esptoolbox.presentation.navigation.home.util

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.crescenzi.esptoolbox.R
import com.crescenzi.esptoolbox.core.presentation.widget.LeadingTile
import com.crescenzi.esptoolbox.core.values.Constants.CARD_PADDING
import com.crescenzi.esptoolbox.presentation.navigation.home.HomeViewModel


/**
 * Section that handles permissions and navigation
 */
@Composable
fun GetStartedSection(
    homeViewModel: HomeViewModel,
    onNavToSerialSection: () -> Unit
) {

    val internetState = homeViewModel.deviceRepo.internet.collectAsStateWithLifecycle().value
    //SSID and BSSID detection
    val locationPermissionState =
        homeViewModel.deviceRepo.locationPermission.collectAsStateWithLifecycle().value
    val locationState = homeViewModel.deviceRepo.location.collectAsStateWithLifecycle().value


    val navBtnStatus = rememberSaveable { mutableStateOf(true) }

    /**
     * If PERMISSIONS ARE GRANTED, proceed
     */
    LaunchedEffect( locationPermissionState) {
        navBtnStatus.value = locationPermissionState
    }

    Text(
        text = stringResource(R.string.get_started_tool),
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.primary
    )
    Spacer(Modifier.padding(top = CARD_PADDING))

    Text(
        modifier = Modifier.padding(bottom = 15.dp),
        text = stringResource(R.string.get_started_desc),
        style = MaterialTheme.typography.labelMedium
    )

    LeadingTile(
        modifier = Modifier.padding(top = 20.dp),
        currentValue = internetState,
        titleRes = R.string.internet_req_title,
        subTitleRes = R.string.internet_req_body,
    )

    LeadingTile(
        modifier = Modifier.padding(top = 20.dp),
        currentValue = locationPermissionState,
        titleRes = R.string.location_permission_req_title
    )

    LeadingTile(
        modifier = Modifier.padding(top = 20.dp),
        currentValue = locationState,
        titleRes = R.string.location_req_title,
        subTitleRes = R.string.location_req_body
    )

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 30.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {

        /*
    Permission Btn
     */
        TextButton(
            enabled = !navBtnStatus.value,
            onClick = {
                homeViewModel.callReqPermission()
            }) {
            Text(
                text = stringResource(R.string.grant_permission_tool),
                style = MaterialTheme.typography.labelMedium
            )
        }


        /**
         * If everything is active, navigate to the next screen
         */
        Button(
            enabled = navBtnStatus.value,
            onClick = {
                /**
                 * EXTRA CHECK
                 */
                if (navBtnStatus.value) onNavToSerialSection.invoke()
            }) {
            Text(
                text = stringResource(R.string.go_tool),
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(horizontal = 10.dp) // inner padding
            )

        }

    }

}