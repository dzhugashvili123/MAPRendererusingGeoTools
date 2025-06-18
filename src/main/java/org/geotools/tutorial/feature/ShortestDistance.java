package org.geotools.tutorial.feature;

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
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;

import javax.measure.MetricPrefix;
import javax.measure.Quantity;
import javax.measure.quantity.Length;
import javax.media.jai.PlanarImage;
import javax.swing.JButton;
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import org.geotools.api.data.*;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.geometry.BoundingBox;
import org.geotools.api.geometry.MismatchedDimensionException;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.TransformException;
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
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.memory.MemoryDataStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.FeatureLayer;
import org.geotools.map.GridReaderLayer;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.map.MapLayerEvent;
import org.geotools.map.MapLayerListEvent;
import org.geotools.map.StyleLayer;
import org.geotools.referencing.CRS;
import org.geotools.referencing.CRS.AxisOrder;
import org.geotools.referencing.GeodeticCalculator;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.SLD;
import org.geotools.swing.JMapFrame;
import org.geotools.swing.JMapPane;
import org.geotools.swing.MapLayerTable;
import org.geotools.swing.MapPane;
import org.geotools.swing.action.SafeAction;
import org.geotools.swing.data.JParameterListWizard;
import org.geotools.swing.event.MapPaneAdapter;
import org.geotools.swing.event.MapPaneEvent;
import org.geotools.swing.wizard.JWizard;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.*;

import si.uom.SI;
import systems.uom.common.USCustomary;
import tech.units.indriya.quantity.Quantities;

/**
 * Hello world!
 *
 */
public class ShortestDistance {

	private StyleFactory sf = CommonFactoryFinder.getStyleFactory();
	private FilterFactory ff = CommonFactoryFinder.getFilterFactory();
	private static FeatureLayer pointLayer1;
	private static MemoryDataStore memoryDataStore;
	private static SimpleFeatureType pointFeatureType;
	static MapContent mapContent;
	static JMapFrame frame;
	static JMapPane mapPane;
	static MapLayerTable mapLayerTable;
	private DefaultFeatureCollection featureCollection;
	private Layer pointLayer;
	private Layer shapeLayer;
	private boolean hasDummyFeature = false; // Track if a dummy feature exists
	DecimalFormat decFormat = new DecimalFormat("##.##");
	JTextField distanceValue;

	private GridCoverage2DReader reader;

	public static void main(String[] args) throws Exception {
		ShortestDistance app = new ShortestDistance();
		app.getLayersAndDisplay();
		CRS.getAuthorityFactory(true);
		System.setProperty("org.geotools.referencing.forceXY", "true");
	
	}

	private void getLayersAndDisplay() throws Exception {
		File shapefile = new File("C:/Users/venka/OneDrive/Desktop/GeoToolsProject/Maps/IndiaShape/States/Admin2.shp");
		File tiffFolder = new File("C:/Users/venka/OneDrive/Desktop/GeoToolsProject/defcsedfvsf");
		mapContent = new MapContent();
		mapContent.setTitle("Dynamic GeoTIFF Loader");
		addShapefileLayer(shapefile);
		frame = new JMapFrame(mapContent);
		frame.setSize(800, 600);
		frame.enableStatusBar(true);
		frame.enableTool(JMapFrame.Tool.PAN, JMapFrame.Tool.RESET, JMapFrame.Tool.POINTER);

		JTextField x1Field = new JTextField();
		JTextField y1Field = new JTextField();
		JTextField x2Field = new JTextField();
		JTextField y2Field = new JTextField();
		distanceValue = new JTextField();
		JButton getDistanceButton = new JButton("Get");
		JToolBar tb = frame.getToolBar();
		tb.addSeparator();
		tb.add(x1Field);
		tb.addSeparator();
		tb.add(y1Field);
		tb.addSeparator();

		tb.add(getDistanceButton);
		tb.addSeparator();
		tb.add(distanceValue);
		mapPane = frame.getMapPane();
		mapLayerTable = new MapLayerTable(mapPane);
		featureCollection = new DefaultFeatureCollection();
		StreamingRenderer renderer = new StreamingRenderer();
		renderer.setRendererHints(Collections.singletonMap("rendering.buffer", false));
		renderer.setRendererHints(Collections.singletonMap("optimizedDataLoadingEnabled", Boolean.TRUE));
//		mapPane.setRenderer(renderer);
		
		
		getDistanceButton.addActionListener(new ActionListener() {
			JPanel currentOverlayPanel = null;

			@Override
			public void actionPerformed(ActionEvent e) {

				double latitude1 = Double.parseDouble(x1Field.getText());
				double longitude1 = Double.parseDouble(y1Field.getText());


				GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
				Point point = geometryFactory.createPoint(new Coordinate(latitude1, longitude1));


				try {
					SimpleFeatureSource pointFeatureSource = memoryDataStore.getFeatureSource("PointLayer");

					if (pointFeatureSource instanceof FeatureStore) {
						FeatureStore<SimpleFeatureType, SimpleFeature> pointFeatureStore =
								(FeatureStore<SimpleFeatureType, SimpleFeature>) pointFeatureSource;

						Transaction transaction = new DefaultTransaction("addPoint");
						pointFeatureStore.setTransaction(transaction);

						try {
							// Clear previous points and lines
							pointFeatureStore.removeFeatures(Filter.INCLUDE);

							// Add points to feature store
							SimpleFeatureBuilder pointBuilder = new SimpleFeatureBuilder(pointFeatureType);
							pointBuilder.add(point);
							SimpleFeature newPointFeatures = pointBuilder.buildFeature(null);

							pointFeatureStore.addFeatures(DataUtilities.collection(newPointFeatures));

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
		initializeMapLayers();
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

			if (existingLayer == null) {
				Style rasterStyle = createColourStyle(reader);
				Layer tiffLayer = new GridReaderLayer(reader, rasterStyle);
				tiffLayer.setTitle(tiffFile.getName());
				mapContent.addLayer(tiffLayer);
			}
//	        }
		}

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

//	public Layer plotPoint(MapContent mapContent, double lat1, double lng1, double lat2, double lng2) {
//		featureCollection.clear();
//		GeometryFactory geometryFactory = new GeometryFactory();
//
//		// Create the coordinates for the two points
//		Coordinate coordinate1 = new Coordinate(lat1, lng1); // Longitude is x, latitude is y
//		Coordinate coordinate2 = new Coordinate(lat2, lng2);
//
//		// Create the points
//		Point point1 = geometryFactory.createPoint(coordinate1);
//		Point point2 = geometryFactory.createPoint(coordinate2);
//
//		// Create the line connecting the two points
//		LineString line = geometryFactory.createLineString(new Coordinate[] { coordinate1, coordinate2 });
//		int numberOfPoints = line.getNumPoints();
//
//		// Print the number of points
//		System.out.println("Number of points in the LineString: " + numberOfPoints);
//
//		// Define the feature type for points and lines
//		SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
//		builder.setName("GeometryLayer");
//		builder.setCRS(DefaultGeographicCRS.WGS84);
//		builder.add("LineString", LineString.class); // Generic geometry type
//		SimpleFeatureType featureType = builder.buildFeatureType();
//
//		SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);
//
//		// Add the first point to the feature collection
//		featureBuilder.add(point1);
//		SimpleFeature feature1 = featureBuilder.buildFeature("point1");
//		featureCollection.add(feature1);
//
//		// Add the second point to the feature collection
//		featureBuilder.reset();
//		featureBuilder.add(point2);
//		SimpleFeature feature2 = featureBuilder.buildFeature("point2");
//		featureCollection.add(feature2);
//
//		// Add the line to the feature collection
//		featureBuilder.reset();
//		featureBuilder.add(line);
//		SimpleFeature featureLine = featureBuilder.buildFeature("line");
//		featureCollection.add(featureLine);
//
//		GeodeticCalculator geodeticCalculator = new GeodeticCalculator(DefaultGeographicCRS.WGS84);
//		geodeticCalculator.setStartingGeographicPoint(lng1, lat1);
//		geodeticCalculator.setDestinationGeographicPoint(lng2, lat2);
//		double distance = (geodeticCalculator.getOrthodromicDistance()) / 1000; // Distance in meters
//		distanceValue.setText(String.valueOf(decFormat.format(distance)));
//		System.out.println("Distance between points: " + distance + " Km");
//
//		// Create styles for the points and the line
//		Style pointStyle = SLD.createPointStyle("Circle", Color.RED, Color.RED, 1.0f, 5.0f);
//		Style lineStyle = SLD.createLineStyle(Color.BLUE, 1.0f);
//
//		// Combine the styles into a single style
//		StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory();
//		Style combinedStyle = styleFactory.createStyle();
//		combinedStyle.featureTypeStyles()
//				.addAll(List.of(pointStyle.featureTypeStyles().get(0), lineStyle.featureTypeStyles().get(0)));
//
//		// Create and return the layer with the combined geometry and style
//		pointLayer = new FeatureLayer(featureCollection, combinedStyle);
//		return pointLayer;
//	}

	private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
		final int EARTH_RADIUS = 6378; // Radius of Earth in kilometers
		double latDistance = Math.toRadians(lat2 - lat1);
		double lonDistance = Math.toRadians(lon2 - lon1);
		double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		return EARTH_RADIUS * c; // Distance in kilometers
	}

	private double calculateDistance2(double lat1, double lon1, double lat2, double lon2) {
		Coordinate source = new Coordinate(lon1, lat1); /// Mumbai Lat Long
		Coordinate destination1 = new Coordinate(lon2, lat2); // Durban Lat Long

		GeometryFactory geometryFactory = new GeometryFactory();
		Geometry point1 = geometryFactory.createPoint(source);
		Geometry point2 = geometryFactory.createPoint(destination1);

		Geometry g3 = null;
		Geometry g4 = null;
		try {
			CoordinateReferenceSystem auto = auto = CRS.decode("AUTO:42001,13.45,52.3");
			MathTransform transform = CRS.findMathTransform(DefaultGeographicCRS.WGS84, auto);
			g3 = JTS.transform(point1, transform);
			g4 = JTS.transform(point2, transform);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		double distance = g3.distance(g4);
		return distance; // Distance in kilometers
	}

	private BigDecimal calculateDistance3(double lat1, double lon1, double lat2, double lon2) {
		double distance = 0.0;

		GeodeticCalculator calc = new GeodeticCalculator(DefaultGeographicCRS.WGS84);
		calc.setStartingGeographicPoint(lat1, lon1);
		calc.setDestinationGeographicPoint(lat2, lon2);

		distance = calc.getOrthodromicDistance();
		double bearing = calc.getAzimuth();

		Quantity<Length> dist = Quantities.getQuantity(distance, SI.METRE);
		BigDecimal distance1 = (BigDecimal) dist.to(MetricPrefix.KILO(SI.METRE)).getValue();
		System.out.println(distance1 + " Km");
		System.out.println(dist.to(USCustomary.MILE).getValue() + " miles");
		System.out.println("Bearing " + bearing + " degrees");
		return distance1;
	}

	private static void initializeMapLayers() {
		memoryDataStore = new MemoryDataStore();


		//Point Layer Schema
		SimpleFeatureTypeBuilder pointBuilder = new SimpleFeatureTypeBuilder();
		pointBuilder.setName("PointLayer");
		pointBuilder.add("location", Point.class);
		pointBuilder.setCRS(DefaultGeographicCRS.WGS84);
		pointFeatureType = pointBuilder.buildFeatureType();


		try {
			memoryDataStore.createSchema(pointFeatureType);
		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}

		// Define styles
		Style pointStyle = SLD.createPointStyle("circle", Color.RED, Color.RED, 1.0f, 5.0f);


		try {
			pointLayer1 = new FeatureLayer(memoryDataStore.getFeatureSource("PointLayer"), pointStyle);


		} catch (IOException ex) {
			throw new RuntimeException(ex);
		}

		frame.getMapContent().addLayer(pointLayer1);
	}
}
