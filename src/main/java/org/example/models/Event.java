package org.example.models;

// eveniment pentru notificare
public class Event {
    private final String ipAddress;
    private final String type;
    private final String severity;
    private final String message;

    public Event(String ipAddress, String type, String severity, String message) {
        this.ipAddress = ipAddress;
        this.type = type;
        this.severity = severity;
        this.message = message;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    @Override
    public String toString() {
        return "EVENT: " + ipAddress + ": type = " + type +
               " && severity = " + severity + " && message = " + message;
    }
}

