package com.crescenzi.esp32.params


/**
 * Defines formats for sending Wi-Fi credentials via USB
 */
sealed class SerialFormat(val description: String) {
    abstract fun serialize(ssid: String, password: String): ByteArray

    object Plain : SerialFormat("Plaintext (2 row)") {
        override fun serialize(ssid: String, password: String): ByteArray =
            "$ssid\n$password\n".toByteArray()
    }
    object Json : SerialFormat("JSON {ssid, password}") {
        override fun serialize(ssid: String, password: String): ByteArray =
            """{"ssid":"$ssid","password":"$password"}\n""".toByteArray()
    }

    object AT : SerialFormat("AT commands") {
        override fun serialize(ssid: String, password: String): ByteArray =
            "AT+SSID=$ssid\nAT+PWD=$password\n".toByteArray()
    }

    object Csv : SerialFormat("CSV ssid,password") {
        override fun serialize(ssid: String, password: String): ByteArray =
            "$ssid,$password\n".toByteArray()
    }

    object Credentials : SerialFormat("CREDENTIALS ssid password") {
        override fun serialize(ssid: String, password: String): ByteArray =
            "CREDENTIALS $ssid $password\n".toByteArray()
    }

    object Custom : SerialFormat("Custom-defined") {
        override fun serialize(ssid: String, password: String): ByteArray =
            "ssid=$ssid;password=$password;\n".toByteArray()
    }

}
