package com.crescenzi.esptoolbox.data.core.params

enum class BaudRateFormat(val value: Int) {
    B9600(9600),
    B19200(19200),
    B38400(38400),
    B57600(57600),
    B74880(74880),
    B115200(115200),  // Default ESP-IDF console baudrate
    B230400(230400),
    B460800(460800),
    B921600(921600),
    B1000000(1000000),
    B2000000(2000000);
}
