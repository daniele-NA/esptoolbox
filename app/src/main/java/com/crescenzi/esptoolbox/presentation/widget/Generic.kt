package com.crescenzi.esptoolbox.presentation.widget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.crescenzi.esptoolbox.theme.titlesFont

/**
 * Page header: a small uppercase "up title" over a larger page title.
 */
@Composable
fun PageHeader(upTitle: String, title: String, modifier: Modifier = Modifier) {
    Column(modifier.fillMaxWidth()) {
        Text(
            text = upTitle.uppercase(),
            style = MaterialTheme.typography.labelSmall.copy(
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp
            ),
            color = MaterialTheme.colorScheme.secondary.copy(alpha = 0.8f)
        )
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily(titlesFont)
            ),
            color = MaterialTheme.colorScheme.primary
        )
    }
}