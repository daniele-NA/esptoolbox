package com.crescenzi.esptoolbox.presentation.navigation.home.util

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.crescenzi.esptoolbox.R
import com.crescenzi.esptoolbox.core.base.BaseComponentActivity.Companion.APP_NAME
import com.crescenzi.esptoolbox.core.presentation.widget.CardContainer
import com.crescenzi.esptoolbox.core.presentation.widget.InfoTile

@Composable
fun InfoSection() {
    Text(
        text = stringResource(R.string.info_tool),
        style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
        color = MaterialTheme.colorScheme.primary
    )
    Spacer(Modifier.padding(bottom = 10.dp))


    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp) // optional: space between cards
        ) {
            CardContainer(
                modifier = Modifier.weight(1f),
                cardColors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Column(horizontalAlignment = Alignment.Start) {
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.usb_icon),
                            contentDescription = null,
                        )
                    }
                    Text(
                        modifier = Modifier.padding(top = 13.dp, bottom = 8.dp),
                        text = stringResource(R.string.usb_desc),
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = MaterialTheme.colorScheme.inversePrimary,
                            fontWeight = FontWeight.Bold
                        ), maxLines = 1, overflow = TextOverflow.Ellipsis
                    )
                }
            }

            CardContainer(
                modifier = Modifier.weight(1f),
                cardColors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Column {
                    Row(
                        Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.wifi_icon),
                            contentDescription = null,
                        )
                    }

                    Text(
                        modifier = Modifier.padding(top = 13.dp, bottom = 8.dp),
                        text = stringResource(R.string.wifi_desc),
                        style = MaterialTheme.typography.labelMedium.copy(
                            color = MaterialTheme.colorScheme.inversePrimary,
                            fontWeight = FontWeight.Bold
                        ), maxLines = 1, overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }

        InfoTile(stringResource(R.string.info_desc, APP_NAME))

    }


}

