//Calls copier and converter in quick succession. Can be used in a loop to generate a steady stream of .shp point coordinates


package org.geotools.tutorial.checker;

public class tester {

    public static void Test() {
        String input = "src/main/Files/Continuous/masterCopy.csv";
        String output = "src/main/Files/Continuous/output.csv";
        copier.csvToCsv(input, output);
        System.gc();
        try {
            converter.csvToShp(output);
        } catch (Exception e) {
            System.out.println("!!!Exception!!!");
        }
    }
}
