//Simple .shp file rendering program


package org.geotools.tutorial.quickstart;

import org.geotools.api.data.FileDataStore;
import org.geotools.api.data.FileDataStoreFinder;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.style.Style;
import org.geotools.map.FeatureLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.styling.SLD;
import org.geotools.swing.JMapFrame;

import java.awt.Color;
import java.io.File;

public class twoshpviewer {

    public static void main(String[] args) throws Exception {
        // File paths for the shapefiles
        String indiaShapefilePath = "C:/Users/venka/Downloads/Newfolder/shapefile_output/multilinestring.shp"; // Path to India shapefile
        String locations1Path = "C:/Users/venka/Downloads/mmm.shp";  // Path to first point shapefile
//        String locations2Path = "C:/Users/venka/OneDrive/Desktop/GeoToolsProject/locations2.shp";  // Path to second point shapefile

        // Load the India shapefile
        File indiaShapefile = new File(indiaShapefilePath);
        FileDataStore indiaStore = FileDataStoreFinder.getDataStore(indiaShapefile);
        SimpleFeatureSource indiaFeatureSource = indiaStore.getFeatureSource();

        // Load the locations1 shapefile
        File locations1Shapefile = new File(locations1Path);
        FileDataStore locations1Store = FileDataStoreFinder.getDataStore(locations1Shapefile);
        SimpleFeatureSource locations1FeatureSource = locations1Store.getFeatureSource();


        // Load the locations2 shapefile
//        File locations2Shapefile = new File(locations2Path);
//        FileDataStore locations2Store = FileDataStoreFinder.getDataStore(locations2Shapefile);
//        SimpleFeatureSource locations2FeatureSource = locations2Store.getFeatureSource();

        // Create the map content and set the title
        MapContent map = new MapContent();
        map.setTitle("India Map with Points Animation");

        // Add the India map as a layer
        Style indiaStyle = SLD.createSimpleStyle(indiaFeatureSource.getSchema());
        Layer indiaLayer = new FeatureLayer(indiaFeatureSource, indiaStyle);
        map.addLayer(indiaLayer);

        // Add the first location as a layer
        Style pointStyle = SLD.createPointStyle("circle", Color.RED, Color.RED, 1.0f, 10.0f);
        Layer locations1Layer = new FeatureLayer(locations1FeatureSource, pointStyle);
        map.addLayer(locations1Layer);



        // Wait for 5 seconds



        // Add the second location as a new layer

//        Layer locations2Layer = new FeatureLayer(locations2FeatureSource, pointStyle);
//        map.addLayer(locations2Layer);
        // Display the map
        JMapFrame mapFrame = new JMapFrame(map);
        mapFrame.setSize(800, 600);
        mapFrame.setVisible(true);
        mapFrame.enableToolBar(true);

    }
}
