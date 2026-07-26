package com.crescenzi.esptoolbox.presentation.entry

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.crescenzi.esptoolbox.R
import com.crescenzi.esptoolbox.core.AppConstants.BLUE_GLYPH
import com.crescenzi.esptoolbox.core.AppConstants.GREEN_GLYPH
import com.crescenzi.esptoolbox.core.AppConstants.ON_BLUE_GLYPH
import com.crescenzi.esptoolbox.core.AppConstants.ON_GREEN_GLYPH
import com.crescenzi.esptoolbox.core.AppConstants.ON_ORANGE_GLYPH
import com.crescenzi.esptoolbox.core.AppConstants.ORANGE_GLYPH
import com.crescenzi.esptoolbox.presentation.main_shell.LocalNavController
import com.crescenzi.esptoolbox.presentation.main_shell.UsbPage
import com.crescenzi.esptoolbox.presentation.widget.AppButton
import com.crescenzi.esptoolbox.presentation.widget.AppScaffold
import com.crescenzi.esptoolbox.presentation.widget.LeadingTile
import com.crescenzi.esptoolbox.theme.LATERAL_PADDING
import com.crescenzi.esptoolbox.theme.SPACE_L


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

    val internetState = entryViewModel.deviceHardwareStatus.internet.collectAsStateWithLifecycle().value
    //SSID and BSSID detection
    val locationPermissionState =
        entryViewModel.deviceHardwareStatus.locationPermission.collectAsStateWithLifecycle().value
    val locationState = entryViewModel.deviceHardwareStatus.location.collectAsStateWithLifecycle().value

    /**
     * If EVERY requirement is met, proceed
     */
    val allRequirementsMet = internetState && locationPermissionState && locationState

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
                enabled = !locationPermissionState || allRequirementsMet,
                onTap = {
                    if (!locationPermissionState) {
                        entryViewModel.callReqPermission()
                    } else if (allRequirementsMet) {
                        navController.navigate(UsbPage)
                    }
                }
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = SPACE_L)
        ) {

            Text(
                modifier = Modifier.padding(horizontal = LATERAL_PADDING),
                text = stringResource(R.string.get_started_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.weight(1f))

            Column(verticalArrangement = Arrangement.spacedBy(SPACE_L)) {

                AnimatedVisibility(
                    visible = contentVisible,
                    enter = tileEnterTransition(fromLeft = true)
                ) {
                    LeadingTile(
                        modifier = Modifier.padding(horizontal = LATERAL_PADDING),
                        currentValue = internetState,
                        titleRes = R.string.internet_req_title,
                        subTitleRes = R.string.internet_req_body,
                        glyphColor = GREEN_GLYPH,
                        glyphContentColor = ON_GREEN_GLYPH
                    )
                }

                AnimatedVisibility(
                    visible = contentVisible,
                    enter = tileEnterTransition(fromLeft = false)
                ) {
                    LeadingTile(
                        modifier = Modifier.padding(horizontal = LATERAL_PADDING),
                        currentValue = locationPermissionState,
                        titleRes = R.string.location_permission_req_title,
                        glyphColor = BLUE_GLYPH,
                        glyphContentColor = ON_BLUE_GLYPH
                    )
                }

                AnimatedVisibility(
                    visible = contentVisible,
                    enter = tileEnterTransition(fromLeft = true)
                ) {
                    LeadingTile(
                        modifier = Modifier.padding(horizontal = LATERAL_PADDING),
                        currentValue = locationState,
                        titleRes = R.string.location_req_title,
                        subTitleRes = R.string.location_req_body,
                        glyphColor = ORANGE_GLYPH,
                        glyphContentColor = ON_ORANGE_GLYPH
                    )
                }
            }

            Spacer(Modifier.weight(1f))
        }
    }
}

private fun tileEnterTransition(fromLeft: Boolean): EnterTransition =
    fadeIn(
        animationSpec = tween(durationMillis = 600, easing = CubicBezierEasing(0.2f, 0.0f, 0f, 1f))
    ) + slideInHorizontally(
        initialOffsetX = { if (fromLeft) -it / 3 else it / 3 },
        animationSpec = tween(durationMillis = 600, easing = CubicBezierEasing(0.2f, 0.0f, 0f, 1f))
    )
