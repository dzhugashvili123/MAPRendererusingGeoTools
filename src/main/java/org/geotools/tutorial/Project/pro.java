//Allows Adding of two layers of shp files at a time


package org.geotools.tutorial.Project;

import java.awt.*;
import java.io.*;
import java.nio.file.Files;
import java.util.*;
import java.util.List;

import java.util.Scanner;
//import org.geotools.tutorial.quickstart;
import org.geotools.api.data.FileDataStore;
import java.nio.file.*;
import org.geotools.api.data.FileDataStoreFinder;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.data.SimpleFeatureStore;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.styling.SLD;
import org.geotools.api.style.Style;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.data.JFileDataStoreChooser;

import org.geotools.api.data.*;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.api.style.Style;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.geotools.data.DefaultTransaction;

public class pro {
    public static MapContent map = new MapContent();


    public static void main(String[] args) throws Exception {

//        File file1 = new File("src/main/Files/india_India_Country_Boundary.shp");
        File file1 = JFileDataStoreChooser.showOpenFile("shp", null);
        if (file1 == null) {
            return;
        }
//        File file2 = new File("src/main/Files/india_ds.shp");
        File file2 = JFileDataStoreChooser.showOpenFile("shp", null);
        if (file2 == null) {
            return;
        }

        FileDataStore layer1 = FileDataStoreFinder.getDataStore(file1);
        SimpleFeatureSource layer1FeatureSource = layer1.getFeatureSource();
        FileDataStore layer2 = FileDataStoreFinder.getDataStore(file2);
        SimpleFeatureSource layer2FeatureSource = layer2.getFeatureSource();

        Style stateBoundaryStyle = SLD.createLineStyle(Color.BLACK, 2.0f); // Black lines for states
        Style districtBoundaryStyle = SLD.createLineStyle(Color.GREEN, 1.0f); // Green lines for districts
        Layer stateLayer = new FeatureLayer(layer1FeatureSource, stateBoundaryStyle);
        Layer districtLayer = new FeatureLayer(layer2FeatureSource, districtBoundaryStyle);

        map.setTitle("Demo");
        map.addLayer(districtLayer);
        map.addLayer(stateLayer);
        JMapFrame.showMap(map);
        Thread updater = new Thread(new pointLoader());
        //updater.start();
    }

    static class pointLoader implements Runnable {

        @Override
        public void run() {
            File file = new File("src/main/Files/coord.txt");
            try {
                final SimpleFeatureType TYPE =
                        DataUtilities.createType(
                                "Location",
                                "the_geom:Point:srid=4326,"
                                        + // <- the geometry attribute: Point type
                                        "name:String,"
                                        + // <- a String attribute
                                        "number:Integer" // a number attribute
                        );
            } catch(Exception e) {}

            // Read all lines from the file
            java.util.List<String> lines = new ArrayList<>();
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                }
            } catch (Exception e) {}

            // Check if the file has any content
            if (lines.isEmpty()) {
                System.out.println("The file is empty.");
                return;
            }

            // Get and print the last line
            String lastLine = lines.remove(lines.size() - 1);
            System.out.println(lastLine);
            String[] lineArray = lastLine.trim().split("\\s+");
            double latitude = convertToDecimalDegrees(lineArray[0], lineArray[1]);
            double longitude = Double.parseDouble(lineArray[2]);
            if(lineArray[3].equals("W"))    longitude *= -1;
            System.out.println(latitude + "\t" + longitude);

            // Write the remaining lines back to the file
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (String line : lines) {
                    writer.write(line);
                    writer.newLine();
                }
            } catch (Exception e) {}

        }
    }

    public static double convertToDecimalDegrees(String coordinate, String direction) {
        double degrees, minutes;

        // Check if input has both degrees and minutes (e.g., 2235.5001 or 78.9629)
        if (coordinate.contains(".")) {
            String[] parts = coordinate.split("\\.");

            // Degrees are the first part, take first two digits
            int degreePartLength = direction.equals("N") || direction.equals("S") ? 2 : 3;
            degrees = Double.parseDouble(coordinate.substring(0, degreePartLength));

            // Minutes are the rest
            minutes = Double.parseDouble(coordinate.substring(degreePartLength));
        } else {
            degrees = Double.parseDouble(coordinate);
            minutes = 0.0; // No minutes to parse
        }

        // Convert to decimal degrees
        double decimalDegrees = degrees + (minutes / 60.0);

        // Handle direction (N/S/E/W)
        if (direction.equals("S") || direction.equals("W")) {
            decimalDegrees *= -1; // South and West are negative
        }

        return decimalDegrees;
    }

}
