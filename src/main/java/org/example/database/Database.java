package org.example.database;

import org.example.models.Server;
import org.example.models.ResourceGroup;
import org.example.models.Alert;

import java.util.HashSet;
import java.util.Set;

// singleton pentru baza de date
public class Database {

    private static Database instance;

    private final Set<Server> servers = new HashSet<>();
    private final Set<ResourceGroup> resourceGroups = new HashSet<>();
    private final Set<Alert> alerts = new HashSet<>();

    private Database() {
        // Constructor privat
    }

    public static Database getInstance() {
        if (instance == null) {
            instance = new Database();
        }
        return instance;
    }

    public void addServer(Server server) {
        servers.add(server);
    }

    public void addServers(Set<Server> serverSet) {
        servers.addAll(serverSet);
    }

    public Set<Server> getServers() {
        return servers;
    }

    public void addResourceGroup(ResourceGroup group) {
        resourceGroups.add(group);
    }

    public void addResourceGroups(Set<ResourceGroup> groups) {
        resourceGroups.addAll(groups);
    }

    public Set<ResourceGroup> getResourceGroups() {
        return resourceGroups;
    }

    public void addAlert(Alert alert) {
        alerts.add(alert);
    }

    public Set<Alert> getAlerts() {
        return alerts;
    }
}
