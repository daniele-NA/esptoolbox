package com.crescenzi.esptoolbox.theme

import android.content.Context
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialExpressiveTheme
import androidx.compose.material3.MotionScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import com.crescenzi.esptoolbox.R

// == The app ships with a single (night) theme; colors always come from XML == //
private fun getColorScheme(context: Context): ColorScheme {
    val baseScheme = darkColorScheme()

    return baseScheme.copy(
        primary = Color(ContextCompat.getColor(context, R.color.md_theme_primary)),
        onPrimary = Color(ContextCompat.getColor(context, R.color.md_theme_onPrimary)),

        secondary = Color(ContextCompat.getColor(context, R.color.md_theme_secondary)),
        onSecondary = Color(ContextCompat.getColor(context, R.color.md_theme_onSecondary)),

        tertiary = Color(ContextCompat.getColor(context, R.color.md_theme_tertiary)),
        onTertiary = Color(ContextCompat.getColor(context, R.color.md_theme_onTertiary)),
        tertiaryContainer = Color(ContextCompat.getColor(context, R.color.md_theme_tertiaryContainer)),
        onTertiaryContainer = Color(ContextCompat.getColor(context, R.color.md_theme_onTertiaryContainer)),

        background = Color(ContextCompat.getColor(context, R.color.md_theme_background)),
        onBackground = Color(ContextCompat.getColor(context, R.color.md_theme_onBackground)),

        surface = Color(ContextCompat.getColor(context, R.color.md_theme_surface)),
        onSurface = Color(ContextCompat.getColor(context, R.color.md_theme_onSurface)),

        surfaceVariant = Color(ContextCompat.getColor(context, R.color.md_theme_surfaceVariant)),
        onSurfaceVariant = Color(ContextCompat.getColor(context, R.color.md_theme_onSurfaceVariant)),

        surfaceContainer = Color(ContextCompat.getColor(context, R.color.md_theme_surfaceContainer)),
        surfaceContainerHigh = Color(ContextCompat.getColor(context, R.color.md_theme_surfaceContainerHigh)),
        surfaceContainerHighest = Color(ContextCompat.getColor(context, R.color.md_theme_surfaceContainerHighest)),

        outline = Color(ContextCompat.getColor(context, R.color.md_theme_outline)),

        error = Color(ContextCompat.getColor(context, R.color.md_theme_error)),
        onError = Color(ContextCompat.getColor(context, R.color.md_theme_onError)),
    )



}


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AppTheme(
    context: Context,
    content: @Composable () -> Unit
) {
    /* ==

    Must be initialized first to avoid color issues in Type.kt

    == */
    val customColorScheme = getColorScheme(context)

    MaterialExpressiveTheme(
        colorScheme = customColorScheme,
        motionScheme = MotionScheme.expressive(),
        typography = getTypography(customColorScheme),
        content = content
    )
}
