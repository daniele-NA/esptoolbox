package com.crescenzi.esptoolbox.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun AppTheme(
    content: @Composable () -> Unit,
) {
    MaterialTheme(
        colorScheme = appLightColorScheme(),
        typography = AppTypography,
        content = content,
    )
}
