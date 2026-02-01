package org.example.models;

import org.example.enums.AlertType;
import org.example.enums.Severity;

// alerta de sistem
public class Alert {
    private String message;
    private AlertType type;
    private Severity severity;
    private String ipAddress;

    public Alert(String message, AlertType type, Severity severity, String ipAddress) {
        // toate campurile sunt obligatorii
        if (message == null || message.isEmpty() || type == null ||
            severity == null || ipAddress == null || ipAddress.isEmpty()) {
            throw new IllegalArgumentException("Invalid Alert: All fields must be provided.");
        }
        this.message = message;
        this.type = type;
        this.severity = severity;
        this.ipAddress = ipAddress;
    }

    public String getMessage() {
        return message;
    }

    public AlertType getType() {
        return type;
    }

    public Severity getSeverity() {
        return severity;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    @Override
    public String toString() {
        return String.format("ALERT: [%s] (%s) - %s -> %s",
                type.name(), severity.name(), message, ipAddress);
    }
}

