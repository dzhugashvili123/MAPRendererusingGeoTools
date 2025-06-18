package org.geotools.tutorial.checker;

import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.map.MapLayerListListener;

public class mapRedo extends MapContent {
    private boolean supressEvents = false;
    mapRedo() {
        super();
    }

    public void setSupressEvents(boolean supress) {
        this.supressEvents = supress;
    }

//    @Override
//    public boolean addLayer(Layer layer) {
//        if(supressEvents == false) super.addLayer(layer);
//        else
//    }
}
