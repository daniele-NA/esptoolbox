package com.crescenzi.esptoolbox.presentation.entry

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.crescenzi.esptoolbox.R
import com.crescenzi.esptoolbox.presentation.main_shell.LocalNavController
import com.crescenzi.esptoolbox.presentation.main_shell.UsbPage
import com.crescenzi.esptoolbox.presentation.widget.AppButton
import com.crescenzi.esptoolbox.presentation.widget.AppScaffold
import com.crescenzi.esptoolbox.presentation.widget.LeadingTile
import com.crescenzi.esptoolbox.theme.LATERAL_PADDING
import com.crescenzi.esptoolbox.theme.SPACE_L

private val GreenGlyph = Color(0xFF6DD58C)
private val OnGreenGlyph = Color(0xFF0A2E16)
private val BlueGlyph = Color(0xFF7CD0FF)
private val OnBlueGlyph = Color(0xFF00344F)
private val OrangeGlyph = Color(0xFFFFB871)
private val OnOrangeGlyph = Color(0xFF4A2800)


/**
 * Page used for all permission checks
 */
@Composable
fun EntryScreen(
    entryViewModel: EntryViewModel
) {

    val activity = LocalActivity.current

    LaunchedEffect(activity) {
        entryViewModel.onReqPermissionCallback = {
            activity?.startActivity(
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", activity.packageName, null)
                }
            )
        }
    }

    val navController = LocalNavController.current

    val internetState = entryViewModel.deviceRepo.internet.collectAsStateWithLifecycle().value
    //SSID and BSSID detection
    val locationPermissionState =
        entryViewModel.deviceRepo.locationPermission.collectAsStateWithLifecycle().value
    val locationState = entryViewModel.deviceRepo.location.collectAsStateWithLifecycle().value

    val navBtnStatus = rememberSaveable { mutableStateOf(true) }

    /**
     * If PERMISSIONS ARE GRANTED, proceed
     */
    LaunchedEffect(locationPermissionState, locationState) {
        navBtnStatus.value = locationPermissionState && locationState
    }

    val haptic = LocalHapticFeedback.current

    var contentVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        contentVisible = true
    }

    AppScaffold(
        title = stringResource(R.string.get_started_tool),
        reserveTopBarSpace = true,
        scrollable = false,
        bottomBar = {
            AppButton(
                txt = stringResource(
                    if (locationPermissionState) R.string.go_tool else R.string.grant_permission_tool
                ),
                enabled = !locationPermissionState || navBtnStatus.value,
                onTap = {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    if (!locationPermissionState) {
                        entryViewModel.callReqPermission()
                    } else if (navBtnStatus.value) {
                        navController.navigate(UsbPage)
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = LATERAL_PADDING)
                .padding(top = SPACE_L)
        ) {

            Text(
                text = stringResource(R.string.get_started_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.weight(1f))

            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn(
                    animationSpec = tween(durationMillis = 600, easing = CubicBezierEasing(0.2f, 0.0f, 0f, 1f))
                ) + slideInHorizontally(
                    initialOffsetX = { it / 3 },
                    animationSpec = tween(durationMillis = 600, easing = CubicBezierEasing(0.2f, 0.0f, 0f, 1f))
                )
            ) {
                Column(verticalArrangement = Arrangement.spacedBy(SPACE_L)) {

                    LeadingTile(
                        currentValue = internetState,
                        titleRes = R.string.internet_req_title,
                        subTitleRes = R.string.internet_req_body,
                        glyphColor = GreenGlyph,
                        glyphContentColor = OnGreenGlyph
                    )

                    LeadingTile(
                        currentValue = locationPermissionState,
                        titleRes = R.string.location_permission_req_title,
                        glyphColor = BlueGlyph,
                        glyphContentColor = OnBlueGlyph
                    )

                    LeadingTile(
                        currentValue = locationState,
                        titleRes = R.string.location_req_title,
                        subTitleRes = R.string.location_req_body,
                        glyphColor = OrangeGlyph,
                        glyphContentColor = OnOrangeGlyph
                    )
                }
            }

            Spacer(Modifier.weight(1f))
        }
    }
}
