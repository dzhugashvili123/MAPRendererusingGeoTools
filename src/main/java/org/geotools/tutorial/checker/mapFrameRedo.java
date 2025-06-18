package org.geotools.tutorial.checker;

import org.geotools.swing.JMapFrame;
import org.geotools.map.MapContent;

public class mapFrameRedo extends JMapFrame {
    mapFrameRedo(MapContent map) {
        super(map);
    }

    @Override
    public void repaint() {
//        System.out.println("*****\tRepainting\t*****");
        super.repaint();
//        System.out.println("Thread sleep after");
    }
}

