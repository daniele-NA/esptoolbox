package com.crescenzi.esptoolbox.presentation.navigation.physical.log.util

import android.content.ClipData
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.crescenzi.esptoolbox.R
import com.crescenzi.esptoolbox.presentation.navigation.physical.log.LogViewModel

@Composable
fun LogScreenTopBarWidget(logViewModel: LogViewModel) {

    val localClipboard = LocalClipboard.current
    val logs by logViewModel.logRepo.logs.collectAsStateWithLifecycle()
    val hasLogs = logs.isNotEmpty()

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {

        /**
         * Copy to clipboard (filled)
         */
        Button(
            enabled = hasLogs,
            onClick = {
                localClipboard.nativeClipboard.setPrimaryClip(
                    ClipData.newPlainText("Log", logViewModel.logRepo.toString())
                )
            }
        ) {
            Text(
                text = stringResource(R.string.btn_copy),
                style = MaterialTheme.typography.labelMedium
            )
        }

        Spacer(Modifier.width(10.dp))

        /**
         * Clear logs (outlined, red)
         */
        OutlinedButton(
            enabled = hasLogs,
            onClick = { logViewModel.logRepo.cleanLog() },
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = MaterialTheme.colorScheme.error
            ),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
        ) {
            Text(
                text = stringResource(R.string.btn_clear),
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}
