//Renders random locations on a map of India, this map is updated every now and then.


package org.geotools.tutorial.checker;

import org.geotools.api.data.FileDataStore;
import org.geotools.api.data.FileDataStoreFinder;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.style.Style;
import org.geotools.map.*;
import org.geotools.styling.SLD;
import org.geotools.swing.JMapFrame;

import java.awt.*;
import java.io.File;
import java.util.*;

public class SHPRendering {

    public static void main(String[] args) throws Exception {
        // File paths for the shapefiles
        String indiaShapefilePath = "src/main/Files/India_Boundary/India_Boundary.shp"; // Path to India shapefile
//        String locations1Path = "src/main/Files/Continuous/output.shp";  // Path to first point shapefile
//        String locations2Path = "C:/Users/venka/OneDrive/Desktop/GeoToolsProject/locations2.shp";  // Path to second point shapefile

        // Load the India shapefile
        File indiaShapefile = new File(indiaShapefilePath);
        FileDataStore indiaStore = FileDataStoreFinder.getDataStore(indiaShapefile);
        SimpleFeatureSource indiaFeatureSource = indiaStore.getFeatureSource();

//        ArrayDeque<File> filesDeque = new ArrayDeque<File>();
//        ArrayDeque<FileDataStore> locationsStoreDeque = new ArrayDeque<FileDataStore>();
//        ArrayDeque<SimpleFeatureSource> locationsFeatureSourceDeque = new ArrayDeque<SimpleFeatureSource>();
//        ArrayDeque<Layer> layersDeque = new ArrayDeque<Layer>();
//        System.out.println("Size : " + layersDeque.size() + '\t' + locationsStoreDeque.size() + '\t' + locationsFeatureSourceDeque.size() + '\t' + layersDeque.size());

        // Load the locations1 shapefile
//        filesDeque.addFirst(new File(locations1Path));
//        locationsStoreDeque.addFirst(FileDataStoreFinder.getDataStore(filesDeque.peekFirst()));
//        FileDataStore locations1Store = locationsStoreDeque.peekFirst();
//        locationsFeatureSourceDeque.addFirst(locationsStoreDeque.peekFirst().getFeatureSource());
//        SimpleFeatureSource locations1FeatureSource = locationsFeatureSourceDeque.peekFirst();

        // Load the locations2 shapefile
//        File locations2Shapefile = new File(locations2Path);
//        FileDataStore locations2Store = FileDataStoreFinder.getDataStore(locations2Shapefile);
//        SimpleFeatureSource locations2FeatureSource = locations2Store.getFeatureSource();

        // Create the map content and set the title
        MapContent map = new MapContent();
//        map.addMapLayerListListener(new MapLayerListListener() {
//            @Override
//            public void layerAdded(MapLayerListEvent event) {
//                System.out.println("!!!!!\tlayerAdded\t!!!!!");
//            }
//
//            @Override
//            public void layerRemoved(MapLayerListEvent event) {
//                System.out.println("!!!!!\tlayerRemoved\t!!!!!");
//            }
//
//            @Override
//            public void layerChanged(MapLayerListEvent event) {
//                System.out.println("!!!!!\tlayerChanged\t!!!!!");
//            }
//
//            @Override
//            public void layerMoved(MapLayerListEvent event) {
//                System.out.println("!!!!!\tlayerMoved\t!!!!!");
//            }
//            @Override
//            public void layerPreDispose(MapLayerListEvent event) {
//                System.out.println("!!!!!\tlayerPreDisposed\t!!!!!");
//            }
//        });
        map.setTitle("India Map with Points Animation");

        // Add the India map as a layer
        Style indiaStyle = SLD.createSimpleStyle(indiaFeatureSource.getSchema());
        Layer indiaLayer = new FeatureLayer(indiaFeatureSource, indiaStyle);
        map.addLayer(indiaLayer);

        // Add the first location as a layer
        Style pointStyle = SLD.createPointStyle("circle", Color.RED, Color.RED, 1.0f, 10.0f);
//        layersDeque.addFirst(new FeatureLayer(locationsFeatureSourceDeque.peekFirst(), pointStyle));
//        map.addLayer(layersDeque.peekFirst());


        // Display the map
        JMapFrame mapFrame = new JMapFrame(map);
//        mapFrame.setSize(800, 600);
        mapFrame.setVisible(true);
//        Thread.sleep(4000);

//        map.removeLayer(layersDeque.peekFirst());
//        mapFrame.

//        while(true) {
//            Thread.sleep(3000);
//            if(layersDeque.size() == 0) continue;
//            map.removeLayer(layersDeque.removeLast());
//            locationsFeatureSourceDeque.removeLast();
//            locationsStoreDeque.removeLast();
//            filesDeque.removeLast();
//            System.out.println("Size : " + layersDeque.size() + '\t' + locationsStoreDeque.size() + '\t' + locationsFeatureSourceDeque.size() + '\t' + layersDeque.size());
//            System.gc();
//            tester.Test();
//            Thread.sleep(300);
//
//            filesDeque.addFirst(new File(locations1Path));
//            locationsStoreDeque.addFirst(FileDataStoreFinder.getDataStore(filesDeque.peekFirst()));
//            locationsFeatureSourceDeque.addFirst(locationsStoreDeque.peekFirst().getFeatureSource());
//            layersDeque.addFirst(new FeatureLayer(locationsFeatureSourceDeque.peekFirst(), pointStyle));
//            map.addLayer(layersDeque.peekFirst());
//            mapFrame.repaint();
//            System.out.println("Size : " + layersDeque.size() + '\t' + locationsStoreDeque.size() + '\t' + locationsFeatureSourceDeque.size() + '\t' + layersDeque.size());
//
//        }
    }
}
