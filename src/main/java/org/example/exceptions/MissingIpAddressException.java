package org.example.exceptions;

public class MissingIpAddressException extends RuntimeException {
    public MissingIpAddressException() {
        super("Server IP Address was not provided.");
    }

    public MissingIpAddressException(String message) {
        super(message);
    }
}

