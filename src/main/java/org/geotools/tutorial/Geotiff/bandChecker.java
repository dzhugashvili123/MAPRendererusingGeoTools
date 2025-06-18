package org.geotools.tutorial.Geotiff;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.gce.geotiff.GeoTiffReader;

import java.io.File;

public class bandChecker {
    public static void main(String[] args) {
        try {
            // Path to the GeoTIFF file
            String filePath = "C:/Users/venka/OneDrive/Desktop/GeoToolsProject/drive-download-20241129T041457Z-001/India-geoMap-v1.tif";
            File geoTiffFile = new File(filePath);

            if (!geoTiffFile.exists()) {
                System.err.println("File not found: " + filePath);
                return;
            }

            // Create a GeoTiffReader
            GeoTiffReader reader = new GeoTiffReader(geoTiffFile);

            // Read the coverage from the GeoTIFF
            GridCoverage2D coverage = reader.read(null);

            // Get the number of bands
            int numBands = coverage.getNumSampleDimensions();
            System.out.println("Number of bands in the GeoTIFF: " + numBands);

            // Cleanup
            reader.dispose();
        } catch (Exception e) {
            System.err.println("Error reading the GeoTIFF file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
