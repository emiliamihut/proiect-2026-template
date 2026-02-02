package org.example.commands;

import org.example.database.Database;
import org.example.exceptions.MissingIpAddressException;
import org.example.models.ResourceGroup;
import org.example.models.Server;

import java.io.BufferedWriter;
import java.io.IOException;

// clasa pentru comanda de adaugare grup
public class AddGroup implements Command {

    @Override
    public void execute(String[] tokens, int lineNumber, BufferedWriter writer) {
        try {
            // verifica ca avem destule date
            if (tokens.length < 2) {
                throw new MissingIpAddressException();
            }

            // extrage adresa IP
            String ip = tokens[1].trim();
            if (ip.isEmpty()) {
                throw new MissingIpAddressException();
            }

            // verifica daca exista serverul
            boolean serverExists = false;
            Database database = Database.getInstance();
            for (Server server : database.getServers()) {
                if (server.getIpAddress().equals(ip)) {
                    serverExists = true;
                    break;
                }
            }

            if (!serverExists) {
                writer.write("ADD GROUP: Server not found: ipAddress = " + ip);
                writer.newLine();
                return;
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
