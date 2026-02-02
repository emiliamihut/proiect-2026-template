package org.example.factories;

import org.example.models.Admin;
import org.example.models.Operator;
import org.example.models.User;

// factory pentru creare utilizatori
public class UserFactory {

    public static User createUser(String name, String role, String email,
                                  String department, String clearanceLevel) {

        // creeaza admin daca e cazul
        if (role.equalsIgnoreCase("Admin")) {
            int level = 0;
            try {
                if (clearanceLevel != null && !clearanceLevel.isEmpty()) {
                    level = Integer.parseInt(clearanceLevel);
                }
            } catch (NumberFormatException e) {
                level = 0;
            }
            return new Admin(name, role, email, department, level);
        }

        // creeaza operator daca e cazul
        if (role.equalsIgnoreCase("Operator")) {
            return new Operator(name, role, email, department);
        }

        // user simplu
        return new User(name, role, email);
    }
}

