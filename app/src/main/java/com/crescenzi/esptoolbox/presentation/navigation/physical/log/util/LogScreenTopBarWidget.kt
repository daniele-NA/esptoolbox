package com.crescenzi.esptoolbox.presentation.navigation.physical.log.util

import android.content.ClipData
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.crescenzi.esptoolbox.R
import com.crescenzi.esptoolbox.presentation.navigation.physical.log.LogViewModel

@Composable
fun LogScreenTopBarWidget(logViewModel: LogViewModel) {


    val localClipboard = LocalClipboard.current

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {
        /**
         * Copy to clipboard
         */
        IconButton(
            onClick = {
                localClipboard.nativeClipboard.setPrimaryClip(
                    ClipData.newPlainText("Log", logViewModel.toString())
                )
            }
        ) {
            /**
             * Small outer BG to stay above the log text
             */
            Icon(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(top = 5.dp, end = 5.dp),
                painter = painterResource(R.drawable.copy_icon),
                contentDescription = null
            )
        }

        /**
         * Clear logs
         */

        IconButton(
            onClick = {
                logViewModel.baseRepo.cleanLog()
            }
        ) {
            Icon(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background)
                    .padding(top = 5.dp, end = 5.dp),
                painter = painterResource(R.drawable.clear_all_icon),
                contentDescription = null
            )
        }
    }
}