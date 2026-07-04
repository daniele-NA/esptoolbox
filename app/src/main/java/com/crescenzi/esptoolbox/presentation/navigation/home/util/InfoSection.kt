package com.crescenzi.esptoolbox.presentation.navigation.home.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.annotation.DrawableRes
import com.crescenzi.esptoolbox.R
import com.crescenzi.esptoolbox.presentation.widget.OutlinedCardContainer

@Composable
fun InfoSection() {

    /**
     * Header: app logo + name
     */
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.main_ic),
            contentDescription = null,
            modifier = Modifier.size(48.dp)
        )
        Spacer(Modifier.width(12.dp))
        Text(
            text = stringResource(R.string.application_name),
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.primary
        )
    }

    Spacer(Modifier.padding(top = 18.dp))

    /**
     * Two outlined info cards, side by side
     */
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        InfoCard(
            modifier = Modifier.weight(1f),
            icon = R.drawable.usb_icon,
            title = stringResource(R.string.usb_desc),
            subtitle = stringResource(R.string.usb_subdesc)
        )
        InfoCard(
            modifier = Modifier.weight(1f),
            icon = R.drawable.wifi_icon,
            title = stringResource(R.string.wifi_desc),
            subtitle = stringResource(R.string.wifi_subdesc)
        )
    }
}

@Composable
private fun InfoCard(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    title: String,
    subtitle: String
) {
    OutlinedCardContainer(modifier = modifier) {
        Column(horizontalAlignment = Alignment.Start) {
            Image(
                painter = painterResource(icon),
                contentDescription = null,
                modifier = Modifier.size(30.dp)
            )
            Spacer(Modifier.padding(top = 10.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.padding(top = 4.dp))
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
