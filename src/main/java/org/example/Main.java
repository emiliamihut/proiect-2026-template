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
            processFile("servers", filePaths[0]);
            processFile("groups", filePaths[1]);
            processFile("listeners", filePaths[2]);
        } else {
            for (String path : filePaths) {
                processFile(type, path);
            }
        }
    }

    private static void processFile(String type, String filePath) {
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

                if (line.startsWith("ADD SERVER")) {
                    String[] tokens = line.split("\\|");
                    AddServer.execute(tokens, lineNumber, writer);
                } else if (line.startsWith("ADD GROUP")) {
                    AddGroup.execute(line, lineNumber, writer);
                } else if (line.startsWith("ADD MEMBER")) {
                    AddMember.execute(line, lineNumber, writer);
                } else if (line.startsWith("FIND MEMBER")) {
                    FindMember.execute(line, lineNumber, writer);
                } else if (line.startsWith("REMOVE MEMBER")) {
                    RemoveMember.execute(line, lineNumber, writer);
                } else if (line.startsWith("FIND GROUP")) {
                    FindGroup.execute(line, lineNumber, writer);
                } else if (line.startsWith("REMOVE GROUP")) {
                    RemoveGroup.execute(line, lineNumber, writer);
                } else if (line.startsWith("ADD EVENT")) {
                    AddEvent.execute(line, lineNumber, writer);
                }
            }

            reader.close();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
