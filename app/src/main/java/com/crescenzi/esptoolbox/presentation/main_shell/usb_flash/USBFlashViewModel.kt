package com.crescenzi.esptoolbox.presentation.main_shell.usb_flash

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.crescenzi.esptoolbox.R
import com.crescenzi.esptoolbox.core.LOG
import com.crescenzi.esptoolbox.presentation.util.getMessage
import com.crescenzi.esp32.LogRepo
import com.crescenzi.esp32.params.BaudRateFormat
import com.crescenzi.esp32.usb.UsbRepo
import com.crescenzi.esp32.usb.model.LogLevel
import com.crescenzi.esp32.firmware.EspRepo
import com.crescenzi.esp32.firmware.EspCallback
import com.crescenzi.esptoolbox.presentation.main_shell.usb_flash.FlashFileEntry
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.InputStream
import java.util.concurrent.locks.ReentrantLock

/**
 * Handles operations such as Flash
 */
class USBFlashViewModel(
    private val usbRepo: UsbRepo,
    private val espRepo: EspRepo,
    val logRepo: LogRepo
) : ViewModel() {

    private val _baudRate = MutableStateFlow(BaudRateFormat.B115200)
    val baudRate = _baudRate.asStateFlow()

    fun updateBaudRate(baudRateFormat: BaudRateFormat) {
        _baudRate.value = baudRateFormat
    }

    private val _flashFiles = MutableStateFlow(
        listOf(
            FlashFileEntry(address = 0x8000),
            FlashFileEntry(address = 0x1000),
            FlashFileEntry(address = 0x10000),
            FlashFileEntry(address = 0x9000),
            FlashFileEntry(address = 0x20000),
        )
    )
    val flashFiles = _flashFiles.asStateFlow()

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    fun updateFlashFile(index: Int, label: String, address: Int=0, uri: Uri?) {
        val current = _flashFiles.value.toMutableList()
        current[index] = current[index].copy(label = label, address = address, uri = uri)
        _flashFiles.value = current
    }



    init {
        espRepo.setEspCallback(espCallback = object : EspCallback {
            override fun onInfo(line: String) {
                LOG("On Info $line")
                logRepo.plusLog(line, LogLevel.INFO)
            }


            override fun onFlashLoading(percentage: Int) {
                logRepo.plusLog("$percentage %", LogLevel.INFO)
            }

            override fun onError(e: Throwable) {
                LOG("Exception ${e.message.toString()}")
                logRepo.plusLog(e.message.toString(), LogLevel.ERROR)
            }

        })
    }

    private val flashLock = ReentrantLock()


    fun commandReset() = usbRepo.reset()

    /**
     * Firmware flash
     * CANNOT INTERCEPT THE PERMISSION REQUEST
     */
    fun flash(
        context: Context
    ) {
        Thread {
            var acquired = false
            try {
                acquired = flashLock.tryLock()
                if (!acquired) {
                    logRepo.plusLog(
                        context.getString(R.string.flash_in_progress_warning),
                        LogLevel.WARNING
                    )
                    return@Thread
                }

                _loading.value = true

                espRepo.setBaudRateCallback { _baudRate.value }

                if (espRepo.chipValidation()) {
                    for (item in _flashFiles.value) {
                        logRepo.plusLog(
                            context.getString(R.string.flash_do_not_disconnect_usb),
                            LogLevel.WARNING
                        )
                        item.uri?.let {
                            LOG(_flashFiles.value.toString())


                            val firmware: InputStream =
                                context.contentResolver.openInputStream(it) ?: return@let

                            espRepo.apply {
                                changeBaudRate()
                                init()
                                readFile(firmware)?.let { byteArray ->
                                    espRepo.flashFirmware(byteArray, item.address)
                                }
                            }
                        } ?: run {
                            logRepo.plusLog(
                                context.getString(R.string.invalid_file_detected),
                                LogLevel.WARNING
                            )
                        }
                    }
                    logRepo.plusLog(
                        context.getString(R.string.flash_rst), LogLevel.WARNING
                    )
                    _loading.value = false

                } else {
                    espRepo.reqPermission()
                    _loading.value = false
                }

            } catch (e: Exception) {
                _loading.value = false
                logRepo.plusLog(getMessage(context, e), LogLevel.ERROR)
            } finally {
                if (acquired) {
                    flashLock.unlock()
                }
            }
        }.start()
    }


}