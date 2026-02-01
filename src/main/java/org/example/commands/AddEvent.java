package org.example.commands;

import org.example.database.Database;
import org.example.enums.AlertType;
import org.example.enums.Severity;
import org.example.exceptions.MissingIpAddressException;
import org.example.models.Alert;
import org.example.models.Event;
import org.example.models.ResourceGroup;
import org.example.models.Server;

import java.io.BufferedWriter;
import java.io.IOException;

public class AddEvent {

    public static void execute(String line, int lineNumber, BufferedWriter writer) {
        try {
            String[] tokens = line.split("\\|");

            if (tokens.length < 5) {
                throw new IllegalArgumentException("Missing event fields.");
            }

            String typeStr = tokens[1].trim();
            String severityStr = tokens[2].trim();
            String ip = tokens[3].trim();
            String message = tokens[4].trim();

            if (ip.isEmpty()) {
                throw new MissingIpAddressException();
            }

            AlertType type = AlertType.valueOf(typeStr);
            Severity severity = Severity.valueOf(severityStr);

            Alert alert = new Alert(message, type, severity, ip);
            Database db = Database.getInstance();
            db.addAlert(alert);

            Event event = new Event(ip, typeStr, severityStr, message);

            // cauta serverul si grupul
            Server server = null;
            for (Server s : db.getServers()) {
                if (s.getIpAddress().equals(ip)) {
                    server = s;
                    break;
                }
            }

            ResourceGroup group = null;
            for (ResourceGroup g : db.getResourceGroups()) {
                if (g.getIpAddress().equals(ip)) {
                    group = g;
                    break;
                }
            }

            // notifica grupul daca exista
            if (server != null && group != null) {
                server.addObserver(group);
                server.notifyObservers(event);
            }

            writer.write("ADD EVENT: " + ip + ": type = " + typeStr + " && severity = " + severityStr + " && message = " + message);
            writer.newLine();

        } catch (Exception e) {
            try {
                writer.write("ADD EVENT: " + e.getClass().getSimpleName() +
                           ": " + e.getMessage() + " ## line no: " + lineNumber);
                writer.newLine();
            } catch (IOException ignored) {}
        }
    }
}

