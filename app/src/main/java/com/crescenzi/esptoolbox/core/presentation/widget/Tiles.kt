package com.crescenzi.esptoolbox.core.presentation.widget

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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

    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        /**
         * V -> primary / X -> error
         */
        Icon(
            painter = painterResource(iconRes),
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.padding(end = 8.dp)
        )
        Column {
            Text(
                modifier = Modifier.padding(bottom = 3.dp),
                text = stringResource(titleRes),
                style = MaterialTheme.typography.labelMedium
            )
            subTitleRes?.let { txtRes ->
                Text(text = stringResource(txtRes), style = MaterialTheme.typography.labelSmall)
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
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    modifier = Modifier.padding(end = 5.dp),
                    painter = painterResource(R.drawable.lamp_icon),
                    contentDescription = null
                )
                Text(
                    text = text,
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    )
}