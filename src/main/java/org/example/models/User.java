package org.example.models;

import org.example.exceptions.UserException;

// utilizator basic
public class User {
    protected String name;
    protected String role;
    protected String email;

    public User(String name, String role, String email) {
        // nume si rol sunt obligatorii
        if (name == null || name.isEmpty() || role == null || role.isEmpty()) {
            throw new UserException();
        }
        this.name = name;
        this.role = role;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "name = " + name + " && role = " + role;
    }
}

