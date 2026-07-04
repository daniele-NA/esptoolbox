package com.crescenzi.esptoolbox.presentation.navigation.physical.usb.core

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
import com.crescenzi.esp32.params.BaudRateFormat



/*
Manages the baud rate
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsbBaudRateWidget(
    selectedBaudRate: BaudRateFormat,
    onBaudRateSelected: (BaudRateFormat) -> Unit,
) {

    var baudRateExpanded by rememberSaveable { mutableStateOf(false) }


    // Baud Rate Dropdown
    ExposedDropdownMenuBox(
        expanded = baudRateExpanded,
        onExpandedChange = { baudRateExpanded = it }
    ) {
        TextField(
            readOnly = true,
            value = selectedBaudRate.value.toString(),
            onValueChange = {},
            label = { Text("Baud Rate") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = baudRateExpanded) },
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = baudRateExpanded,
            onDismissRequest = { baudRateExpanded = false },
            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
            shape = RoundedCornerShape(CARD_CORNER)
        ) {
            BaudRateFormat.entries.forEach { rate ->
                DropdownMenuItem(
                    text = {
                        Text(
                            "${rate.value} bps",
                            style = MaterialTheme.typography.labelMedium
                        )
                    },
                    onClick = {
                        onBaudRateSelected(rate)
                        baudRateExpanded = false
                    }
                )
            }
        }
    }

}