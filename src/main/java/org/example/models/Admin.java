package org.example.models;

// admin cu nivel de acces
public class Admin extends Operator {
    private int clearanceLevel;

    public Admin(String name, String role, String email, String department, int clearanceLevel) {
        super(name, role, email, department);
        this.clearanceLevel = clearanceLevel;
    }

    public int getClearanceLevel() {
        return clearanceLevel;
    }

    @Override
    public String toString() {
        return "Admin{name='" + getName() + "', role='" + getRole() + "', email='" + getEmail() +
               "', department='" + getDepartment() + "', clearanceLevel=" + clearanceLevel + "}";
    }
}
