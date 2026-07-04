package com.crescenzi.esptoolbox.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.crescenzi.esptoolbox.R

// Custom sizes
val h1 = 30.sp
val h2 = 22.sp
val h3 = 15.sp
val h4 = 11.sp

val bodiesFont = Font(R.font.roboto_flex)
val titlesFont = Font(R.font.roboto_serif)

fun getTypography(materialColorScheme: ColorScheme): Typography {
    return Typography(



        titleLarge = TextStyle( // --> Titles
            fontFamily = FontFamily(titlesFont),
            fontWeight = FontWeight.Medium,
            fontSize = h1,
            lineHeight = 32.sp,
            letterSpacing = 0.sp
        ),

        titleMedium = TextStyle( // --> Titles
            fontFamily = FontFamily(titlesFont),
            fontWeight = FontWeight.Medium,
            fontSize = h2,
            lineHeight = 32.sp,
            letterSpacing = 0.sp
        ),

        titleSmall = TextStyle( // --> Titles
            fontFamily = FontFamily(titlesFont),
            fontWeight = FontWeight.Medium,
            fontSize = h3,
            lineHeight = 32.sp,
            letterSpacing = 0.sp
        ),

        labelLarge = TextStyle( // --> Card title
            fontFamily = FontFamily(titlesFont),
            fontWeight = FontWeight.Medium,
            fontSize = h2,
            lineHeight = 28.sp,
            letterSpacing = 0.sp,
            color = materialColorScheme.primary
        ),

        labelMedium = TextStyle( // --> Label title (Card) (TextField)
            fontFamily = FontFamily(bodiesFont),
            fontWeight = FontWeight.Normal,
            fontSize = h3,
            lineHeight = 20.sp,
            letterSpacing = 0.25.sp
        ),

        labelSmall = TextStyle( // --> Label body (Card) (TextField)
            fontFamily = FontFamily(bodiesFont),
            fontWeight = FontWeight.Medium,
            fontSize = h4,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        )
    )
}