package org.example.commands;

import org.example.database.Database;
import org.example.AlertType;
import org.example.Severity;
import org.example.exceptions.MissingIpAddressException;
import org.example.models.Alert;
import org.example.models.ResourceGroup;
import org.example.models.Server;
import org.example.models.Event;

import java.io.BufferedWriter;
import java.io.IOException;

// clasa pentru comanda de adaugare eveniment
public class AddEvent implements Command {

    @Override
    public void execute(String[] tokens, int lineNumber, BufferedWriter writer) {
        try {
            // verifica ca avem destule date
            if (tokens.length < 5) {
                throw new IllegalArgumentException("Missing event fields.");
            }

            // extrage datele din linie
            String typeStr = tokens[1].trim();
            String severityStr = tokens[2].trim();
            String ip = tokens[3].trim();
            String message = tokens[4].trim();

            if (ip.isEmpty()) {
                throw new MissingIpAddressException();
            }

            // creeaza alerta
            AlertType type = AlertType.valueOf(typeStr);
            Severity severity = Severity.valueOf(severityStr);
            Alert alert = new Alert(message, type, severity, ip);

            // adauga alerta in baza de date
            Database.getInstance().addAlert(alert);

            writer.write("ADD EVENT: " + ip + ": type = " + type + " && severity = " + severity + " && message = " + message);
            writer.newLine();

            // gaseste serverul si grupul si notifica prin observer pattern
            Database db = Database.getInstance();

            // gaseste serverul
            Server targetServer = null;
            for (Server server : db.getServers()) {
                if (server.getIpAddress().equals(ip)) {
                    targetServer = server;
                    break;
                }
            }

            // gaseste grupul
            ResourceGroup targetGroup = null;
            for (ResourceGroup group : db.getResourceGroups()) {
                if (group.getIpAddress().equals(ip)) {
                    targetGroup = group;
                    break;
                }
            }

            // conecteaza serverul cu grupul si notifica
            if (targetServer != null && targetGroup != null) {
                targetServer.addObserver(targetGroup);
                targetServer.notifyObservers(new Event(ip, alert));
            }

        } catch (Exception e) {
            try {
                writer.write("ADD EVENT: " + e.getClass().getSimpleName() +
                           ": " + e.getMessage() + " ## line no: " + lineNumber);
                writer.newLine();
            } catch (IOException ignored) {}
        }
    }
}
