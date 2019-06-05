package com.github.code31415926535.engine.exceptions;

public class InvalidMapFormatException extends Exception {
    public InvalidMapFormatException(String reason) {
        super("InvalidMapFormatException: " + reason);
    }
}
