package com.crescenzi.esptoolbox.presentation.navigation.physical.usb.connection.util

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.unit.dp
import com.crescenzi.esptoolbox.core.values.Constants.CARD_CORNER
import com.crescenzi.esptoolbox.presentation.widget.EditText
import com.crescenzi.esp32.params.SerialFormat


/**
 * Manages serial format settings — custom dropdown anchored to our EditText field
 */
@Composable
fun UsbConnectionSerialFormatWidget(
    selectedFormat: SerialFormat,
    onFormatSelected: (SerialFormat) -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }
    val haptic = LocalHapticFeedback.current

    val serialFormats = listOf(
        SerialFormat.Plain,
        SerialFormat.Json,
        SerialFormat.AT,
        SerialFormat.Csv,
        SerialFormat.Credentials,
        SerialFormat.Custom
    )

    val arrowRotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = "arrow_rotation"
    )

    Box(modifier = Modifier.fillMaxWidth()) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                    expanded = true
                }
        ) {
            key(selectedFormat) {
                EditText(
                    modifier = Modifier.fillMaxWidth(),
                    opt = KeyboardOptions.Default,
                    onValueChange = {},
                    label = "Format Serial",
                    initialValue = selectedFormat.description,
                    readOnly = true
                )
            }
            Text(
                text = "▼",
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 26.dp)
                    .rotate(arrowRotation)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            containerColor = MaterialTheme.colorScheme.surfaceContainerHigh,
            shape = RoundedCornerShape(CARD_CORNER)
        ) {
            serialFormats.forEach { format ->
                DropdownMenuItem(
                    text = {
                        Text(
                            format.description,
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
                    onClick = {
                        haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        onFormatSelected(format)
                        expanded = false
                    }
                )
            }
        }
    }
}
