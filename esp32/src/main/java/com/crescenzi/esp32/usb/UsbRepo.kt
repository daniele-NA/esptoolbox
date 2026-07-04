package com.crescenzi.esp32.usb

import android.content.Context
import android.hardware.usb.UsbDeviceConnection
import android.hardware.usb.UsbManager
import com.crescenzi.esp32.internal.LOG
import com.crescenzi.esp32.internal.safeLet
import com.crescenzi.esp32.internal.WRITE_WAIT_MILLIS
import com.crescenzi.esp32.LogRepo
import com.crescenzi.esp32.params.BaudRateFormat
import com.crescenzi.esp32.usb.model.LogLevel
import com.crescenzi.esp32.usb.model.UsbConnectionArgs
import com.crescenzi.esp32.usb.model.UsbStatus
import com.crescenzi.esp32.usb.UsbPermission
import com.crescenzi.esp32.usb.getCustomProber
import com.crescenzi.esp32.exception.UsbConnectionException
import com.hoho.android.usbserial.driver.UsbSerialDriver
import com.hoho.android.usbserial.driver.UsbSerialPort
import com.hoho.android.usbserial.driver.UsbSerialProber
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.lang.Thread.sleep


/**
 * Handles low-level interaction with the ESP32
 */
class UsbRepo(
    private val context: Context, val logRepo: LogRepo
) {


    var _usbPermission = MutableStateFlow(UsbPermission.NOT_GRANTED)
    val _currentDevice = MutableStateFlow<UsbStatus?>(null)

    var currentBaudRateFormat= BaudRateFormat.B115200


    private var usbSerialPort: UsbSerialPort? = null
    private var usbDeviceConnection: UsbDeviceConnection? = null

    /**
     * Connects to the USB device (suspendable)
     * The write format is passed as parameter
     */
    suspend fun writeCredentials(usbConnectionArgs: UsbConnectionArgs): Result<Any> =
        withContext(Dispatchers.IO) {
            try {
                currentBaudRateFormat=usbConnectionArgs.baudRateFormat
                initSerialPort()
                safeLet(usbSerialPort, _currentDevice.value) { port, status ->
                    port.write(
                        usbConnectionArgs.serialFormat.serialize(
                            usbConnectionArgs.ssid, usbConnectionArgs.pwd
                        ), WRITE_WAIT_MILLIS
                    )
                }
                sleep(3000L)
                return@withContext Result.success(Any())
            } catch (_: Exception) {
                disconnect()
                return@withContext Result.failure(UsbConnectionException())
            }
        }


    /**
     * Started for each connection and cleaned up afterwards
     */
    fun readLog() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                initSerialPort()
                val buffer = ByteArray(1024)

                while (isActive && usbSerialPort != null) {
                    val len = usbSerialPort?.read(buffer, 100) ?: 0
                    val data = buffer.copyOf(len).toString(Charsets.UTF_8).trim()
                    if (data.isNotEmpty()) {
                        logRepo.plusLog(data, LogLevel.INFO)

                    }
                    delay(50)
                }
            } catch (e: Exception) {
                logRepo.plusLog(e.message.toString(), LogLevel.ERROR)
                disconnect()

            }
        }
    }


    /**
     * Equivalent to pressing the buttons:
     *
     * @dtr [RESET]
     * @rts [BOOT]
     */
    fun reset() {
        try {
            initSerialPort()

            //Clear logs
            logRepo.cleanLog()

            usbSerialPort?.let {
                it.dtr = false //reset
                it.rts = true  //boot
                sleep(100)
                it.dtr = true  //reset
                it.rts = false //boot
            }
        } catch (e: Exception) {
            logRepo.plusLog(e.message.toString(), LogLevel.ERROR)
            LOG("Exception while resetting ${e.message.toString()}")
        }

    }


    /**
     * Persistent method to never miss permissions
     */
    private fun initSerialPort() {
        if (_usbPermission.value != UsbPermission.GRANTED) throw UsbConnectionException()
        val usbManager = context.getSystemService(Context.USB_SERVICE) as UsbManager
        val defaultProber = UsbSerialProber.getDefaultProber()
        val customProber = getCustomProber()

        var driver: UsbSerialDriver? = defaultProber.probeDevice(_currentDevice.value?.device)
        if (driver == null) driver = customProber.probeDevice(_currentDevice.value?.device)
        if (driver == null) throw UsbConnectionException()

        _currentDevice.value?.port?.let { if (it >= driver.ports.size) throw UsbConnectionException() }
        val port = driver.ports[_currentDevice.value?.port ?: 0]
        val connection = usbManager.openDevice(driver.device) ?: throw UsbConnectionException()

        port.open(connection)

        /**
         * x,8,1,0
         */
        port.setParameters(
            currentBaudRateFormat.value,
            UsbSerialPort.DATABITS_8, UsbSerialPort.STOPBITS_1, UsbSerialPort.PARITY_NONE
        )

        usbSerialPort = port
        usbDeviceConnection = connection
    }


    /**
     * Disconnects the USB port and resets the state
     */
    fun disconnect() {
        try {
            usbSerialPort?.close()
        } catch (_: IOException) {
        }
        usbSerialPort = null
        usbDeviceConnection = null
        _usbPermission.value = UsbPermission.NOT_GRANTED
    }
}