package org.example.commands;

import org.example.database.Database;
import org.example.exceptions.MissingIpAddressException;
import org.example.models.ResourceGroup;

import java.io.BufferedWriter;
import java.io.IOException;

public class FindGroup implements Command {

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

            ResourceGroup group = null;
            for (ResourceGroup g : db.getResourceGroups()) {
                if (g.getIpAddress().equals(ip)) {
                    group = g;
                    break;
                }
            }

            if (group == null) {
                writer.write("FIND GROUP: Group not found: ipAddress = " + ip);
                writer.newLine();
                return;
            }

            writer.write("FIND GROUP: " + ip);
            writer.newLine();


        } catch (Exception e) {
            try {
                writer.write("FIND GROUP: " + e.getClass().getSimpleName() +
                        ": " + e.getMessage() + " ## line no: " + lineNumber);
                writer.newLine();
            } catch (IOException ignored) {}
        }
    }
}
