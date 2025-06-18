package org.geotools.tutorial.Geotiff;

import org.geotools.api.data.*;
import org.geotools.data.DefaultTransaction;
import org.geotools.feature.FeatureCollection;
import org.geotools.tutorial.Geotiff.LatLong;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.style.*;
import org.geotools.coverage.processing.operation.Affine;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.swing.JMapPane;
import org.geotools.swing.MapPane;
import org.geotools.tutorial.checker.*;

import java.awt.Color;
import java.awt.Stroke;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;

import org.geotools.api.filter.FilterFactory;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.map.FeatureLayer;
import org.geotools.map.GridReaderLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.map.StyleLayer;
import org.geotools.styling.SLD;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.action.SafeAction;
import org.geotools.swing.data.JParameterListWizard;
import org.geotools.swing.wizard.JWizard;
import org.geotools.util.KVP;
import org.geotools.util.factory.Hints;
import javax.swing.JFrame;
import java.awt.geom.AffineTransform;
//************************************************************************
import org.geotools.styling.*;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.util.SimpleInternationalString;
import org.geotools.api.coverage.grid.GridCoverageReader;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import org.geotools.swing.JMapFrame;
import org.geotools.map.MapContent;
import org.geotools.referencing.CRS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.geom.Point;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;
//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureImpl;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.map.FeatureLayer;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.filter.identity.FeatureIdImpl;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;
import org.geotools.styling.*;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.geometry.jts.JTSFactoryFinder;

import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.geometry.jts.GeometryBuilder;
import org.geotools.styling.*;
import org.geotools.factory.CommonFactoryFinder;
import java.awt.Color;
import org.geotools.api.feature.FeatureVisitor;

//************************************************************************

public class ImageLab {
    private FeatureLayer pointLayer;
    private SimpleFeatureCollection pointsCollection;
    private MemoryDataStore memoryDataStore;
    private SimpleFeatureType featureType;
    private StyleFactory sf = CommonFactoryFinder.getStyleFactory();
    private FilterFactory ff = CommonFactoryFinder.getFilterFactory();
    static MapContent map;
    static JMapFrame frame;
    ArrayList<LatLong> pointArray;
    private GridCoverage2DReader reader;
    SimpleFeatureType lineFeatureType;
    FeatureLayer lineLayer;

    public static void main(String[] args) throws Exception {
        ImageLab me = new ImageLab();
        me.getLayersAndDisplay();

        String locations1Path = "src/main/Files/Continuous/output.shp";
//        ArrayDeque<File> filesDeque = new ArrayDeque<File>();
//        ArrayDeque<FileDataStore> locationsStoreDeque = new ArrayDeque<FileDataStore>();
//        ArrayDeque<SimpleFeatureSource> locationsFeatureSourceDeque = new ArrayDeque<SimpleFeatureSource>();
//        ArrayDeque<Layer> layersDeque = new ArrayDeque<Layer>();
//        System.out.println("Size : " + layersDeque.size() + '\t' + locationsStoreDeque.size() + '\t' + locationsFeatureSourceDeque.size() + '\t' + layersDeque.size());
//        filesDeque.addFirst(new File(locations1Path));
//        locationsStoreDeque.addFirst(FileDataStoreFinder.getDataStore(filesDeque.peekFirst()));
//        locationsFeatureSourceDeque.addFirst(locationsStoreDeque.peekFirst().getFeatureSource());
//        Style pointStyle = SLD.createPointStyle("circle", Color.RED, Color.RED, 1.0f, 10.0f);
//        layersDeque.addFirst(new FeatureLayer(locationsFeatureSourceDeque.peekFirst(), pointStyle));
//        System.out.print(layersDeque.size());
//        map.addLayer(layersDeque.peekFirst());
//        Thread.sleep(4000);
//        map.removeLayer(layersDeque.peekFirst());
//
//
//        while(true) {
//            Thread.sleep(4000);
//            if(layersDeque.size() == 0) continue;
//            map.removeLayer(layersDeque.removeLast());
//            locationsFeatureSourceDeque.removeLast();
//            locationsStoreDeque.removeLast();
//            filesDeque.removeLast();
//            System.out.println("Size : " + layersDeque.size() + '\t' + locationsStoreDeque.size() + '\t' + locationsFeatureSourceDeque.size() + '\t' + layersDeque.size());
//            System.gc();
//            tester.Test();
//            Thread.sleep(3000);
//
//            filesDeque.addFirst(new File(locations1Path));
//            locationsStoreDeque.addFirst(FileDataStoreFinder.getDataStore(filesDeque.peekFirst()));
//            locationsFeatureSourceDeque.addFirst(locationsStoreDeque.peekFirst().getFeatureSource());
//            layersDeque.addFirst(new FeatureLayer(locationsFeatureSourceDeque.peekFirst(), pointStyle));
//            map.addLayer(layersDeque.peekFirst());
//            frame.repaint();
//            System.out.println("Size : " + layersDeque.size() + '\t' + locationsStoreDeque.size() + '\t' + locationsFeatureSourceDeque.size() + '\t' + layersDeque.size());
//
//        }
        System.out.println("hello");
        frame.repaint();

    }

    private void getLayersAndDisplay() throws Exception {
        java.util.List<Parameter<?>> list = new ArrayList<>();
        list.add(
                new Parameter<>(
                        "image",
                        File.class,
                        "Image",
                        "GeoTiff or World+Image to display as basemap",
                        new KVP(Parameter.EXT, "tif", Parameter.EXT, "jpg")));
//        list.add(
//                new Parameter<>(
//                        "shape",
//                        File.class,
//                        "Shapefile",
//                        "Shapefile contents to display",
//                        new KVP(Parameter.EXT, "shp")));

        JParameterListWizard wizard =
                new JParameterListWizard("Image Lab", "Fill in the following layers", list);
        int finish = wizard.showModalDialog();

        if (finish != JWizard.FINISH) {
            System.exit(0);
        }
        File imageFile = (File) wizard.getConnectionParameters().get("image");
//        File shapeFile = (File) wizard.getConnectionParameters().get("shape");
//        displayLayers(imageFile, shapeFile);
        displayLayers(imageFile);
    }

    private void displayLayers(File rasterFile) throws Exception {
        AbstractGridFormat format = GridFormatFinder.findFormat(rasterFile);
        // this is a bit hacky but does make more geotiffs work
        Hints hints = new Hints();
        if (format instanceof GeoTiffFormat) {
            hints = new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
        }
        reader = format.getReader(rasterFile, hints);

        // Initially display the raster in scale using the
        // data from the first image band
        Style rasterStyle = createGreyscaleStyle();
//        Style rasterStyle = createRGBStyle();

        // Connect to the shapefile
//        FileDataStore dataStore = FileDataStoreFinder.getDataStore(shpFile);
//        SimpleFeatureSource shapefileSource = dataStore.getFeatureSource();

        // Create a basic style with yellow lines and no fill
        Style shpStyle = SLD.createPolygonStyle(Color.YELLOW, null, 0.0f);

        // Set up a MapContent with the two layers
        map = new MapContent();
        map.setTitle("ImageLab");

        Layer rasterLayer = new GridReaderLayer(reader, rasterStyle);
        map.addLayer(rasterLayer);

//        Layer shpLayer = new FeatureLayer(shapefileSource, shpStyle);
//        map.addLayer(shpLayer);

        // Create a JMapFrame with a menu to choose the display style for the

        frame = new JMapFrame(map);
        frame.setSize(800, 600);
        frame.enableStatusBar(true);
        frame.enableTool(JMapFrame.Tool.PAN, JMapFrame.Tool.RESET, JMapFrame.Tool.POINTER);

        JTextField xField = new JTextField();
        JTextField yField = new JTextField();
        JButton mapCoords = new JButton("Map");
        JButton clear = new JButton("Clear");
//        frame.enableToolBar(true);
        JToolBar tb = frame.getToolBar();
        tb.addSeparator(); tb.add(xField); tb.addSeparator(); tb.add(yField);
        tb.addSeparator(); tb.add(mapCoords); tb.addSeparator(); tb.add(clear);
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//                if(memoryDataStore != null){
//                    try {
//                        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
//                        Point point = geometryFactory.createPoint(new Coordinate(80, 17));
//                        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
//                        builder.setName("PointLayer");
//                        builder.add("location", Point.class);
//                        builder.setCRS(DefaultGeographicCRS.WGS84); // Set CRS to WGS84
//                        featureType = builder.buildFeatureType();
//                        memoryDataStore.updateSchema("PointLayer", featureType);
//                        ReferencedEnvelope currentView = frame.getMapPane().getDisplayArea();
//                        frame.getMapPane().setDisplayArea(new ReferencedEnvelope(currentView));
//                    } catch (Exception ex) {
//                        System.out.println("Exception in clear button");
//                    }
//
//                }

                try {
                    if (memoryDataStore.getNames().contains("pointLayer")) {
                        SimpleFeatureSource featureSource = memoryDataStore.getFeatureSource("pointLayer");

                        if (featureSource instanceof SimpleFeatureStore) {
                            SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;

                            // Create a filter to find the feature by its ID
//                            FilterFactory ff = CommonFactoryFinder.getFilterFactory();
//                            Filter filter = ff.id(ff.featureId(pointID));

                            // Remove the specific feature
                            featureStore.removeFeatures(Filter.INCLUDE);
//                            ReferencedEnvelope currentView = frame.getMapPane().getDisplayArea();
//                            frame.getMapPane().setDisplayArea(new ReferencedEnvelope(currentView));
                            frame.getMapPane().repaint();
                        }
                    }
                } catch(Exception eg) { System.out.println("clear button catch");}
            }
        });

//-----------------------------------------------------------------------------------------------
        pointArray = new ArrayList<LatLong>();
        mapCoords.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double latitude = Double.parseDouble(yField.getText());
                double longitude = Double.parseDouble(xField.getText());
                LatLong newPoint = new LatLong();
                newPoint.latitude = latitude;
                newPoint.longitude = longitude;
                pointArray.add(newPoint);

// Create a point geometry
                GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
                Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));

// If this is the first time, initialize the memoryDataStore and schema
                if (memoryDataStore == null) {
                    memoryDataStore = new MemoryDataStore();

                    // Define the feature type (schema)
                    SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
                    builder.setName("PointLayer");
                    builder.add("location", Point.class);
                    builder.setCRS(DefaultGeographicCRS.WGS84); // Set CRS to WGS84
                    featureType = builder.buildFeatureType();

                    try {
                        memoryDataStore.createSchema(featureType);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

                    // Create a style for the points
                    Style pointStyle = SLD.createPointStyle("circle", Color.RED, Color.RED, 1.0f, 5.0f);

                    // Add the layer to the map content
                    try {
                        pointLayer = new FeatureLayer(memoryDataStore.getFeatureSource("PointLayer"), pointStyle);
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    frame.getMapContent().addLayer(pointLayer);
                }

// Retrieve the FeatureStore to modify the data without refreshing
                SimpleFeatureSource featureSource;
                try {
                    featureSource = memoryDataStore.getFeatureSource("PointLayer");
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }

                if (featureSource instanceof FeatureStore) {
                    FeatureStore<SimpleFeatureType, SimpleFeature> featureStore = (FeatureStore<SimpleFeatureType, SimpleFeature>) featureSource;

                    // Start a transaction to avoid full refresh
                    Transaction transaction = new DefaultTransaction("addPoint");
                    featureStore.setTransaction(transaction);

                    try {
                        // Add new feature using transaction
                        SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);
                        featureBuilder.add(point);
                        SimpleFeature feature = featureBuilder.buildFeature(null);
                        featureStore.addFeatures(DataUtilities.collection(feature));

                        // Commit changes
                        transaction.commit();
                    } catch (Exception eg) {
                        eg.printStackTrace();
                        try {
                            transaction.rollback();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    } finally {
                        try {
                            transaction.close();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }

                    ReferencedEnvelope currentView = frame.getMapPane().getDisplayArea();
                    frame.getMapPane().setDisplayArea(new ReferencedEnvelope(currentView));
                }

//_______________________________________________________________________________________________________
            }
        });

// ____________________________________________________________________________________

        frame.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                applyZoom(frame.getMapPane(), e);
            }
        });
        JMenuBar menuBar = new JMenuBar();
        frame.setJMenuBar(menuBar);
        JMenu menu = new JMenu("Raster");
        menuBar.add(menu);

        menu.add(
                new SafeAction("Grayscale display") {
                    public void action(ActionEvent e) throws Throwable {
                        Style style = createGreyscaleStyle();
                        if (style != null) {
                            ((StyleLayer) map.layers().get(0)).setStyle(style);

                            frame.repaint();
                        } else {    System.out.println("!!!123!!!");}
                    }
                });
//
//        menu.add(
//                new SafeAction("RGB display") {
//                    public void action(ActionEvent e) throws Throwable {
//                        Style style = createRGBStyle();
//                        if (style != null) {
//                            ((StyleLayer) map.layers().get(0)).setStyle(style);
//                            frame.repaint();
//                        }
//                    }
//                });
        // Finally display the map frame.
        // When it is closed the app will exit.
        frame.setVisible(true);
    }

    private Style createGreyscaleStyle() {
        GridCoverage2D cov = null;
        try {
            cov = reader.read(null);
        } catch (IOException giveUp) {
            throw new RuntimeException(giveUp);
        }
        int numBands = cov.getNumSampleDimensions();
        System.out.println("Number of bands : " + numBands);
        System.out.println("*****\t" + numBands + "\t*****");
        Integer[] bandNumbers = new Integer[numBands];
        for (int i = 0; i < numBands; i++) {
            bandNumbers[i] = i + 1;
        }
//        Object selection =
//                JOptionPane.showInputDialog(
//                        frame,
//                        "Band to use for greyscale display",
//                        "Select an image band",
//                        JOptionPane.QUESTION_MESSAGE,
//                        null,
//                        bandNumbers,
//                        1);
        if (true) {
//            int band = ((Number) selection).intValue();
            return createGreyscaleStyle(1);
        }
        return null;
    }

    private Style createGreyscaleStyle(int band) {
        ContrastEnhancement ce = sf.contrastEnhancement(ff.literal(1.0), ContrastMethod.NORMALIZE);
        SelectedChannelType sct = sf.createSelectedChannelType(String.valueOf(band), ce);

        RasterSymbolizer sym = sf.getDefaultRasterSymbolizer();
        ChannelSelection sel = sf.channelSelection(sct);
        sym.setChannelSelection(sel);

        return SLD.wrapSymbolizers(sym);
    }

//    private Style createColourStyle() {
//        GridCoverage2D cov = null;
//        try {
//            cov = reader.read(null);
//        } catch (IOException giveUp) {
//            throw new RuntimeException(giveUp);
//        }
//
//        // Check if there are at least three bands for RGB display
//        int numBands = cov.getNumSampleDimensions();
//        if (numBands < 3) {
//            JOptionPane.showMessageDialog(frame,
//                    "The raster does not have enough bands to create an RGB style!",
//                    "Error",
//                    JOptionPane.ERROR_MESSAGE);
//            return null;
//        }
//
//        // Default RGB band indices (assuming first three bands for RGB)
//        final int RED = 1;   // Band 1 for Red
//        final int GREEN = 2; // Band 2 for Green
//        final int BLUE = 3;  // Band 3 for Blue
//
//        // Create selected channel types for each color band
//        ContrastEnhancement ce = sf.contrastEnhancement(ff.literal(1.0), ContrastMethod.NORMALIZE);
//
//        SelectedChannelType redChannel = sf.createSelectedChannelType(String.valueOf(RED), ce);
//        SelectedChannelType greenChannel = sf.createSelectedChannelType(String.valueOf(GREEN), ce);
//        SelectedChannelType blueChannel = sf.createSelectedChannelType(String.valueOf(BLUE), ce);
//
//        // Configure the RGB channels in a ChannelSelection
//        ChannelSelection sel = sf.channelSelection(redChannel, greenChannel, blueChannel);
//
//        // Create a RasterSymbolizer and set the channel selection
//        RasterSymbolizer sym = sf.getDefaultRasterSymbolizer();
//        sym.setChannelSelection(sel);
//
//        // Wrap the RasterSymbolizer into a Style and return it
//        return SLD.wrapSymbolizers(sym);
//    }

//    private Style colourCheck() {
//        GridCoverage2D cov = null;
//        try {
//            cov = reader.read(null);
//        } catch (IOException giveUp) {
//            throw new RuntimeException(giveUp);
//        }
//
//        // Check if the raster has at least one band (single-band check)
//        int numBands = cov.getNumSampleDimensions();
//        if (numBands < 1) {
//            JOptionPane.showMessageDialog(frame,
//                    "The raster does not have any bands to create a style!",
//                    "Error",
//                    JOptionPane.ERROR_MESSAGE);
//            return null;
//        }
//
//        // Use the first (and only) band for RGB (this is a single-band raster)
//        final int BAND = 1;  // Band 1 for Grayscale (mapped to all RGB channels)
//
//        // Create contrast enhancement (for better display)
//        ContrastEnhancement ce = sf.contrastEnhancement(ff.literal(1.0), "normalize");  // String for contrast method
//
//        // Create the selected channel type for the single band (applied to R, G, and B)
//        SelectedChannelType redChannel = sf.createSelectedChannelType(String.valueOf(BAND), ce);
//        SelectedChannelType greenChannel = sf.createSelectedChannelType(String.valueOf(BAND), ce);
//        SelectedChannelType blueChannel = sf.createSelectedChannelType(String.valueOf(BAND), ce);
//
//        // Configure the RGB channels in a ChannelSelection
//        ChannelSelection sel = sf.channelSelection(redChannel, greenChannel, blueChannel);
//
//        // Create a RasterSymbolizer and set the channel selection
//        RasterSymbolizer sym = sf.getDefaultRasterSymbolizer();
//        sym.setChannelSelection(sel);
//
//        // Wrap the RasterSymbolizer into a Style and return it
//        return SLD.wrapSymbolizers(sym);
//    }

    public static void applyZoom(JMapPane mapPane, MouseWheelEvent e) {
        if (mapPane != null) {

            double scaleFactor = e.getWheelRotation() < 0 ? 0.8 : 1.2;
            // Get the current viewport bounds
            ReferencedEnvelope currentEnvelope = mapPane.getDisplayArea();
            // Calculate the center of the current envelope


            MapContent mapContent = mapPane.getMapContent();
            AffineTransform transScreen2World = mapContent.getViewport().getScreenToWorld();
            Point2D screenPoint = new Point2D.Double(e.getPoint().x-7, e.getPoint().y-97);
            Point2D worldPoint = transScreen2World.transform(screenPoint, null);

            // Create a new envelope around the center
            double prevxdist = worldPoint.getX()-currentEnvelope.getMinX(); double nextxdist = currentEnvelope.getMaxX()-worldPoint.getX();
            double newprevxdist = prevxdist*scaleFactor; double newnextxdist = nextxdist*scaleFactor;
            double prevydist = worldPoint.getY()-currentEnvelope.getMinY(); double nextydist = currentEnvelope.getMaxY()-worldPoint.getY();
            double newprevydist = prevydist*scaleFactor; double newnextydist = nextydist*scaleFactor;
            ReferencedEnvelope new2 = new ReferencedEnvelope(
                    //Parameters are the min, max bounds of the envelope along X and Y
                    worldPoint.getX()-newprevxdist,
                    worldPoint.getX()+newnextxdist,
                    worldPoint.getY()-newprevydist,
                    worldPoint.getY()+newnextydist,
                    currentEnvelope.getCoordinateReferenceSystem()
            );

            // Set the new display area
            mapPane.setDisplayArea(new2);

//            mapPane.repaint();
        }
    }

}
