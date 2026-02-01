package org.example.commands;

import org.example.database.Database;
import org.example.exceptions.MissingIpAddressException;
import org.example.exceptions.UserException;
import org.example.models.ResourceGroup;
import org.example.models.User;

import java.io.BufferedWriter;
import java.io.IOException;

public class RemoveMember {

    public static void execute(String line, int lineNumber, BufferedWriter writer) {
        try {
            String[] tokens = line.split("\\|");

            if (tokens.length < 4) {
                throw new UserException("Name and role can't be empty.");
            }

            String ip = tokens[1].trim();
            String name = tokens[2].trim();
            String role = tokens[3].trim();

            if (ip.isEmpty()) {
                throw new MissingIpAddressException();
            }
            if (name.isEmpty() || role.isEmpty()) {
                throw new UserException("Name and role can't be empty.");
            }

            Database db = Database.getInstance();

            // cauta grupul
            ResourceGroup group = null;
            for (ResourceGroup g : db.getResourceGroups()) {
                if (g.getIpAddress().equals(ip)) {
                    group = g;
                    break;
                }
            }

            if (group == null) {
                writer.write("REMOVE MEMBER: Group not found: ipAddress = " + ip);
                writer.newLine();
                return;
            }

            // cauta si sterge utilizatorul
            User userToRemove = null;
            for (User u : group.getMembers()) {
                if (u.getName().equals(name) && u.getRole().equalsIgnoreCase(role)) {
                    userToRemove = u;
                    break;
                }
            }

            if (userToRemove == null) {
                writer.write("REMOVE MEMBER: Member not found: ipAddress = " + ip + ": name = " + name + " && role = " + role);
                writer.newLine();
                return;
            }

            group.getMembers().remove(userToRemove);

            writer.write("REMOVE MEMBER: " + ip + ": name = " + name + " && role = " + role);
            writer.newLine();

        } catch (Exception e) {
            try {
                writer.write("REMOVE MEMBER: " + e.getClass().getSimpleName() + ": " + e.getMessage() + " ## line no: " + lineNumber);
                writer.newLine();
            } catch (IOException ignored) {}
        }
    }
}

