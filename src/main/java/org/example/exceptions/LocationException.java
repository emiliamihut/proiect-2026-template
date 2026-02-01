package org.example.exceptions;

public class LocationException extends RuntimeException {
    public LocationException() {
        super("Country is missing.");
    }

    public LocationException(String message) {
        super(message);
    }
}

