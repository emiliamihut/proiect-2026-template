package org.example.commands;

import org.example.database.Database;
import org.example.exceptions.MissingIpAddressException;
import org.example.exceptions.UserException;
import org.example.factories.UserFactory;
import org.example.models.ResourceGroup;
import org.example.models.User;

import java.io.BufferedWriter;
import java.io.IOException;

public class AddMember {

    public static void execute(String line, int lineNumber, BufferedWriter writer) {
        try {
            String[] tokens = line.split("\\|");

            if (tokens.length < 4) {
                throw new UserException("Name and role can't be empty.");
            }

            // extrage datele
            String ip = tokens[1].trim();
            String name = tokens[2].trim();
            String role = tokens[3].trim();
            String email = tokens.length > 4 ? tokens[4].trim() : "";
            String department = tokens.length > 5 ? tokens[5].trim() : "";
            String clearanceLevel = tokens.length > 6 ? tokens[6].trim() : "";

            // valideaza
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
                writer.write("ADD MEMBER: Group not found: ipAddress = " + ip);
                writer.newLine();
                return;
            }

            // adauga user in grup
            User user = UserFactory.createUser(name, role, email, department, clearanceLevel);
            group.addMember(user);

            writer.write("ADD MEMBER: " + ip + ": name = " + name + " && role = " + role);
            writer.newLine();

        } catch (Exception e) {
            try {
                writer.write("ADD MEMBER: " + e.getClass().getSimpleName() + ": " + e.getMessage() + " ## line no: " + lineNumber);
                writer.newLine();
            } catch (IOException ignored) {}
        }
    }
}

