package com.crescenzi.esp32.exception


// == For unsupported commands == //
class UnsupportedCommandException(location: String) :
    RuntimeException("Invalid command,review syntax and values,maybe in  : -> \\$location\\")

// == Queued operation failed == //
class ReachTargetException : RuntimeException()

// == Exception thrown on USB connection failure == //
class UsbConnectionException : RuntimeException()

// == Exception thrown on Wi-Fi connection failure == //
class WifiConnectionException : RuntimeException()
