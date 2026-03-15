package com.crescenzi.esptoolbox.presentation.navigation.physical.wifi

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.crescenzi.esptoolbox.R
import com.crescenzi.esptoolbox.core.presentation.widget.InfoTile
import com.crescenzi.esptoolbox.core.values.Constants.HORIZONTAL_PADDING
import com.crescenzi.esptoolbox.core.values.Constants.WIFI_ANIM
import com.crescenzi.esptoolbox.presentation.navigation.physical.wifi.util.widget.DataSection

/**
 * WiFi Connection Screen
 */
@Composable
fun WifiScreen(
     wifiViewModel: WifiViewModel
) {


    val animation by rememberLottieComposition(LottieCompositionSpec.Asset(WIFI_ANIM))
    val playAnimation = remember { mutableStateOf(false) }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = HORIZONTAL_PADDING)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LottieAnimation(
            composition = animation,
            isPlaying = playAnimation.value,
            iterations = LottieConstants.IterateForever
        )

        InfoTile(stringResource(R.string.wifi_connection_title))
        Spacer(Modifier.padding(vertical = 15.dp))


        DataSection(
            wifiViewModel, onButtonClick = wifiViewModel::sendBroadcast
        )

    }
}