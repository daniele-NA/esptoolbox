package com.crescenzi.esptoolbox.presentation.navigation.home.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.annotation.DrawableRes
import com.crescenzi.esptoolbox.R
import com.crescenzi.esptoolbox.presentation.widget.OutlinedCardContainer
import com.crescenzi.esptoolbox.theme.titlesFont

import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.foundation.layout.fillMaxSize

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
            modifier = Modifier.size(54.dp)
        )
        Spacer(Modifier.width(14.dp))
        Text(
            text = stringResource(R.string.application_name),
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily(titlesFont)
            ),
            color = MaterialTheme.colorScheme.primary
        )
    }

    Spacer(Modifier.padding(top = 28.dp))

    /**
     * Two outlined info cards, staggered and asymmetrical
     */
    Row(
        modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        InfoCard(
            modifier = Modifier.weight(1.05f).offset(y = (-8).dp),
            shape = RoundedCornerShape(topStart = 36.dp, topEnd = 12.dp, bottomStart = 12.dp, bottomEnd = 36.dp),
            icon = R.drawable.usb_icon,
            title = stringResource(R.string.usb_desc),
            subtitle = stringResource(R.string.usb_subdesc)
        )
        InfoCard(
            modifier = Modifier.weight(0.95f).offset(y = 12.dp),
            shape = RoundedCornerShape(topStart = 12.dp, topEnd = 36.dp, bottomStart = 36.dp, bottomEnd = 12.dp),
            icon = R.drawable.wifi_icon,
            title = stringResource(R.string.wifi_desc),
            subtitle = stringResource(R.string.wifi_subdesc)
        )
    }
}

@Composable
private fun InfoCard(
    modifier: Modifier = Modifier,
    shape: androidx.compose.ui.graphics.Shape = RoundedCornerShape(28.dp),
    @DrawableRes icon: Int,
    title: String,
    subtitle: String
) {
    val primaryColor = MaterialTheme.colorScheme.primary
    val secondaryColor = MaterialTheme.colorScheme.secondary

    OutlinedCardContainer(modifier = modifier, shape = shape) {
        Box(modifier = Modifier.fillMaxWidth()) {
            // == Dynamic background Canvas blueprints pattern == //
            Canvas(modifier = Modifier.matchParentSize()) {
                val pathColor = primaryColor.copy(alpha = 0.05f)
                val lineColor = secondaryColor.copy(alpha = 0.03f)
                
                // Draw decorative intersecting circles
                drawCircle(
                    color = pathColor,
                    radius = size.minDimension / 1.3f,
                    center = androidx.compose.ui.geometry.Offset(size.width * 0.95f, size.height * 0.15f),
                    style = Stroke(width = 1.5.dp.toPx())
                )
                drawCircle(
                    color = pathColor,
                    radius = size.minDimension / 2.0f,
                    center = androidx.compose.ui.geometry.Offset(size.width * 0.95f, size.height * 0.15f),
                    style = Stroke(width = 1.dp.toPx())
                )
                // Draw technical grid line
                drawLine(
                    color = lineColor,
                    start = androidx.compose.ui.geometry.Offset(0f, size.height * 0.7f),
                    end = androidx.compose.ui.geometry.Offset(size.width, size.height * 0.3f),
                    strokeWidth = 1.dp.toPx()
                )
            }

            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier.fillMaxWidth()
            ) {
                Box(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.12f), shape = CircleShape)
                        .padding(8.dp)
                ) {
                    Image(
                        painter = painterResource(icon),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(Modifier.padding(top = 14.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.padding(top = 6.dp))
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
