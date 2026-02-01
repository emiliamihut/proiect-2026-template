package org.example.exceptions;

public class UserException extends RuntimeException {
    public UserException() {
        super("Name and role can't be empty.");
    }

    public UserException(String message) {
        super(message);
    }
}

