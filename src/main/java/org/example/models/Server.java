package org.example.models;

import org.example.observers.Observer;
import org.example.observers.Subject;
import org.example.ServerStatus;

import java.util.ArrayList;
import java.util.List;

// server cu builder si observer
public class Server implements Subject {
    private String ipAddress;
    private Location location;
    private List<User> users;
    private String hostname;
    private ServerStatus status;
    private Integer cpuCores;
    private Integer ramGb;
    private Integer storageGb;
    private List<Observer> observers = new ArrayList<>();

    // builder pentru creare usoara
    public static class ServerBuilder {
        private String ipAddress;
        private Location location;
        private List<User> users = new ArrayList<>();
        private String hostname;
        private ServerStatus status;
        private Integer cpuCores;
        private Integer ramGb;
        private Integer storageGb;

        public ServerBuilder ipAddress(String ip) {
            this.ipAddress = ip;
            return this;
        }

        public ServerBuilder location(Location loc) {
            this.location = loc;
            return this;
        }

        public ServerBuilder users(List<User> userList) {
            this.users = userList;
            return this;
        }

        public ServerBuilder hostname(String hostname) {
            this.hostname = hostname;
            return this;
        }

        public ServerBuilder status(ServerStatus status) {
            this.status = status;
            return this;
        }

        public ServerBuilder cpuCores(Integer cpuCores) {
            this.cpuCores = cpuCores;
            return this;
        }

        public ServerBuilder ramGb(Integer ramGb) {
            this.ramGb = ramGb;
            return this;
        }

        public ServerBuilder storageGb(Integer storageGb) {
            this.storageGb = storageGb;
            return this;
        }

        public Server build() {
            return new Server(ipAddress, location, users, hostname, status, cpuCores, ramGb, storageGb);
        }
    }

    public Server(String ipAddress, Location location, List<User> users, String hostname, ServerStatus status, Integer cpuCores, Integer ramGb, Integer storageGb) {
        this.ipAddress = ipAddress;
        this.location = location;
        this.users = users;
        this.hostname = hostname;
        this.status = status;
        this.cpuCores = cpuCores;
        this.ramGb = ramGb;
        this.storageGb = storageGb;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public List<User> getUsers() {
        return users;
    }

    // notifica observatorii despre evenimente
    @Override
    public void addObserver(Observer o) {
        observers.add(o);
    }

    @Override
    public void notifyObservers(Event event) {
        for (Observer o : observers) {
            o.update(event);
        }
    }
}

