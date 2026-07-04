package com.crescenzi.esptoolbox.presentation.navigation.physical.usb.updater

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.crescenzi.esptoolbox.R
import com.crescenzi.esptoolbox.core.function.getFileNameWithoutBin
import com.crescenzi.esptoolbox.presentation.util.getMessage
import com.crescenzi.esptoolbox.presentation.widget.EditText
import com.crescenzi.esptoolbox.core.values.Constants.HORIZONTAL_PADDING
import com.crescenzi.esptoolbox.core.values.Constants.PICK_MIME_TYPE
import com.crescenzi.esp32.usb.model.LogLevel
import com.crescenzi.esptoolbox.presentation.navigation.physical.usb.core.UsbBaudRateWidget
import com.crescenzi.esptoolbox.presentation.navigation.physical.usb.updater.util.UsbUpdaterButtonsWidget

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun UsbUpdaterScreen(usbUpdaterViewModel: UsbUpdaterViewModel) {

    val context = LocalContext.current
    val loading by usbUpdaterViewModel.loading.collectAsStateWithLifecycle()

    val baudRate by usbUpdaterViewModel.baudRate.collectAsStateWithLifecycle()
    val flashFiles by usbUpdaterViewModel.flashFiles.collectAsStateWithLifecycle()

    // == Per-file address validity; Flash is enabled only with >=1 attached file and valid addresses == //
    val addressValid = remember { List(flashFiles.size) { true }.toMutableStateList() }
    val flashEnabled = flashFiles.any { it.uri != null } &&
            flashFiles.indices.all { flashFiles[it].uri == null || addressValid[it] }

    /**
     * 5 Launcher
     */
    val pickers = List(flashFiles.size) { index ->
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.OpenDocument()
        ) { uri: Uri? ->
            uri?.let {
                usbUpdaterViewModel.updateFlashFile(
                    index = index,
                    label = it.getFileNameWithoutBin(context).toString(),
                    uri = it
                )
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(WindowInsets.navigationBars.asPaddingValues())
                .padding(horizontal = HORIZONTAL_PADDING)
                .padding(top = 16.dp, bottom = 110.dp)
        ) {

            Text(
                modifier = Modifier.padding(bottom = 8.dp),
                text = stringResource(R.string.baud_rate_title),
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.inversePrimary,
                    fontWeight = FontWeight.Bold
                ), maxLines = 1, overflow = TextOverflow.Ellipsis
            )

            UsbBaudRateWidget(
                selectedBaudRate = baudRate,
                onBaudRateSelected = {
                    usbUpdaterViewModel.updateBaudRate(it)
                }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 18.dp))

            Text(
                modifier = Modifier.padding(bottom = 12.dp),
                text = stringResource(R.string.files_title),
                style = MaterialTheme.typography.labelMedium.copy(
                    color = MaterialTheme.colorScheme.inversePrimary,
                    fontWeight = FontWeight.Bold
                ), maxLines = 1, overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier.padding(bottom = 5.dp),
                text = stringResource(R.string.files_description),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error,
            )

            flashFiles.forEachIndexed { index, fileEntry ->

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    val iconRes =
                        if (fileEntry.uri != null) R.drawable.remove_icon else R.drawable.attach_icon

                    Icon(
                        painter = painterResource(id = iconRes),
                        contentDescription = null,
                        tint = null,
                        modifier = Modifier
                            .size(28.dp)
                            .clickable {
                                if (fileEntry.uri != null) {
                                    // remove file reference
                                    usbUpdaterViewModel.updateFlashFile(
                                        index,
                                        label = ".bin",
                                        address = 0,
                                        uri = null
                                    )
                                    addressValid[index] = true
                                } else {
                                    // open picker
                                    pickers[index].launch(arrayOf(PICK_MIME_TYPE))
                                }
                            }
                    )
                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = fileEntry.label,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        modifier = Modifier.weight(1f)
                    )
                    Box(modifier = Modifier.weight(1f)) {
                        key(index, fileEntry.uri) {
                            EditText(
                                opt = KeyboardOptions.Default,
                                label = "",
                                initialValue = "0x${fileEntry.address.toString(16)}",
                                onValueChange = { newText ->
                                    try {
                                        val parsed = newText
                                            .trim()
                                            .lowercase()
                                            .removePrefix("0x")
                                            .toIntOrNull(16)

                                        addressValid[index] = parsed != null

                                        if (parsed != null) {
                                            usbUpdaterViewModel.updateFlashFile(
                                                index = index,
                                                label = fileEntry.label,
                                                uri = fileEntry.uri,
                                                address = parsed
                                            )
                                        }
                                    } catch (e: Exception) {
                                        usbUpdaterViewModel.logRepo.plusLog(
                                            getMessage(context, e),
                                            LogLevel.ERROR
                                        )
                                    }
                                }
                            )
                        }
                    }

                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            UsbUpdaterButtonsWidget(
                enabled = flashEnabled,
                onReset = usbUpdaterViewModel::commandReset,
                onFlash = {
                    /**
                     * Takes all entries and flashes them
                     */
                    usbUpdaterViewModel.flash(context)
                }
            )
            Spacer(Modifier.padding(bottom = 15.dp))
        }

        if (loading) {
            LoadingIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}
