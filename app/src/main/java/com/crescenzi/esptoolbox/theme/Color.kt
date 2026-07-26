package com.crescenzi.esptoolbox.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import com.crescenzi.esptoolbox.R

@Composable
fun appLightColorScheme(): ColorScheme =
    lightColorScheme(
        primary = colorResource(R.color.primary),
        onPrimary = colorResource(R.color.onPrimary),
        primaryContainer = colorResource(R.color.primaryContainer),
        onPrimaryContainer = colorResource(R.color.onPrimaryContainer),
        inversePrimary = colorResource(R.color.inversePrimary),
        primaryFixed = colorResource(R.color.primaryFixed),
        primaryFixedDim = colorResource(R.color.primaryFixedDim),
        onPrimaryFixed = colorResource(R.color.onPrimaryFixed),
        onPrimaryFixedVariant = colorResource(R.color.onPrimaryFixedVariant),
        secondary = colorResource(R.color.secondary),
        onSecondary = colorResource(R.color.onSecondary),
        secondaryContainer = colorResource(R.color.secondaryContainer),
        onSecondaryContainer = colorResource(R.color.onSecondaryContainer),
        secondaryFixed = colorResource(R.color.secondaryFixed),
        secondaryFixedDim = colorResource(R.color.secondaryFixedDim),
        onSecondaryFixed = colorResource(R.color.onSecondaryFixed),
        onSecondaryFixedVariant = colorResource(R.color.onSecondaryFixedVariant),
        tertiary = colorResource(R.color.tertiary),
        onTertiary = colorResource(R.color.onTertiary),
        tertiaryContainer = colorResource(R.color.tertiaryContainer),
        onTertiaryContainer = colorResource(R.color.onTertiaryContainer),
        tertiaryFixed = colorResource(R.color.tertiaryFixed),
        tertiaryFixedDim = colorResource(R.color.tertiaryFixedDim),
        onTertiaryFixed = colorResource(R.color.onTertiaryFixed),
        onTertiaryFixedVariant = colorResource(R.color.onTertiaryFixedVariant),
        error = colorResource(R.color.error),
        onError = colorResource(R.color.onError),
        errorContainer = colorResource(R.color.errorContainer),
        onErrorContainer = colorResource(R.color.onErrorContainer),
    )
