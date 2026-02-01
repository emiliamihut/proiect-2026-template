package org.example.commands;

import org.example.database.Database;
import org.example.exceptions.MissingIpAddressException;
import org.example.models.ResourceGroup;

import java.io.BufferedWriter;
import java.io.IOException;

public class AddGroup {

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

            // creeaza grup nou
            ResourceGroup group = new ResourceGroup(ip);
            Database.getInstance().addResourceGroup(group);

            writer.write("ADD GROUP: " + ip);
            writer.newLine();

        } catch (Exception e) {
            try {
                writer.write("ADD GROUP: " + e.getClass().getSimpleName() +
                           ": " + e.getMessage() + " ## line no: " + lineNumber);
                writer.newLine();
            } catch (IOException ignored) {}
        }
    }
}

