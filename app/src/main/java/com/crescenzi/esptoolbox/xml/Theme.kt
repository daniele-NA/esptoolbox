package com.crescenzi.esptoolbox.xml

import android.content.Context
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.core.content.ContextCompat
import com.crescenzi.esptoolbox.R

/**
 * Configured the same way for both themes
 */
private fun getColorScheme(context: Context, darkTheme: Boolean): ColorScheme {
    val baseScheme = if (darkTheme) darkColorScheme() else lightColorScheme()

    return baseScheme.copy(
        primary = Color(ContextCompat.getColor(context, R.color.md_theme_primary)),
        onPrimary = Color(ContextCompat.getColor(context, R.color.md_theme_onPrimary)),
        primaryContainer = Color(ContextCompat.getColor(context, R.color.md_theme_primaryContainer)),
        onPrimaryContainer = Color(ContextCompat.getColor(context, R.color.md_theme_onPrimaryContainer)),

        secondary = Color(ContextCompat.getColor(context, R.color.md_theme_secondary)),
        onSecondary = Color(ContextCompat.getColor(context, R.color.md_theme_onSecondary)),
        secondaryContainer = Color(ContextCompat.getColor(context, R.color.md_theme_secondaryContainer)),
        onSecondaryContainer = Color(ContextCompat.getColor(context, R.color.md_theme_onSecondaryContainer)),

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

        outline = Color(ContextCompat.getColor(context, R.color.md_theme_outline)),
        outlineVariant = Color(ContextCompat.getColor(context, R.color.md_theme_outlineVariant)),

        error = Color(ContextCompat.getColor(context, R.color.md_theme_error)),
        onError = Color(ContextCompat.getColor(context, R.color.md_theme_onError)),
        errorContainer = Color(ContextCompat.getColor(context, R.color.md_theme_errorContainer)),
        onErrorContainer = Color(ContextCompat.getColor(context, R.color.md_theme_onErrorContainer)),

        inverseSurface = Color(ContextCompat.getColor(context, R.color.md_theme_inverseSurface)),
        inverseOnSurface = Color(ContextCompat.getColor(context, R.color.md_theme_inverseOnSurface)),
        inversePrimary = Color(ContextCompat.getColor(context, R.color.md_theme_inversePrimary)),

        /*
        Overlay color
         */
        scrim = Color(ContextCompat.getColor(context, R.color.md_theme_scrim))
    )



}


/**
 * Theme color management via XML, all colors come from XML,
 * it handles the colors -> colors-night switch
 */
@Composable
fun AppTheme(
    context: Context,
    content: @Composable () -> Unit
) {
    /*
    Must be initialized first to avoid color issues in Type.kt
     */
    val customColorScheme = getColorScheme(context, isSystemInDarkTheme())

    MaterialTheme(
        colorScheme = customColorScheme,
        typography = getTypography(customColorScheme),
        content = content
    )
}
