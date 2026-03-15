package com.crescenzi.esptoolbox.data.usb.firmware.data.util

import com.crescenzi.esptoolbox.data.usb.firmware.data.repository.EspRepo.CmdRet
import com.crescenzi.esptoolbox.data.usb.firmware.domain.EspCallback
import com.physicaloid.lib.Physicaloid
import java.lang.Thread.sleep
import kotlin.math.max


/**
 * Write/byte conversion methods for firmware flash
 */

/*
 * This will initialise the chip
 */
fun sync(physicalOid: Physicaloid,espCallback: EspCallback): Int {
    var response = 0
    val cmdData = ByteArray(36)

    cmdData[0] = (0x07).toByte()
    cmdData[1] = (0x07).toByte()
    cmdData[2] = (0x12).toByte()
    cmdData[3] = (0x20).toByte()
    var x = 4
    while (x < 36) {
        cmdData[x] = (0x55).toByte()
        x++
    }

    x = 0
    while (x < 7) {
        val ret = sendCommand(EspRepoParams.ESP_SYNC.toByte(), cmdData, 0, 100,physicalOid,espCallback)
        if (ret.retCode == 1) {
            response = 1
            break
        }
        x++
    }
    return response
}

/*
 * This will send a command to the chip
 */
fun sendCommand(
    opcode: Byte,
    buffer: ByteArray,
    chk: Int,
    timeout: Int,
    physicalOid: Physicaloid,
    espCallback: EspCallback
): CmdRet {
    val retVal = CmdRet()
    var i = 0
    val data = ByteArray(8 + buffer.size)
    data[0] = 0x00
    data[1] = opcode
    data[2] = ((buffer.size) and 0xFF).toByte()
    data[3] = ((buffer.size shr 8) and 0xFF).toByte()
    data[4] = ((chk and 0xFF)).toByte()
    data[5] = ((chk shr 8) and 0xFF).toByte()
    data[6] = ((chk shr 16) and 0xFF).toByte()
    data[7] = ((chk shr 24) and 0xFF).toByte()

    i = 0
    while (i < buffer.size) {
        data[8 + i] = buffer[i]
        i++
    }

    retVal.retCode = 0
    val buf = slipEncode(data)

    physicalOid.write(buf, buf.size)
    try {
        sleep(5)
    } catch (e: InterruptedException) {
        espCallback.onError(e)
    }

    var numRead = 0

    i = 0
    while (i < 10) {
        numRead = rec(retVal.retValue, retVal.retValue.size, (timeout / 5).toLong(), physicalOid = physicalOid)
        if (numRead == 0) {
            retVal.retCode = -1
            i++
            continue
        } else if (numRead == -1) {
            retVal.retCode = -1
            i++
            continue
        }

        if (retVal.retValue[0] != 0xC0.toByte()) {
            retVal.retCode = -1
            i++
            continue
        }

        retVal.retCode = 1
        break
        i++
    }

    return retVal
}

fun rec(buf: ByteArray, length: Int, timeout: Long, physicalOid: Physicaloid): Int {
    var retVal = 0
    var totalRetVal = 0
    var endTime: Long
    var startTime = System.currentTimeMillis()
    val tmpBuf = ByteArray(length)

    while (true) {
        retVal = physicalOid.read(tmpBuf, length)

        if (retVal > 0) {
            System.arraycopy(tmpBuf, 0, buf, totalRetVal, retVal)
            totalRetVal += retVal
            startTime = System.currentTimeMillis()
        }

        if (totalRetVal >= 8) {
            break
        }

        endTime = System.currentTimeMillis()
        if ((endTime - startTime) > timeout) {
            break
        }
    }
    return retVal
}

fun slipEncode(buffer: ByteArray): ByteArray {
    var encoded = byteArrayOf((0xC0).toByte())

    for (b in buffer) {
        if (b == (0xC0).toByte()) {
            encoded = appendArray(encoded, byteArrayOf((0xDB).toByte()))
            encoded = appendArray(encoded, byteArrayOf((0xDC).toByte()))
        } else if (b == (0xDB).toByte()) {
            encoded = appendArray(encoded, byteArrayOf((0xDB).toByte()))
            encoded = appendArray(encoded, byteArrayOf((0xDD).toByte()))
        } else {
            encoded = appendArray(encoded, byteArrayOf(b))
        }
    }
    encoded = appendArray(encoded, byteArrayOf((0xC0).toByte()))

    return encoded
}


/*
 * This takes 2 arrays as params and return a concatenate array
 */
fun appendArray(arr1: ByteArray, arr2: ByteArray): ByteArray {
    val c = ByteArray(arr1.size + arr2.size)

    System.arraycopy(arr1, 0, c, 0, arr1.size)
    System.arraycopy(arr2, 0, c, arr1.size, arr2.size)
    return c
}

/*
 * get part of an array
 */
fun subArray(arr1: ByteArray, pos: Int, length: Int): ByteArray {
    val c = ByteArray(length)

    System.arraycopy(arr1, pos, c, 0, length)
    return c
}

/*
 * Calculate the checksum.
 */
fun checksum(data: ByteArray): Int {
    var chk = EspRepoParams.ESP_CHECKSUM_MAGIC.toInt()
    var x = 0
    x = 0
    while (x < data.size) {
        chk = chk xor data[x].toInt()
        x++
    }
    return chk
}


fun readRegister(reg: Int,physicalOid: Physicaloid, espCallback: EspCallback): Int {
    var retVals = longArrayOf(0)
    val ret: CmdRet
    try {
        val packet = intToByteArray(reg)

        ret = sendCommand(EspRepoParams.ESP_READ_REG.toByte(), packet, 0, 0, physicalOid,espCallback)
        val myRet = EspStruct()

        val subArray = ByteArray(4)
        subArray[0] = ret.retValue[5]
        subArray[1] = ret.retValue[6]
        subArray[2] = ret.retValue[7]
        subArray[3] = ret.retValue[8]

        retVals = myRet.unpack("I", subArray)
    } catch (e: Exception) {
        espCallback.onError(e)
    }
    return retVals[0].toInt()
}


fun timeoutPerMb(sizeBytes: Int): Int {
    val result = EspRepoParams.ERASE_REGION_TIMEOUT_PER_MB * (sizeBytes / 1000000)
    return max(result.toDouble(), 3000.0).toInt()
}

fun intToByteArray(i: Int): ByteArray {
    return byteArrayOf(
        (i and 0xff).toByte(), ((i shr 8) and 0xff).toByte(), ((i shr 16) and 0xff).toByte(),
        ((i shr 24) and 0xff).toByte()
    )
}
