package com.crescenzi.esptoolbox.data.core.exception;

/**
 * Per comandi non supportati
 */
public class UnsupportedCommandException extends RuntimeException {
    public UnsupportedCommandException(String location) {
        super("Invalid command,review syntax and values,maybe in  : -> \\"+location+"\\");
    }
}
