package com.crescenzi.esptoolbox.presentation.main_shell.usb_connection

import android.app.Application
import android.content.Context
import android.hardware.usb.UsbManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crescenzi.esptoolbox.R
import com.crescenzi.esptoolbox.core.LOG
import com.crescenzi.esptoolbox.presentation.util.getMessage
import com.crescenzi.esptoolbox.core.values.Constants
import com.crescenzi.esp32.LogRepo
import com.crescenzi.esptoolbox.data.phone.data.DeviceRepo
import com.crescenzi.esp32.usb.UsbRepo
import com.crescenzi.esp32.usb.model.LogLevel
import com.crescenzi.esp32.usb.model.UsbConnectionArgs
import com.crescenzi.esp32.usb.model.UsbStatus
import com.crescenzi.esp32.usb.UsbPermission
import com.crescenzi.esp32.usb.getCustomProber
import com.hoho.android.usbserial.driver.UsbSerialProber
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

/**
 * Handles USB connection
 */
class USBConnectionViewModel(
    private val application: Application,
    private val usbRepo: UsbRepo,
    private val deviceRepo: DeviceRepo,
    val logRepo: LogRepo
) : ViewModel() {

    val currentDeviceSnapshot: StateFlow<UsbStatus.SnapshotUsb> = usbRepo._currentDevice
        .map { usbDevice -> usbDevice?.toSnapshotUsbDevice() ?: UsbStatus.SnapshotUsb() }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Companion.WhileSubscribed(5000),
            initialValue = UsbStatus.SnapshotUsb()
        )

    val ssidState = deviceRepo.ssid

    val currentDevice = usbRepo._currentDevice
        .asStateFlow()

    private var observeJob: Job? = null

    var usbPermission = usbRepo._usbPermission.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()


    /**
     * As soon as permissions are granted, logging starts
     */
    fun updateUsbPermission(usbPermission: UsbPermission) {
        usbRepo._usbPermission.value = usbPermission
        if (usbPermission == UsbPermission.GRANTED) {
            usbRepo.readLog()
        }
    }

    fun openObserver(context: Context) {
        val usbManager = context.getSystemService(Context.USB_SERVICE) as UsbManager
        observeJob?.cancel()


        observeJob = CoroutineScope(Dispatchers.Default).launch {
            try {
                while (isActive) {
                    val device =
                        usbManager.deviceList.values.firstOrNull() // take the first device

                    /**
                     * Checks if the device name is correct
                     */
                    if (device != null) {
                        val usbDefaultProber = UsbSerialProber.getDefaultProber()
                        val usbCustomProber = getCustomProber()
                        var driver = usbDefaultProber.probeDevice(device)
                        if (driver == null) driver = usbCustomProber.probeDevice(device)

                        val usbStatus =
                            if (driver != null) UsbStatus(device, 0, driver) else UsbStatus(
                                device,
                                0,
                                null
                            )

                        usbRepo._currentDevice.value = usbStatus
                    } else {
                        updateUsbPermission(UsbPermission.NOT_GRANTED)
                        usbRepo._currentDevice.value = null
                    }
                    delay(Constants.OBSERVE_STATUS_DELAY)
                }
            } catch (e: Exception) {
                LOG("Exception usb ${e.message.toString()}")
                usbRepo._currentDevice.value = null
            }
        }
    }

    /**
     * Main-safety function, if permissions are missing the fallback is triggered
     * If no user-overridden SSID is provided, the detected one is used
     */
    fun sendCredentials(
       usbConnectionArgs: UsbConnectionArgs,
        permissionFallback: () -> Unit
    ) {
        viewModelScope.launch {
            if (usbRepo._usbPermission.value == UsbPermission.NOT_GRANTED) {
                permissionFallback()
            } else {

                if (usbConnectionArgs.ssid.trim().isEmpty() || usbConnectionArgs.pwd.trim().isEmpty()) return@launch
                _loading.value = true
                usbRepo.writeCredentials(usbConnectionArgs).onSuccess {
                    _loading.value = false
                    logRepo.plusLog(
                        line = application.baseContext?.getString(R.string.connection_successfully)
                            .toString()
                    )
                }.onFailure { exception ->
                    _loading.value = false
                    application.baseContext?.let { context ->
                        logRepo.plusLog(
                            line = getMessage(context, exception)
                                .toString(), logLevel = LogLevel.ERROR
                        )
                    }


                }
            }
        }
    }

    fun clearAll() {
        observeJob?.cancel()
        observeJob = null
        usbRepo.disconnect()
    }

    override fun onCleared() {
        super.onCleared()
        clearAll()
    }


}