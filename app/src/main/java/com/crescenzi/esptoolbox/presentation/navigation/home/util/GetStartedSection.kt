package com.crescenzi.esptoolbox.presentation.navigation.home.util

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.crescenzi.esptoolbox.R
import com.crescenzi.esptoolbox.presentation.widget.LeadingTile
import com.crescenzi.esptoolbox.core.values.Constants.CARD_PADDING
import com.crescenzi.esptoolbox.presentation.LocalNavController
import com.crescenzi.esptoolbox.presentation.UsbPage
import com.crescenzi.esptoolbox.presentation.navigation.home.HomeViewModel
import com.crescenzi.esptoolbox.theme.titlesFont


/**
 * Section that handles permissions and navigation
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun GetStartedSection(
    homeViewModel: HomeViewModel
) {

    val navController = LocalNavController.current

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

    val haptic = LocalHapticFeedback.current

    Text(
        text = stringResource(R.string.get_started_tool),
        style = MaterialTheme.typography.titleLarge.copy(
            fontSize = 26.sp,
            fontWeight = FontWeight.Black,
            fontFamily = FontFamily(titlesFont)
        ),
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
        val permissionInteractionSource = remember { MutableInteractionSource() }
        val permissionPressed by permissionInteractionSource.collectIsPressedAsState()
        val permissionScale by animateFloatAsState(
            targetValue = if (permissionPressed) 0.95f else 1f,
            label = "permission_btn_scale"
        )

        TextButton(
            enabled = !navBtnStatus.value,
            shape = RoundedCornerShape(24.dp),
            interactionSource = permissionInteractionSource,
            modifier = Modifier.scale(permissionScale),
            onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
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
        val goInteractionSource = remember { MutableInteractionSource() }
        val goPressed by goInteractionSource.collectIsPressedAsState()
        val goScale by animateFloatAsState(
            targetValue = if (goPressed) 0.95f else 1f,
            label = "go_btn_scale"
        )

        Button(
            enabled = navBtnStatus.value,
            shape = RoundedCornerShape(24.dp),
            interactionSource = goInteractionSource,
            modifier = Modifier.scale(goScale),
            onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                /**
                 * EXTRA CHECK
                 */
                if (navBtnStatus.value) navController.navigate(UsbPage)
            }) {
            Text(
                text = stringResource(R.string.go_tool),
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.padding(horizontal = 10.dp) // inner padding
            )

        }

    }

}