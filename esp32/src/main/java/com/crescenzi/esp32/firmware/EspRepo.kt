package com.crescenzi.esp32.firmware

import android.content.Context
import com.crescenzi.esp32.params.BaudRateFormat
import com.crescenzi.esp32.params.EspModel
import com.crescenzi.esp32.firmware.EspRepoParams
import com.crescenzi.esp32.firmware.EspRepoParams.CHIP_DETECT_MAGIC_REG_ADDRESS
import com.crescenzi.esp32.firmware.EspRepoParams.ESP_SPI_SET_PARAMS
import com.crescenzi.esp32.firmware.appendArray
import com.crescenzi.esp32.firmware.checksum
import com.crescenzi.esp32.firmware.intToByteArray
import com.crescenzi.esp32.firmware.readRegister
import com.crescenzi.esp32.firmware.sendCommand
import com.crescenzi.esp32.firmware.subArray
import com.crescenzi.esp32.firmware.sync
import com.crescenzi.esp32.firmware.timeoutPerMb
import com.crescenzi.esp32.firmware.EspCallback
import com.physicaloid.lib.Physicaloid
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.lang.Thread.sleep
import kotlin.math.floor


/**
 * Firmware flash management
 *
 * No reset with this repo
 * No bootloader
 */
class EspRepo(private val context: Context) {


    class CmdRet {
        var retCode: Int = 0
        var retValue: ByteArray = ByteArray(512)
    }

    private lateinit var espCallback: EspCallback
    private lateinit var baudRateCallback: () -> BaudRateFormat
    private var physicalOid = Physicaloid(context).apply {
        setDebug(false)
    }


    /**
     * The BaudRate comes from another repo
     */
    fun setEspCallback(espCallback: EspCallback) {
        this.espCallback = espCallback
    }

    fun setBaudRateCallback(baudRateCallback: () -> (BaudRateFormat)) {
        this.baudRateCallback = baudRateCallback
    }

    /**
     * Opens and closes the port, permissions cannot be checked
     */
    fun reqPermission() {
        physicalOid.open()
        sleep(2000L)
        physicalOid.close()
    }

    /*
    *  Init chip
    *
    */
    fun chipValidation(): Boolean {
        var syncSuccess = false
        physicalOid.apply {
            open()
            setBaudrate(baudRateCallback.invoke().value)
            setParity(0)
            setStopBits(1)
        }

        drain()

        drain()

        espCallback.onInfo("Sync")
        // first do the sync
        for (i in 0..5) {
            espCallback.onInfo("Sync attempt:" + (i + 1))
            if (sync(physicalOid, espCallback) != 0) {
                syncSuccess = true
                espCallback.onInfo("Sync Success!!!")
                try {
                    sleep(1000)
                } catch (e: InterruptedException) {
                    espCallback.onError(e)

                }
                break
            }
        }
        val ret = syncSuccess && detectKnownChip()
        if (!ret) espCallback.onError(Exception("FAILED CHIP DETECTION : check permission and Esp mode"))

        return ret
    }

    /*
  * Send a command to the chip to find out what type it is
  * This is usefull for sending specific commands and know if the workflow can continue
  */
    fun detectKnownChip(): Boolean {
        val chipMagicValue = readRegister(CHIP_DETECT_MAGIC_REG_ADDRESS, physicalOid, espCallback)

        return EspModel.entries.any { chipModel ->
            chipMagicValue in chipModel.magicValues
        }
    }

    fun changeBaudRate() {
        val pkt = appendArray(intToByteArray(baudRateCallback.invoke().value), intToByteArray(0))
        sendCommand(
            EspRepoParams.ESP_CHANGE_BAUD_RATE.toByte(),
            pkt,
            0,
            100,
            physicalOid,
            espCallback
        )

        physicalOid.setBaudrate(baudRateCallback.invoke().value)
        try {
            sleep(50)
        } catch (e: InterruptedException) {
            espCallback.onError(e)

        }

        // flush anything on the port
        drain()
    }

    fun drain() {
        val buf = ByteArray(1)
        var endTime: Long
        val startTime = System.currentTimeMillis()
        do {
            physicalOid.read(buf, 1)
            endTime = System.currentTimeMillis()
        } while ((endTime - startTime) <= 1000)
    }


    fun flashBlock(data: ByteArray, seq: Int, timeout: Int): CmdRet {
        val retVal: CmdRet
        var pkt = appendArray(intToByteArray(data.size), intToByteArray(seq))
        pkt = appendArray(pkt, intToByteArray(0))
        pkt = appendArray(pkt, intToByteArray(0))
        pkt = appendArray(pkt, data)

        retVal = sendCommand(
            EspRepoParams.ESP_FLASH_DATA.toByte(),
            pkt,
            checksum(data),
            timeout,
            physicalOid,
            espCallback
        )
        return retVal
    }

    fun init() {
        val flashSize = 4 * 1024 * 1024

        val pkt = appendArray(intToByteArray(0), intToByteArray(0))
        sendCommand(EspRepoParams.ESP_SPI_ATTACH.toByte(), pkt, 0, 100, physicalOid, espCallback)


        espCallback.onInfo("Configuring flash size...")

        var pkt2 = appendArray(intToByteArray(0), intToByteArray(flashSize))
        pkt2 = appendArray(pkt2, intToByteArray(0x10000))
        pkt2 = appendArray(pkt2, intToByteArray(4096))
        pkt2 = appendArray(pkt2, intToByteArray(256))
        pkt2 = appendArray(pkt2, intToByteArray(0xFFFF))

        sendCommand(
            ESP_SPI_SET_PARAMS.toByte(),
            pkt2,
            0,
            100,
            physicalOid,
            espCallback
        )
    }


    fun flashFirmware(binaryData: ByteArray, address: Int) {
        val fileSize = binaryData.size

        espCallback.onInfo("Writing data with fileSize: $fileSize")

        val blocks = flashBegin(fileSize, binaryData.size, address = address)

        var seq = 0
        var position = 0


        val t1 = System.currentTimeMillis()

        var lastPercentage = -1

        while (binaryData.size - position > 0) {
            val percentage = (floor((100 * (seq + 1)).toDouble() / blocks.toDouble())).toInt()


            if (percentage % 10 == 0 && lastPercentage != percentage) {
                espCallback.onFlashLoading(percentage)
                lastPercentage = percentage
            }

            val block = if (binaryData.size - position >= EspRepoParams.FLASH_WRITE_SIZE) {
                subArray(binaryData, position, EspRepoParams.FLASH_WRITE_SIZE)
            } else {
                // Pad the last block
                subArray(binaryData, position, binaryData.size - position)
            }

            var retVal = flashBlock(block, seq, 100)
            if (retVal.retCode == -1) {
                //This should fix issue when writing is incorrect by trying again
                espCallback.onInfo("Retry because Ret code: ${retVal.retCode}")
                retVal = flashBlock(block, seq,  /*block_timeout*/100)
            }
            seq += 1
            position += EspRepoParams.FLASH_WRITE_SIZE
        }

        val t2 = System.currentTimeMillis()
        espCallback.onInfo("Took ${(t2 - t1)} ms to write $fileSize bytes,- Wait")
    }


    private fun flashBegin(size: Int, compSize: Int, address: Int): Int {
        val numBlocks =
            floor((compSize + EspRepoParams.FLASH_WRITE_SIZE - 1).toDouble() / EspRepoParams.FLASH_WRITE_SIZE.toDouble()).toInt()
        val eraseBlocks =
            floor((size + EspRepoParams.FLASH_WRITE_SIZE - 1).toDouble() / EspRepoParams.FLASH_WRITE_SIZE.toDouble()).toInt()
        // Start time
        val t1 = System.currentTimeMillis()

        val writeSize: Int = eraseBlocks * EspRepoParams.FLASH_WRITE_SIZE
        val timeout = timeoutPerMb(writeSize)


        espCallback.onInfo("Compressed $size bytes to $compSize...")

        var pkt = appendArray(intToByteArray(writeSize), intToByteArray(numBlocks))
        pkt = appendArray(pkt, intToByteArray(EspRepoParams.FLASH_WRITE_SIZE))
        pkt = appendArray(pkt, intToByteArray(address))
        pkt =
            appendArray(pkt, intToByteArray(0))


        sendCommand(
            EspRepoParams.ESP_FLASH_BEGIN.toByte(),
            pkt,
            0,
            timeout,
            physicalOid,
            espCallback
        )

        // end time
        val t2 = System.currentTimeMillis()
        if (size != 0) {
            espCallback.onInfo("Took  ${((t2 - t1) / 1000)}.${((t2 - t1) % 1000)}s to erase flash block\n")
        }
        return numBlocks
    }


    fun readFile(inputStream: InputStream): ByteArray? {
        var byteArrayOutputStream: ByteArrayOutputStream? = null

        var i: Int
        try {
            byteArrayOutputStream = ByteArrayOutputStream()
            i = inputStream.read()
            while (i != -1) {
                byteArrayOutputStream.write(i)
                i = inputStream.read()
            }
            inputStream.close()
        } catch (e: IOException) {
            espCallback.onError(e)
        }

        return byteArrayOutputStream!!.toByteArray()
    }

}