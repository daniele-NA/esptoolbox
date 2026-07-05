package com.crescenzi.esptoolbox.presentation.navigation.physical.usb.updater.util


import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.crescenzi.esptoolbox.R


/**
 * RESET and FLASH section
 */
@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun UsbUpdaterButtonsWidget(enabled: Boolean, onReset: () -> Unit, onFlash: () -> Unit) {

    val haptic = LocalHapticFeedback.current

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {

        /**
         * Reset btn
         */
        val resetInteractionSource = remember { MutableInteractionSource() }
        val resetPressed by resetInteractionSource.collectIsPressedAsState()
        val resetScale by animateFloatAsState(
            targetValue = if (resetPressed) 0.95f else 1f,
            label = "reset_btn_scale"
        )

        TextButton(
            shape = RoundedCornerShape(24.dp),
            interactionSource = resetInteractionSource,
            modifier = Modifier.scale(resetScale),
            onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
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
        val flashInteractionSource = remember { MutableInteractionSource() }
        val flashPressed by flashInteractionSource.collectIsPressedAsState()
        val flashScale by animateFloatAsState(
            targetValue = if (flashPressed) 0.95f else 1f,
            label = "flash_btn_scale"
        )

        Button(
            enabled = enabled,
            shape = RoundedCornerShape(24.dp),
            interactionSource = flashInteractionSource,
            modifier = Modifier.scale(flashScale),
            onClick = {
                haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                onFlash()
            }) {
            Text(
                text = stringResource(R.string.btn_flash),
                style = MaterialTheme.typography.labelMedium)
        }

    }
}