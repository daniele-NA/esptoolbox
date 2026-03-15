package com.crescenzi.esptoolbox.core.presentation.widget

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun WavyProgress(
    isVisible: MutableState<Boolean>,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "wavy-loop")
    val animatedProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 500, easing = LinearEasing)
        ),
        label = "animated-progress"
    )

    val displayProgress = if (isVisible.value) {
        { animatedProgress } // animated progress
    } else {
        { 0f } // flat line or "idle" state
    }

    LinearWavyProgressIndicator(
        progress = displayProgress,
        modifier = modifier
            .fillMaxWidth()
            .height(2.8.dp),
        gapSize = if (isVisible.value) 20.dp else 0.dp
    )
}
