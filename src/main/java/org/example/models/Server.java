package org.example.models;

import org.example.observers.Observer;
import org.example.observers.Subject;

import java.util.ArrayList;
import java.util.List;

// server cu builder si observer
public class Server implements Subject {
    private String ipAddress;
    private Location location;
    private List<User> users;
    private List<Observer> observers = new ArrayList<>();

    // builder pentru creare usoara
    public static class ServerBuilder {
        private String ipAddress;
        private Location location;
        private List<User> users = new ArrayList<>();

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

        public Server build() {
            return new Server(ipAddress, location, users);
        }
    }

    public Server(String ipAddress, Location location, List<User> users) {
        this.ipAddress = ipAddress;
        this.location = location;
        this.users = users;
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

