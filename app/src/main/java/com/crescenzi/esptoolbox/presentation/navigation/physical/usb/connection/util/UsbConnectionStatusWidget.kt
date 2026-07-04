package com.crescenzi.esptoolbox.presentation.navigation.physical.usb.connection.util

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.crescenzi.esptoolbox.R
import com.crescenzi.esp32.usb.model.UsbStatus.SnapshotUsb

/**
 * Shows some of the board characteristics via USB
 */
@Composable
fun UsbConnectionStatusWidget(deviceSnapshot: SnapshotUsb) {
    val labels = listOf(
        stringResource(R.string.usb_version) to deviceSnapshot.version,
        stringResource(R.string.usb_manufacturer) to deviceSnapshot.manufacturerName,
        stringResource(R.string.usb_vendor_id) to deviceSnapshot.vendorId.toString(),
        stringResource(R.string.usb_product_id) to deviceSnapshot.productId.toString(),
        stringResource(R.string.usb_port) to deviceSnapshot.port.toString()
    )

    labels.forEach { (label, value) ->
        Row(modifier = Modifier.padding(vertical = 5.dp)) {
            Text(
                text = "$label:",
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.weight(1f)
            )
            Text(
                text = value,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
            )
        }
    }
}
