package com.crescenzi.esptoolbox.presentation.navigation.physical.wifi

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.crescenzi.esptoolbox.R
import com.crescenzi.esptoolbox.presentation.widget.InfoTile
import com.crescenzi.esptoolbox.core.values.Constants.HORIZONTAL_PADDING
import com.crescenzi.esptoolbox.core.values.Constants.WIFI_ANIM
import com.crescenzi.esptoolbox.presentation.navigation.physical.wifi.util.widget.DataSection
import com.crescenzi.esptoolbox.presentation.widget.PageHeader

/**
 * WiFi Connection Screen
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun WifiScreen(
    wifiViewModel: WifiViewModel
) {

    val animation by rememberLottieComposition(LottieCompositionSpec.Asset(WIFI_ANIM))
    val loading by wifiViewModel.loading.collectAsStateWithLifecycle()


    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(WindowInsets.systemBars.asPaddingValues())
                .padding(horizontal = HORIZONTAL_PADDING)
                .padding(top = HORIZONTAL_PADDING)
                .padding(bottom = 110.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            PageHeader(
                upTitle = stringResource(R.string.wifi_up_title),
                title = stringResource(R.string.wifi_title),
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(Modifier.padding(top = 8.dp))

            LottieAnimation(
                composition = animation,
                isPlaying = loading,
                iterations = LottieConstants.IterateForever
            )

            InfoTile(stringResource(R.string.wifi_connection_title))
            Spacer(Modifier.padding(vertical = 15.dp))


            DataSection(
                wifiViewModel, onButtonClick = wifiViewModel::sendBroadcast
            )

        }

        if (loading) {
            LoadingIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}
