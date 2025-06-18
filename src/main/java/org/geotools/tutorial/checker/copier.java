//Moves the last entry of input .csv to another fresh .csv(if exists, it deletes and recreates)


package org.geotools.tutorial.checker;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class copier {

    public static void processCSV(String inputFilePath, String outputFilePath) {
        // List to store the lines from the input file
        List<String> lines = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(inputFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            System.err.println("Error reading the input file: " + e.getMessage());
            return;
        }

        // Check if the file has enough lines
        if (lines.size() < 2) {
            System.err.println("The input file must contain at least two lines.");
            return;
        }

        // Extract the first and last lines
        String firstLine = lines.get(0);
        String lastLine = lines.get(lines.size() - 1);

        // Write the extracted lines to the output file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            writer.write(firstLine);
            writer.newLine();
            writer.write(lastLine);
        } catch (IOException e) {
            System.err.println("Error writing to the output file: " + e.getMessage());
        }

        // Remove the last line from the input file
        lines.remove(lines.size() - 1);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(inputFilePath))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error updating the input file: " + e.getMessage());
        }
    }

    public static void csvToCsv(String input, String output) {
        processCSV(input, output);
        System.out.println("Processing completed. Check the output and input files.");
    }
}
