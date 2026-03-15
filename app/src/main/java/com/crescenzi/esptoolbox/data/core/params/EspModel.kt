package com.crescenzi.esptoolbox.data.core.params


/**
 * ESP models
 */
enum class EspModel(val displayName: String, val magicValues: Set<Int>) {
    ESP8266("ESP8266", setOf(-0xF3EFF)),
    ESP32("ESP32", setOf(0x00F01D83)),
    ESP32_S2("ESP32-S2", setOf(0x000007C6)),
    ESP32_S3("ESP32-S3", setOf(0x00000009, 538052359)),
    ESP32_C2("ESP32-C2", setOf(0x6F51306F)),
    ESP32_C3("ESP32-C3", setOf(0x6921506F, 0x1B31506F)),
    ESP32_C6("ESP32-C6", setOf(0x0DA1806F)),
    ESP32_H2("ESP32-H2", setOf(-0x35D933DE));
}