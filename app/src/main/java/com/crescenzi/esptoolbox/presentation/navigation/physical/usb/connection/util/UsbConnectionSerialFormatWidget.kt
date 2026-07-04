package com.crescenzi.esptoolbox.presentation.navigation.physical.usb.connection.util

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.crescenzi.esptoolbox.core.values.Constants.CARD_CORNER
import com.crescenzi.esp32.params.SerialFormat


/**
 * Manages serial format settings
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsbConnectionSerialFormatWidget(
    selectedFormat: SerialFormat,
    onFormatSelected: (SerialFormat) -> Unit
) {
    var formatExpanded by rememberSaveable { mutableStateOf(false) }

    val serialFormats = listOf(
        SerialFormat.Plain,
        SerialFormat.Json,
        SerialFormat.AT,
        SerialFormat.Csv,
        SerialFormat.Credentials,
        SerialFormat.Custom
    )


    // Serial Format Dropdown
    ExposedDropdownMenuBox(
        expanded = formatExpanded,
        onExpandedChange = { formatExpanded = it }
    ) {
        TextField(
            readOnly = true,
            value = selectedFormat.description,
            onValueChange = {},
            label = { Text("Format Serial") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = formatExpanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = formatExpanded,
            onDismissRequest = { formatExpanded = false },
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
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
                        onFormatSelected(format)
                        formatExpanded = false
                    }
                )
            }
        }

    }

}