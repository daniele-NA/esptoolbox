package com.crescenzi.esptoolbox.presentation.navigation.home.util

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import com.crescenzi.esptoolbox.R
import com.crescenzi.esptoolbox.core.presentation.widget.CardContainer
import com.crescenzi.esptoolbox.core.values.Constants.GITHUB_URL

@Composable
fun AboutSection() {

    val context = LocalContext.current

    Text(
        text = stringResource(R.string.about_tool),
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.primary
    )
    Spacer(Modifier.padding(bottom = 10.dp))

    CardContainer(
        modifier = Modifier.fillMaxWidth(),
        cardColors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.Start
            ) {
                Text(
                    text = stringResource(R.string.about_desc),
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    modifier = Modifier.padding(top = 13.dp, bottom = 8.dp),
                    text = stringResource(R.string.follow_tool),
                    style = MaterialTheme.typography.labelMedium.copy(
                        color = MaterialTheme.colorScheme.inversePrimary,
                        fontWeight = FontWeight.Bold
                    )
                )

                Button(
                    onClick = {
                        context.startActivity(Intent(Intent.ACTION_VIEW, GITHUB_URL.toUri()))
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                ) {
                    Text(
                        text = stringResource(R.string.github_tool),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }

            Image(
                painter = painterResource(R.drawable.esp_icon),
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp)
                    .padding(start = 8.dp)
            )
        }
    }

}