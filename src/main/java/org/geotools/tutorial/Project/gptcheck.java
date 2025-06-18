package org.geotools.tutorial.Project;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.api.data.DataStoreFinder;
import org.geotools.map.MapContent;
import org.geotools.map.GridReaderLayer;
import org.geotools.swing.JMapFrame;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

//public class DisplayGeoTIFF {
//
//    public static void main(String[] args) {
//        try {
//            // Call the method to display the GeoTIFF
//            displayTiff("path/to/your/tiff-file.tif");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    public static void displayTiff(String tiffPath) throws Exception {
//        // Load the GeoTIFF file
//        File file = new File(tiffPath);
//        if (!file.exists()) {
//            throw new IllegalArgumentException("File not found: " + tiffPath);
//        }
//
//        // Create a reader for the GeoTIFF file
//        Map<String, Object> params = new HashMap<>();
//        params.put("url", file.toURI().toURL());
//        GridCoverage2DReader reader = (GridCoverage2DReader) DataStoreFinder.getDataStore(params);
//
//        // Read the coverage
//        GridCoverage2D coverage = reader.read(null);
//
//        // Create a MapContent and add the GeoTIFF as a layer
//        MapContent mapContent = new MapContent();
//        mapContent.setTitle("GeoTIFF Viewer");
//        mapContent.addLayer(new GridReaderLayer(reader, reader.getFormat().getReadParameters()));
//
//        // Display the map using JMapFrame
//        JMapFrame mapFrame = new JMapFrame(mapContent);
//        mapFrame.enableStatusBar(true);
//        mapFrame.enableToolBar(true);
//        mapFrame.setSize(800, 600);
//        mapFrame.setVisible(true);
//    }
//}