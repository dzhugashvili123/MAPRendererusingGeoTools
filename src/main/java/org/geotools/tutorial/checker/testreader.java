package org.geotools.tutorial.checker;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class testreader {
    public static void main(String[] args) {
        String fileName = "src/main/java/org/geotools/tutorial/checker/newFile.txt"; // Name of the file to read

        try {
            // Create a File object to read
            File file = new File(fileName);

            // Use Scanner to read the file
            Scanner scanner = new Scanner(file);

            System.out.println("File Contents:");
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                System.out.println(line);
            }

            // Close the scanner
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred. File not found.");
            e.printStackTrace();
        }
    }
}

