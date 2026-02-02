package org.example.commands;

// interfata pentru comenzi
public interface Command {
    void execute(String[] tokens, int lineNumber, java.io.BufferedWriter writer);
}
