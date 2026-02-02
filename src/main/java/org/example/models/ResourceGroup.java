package org.example.models;

import org.example.observers.Observer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

// grup de monitorizare
public class ResourceGroup implements Observer {
    private final String ipAddress;
    private final Set<User> members = new HashSet<>();

    public ResourceGroup(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void addMember(User user) {
        members.add(user);
    }

    public void removeMember(User user) {
        members.remove(user);
    }

    public Set<User> getMembers() {
        return members;
    }

    // primeste notificari despre evenimente
    @Override
    public void update(Event event) {
        try {
            String fileName = "event_" + ipAddress.replace(".", "_") + ".out";
            BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
            writer.write(event.toString());
            writer.newLine();
            writer.close();
        } catch (IOException e) {
            System.out.println("Could not write event: " + e.getMessage());
        }
    }
}
