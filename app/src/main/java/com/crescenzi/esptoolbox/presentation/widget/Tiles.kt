package com.crescenzi.esptoolbox.presentation.widget

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.crescenzi.esptoolbox.R


/**
 * Represents a tile with a leading view, title and subtitle
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LeadingTile(
    modifier: Modifier = Modifier,
    currentValue: Boolean,
    @StringRes titleRes: Int,
    @StringRes subTitleRes: Int? = null
) {
    val iconRes = if (currentValue) R.drawable.check_icon else R.drawable.error_icon
    val iconBgColor = if (currentValue) {
        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)
    } else {
        MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.35f)
    }
    
    val borderStroke = if (currentValue) {
        BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.4f))
    } else {
        BorderStroke(1.dp, MaterialTheme.colorScheme.error.copy(alpha = 0.4f))
    }

    // == Card wrapper for the leading tile to make it a neat, expressive row item == //
    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.15f)),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .padding(end = 14.dp)
                    .background(iconBgColor, shape = CircleShape)
                    .border(borderStroke, shape = CircleShape)
                    .padding(8.dp)
            ) {
                Icon(
                    painter = painterResource(iconRes),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(20.dp)
                )
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    modifier = Modifier.padding(bottom = 2.dp),
                    text = stringResource(titleRes),
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                subTitleRes?.let { txtRes ->
                    Text(
                        text = stringResource(txtRes),
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

/**
 * Info tile with light bulb icon
 */
@Composable
fun InfoTile(text: String) {
    CardContainer(
        cardColors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.tertiaryContainer),
        content = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.padding(end = 8.dp),
                    painter = painterResource(R.drawable.lamp_icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onTertiaryContainer
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                )
            }
        }
    )
}