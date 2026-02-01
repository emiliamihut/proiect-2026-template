package org.example.commands;

import org.example.database.Database;
import org.example.exceptions.MissingIpAddressException;
import org.example.models.ResourceGroup;

import java.io.BufferedWriter;
import java.io.IOException;

public class RemoveGroup {

    public static void execute(String line, int lineNumber, BufferedWriter writer) {
        try {
            String[] tokens = line.split("\\|");

            if (tokens.length < 2) {
                throw new MissingIpAddressException();
            }

            String ip = tokens[1].trim();

            if (ip.isEmpty()) {
                throw new MissingIpAddressException();
            }

            Database db = Database.getInstance();

            ResourceGroup group = null;
            for (ResourceGroup g : db.getResourceGroups()) {
                if (g.getIpAddress().equals(ip)) {
                    group = g;
                    break;
                }
            }

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
                writer.write("REMOVE GROUP: " + e.getClass().getSimpleName() + ": " + e.getMessage() + " ## line no: " + lineNumber);
                writer.newLine();
            } catch (IOException ignored) {}
        }
    }
}

