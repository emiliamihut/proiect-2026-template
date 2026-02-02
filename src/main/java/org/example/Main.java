package org.example;

import org.example.commands.*;

import java.io.*;
import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        if (args.length < 2) return;

        String type = args[0];
        String[] filePaths = Arrays.copyOfRange(args, 1, args.length);

        // pentru listeners trebuie procesat in ordine: servers, groups, listeners
        if (type.equals("listeners") && filePaths.length == 3) {
            processFile(filePaths[0]);
            processFile(filePaths[1]);
            processFile(filePaths[2]);
        } else {
            for (String path : filePaths) {
                processFile(path);
            }
        }
    }

    private static void processFile(String filePath) {
        try {
            File inputFile = new File(filePath + ".in");
            File outputFile = new File(filePath + ".out");

            BufferedReader reader = new BufferedReader(new FileReader(inputFile));
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile));

            String line;
            int lineNumber = 0;

            // sarim peste header
            reader.readLine();

            // citim fiecare linie si executam comanda
            while ((line = reader.readLine()) != null) {
                lineNumber++;

                String[] tokens = line.split("\\|");
                Command command = null;

                if (line.startsWith("ADD SERVER")) {
                    command = new AddServer();
                } else if (line.startsWith("ADD GROUP")) {
                    command = new AddGroup();
                } else if (line.startsWith("ADD MEMBER")) {
                    command = new AddMember();
                } else if (line.startsWith("FIND MEMBER")) {
                    command = new FindMember();
                } else if (line.startsWith("REMOVE MEMBER")) {
                    command = new RemoveMember();
                } else if (line.startsWith("FIND GROUP")) {
                    command = new FindGroup();
                } else if (line.startsWith("REMOVE GROUP")) {
                    command = new RemoveGroup();
                } else if (line.startsWith("ADD EVENT")) {
                    command = new AddEvent();
                }

                if (command != null) {
                    command.execute(tokens, lineNumber, writer);
                }
            }

            reader.close();
            writer.close();

        } catch (IOException e) {
            System.err.println("Error processing file: " + e.getMessage());
        }
    }
}
