package org.example.models;

// eveniment pentru notificare
public class Event {
    private final String ipAddress;
    private final Alert alert;

    public Event(String ipAddress, Alert alert) {
        this.ipAddress = ipAddress;
        this.alert = alert;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    @Override
    public String toString() {
        return "EVENT: " + ipAddress + ": type = " + alert.getType() +
               " && severity = " + alert.getSeverity() + " && message = " + alert.getMessage();
    }
}

