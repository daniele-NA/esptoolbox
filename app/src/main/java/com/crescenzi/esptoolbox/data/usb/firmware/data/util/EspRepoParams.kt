package com.crescenzi.esptoolbox.data.usb.firmware.data.util


/**
 * ESP parameters
 */
object EspRepoParams {

    const val FLASH_WRITE_SIZE = 0x400

    const val CHIP_DETECT_MAGIC_REG_ADDRESS = 0x40001000

    // Commands supported by ESP8266 ROM bootloader
    const val ESP_FLASH_BEGIN = 0x02
    const val ESP_FLASH_DATA = 0x03

    const val ESP_SYNC = 0x08
    const val ESP_READ_REG = 0x0A
    const val ESP_SPI_SET_PARAMS = 0x0B // 11
    const val ESP_SPI_ATTACH = 0x0D // 13
    const val ESP_CHANGE_BAUD_RATE = 0x0F // 15
    const val ESP_CHECKSUM_MAGIC = 0xEF.toByte()
    const val ERASE_REGION_TIMEOUT_PER_MB =
        30000 // timeout (per megabyte) for erasing a region in ms
}