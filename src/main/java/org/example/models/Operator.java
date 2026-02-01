package org.example.models;

// operator cu departament
public class Operator extends User {
    protected String department;

    public Operator(String name, String role, String email, String department) {
        super(name, role, email);
        this.department = department;
    }

    public String getDepartment() {
        return department;
    }
}

