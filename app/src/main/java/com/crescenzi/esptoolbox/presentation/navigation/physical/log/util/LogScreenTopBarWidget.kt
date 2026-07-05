package com.crescenzi.esptoolbox.presentation.navigation.physical.log.util

import android.content.ClipData
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalClipboard
import androidx.compose.ui.platform.LocalHapticFeedback
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
    val haptic = LocalHapticFeedback.current

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.End
    ) {

        /**
         * Copy to clipboard (filled)
         */
        val copyInteractionSource = remember { MutableInteractionSource() }
        val copyPressed by copyInteractionSource.collectIsPressedAsState()
        val copyScale by animateFloatAsState(
            targetValue = if (copyPressed) 0.95f else 1f,
            label = "copy_btn_scale"
        )

        Button(
            enabled = hasLogs,
            shape = RoundedCornerShape(20.dp),
            interactionSource = copyInteractionSource,
            modifier = Modifier.scale(copyScale),
            onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
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
        val clearInteractionSource = remember { MutableInteractionSource() }
        val clearPressed by clearInteractionSource.collectIsPressedAsState()
        val clearScale by animateFloatAsState(
            targetValue = if (clearPressed) 0.95f else 1f,
            label = "clear_btn_scale"
        )

        OutlinedButton(
            enabled = hasLogs,
            shape = RoundedCornerShape(20.dp),
            interactionSource = clearInteractionSource,
            modifier = Modifier.scale(clearScale),
            onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                logViewModel.logRepo.cleanLog()
            },
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
