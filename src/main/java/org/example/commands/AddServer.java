package org.example.commands;

import org.example.database.Database;
import org.example.exceptions.LocationException;
import org.example.exceptions.MissingIpAddressException;
import org.example.exceptions.UserException;
import org.example.factories.UserFactory;
import org.example.models.Location;
import org.example.models.Server;
import org.example.models.User;
import org.example.ServerStatus;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

// clasa pentru comanda de adaugare server
public class AddServer implements Command {

    @Override
    public void execute(String[] tokens, int lineNumber, BufferedWriter writer) {
        try {
            // verifica ca avem destule date
            if (tokens.length < 15) {
                throw new UserException("Invalid input format.");
            }

            // extrage datele din linie
            String ip = tokens[2].trim();
            String status = tokens[3].trim();
            String country = tokens[4].trim();
            String city = tokens[5].trim();
            String address = tokens[6].trim();
            String name = tokens[9].trim();
            String role = tokens[10].trim();
            String email = tokens[11].trim();
            String department = tokens[12].trim();
            String clearance = tokens[13].trim();

            // valideaza datele importante
            if (ip.isEmpty()) {
                throw new MissingIpAddressException();
            }
            if (name.isEmpty() || role.isEmpty()) {
                throw new UserException("Name and role can't be empty.");
            }
            if (country.isEmpty()) {
                throw new LocationException("Country is missing.");
            }

            // creeaza obiectele necesare
            Location location = new Location.LocationBuilder()
                    .country(country)
                    .city(city)
                    .address(address)
                    .build();

            User user = UserFactory.createUser(name, role, email, department, clearance);

            // campuri optionale
            String hostname = null;
            if (!tokens[1].trim().isEmpty()) {
                hostname = tokens[1].trim();
            }

            ServerStatus serverStatus = null;
            if (!status.isEmpty()) {
                try {
                    serverStatus = ServerStatus.valueOf(status);
                } catch (IllegalArgumentException ignored) {
                }
            }

            Integer cpuCores = null;
            if (!tokens[14].trim().isEmpty()) {
                try {
                    cpuCores = Integer.parseInt(tokens[14].trim());
                } catch (NumberFormatException ignored) {
                }
            }

            Integer ramGb = null;
            if (!tokens[14].trim().isEmpty()) {
                try {
                    ramGb = Integer.parseInt(tokens[14].trim());
                } catch (NumberFormatException ignored) {
                }
            }

            Integer storageGb = null;
            if (!tokens[15].trim().isEmpty()) {
                try {
                    storageGb = Integer.parseInt(tokens[15].trim());
                } catch (NumberFormatException ignored) {
                }
            }

            Server server = new Server.ServerBuilder()
                    .ipAddress(ip)
                    .location(location)
                    .users(List.of(user))
                    .hostname(hostname)
                    .status(serverStatus)
                    .cpuCores(cpuCores)
                    .ramGb(ramGb)
                    .storageGb(storageGb)
                    .build();

            // salveaza in baza de date
            Database.getInstance().addServer(server);

            writer.write("ADD SERVER: " + ip + ": " + status);
            writer.newLine();

        } catch (Exception e) {
            try {
                writer.write("ADD SERVER: " + e.getClass().getSimpleName() +
                           ": " + e.getMessage() + " ## line no: " + lineNumber);
                writer.newLine();
            } catch (IOException ignored) {}
        }
    }
}
