package com.crescenzi.esptoolbox.data.core.exception

// == For unsupported commands == //
class UnsupportedCommandException(location: String) :
    RuntimeException("Invalid command,review syntax and values,maybe in  : -> \\$location\\")
