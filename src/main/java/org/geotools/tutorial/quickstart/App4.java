package org.geotools.tutorial.quickstart;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.media.jai.PlanarImage;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import org.geotools.api.data.FileDataStore;
import org.geotools.api.data.FileDataStoreFinder;
import org.geotools.api.data.Parameter;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.geometry.BoundingBox;
import org.geotools.api.style.ChannelSelection;
import org.geotools.api.style.ColorMap;
import org.geotools.api.style.ColorMapEntry;
import org.geotools.api.style.ContrastEnhancement;
import org.geotools.api.style.ContrastMethod;
import org.geotools.api.style.RasterSymbolizer;
import org.geotools.api.style.SelectedChannelType;
import org.geotools.api.style.Style;
import org.geotools.api.style.StyleFactory;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.gce.geotiff.GeoTiffFormat;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.GridReaderLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.map.MapLayerEvent;
import org.geotools.map.MapLayerListEvent;
import org.geotools.map.StyleLayer;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.SLD;
import org.geotools.styling.StyleBuilder;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.JMapPane;
import org.geotools.swing.MapLayerTable;
import org.geotools.swing.MapPane;
import org.geotools.swing.action.SafeAction;
import org.geotools.swing.data.JParameterListWizard;
import org.geotools.swing.event.MapPaneAdapter;
import org.geotools.swing.event.MapPaneEvent;
import org.geotools.swing.wizard.JWizard;
import org.geotools.util.KVP;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

/**
 * Hello world!
 *
 */
public class App4 {

	private StyleFactory sf = CommonFactoryFinder.getStyleFactory();
	private FilterFactory ff = CommonFactoryFinder.getFilterFactory();
	static MapContent mapContent;
	static JMapFrame frame;
	static JMapPane mapPane;
	static MapLayerTable mapLayerTable;
	private DefaultFeatureCollection featureCollection;
	private Layer pointLayer;
    private Layer shapeLayer;
    private boolean hasDummyFeature = false; // Track if a dummy feature exists

	private GridCoverage2DReader reader;

	public static void main(String[] args) throws Exception {
		App4 app = new App4();
		app.getLayersAndDisplay();
		CRS.getAuthorityFactory(true);
		System.setProperty("org.geotools.referencing.forceXY", "true");
		StreamingRenderer renderer = new StreamingRenderer();
		renderer.setRendererHints(Collections.singletonMap("rendering.buffer", true));

	}

	private void getLayersAndDisplay() throws Exception {
		File shapefile = new File("C:/Users/venka/OneDrive/Desktop/GeoToolsProject/Maps/50m_cultural/ne_50m_admin_0_countries.shp");
		File tiffFolder = new File("C:/Users/venka/OneDrive/Desktop/GeoToolsProject/abc");
		mapContent = new MapContent();
		mapContent.setTitle("Dynamic GeoTIFF Loader");		
		addShapefileLayer(shapefile);		
		frame = new JMapFrame(mapContent);
		frame.setSize(800, 600);
		frame.enableStatusBar(true);
		frame.enableTool(JMapFrame.Tool.PAN, JMapFrame.Tool.RESET, JMapFrame.Tool.POINTER);		

		JTextField xField = new JTextField();
		JTextField yField = new JTextField();
		JButton mapCoords = new JButton("Map");
		JToolBar tb = frame.getToolBar();
		tb.addSeparator();
		tb.add(xField);
		tb.addSeparator();
		tb.add(yField);
		tb.addSeparator();
		tb.add(mapCoords);
		mapPane = frame.getMapPane();
		mapLayerTable = new MapLayerTable(mapPane);
		featureCollection = new DefaultFeatureCollection();
		
		mapCoords.addActionListener(new ActionListener() {
			JPanel currentOverlayPanel = null;

			@Override
			public void actionPerformed(ActionEvent e) {
				
				double latitude = Double.parseDouble(xField.getText());
				double longitude = Double.parseDouble(yField.getText());				
	            pointLayer = plotPoint(mapContent, latitude, longitude);
	            updateMapLayerEvent();
	            mapContent.addLayer(pointLayer);

	            try {
					loadGeoTIFFsDynamically(tiffFolder, mapContent.getViewport().getBounds());
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
//	            mapPane.repaint();
//	            mapPane.revalidate();
				
//				JMapPane mapPane = frame.getMapPane();
//				double latitude = Double.parseDouble(yField.getText());
//				double longitude = Double.parseDouble(xField.getText());	
//				
//				MapContent mapContent = mapPane.getMapContent();
//                AffineTransform worldToScreen = mapContent.getViewport().getWorldToScreen();
//                Point2D worldPoint = new Point2D.Double(longitude, latitude);
//                Point2D screenPoint = worldToScreen.transform(worldPoint, null);
//                
//                if (currentOverlayPanel != null) {
//                    frame.getLayeredPane().remove(currentOverlayPanel);
//                }
//
//                // Add a custom overlay panel to draw the point
//                currentOverlayPanel = new JPanel() {
//                    @Override
//                    protected void paintComponent(Graphics g) {
//                        super.paintComponent(g);
//                        g.setColor(Color.RED);
//                        g.fillOval((int) screenPoint.getX() - 5, (int) screenPoint.getY() - 5, 10, 10);
//                    }
//                };
//
//                currentOverlayPanel.setOpaque(false); // Ensure transparency
//                currentOverlayPanel.setBounds(0, 0, frame.getWidth(), frame.getHeight()); // Match frame size
//                frame.getLayeredPane().add(currentOverlayPanel, JLayeredPane.PALETTE_LAYER); // Add to layered pane
//                frame.repaint();
				
			}
		});
		
//		frame.addComponentListener(new ComponentAdapter() {
//		
//		    @Override
//		    public void componentShown(ComponentEvent e) {
//		        mapPane.repaint();
//		        mapPane.revalidate();
//		    }
//		});
//
//		// Update mapPane on window opening
//		frame.addWindowListener(new WindowAdapter() {
//
//		    @Override
//		    public void windowOpened(WindowEvent e) {
//		        mapPane.repaint();
//		        mapPane.revalidate();
//		    }
//		});
		
		frame.addMouseWheelListener(new MouseWheelListener() {
			@Override
			public void mouseWheelMoved(MouseWheelEvent e) {
				applyZoom(frame.getMapPane(), e);
			}
		});

		// Add a listener to detect zoom changes
//	    frame.getMapPane().addMapPaneListener(new MapPaneAdapter() {
//	        @Override
//	        public void onDisplayAreaChanged(MapPaneEvent ev) {
//	            try {
//	                // Load GeoTIFFs dynamically based on current zoom level
		loadGeoTIFFsDynamically(tiffFolder, mapContent.getViewport().getBounds());
//	            } catch (Exception e) {
//	                e.printStackTrace();
//	            }
//	        }
//	    });
		frame.setVisible(true);
	}

	private void addShapefileLayer(File shapefile) throws Exception {
		FileDataStore store = FileDataStoreFinder.getDataStore(shapefile);
		SimpleFeatureSource featureSource = store.getFeatureSource();
		Style style = SLD.createSimpleStyle(featureSource.getSchema());
		shapeLayer = new FeatureLayer(featureSource, style);
		mapContent.addLayer(shapeLayer);
	}

	private void loadGeoTIFFsDynamically(File tiffFolder, ReferencedEnvelope currentBounds) throws Exception {
		File[] tiffFiles = tiffFolder.listFiles((dir, name) -> name.endsWith(".tif") || name.endsWith(".tiff"));
		if (tiffFiles == null) {
			return;
		}

		for (File tiffFile : tiffFiles) {
			AbstractGridFormat format = GridFormatFinder.findFormat(tiffFile);
			if (format == null)
				continue;

			Hints hints = new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
			GridCoverage2DReader reader = format.getReader(tiffFile, hints);

			// Get the bounds of the GeoTIFF
			// ReferencedEnvelope tiffBounds = new
			// ReferencedEnvelope(reader.getOriginalEnvelope());
			ReferencedEnvelope bounds = currentBounds.transform(reader.getCoordinateReferenceSystem(), true);

			// Check if the current map bounds intersect with the GeoTIFF bounds
//	        if (currentBounds.intersects((BoundingBox) tiffBounds)) {
			// Create and add the GeoTIFF layer if not already added
			Layer existingLayer = mapContent.layers().stream()
					.filter(layer -> layer instanceof GridReaderLayer && layer.getTitle().equals(tiffFile.getName()))
					.findFirst().orElse(null);
			System.out.println("existing layers : "+existingLayer);
			
			if (existingLayer == null) {
				Style rasterStyle = createColourStyle(reader);
				Layer tiffLayer = new GridReaderLayer(reader, rasterStyle);
				tiffLayer.setTitle(tiffFile.getName());
				mapContent.addLayer(tiffLayer);
			}
//	        }
		}
		System.out.println("-------------------------------------------------------");
	}

	private Style createColourStyle(GridCoverage2DReader reader) throws IOException {
		GridCoverage2D cov = reader.read(null);
		int numBands = cov.getNumSampleDimensions();
		if (numBands < 3) {
			// Single-band grayscale
			RasterSymbolizer sym = sf.getDefaultRasterSymbolizer();
			return SLD.wrapSymbolizers(sym);
		}

		// RGB bands
		final int RED = 1, GREEN = 2, BLUE = 3;
		ContrastEnhancement ce = sf.contrastEnhancement(ff.literal(1.0), ContrastMethod.NORMALIZE);
		ChannelSelection sel = sf.channelSelection(sf.createSelectedChannelType(String.valueOf(RED), ce),
				sf.createSelectedChannelType(String.valueOf(GREEN), ce),
				sf.createSelectedChannelType(String.valueOf(BLUE), ce));
		RasterSymbolizer sym = sf.getDefaultRasterSymbolizer();
		sym.setChannelSelection(sel);
		return SLD.wrapSymbolizers(sym);
	}

	public static void applyZoom(JMapPane mapPane, MouseWheelEvent e) {
		if (mapPane != null) {

			double scaleFactor = e.getWheelRotation() < 0 ? 0.8 : 1.2;
			ReferencedEnvelope currentEnvelope = mapPane.getDisplayArea();

			MapContent mapContent = mapPane.getMapContent();
			AffineTransform transScreen2World = mapContent.getViewport().getScreenToWorld();
			Point2D screenPoint = new Point2D.Double(e.getPoint().x - 7, e.getPoint().y - 97);
			Point2D worldPoint = transScreen2World.transform(screenPoint, null);
			double prevxdist = worldPoint.getX() - currentEnvelope.getMinX();
			double nextxdist = currentEnvelope.getMaxX() - worldPoint.getX();
			double newprevxdist = prevxdist * scaleFactor;
			double newnextxdist = nextxdist * scaleFactor;
			double prevydist = worldPoint.getY() - currentEnvelope.getMinY();
			double nextydist = currentEnvelope.getMaxY() - worldPoint.getY();
			double newprevydist = prevydist * scaleFactor;
			double newnextydist = nextydist * scaleFactor;
			ReferencedEnvelope new2 = new ReferencedEnvelope(
					// Parameters are the min, max bounds of the envelope along X and Y
					worldPoint.getX() - newprevxdist, worldPoint.getX() + newnextxdist,
					worldPoint.getY() - newprevydist, worldPoint.getY() + newnextydist,
					currentEnvelope.getCoordinateReferenceSystem());
			mapPane.setDisplayArea(new2);

		}

	}

	public Layer plotPoint(MapContent mapContent,double lat, double lng) {
		featureCollection.clear();
		GeometryFactory geometryFactory = new GeometryFactory();
		Coordinate coordinate = new Coordinate(lat, lng);
		Point point = geometryFactory.createPoint(coordinate);
		SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
		builder.setName("PointLayer");
		builder.setCRS(DefaultGeographicCRS.WGS84);
		builder.add("location", Point.class);
		SimpleFeatureType featureType = builder.buildFeatureType();
		SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);
		featureBuilder.add(point);
		SimpleFeature feature = featureBuilder.buildFeature(null);
//		DefaultFeatureCollection featureCollection = new DefaultFeatureCollection();
		featureCollection.add(feature);
		Style style = SLD.createPointStyle("Circle", Color.RED, Color.RED, 1.0f, 5.0f);
		pointLayer = new FeatureLayer(featureCollection, style);
		return pointLayer;
	}
	
//	public void updatePoint(double lat, double lng) {
//	    // Remove the dummy feature if it exists
//	    if (hasDummyFeature) {
//	        featureCollection.clear();
////	        hasDummyFeature = false;
//	    }
//
//	    // Create the new point
//	    GeometryFactory geometryFactory = new GeometryFactory();
//	    Coordinate coordinate = new Coordinate(lat, lng);
//	    Point point = geometryFactory.createPoint(coordinate);
//
//	    // Define the feature type (reuse the existing type if already defined)
//	    SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
//	    builder.setName("PointLayer");
//	    builder.setCRS(DefaultGeographicCRS.WGS84); // CRS for WGS84
//	    builder.add("location", Point.class);
//	    SimpleFeatureType featureType = builder.buildFeatureType();
//
//	    // Create the new feature
//	    SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);
//	    featureBuilder.add(point);
//	    SimpleFeature feature = featureBuilder.buildFeature(null);
//
//	    // Add the feature to the collection
//	    featureCollection.add(feature);
//	    
//	    mapContent.addLayer(pointLayer);
//	    updateMapLayerEvent();
//	    // No need to re-add the layer; the map will automatically refresh
//	}
//
//	public void initializeMap(MapContent mapContent) {
//	    // Initialize feature collection
//	    
//
//	    // Add a dummy feature (if necessary)
//	    GeometryFactory geometryFactory = new GeometryFactory();
//	    Coordinate dummyCoordinate = new Coordinate(0, 0); // Use a valid coordinate
//	    Point dummyPoint = geometryFactory.createPoint(dummyCoordinate);
//
//	    // Define the feature type
//	    SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
//	    builder.setName("PointLayer");
//	    builder.setCRS(DefaultGeographicCRS.WGS84);
//	    builder.add("location", Point.class);
//	    SimpleFeatureType featureType = builder.buildFeatureType();
//	    featureCollection = new DefaultFeatureCollection("internal", featureType);
//
//	    // Build a dummy feature
//	    SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);
//	    featureBuilder.add(dummyPoint);
//	    SimpleFeature dummyFeature = featureBuilder.buildFeature(null);
//
//	    // Add the dummy feature to the collection
//	    featureCollection.add(dummyFeature);
//	    hasDummyFeature = true; // Set the flag
//
//	    // Create style for the point
//	    Style style = SLD.createPointStyle("Circle", Color.RED, Color.RED, 1.0f, 5.0f);
//
//	    // Create the feature layer with the dummy feature collection
//	    pointLayer = new FeatureLayer(featureCollection, style);
//	    
//	    // Add the layer to the map content
//	    mapContent.addLayer(pointLayer);
//
//	}

	private void updateMapLayerEvent() {
	    MapLayerEvent mple = new MapLayerEvent(pointLayer, MapLayerEvent.DATA_CHANGED);
	    MapLayerListEvent mplle = new MapLayerListEvent(mapPane.getMapContent(), pointLayer,
	            mapPane.getMapContent().layers().indexOf(pointLayer), mple);
	    mapPane.layerChanged(mplle);
	}
}
