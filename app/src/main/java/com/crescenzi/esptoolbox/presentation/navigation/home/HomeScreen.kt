package com.crescenzi.esptoolbox.presentation.navigation.home

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.crescenzi.esptoolbox.core.device.checkStoreUpdate
import com.crescenzi.esptoolbox.core.values.Constants.HORIZONTAL_PADDING
import com.crescenzi.esptoolbox.presentation.navigation.home.util.GetStartedSection
import com.crescenzi.esptoolbox.presentation.navigation.home.util.InfoSection


/**
 * Page used for all permission checks
 */
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel
) {

    val activity = LocalActivity.current

    /**
     * Update check
     */
    activity?.checkStoreUpdate()

    LaunchedEffect(activity) {
        homeViewModel.onReqPermissionCallback = {
            activity?.startActivity(
                Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", activity.packageName, null)
                }
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(WindowInsets.systemBars.asPaddingValues())
            .padding(horizontal = HORIZONTAL_PADDING)
    ) {
        Spacer(Modifier.padding(top = 60.dp))
        InfoSection()
        Spacer(Modifier.padding(top = 25.dp))
        GetStartedSection(homeViewModel)


    }
}