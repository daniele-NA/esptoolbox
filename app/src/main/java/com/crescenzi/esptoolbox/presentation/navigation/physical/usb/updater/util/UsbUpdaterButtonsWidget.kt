package com.crescenzi.esptoolbox.presentation.navigation.physical.usb.updater.util


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.crescenzi.esptoolbox.R
import com.crescenzi.esptoolbox.core.presentation.widget.OutlinedCardContainer


/**
 * RESET and FLASH section
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun UsbUpdaterButtonsWidget(onReset: () -> Unit, onFlash: () -> Unit) {

    OutlinedCardContainer(
        applyOuterPadding = false, applyInnerPadding = false,

        content = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {


                /**
                 * Reset btn
                 */
                TextButton(
                    shapes = ButtonDefaults.shapes(),
                    onClick = {
                        onReset()
                    }) {
                    Text(
                        text = stringResource(R.string.btn_reset),
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }


                /**
                Flash Btn
                 */
                Button(
                    shapes = ButtonDefaults.shapes(),
                    onClick = {
                        onFlash()
                    }) {
                    Text(
                        text = stringResource(R.string.btn_flash),
                        style = MaterialTheme.typography.labelMedium)
                }

            }
        })
}