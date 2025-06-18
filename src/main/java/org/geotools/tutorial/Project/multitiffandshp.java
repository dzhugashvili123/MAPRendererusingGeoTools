package org.geotools.tutorial.Project;

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
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.util.GeometricShapeFactory;

//************************************************************************

public class multitiffandshp {
    private static FeatureLayer pointLayer;
    private static FeatureLayer lineLayer;
    private static FeatureLayer circleLayer;
    private static MemoryDataStore memoryDataStore;
    private static SimpleFeatureType pointFeatureType;
    private static SimpleFeatureType lineFeatureType;
    private static SimpleFeatureType circleFeatureType;
    private StyleFactory sf = CommonFactoryFinder.getStyleFactory();
    private FilterFactory ff = CommonFactoryFinder.getFilterFactory();
    static MapContent map;
    static JMapFrame frame;;
    static private GridCoverage2DReader reader;
    static private Layer shapeLayer;
    static ArrayList<Point> pointArray;

    public static void main(String[] args) throws Exception {
        multitiffandshp me = new multitiffandshp();

        map = new MapContent();
        map.setTitle("ImageLab");
        File shpFile = new File("C:/Users/venka/OneDrive/Desktop/GeoToolsProject/Maps/IndiaShape/States/Admin2.shp");
        File tiffFolder = new File("C:/Users/venka/OneDrive/Desktop/GeoToolsProject/defcsedfvsf");

        FileDataStore store = FileDataStoreFinder.getDataStore(shpFile);
        SimpleFeatureSource featureSource = store.getFeatureSource();
        Style style = SLD.createSimpleStyle(featureSource.getSchema());
        shapeLayer = new FeatureLayer(featureSource, style);
        map.addLayer(shapeLayer);
        frame = new JMapFrame(map);

                 //Configurations of frame such as toolbar, zoom, buttons, size, etc.
        me.getLayersAndDisplay(tiffFolder);
        configFrame();
        initializeMapLayers();
        frame.setVisible(true);


    }

    private void getLayersAndDisplay(File tiffFolder) throws Exception {

        File[] tiffFiles = tiffFolder.listFiles((dir, name) -> name.endsWith(".tif") || name.endsWith(".tiff"));
        if(tiffFiles == null)   return;
        for(File tiff : tiffFiles) {
            displayLayers(tiff);
        }
    }

    private void displayLayers(File rasterFile) {
        AbstractGridFormat format = GridFormatFinder.findFormat(rasterFile);
        // this is a bit hacky but does make more geotiffs work
        Hints hints = new Hints();
        if (format instanceof GeoTiffFormat) {
            hints = new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
        }
        reader = format.getReader(rasterFile, hints);

        StyleBuilder styleBuilder = new StyleBuilder();
        Style rasterStyle = styleBuilder.createStyle(styleBuilder.createRasterSymbolizer());
        Layer rasterLayer = new GridReaderLayer(reader, rasterStyle);

        map.addLayer(rasterLayer);



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

//        menu.add(
//                new SafeAction("RGB display") {
//                    public void action(ActionEvent e) throws Throwable {
//                        Style style = createColourStyle();
//                        if (style != null) {
//                            ((StyleLayer) map.layers().get(0)).setStyle(style);
//                            frame.repaint();
//                        }
//                    }
//                });
        // Finally display the map frame.
        // When it is closed the app will exit.

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

    public static void configFrame() {
        frame.setSize(800, 600);
        frame.enableStatusBar(true);
        frame.enableTool(JMapFrame.Tool.PAN, JMapFrame.Tool.RESET, JMapFrame.Tool.POINTER);

        JTextField xField = new JTextField();
        xField.setToolTipText("Longitude");
        JTextField yField = new JTextField();
        yField.setToolTipText("Latitude");
//        frame.enableToolBar(true);
        JToolBar tb = frame.getToolBar();
        tb.addSeparator(); tb.add(xField); tb.add(yField);


//-----------------------------------------------------------------------------------------------
        JButton mapCoords = new JButton("Map");
        tb.addSeparator(); tb.add(mapCoords);
        mapCoords.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                double latitude = Double.parseDouble(yField.getText());
                double longitude = Double.parseDouble(xField.getText());

                GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
                Point point = geometryFactory.createPoint(new Coordinate(longitude, latitude));


                try {
                    SimpleFeatureSource pointFeatureSource = memoryDataStore.getFeatureSource("PointLayer");
                    SimpleFeatureSource lineFeatureSource = memoryDataStore.getFeatureSource("LineLayer");

                    if (pointFeatureSource instanceof FeatureStore && lineFeatureSource instanceof FeatureStore) {
                        FeatureStore<SimpleFeatureType, SimpleFeature> pointFeatureStore =
                                (FeatureStore<SimpleFeatureType, SimpleFeature>) pointFeatureSource;
                        FeatureStore<SimpleFeatureType, SimpleFeature> lineFeatureStore =
                                (FeatureStore<SimpleFeatureType, SimpleFeature>) lineFeatureSource;

                        Transaction transaction = new DefaultTransaction("addPoint");
                        pointFeatureStore.setTransaction(transaction);
                        lineFeatureStore.setTransaction(transaction);

                        try {
                            // Ensure only the last two points exist
                            if (pointArray.size() == 2) {
                                pointArray.remove(0); // Remove the oldest point
                            }
                            pointArray.add(point);

                            // Clear previous points and lines
                            pointFeatureStore.removeFeatures(Filter.INCLUDE);
                            lineFeatureStore.removeFeatures(Filter.INCLUDE);

                            // Add points to feature store
                            List<SimpleFeature> newPointFeatures = new ArrayList<>();
                            for (Point p : pointArray) {
                                SimpleFeatureBuilder pointBuilder = new SimpleFeatureBuilder(pointFeatureType);
                                pointBuilder.add(p);
                                newPointFeatures.add(pointBuilder.buildFeature(null));
                            }
                            pointFeatureStore.addFeatures(DataUtilities.collection(newPointFeatures));

                            // If two points exist, draw a line between them
                            if (pointArray.size() == 2) {
                                Coordinate[] coordinates = {
                                        new Coordinate(pointArray.get(0).getX(), pointArray.get(0).getY()), // Correct order
                                        new Coordinate(pointArray.get(1).getX(), pointArray.get(1).getY())
                                };
                                LineString line = geometryFactory.createLineString(coordinates);

                                SimpleFeatureBuilder lineBuilder = new SimpleFeatureBuilder(lineFeatureType);
                                lineBuilder.add(line);
                                SimpleFeature lineFeature = lineBuilder.buildFeature(null);
                                lineFeatureStore.addFeatures(DataUtilities.collection(lineFeature));
                            }

                            transaction.commit();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            transaction.rollback();
                        } finally {
                            transaction.close();
                        }

                        // Refresh the map
                        ReferencedEnvelope currentView = frame.getMapPane().getDisplayArea();
                        frame.getMapPane().setDisplayArea(new ReferencedEnvelope(currentView));
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

//-----------------------------------------------------------------------------------------------
        JButton clear = new JButton("Clear");
        tb.addSeparator(); tb.add(clear);
        clear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (memoryDataStore == null) {
                    return; // No data to clear
                }

                try {
                    // Get FeatureStores for all layers
                    SimpleFeatureSource pointFeatureSource = memoryDataStore.getFeatureSource("PointLayer");
                    SimpleFeatureSource lineFeatureSource = memoryDataStore.getFeatureSource("LineLayer");
                    SimpleFeatureSource circleFeatureSource = memoryDataStore.getFeatureSource("CircleLayer");

                    if (pointFeatureSource instanceof FeatureStore &&
                            lineFeatureSource instanceof FeatureStore &&
                            circleFeatureSource instanceof FeatureStore) {

                        FeatureStore<SimpleFeatureType, SimpleFeature> pointFeatureStore =
                                (FeatureStore<SimpleFeatureType, SimpleFeature>) pointFeatureSource;
                        FeatureStore<SimpleFeatureType, SimpleFeature> lineFeatureStore =
                                (FeatureStore<SimpleFeatureType, SimpleFeature>) lineFeatureSource;
                        FeatureStore<SimpleFeatureType, SimpleFeature> circleFeatureStore =
                                (FeatureStore<SimpleFeatureType, SimpleFeature>) circleFeatureSource;

                        // Start transaction
                        Transaction transaction = new DefaultTransaction("clearAll");
                        pointFeatureStore.setTransaction(transaction);
                        lineFeatureStore.setTransaction(transaction);
                        circleFeatureStore.setTransaction(transaction);

                        try {
                            // Remove all features (points, lines, circles)
                            pointFeatureStore.removeFeatures(Filter.INCLUDE);  // Removes normal points & center point
                            lineFeatureStore.removeFeatures(Filter.INCLUDE);
                            circleFeatureStore.removeFeatures(Filter.INCLUDE);

                            // Commit transaction
                            transaction.commit();
                        } catch (Exception ex) {
                            ex.printStackTrace();
                            transaction.rollback();
                        } finally {
                            transaction.close();
                        }

                        // Clear all stored points, including circle center
                        pointArray.clear();

                        // Refresh map
                        ReferencedEnvelope currentView = frame.getMapPane().getDisplayArea();
                        frame.getMapPane().setDisplayArea(new ReferencedEnvelope(currentView));
                    }
                } catch (Exception ex) {
                    System.out.println("Error while clearing map: " + ex.getMessage());
                }
            }
        });

//-----------------------------------------------------------------------------------------------
        JButton circleButton = new JButton("Circle");
        tb.addSeparator(); tb.add(circleButton);
        circleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    double latitude = Double.parseDouble(yField.getText());
                    double longitude = Double.parseDouble(xField.getText());

                    GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
                    Point centerPoint = geometryFactory.createPoint(new Coordinate(longitude, latitude));

                    double radiusInDegrees = 300.0 / 111.0;

                    GeometricShapeFactory shapeFactory = new GeometricShapeFactory(geometryFactory);
                    shapeFactory.setCentre(new Coordinate(longitude, latitude));
                    shapeFactory.setSize(radiusInDegrees * 2);
                    shapeFactory.setNumPoints(100);
                    Polygon circle = shapeFactory.createCircle();

                    //Center Point Layer exists
                    if (!Arrays.asList(memoryDataStore.getTypeNames()).contains("CenterPointLayer")) {
                        SimpleFeatureTypeBuilder pointBuilder = new SimpleFeatureTypeBuilder();
                        pointBuilder.setName("CenterPointLayer");
                        pointBuilder.add("location", Point.class);
                        pointBuilder.setCRS(DefaultGeographicCRS.WGS84);
                        SimpleFeatureType centerPointFeatureType = pointBuilder.buildFeatureType();
                        memoryDataStore.createSchema(centerPointFeatureType);

                        Style centerPointStyle = SLD.createPointStyle("circle", Color.RED, Color.RED, 1.0f, 5.0f);
                        FeatureLayer centerPointLayer = new FeatureLayer(memoryDataStore.getFeatureSource("CenterPointLayer"), centerPointStyle);
                        frame.getMapContent().addLayer(centerPointLayer);
                    }

                    //Circle Layer exists
                    if (!Arrays.asList(memoryDataStore.getTypeNames()).contains("CircleLayer")) {
                        SimpleFeatureTypeBuilder circleBuilder = new SimpleFeatureTypeBuilder();
                        circleBuilder.setName("CircleLayer");
                        circleBuilder.add("circle", Polygon.class);
                        circleBuilder.setCRS(DefaultGeographicCRS.WGS84);
                        SimpleFeatureType circleFeatureType = circleBuilder.buildFeatureType();
                        memoryDataStore.createSchema(circleFeatureType);

                        Style circleStyle = SLD.createPolygonStyle(Color.BLUE, Color.CYAN, 0.5f);
                        FeatureLayer circleLayer = new FeatureLayer(memoryDataStore.getFeatureSource("CircleLayer"), circleStyle);
                        frame.getMapContent().addLayer(circleLayer);
                    }

                    SimpleFeatureStore centerPointFeatureStore =
                            (SimpleFeatureStore) memoryDataStore.getFeatureSource("CenterPointLayer");
                    SimpleFeatureStore circleFeatureStore =
                            (SimpleFeatureStore) memoryDataStore.getFeatureSource("CircleLayer");

                    Transaction transaction = new DefaultTransaction("addCircle");
                    centerPointFeatureStore.setTransaction(transaction);
                    circleFeatureStore.setTransaction(transaction);

                    try {
                        centerPointFeatureStore.removeFeatures(Filter.INCLUDE);
                        circleFeatureStore.removeFeatures(Filter.INCLUDE);

                        SimpleFeatureBuilder pointFeatureBuilder = new SimpleFeatureBuilder(centerPointFeatureStore.getSchema());
                        pointFeatureBuilder.add(centerPoint);
                        SimpleFeature pointFeature = pointFeatureBuilder.buildFeature(null);
                        centerPointFeatureStore.addFeatures(DataUtilities.collection(pointFeature));

                        SimpleFeatureBuilder circleFeatureBuilder = new SimpleFeatureBuilder(circleFeatureStore.getSchema());
                        circleFeatureBuilder.add(circle);
                        SimpleFeature circleFeature = circleFeatureBuilder.buildFeature(null);
                        circleFeatureStore.addFeatures(DataUtilities.collection(circleFeature));

                        transaction.commit();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        transaction.rollback();
                    } finally {
                        transaction.close();
                    }

                    ReferencedEnvelope currentView = frame.getMapPane().getDisplayArea();
                    frame.getMapPane().setDisplayArea(new ReferencedEnvelope(currentView));

                } catch (Exception ex) {
                    System.out.println("!!!Plotting Circle!!! - Exception occurred");
                    ex.printStackTrace();
                }
            }
        });

//-----------------------------------------------------------------------------------------------
        frame.addMouseWheelListener(new MouseWheelListener() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                applyZoom(frame.getMapPane(), e);
            }
        });
    }

    private static void initializeMapLayers() {
        memoryDataStore = new MemoryDataStore();

        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();

        //Point Layer Schema
        SimpleFeatureTypeBuilder pointBuilder = new SimpleFeatureTypeBuilder();
        pointBuilder.setName("PointLayer");
        pointBuilder.add("location", Point.class);
        pointBuilder.setCRS(DefaultGeographicCRS.WGS84);
        pointFeatureType = pointBuilder.buildFeatureType();

        //Line Layer Schema
        pointArray = new ArrayList<>();
        SimpleFeatureTypeBuilder lineBuilder = new SimpleFeatureTypeBuilder();
        lineBuilder.setName("LineLayer");
        lineBuilder.add("line", LineString.class);
        lineBuilder.setCRS(DefaultGeographicCRS.WGS84);
        lineFeatureType = lineBuilder.buildFeatureType();

        //Circle Layer Schema
        SimpleFeatureTypeBuilder circleBuilder = new SimpleFeatureTypeBuilder();
        circleBuilder.setName("CircleLayer");
        circleBuilder.add("polygon", Polygon.class);
        circleBuilder.setCRS(DefaultGeographicCRS.WGS84);
        circleFeatureType = circleBuilder.buildFeatureType();

        try {
            memoryDataStore.createSchema(pointFeatureType);
            memoryDataStore.createSchema(lineFeatureType);
            memoryDataStore.createSchema(circleFeatureType);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        // Define styles
        Style pointStyle = SLD.createPointStyle("circle", Color.RED, Color.RED, 1.0f, 5.0f);
        Style lineStyle = SLD.createLineStyle(Color.BLUE, 2.0f);
        Style circleStyle = SLD.createPolygonStyle(Color.YELLOW, Color.ORANGE, 0.5f);

        try {
            pointLayer = new FeatureLayer(memoryDataStore.getFeatureSource("PointLayer"), pointStyle);
            lineLayer = new FeatureLayer(memoryDataStore.getFeatureSource("LineLayer"), lineStyle);
            circleLayer = new FeatureLayer(memoryDataStore.getFeatureSource("CircleLayer"), circleStyle);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        frame.getMapContent().addLayer(pointLayer);
        frame.getMapContent().addLayer(lineLayer);
        frame.getMapContent().addLayer(circleLayer);
    }
}
