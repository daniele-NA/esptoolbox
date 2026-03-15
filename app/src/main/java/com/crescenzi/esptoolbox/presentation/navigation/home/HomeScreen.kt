package com.crescenzi.esptoolbox.presentation.navigation.home

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.crescenzi.esptoolbox.core.device.checkStoreUpdate
import com.crescenzi.esptoolbox.core.values.Constants.HORIZONTAL_PADDING
import com.crescenzi.esptoolbox.presentation.navigation.home.util.AboutSection
import com.crescenzi.esptoolbox.presentation.navigation.home.util.GetStartedSection
import com.crescenzi.esptoolbox.presentation.navigation.home.util.InfoSection


/**
 * Page used for all permission checks
 */
@Composable
fun HomeScreen(
    homeViewModel: HomeViewModel,
    onNavToSerialSection: () -> Unit
) {

    /**
     * Update check
     */
    LocalActivity.current?.checkStoreUpdate()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = HORIZONTAL_PADDING)
            .verticalScroll(
                rememberScrollState()
            )
    ) {
        Spacer(Modifier.padding(top = 60.dp))
        InfoSection()
        Spacer(Modifier.padding(top = 25.dp))
        GetStartedSection(homeViewModel, onNavToSerialSection)
        AboutSection()


    }
}