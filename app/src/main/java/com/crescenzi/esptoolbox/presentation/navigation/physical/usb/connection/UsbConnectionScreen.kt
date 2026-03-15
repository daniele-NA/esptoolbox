package com.crescenzi.esptoolbox.presentation.navigation.physical.usb.connection

import android.location.LocationManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.location.LocationManagerCompat.isLocationEnabled
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.crescenzi.esptoolbox.R
import com.crescenzi.esptoolbox.core.base.BaseComponentActivity.Companion.APP_NAME
import com.crescenzi.esptoolbox.core.presentation.widget.CardContainer
import com.crescenzi.esptoolbox.core.presentation.widget.InfoTile
import com.crescenzi.esptoolbox.core.values.Constants
import com.crescenzi.esptoolbox.data.core.params.BaudRateFormat
import com.crescenzi.esptoolbox.data.core.params.SerialFormat
import com.crescenzi.esptoolbox.data.usb.data.model.UsbConnectionArgs
import com.crescenzi.esptoolbox.presentation.navigation.physical.usb.connection.util.UsbConnectionStatusWidget
import com.crescenzi.esptoolbox.presentation.navigation.physical.usb.connection.util.UsbConnectionCredentialsWidget
import com.crescenzi.esptoolbox.presentation.navigation.physical.usb.connection.util.UsbConnectionActionsWidget
import com.crescenzi.esptoolbox.presentation.navigation.physical.usb.connection.util.UsbConnectionSerialFormatWidget
import com.crescenzi.esptoolbox.presentation.navigation.physical.usb.core.UsbBaudRateWidget

/**
 * There will always be only one device connected via USB
 */
@Composable
fun UsbConnectionScreen(
    usbConnectionViewModel: UsbConnectionViewModel,
    onReqUsbPermission: () -> Unit
) {
    val context = LocalContext.current

    /**
     * Only at startup
     */
    LaunchedEffect(Unit) {
        usbConnectionViewModel.openObserver(context)
    }


    val deviceSnapshot by (usbConnectionViewModel.currentDeviceSnapshot.collectAsStateWithLifecycle())

    val ssid = rememberSaveable { mutableStateOf(usbConnectionViewModel.ssidState.value) }
    val pwd = rememberSaveable { mutableStateOf("") }
    val baudRate = remember { mutableStateOf(BaudRateFormat.B115200) }
    val format = remember { mutableStateOf<SerialFormat>(SerialFormat.Plain) }



    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(
                horizontal = Constants.HORIZONTAL_PADDING
            ),
        horizontalAlignment = Alignment.Start
    ) {


        CardContainer(modifier = Modifier.fillMaxWidth()) {
            Column {
                Image(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(vertical = Constants.TOP_PADDING),
                    painter = painterResource(R.drawable.usb_icon),
                    contentDescription = null
                )
                UsbConnectionStatusWidget(deviceSnapshot)
                Text(
                    modifier = Modifier.padding(vertical = 20.dp),
                    text = stringResource(R.string.board_name),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )

                /**
                 * Warns that location is disabled
                 */
                if (isLocationEnabled(LocalContext.current.getSystemService(LocationManager::class.java) as LocationManager) == false) {
                    Text(
                        modifier = Modifier.padding(vertical = 10.dp),
                        text = stringResource(R.string.position_warning, APP_NAME),
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold
                        ), color = MaterialTheme.colorScheme.error
                    )
                }
                UsbConnectionCredentialsWidget(ssid, pwd)

                Spacer(modifier = Modifier.padding(vertical = 12.dp))

                UsbBaudRateWidget(
                    selectedBaudRate = baudRate.value,
                    onBaudRateSelected = { baudRate.value = it })


                UsbConnectionSerialFormatWidget(
                    selectedFormat = format.value,
                    onFormatSelected = { format.value = it })




                UsbConnectionActionsWidget(
                    Modifier.align(Alignment.CenterHorizontally),
                    usbConnectionViewModel,
                ) {
                    /*
                     */

                    /**
                     * - Button onClick  -->  Send credentials or request permissions
                     */
                    usbConnectionViewModel.sendCredentials(
                        UsbConnectionArgs(
                            ssid.value,
                            pwd.value,
                            format.value,
                            baudRate.value
                        ), onReqUsbPermission
                    )

                }


            }
        }


        InfoTile(text = stringResource(R.string.usb_connection_info))

    }


}
