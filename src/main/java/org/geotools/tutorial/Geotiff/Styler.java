package org.geotools.tutorial.Geotiff;

import org.geotools.api.style.*;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.*;
import org.geotools.styling.StyleBuilder;

public class Styler {
    public static Style createMultiColorStyle() {
        // Create a StyleFactory and StyleBuilder
        StyleFactory styleFactory = CommonFactoryFinder.getStyleFactory(null);
        StyleBuilder styleBuilder = new StyleBuilder(styleFactory);

        // Create contrast enhancement
        ContrastEnhancement contrastEnhancement = styleFactory.contrastEnhancement(
                styleBuilder.literalExpression(1.0),
                "normalize");

        // Define the RGB channels, assuming single-band values will be mapped to color
        SelectedChannelType redChannel = styleFactory.createSelectedChannelType("1", contrastEnhancement);
        SelectedChannelType greenChannel = styleFactory.createSelectedChannelType("1", contrastEnhancement);
        SelectedChannelType blueChannel = styleFactory.createSelectedChannelType("1", contrastEnhancement);

        // Assign RGB channels to the raster
        ChannelSelection channelSelection = styleFactory.channelSelection(redChannel, greenChannel, blueChannel);

        // Create the RasterSymbolizer and set the channel selection
        RasterSymbolizer rasterSymbolizer = styleFactory.getDefaultRasterSymbolizer();
        rasterSymbolizer.setChannelSelection(channelSelection);

        // Wrap the RasterSymbolizer into a Style and return it
        return SLD.wrapSymbolizers(rasterSymbolizer);
    }

}