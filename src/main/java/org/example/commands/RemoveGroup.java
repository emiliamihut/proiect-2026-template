package org.example.commands;

import org.example.database.Database;
import org.example.exceptions.MissingIpAddressException;
import org.example.models.ResourceGroup;

import java.io.BufferedWriter;
import java.io.IOException;

// clasa pentru comanda de eliminare grup
public class RemoveGroup implements Command {

    @Override
    public void execute(String[] tokens, int lineNumber, BufferedWriter writer) {
        try {
            if (tokens.length < 2) {
                throw new MissingIpAddressException();
            }

            String ip = tokens[1].trim();

            if (ip.isEmpty()) {
                throw new MissingIpAddressException();
            }

            Database db = Database.getInstance();
            ResourceGroup group = db.getResourceGroups().stream()
                    .filter(g -> g.getIpAddress().equals(ip))
                    .findFirst()
                    .orElse(null);

            if (group == null) {
                writer.write("REMOVE GROUP: Group not found: ipAddress = " + ip);
                writer.newLine();
                return;
            }

            db.getResourceGroups().remove(group);
            writer.write("REMOVE GROUP: " + ip);
            writer.newLine();

        } catch (Exception e) {
            try {
                writer.write("REMOVE GROUP: " + e.getClass().getSimpleName() +
                           ": " + e.getMessage() + " ## line no: " + lineNumber);
                writer.newLine();
            } catch (IOException ignored) {}
        }
    }
}
