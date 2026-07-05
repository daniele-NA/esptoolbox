package com.crescenzi.esptoolbox.presentation.navigation.home

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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

    // == background elements floating animations == //
    val infiniteTransition = rememberInfiniteTransition(label = "ambient_glow")
    
    val translationX1 by infiniteTransition.animateFloat(
        initialValue = -30f,
        targetValue = 30f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 8000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blob1_x"
    )
    val translationY1 by infiniteTransition.animateFloat(
        initialValue = -25f,
        targetValue = 25f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 7000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blob1_y"
    )

    val translationX2 by infiniteTransition.animateFloat(
        initialValue = 35f,
        targetValue = -35f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 9000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blob2_x"
    )
    val translationY2 by infiniteTransition.animateFloat(
        initialValue = -20f,
        targetValue = 20f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 7500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "blob2_y"
    )

    // == Animated visibility entrance state == //
    var contentVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        contentVisible = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // == background ambient glow blobs == //
        Box(
            modifier = Modifier
                .size(320.dp)
                .align(Alignment.TopEnd)
                .offset(
                    x = 100.dp + translationX1.dp,
                    y = (-80).dp + translationY1.dp
                )
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.16f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )
        Box(
            modifier = Modifier
                .size(260.dp)
                .align(Alignment.CenterStart)
                .offset(
                    x = (-80).dp + translationX2.dp,
                    y = 100.dp + translationY2.dp
                )
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.secondary.copy(alpha = 0.10f),
                            Color.Transparent
                        )
                    ),
                    shape = CircleShape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(
                    rememberScrollState()
                )
                .padding(WindowInsets.systemBars.asPaddingValues())
                .padding(horizontal = HORIZONTAL_PADDING)
        ) {
            Spacer(Modifier.padding(top = 40.dp))
            
            AnimatedVisibility(
                visible = contentVisible,
                enter = fadeIn(
                    animationSpec = tween(durationMillis = 600, easing = CubicBezierEasing(0.2f, 0.0f, 0f, 1f))
                ) + slideInVertically(
                    initialOffsetY = { it / 3 },
                    animationSpec = tween(durationMillis = 600, easing = CubicBezierEasing(0.2f, 0.0f, 0f, 1f))
                )
            ) {
                Column {
                    InfoSection()
                    Spacer(Modifier.padding(top = 30.dp))
                    GetStartedSection(homeViewModel)
                }
            }
            
            Spacer(Modifier.padding(bottom = 40.dp))
        }
    }
}