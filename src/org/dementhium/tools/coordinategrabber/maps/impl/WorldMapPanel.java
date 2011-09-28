package org.dementhium.tools.coordinategrabber.maps.impl;

import org.dementhium.tools.coordinategrabber.maps.Map;
import org.dementhium.tools.coordinategrabber.visual.CoordinateGrabber;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseWheelEvent;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Stephen
 */
public class WorldMapPanel extends Map {

    private static final long serialVersionUID = -1597640291466572586L;

    private final File imageFile = new File("./data/tools/worldmap.jpg");

    public WorldMapPanel(final CoordinateGrabber frame) {
        super(frame);
        try {
            background = ImageIO.read(imageFile);
            scale = 1.0;
        } catch (IOException ex) {
            Logger.getLogger(WorldMapPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public int xAxisPixelToGameTile(int x) { //2213. 3797
        return (int) ((2049 + (x / (2 * scale))));
    }
    //1983
    //638

    @Override
    public int yAxisPixelToGameTile(int y) {
        return (int) ((830 + background.getHeight()) - (y / (2 * scale)));
    }

    @Override
    public void zoomIn(MouseWheelEvent e) {
        if (e.getWheelRotation() > 0 && scale != 1.0) {
            scale -= .05;
            setPreferredSize(new Dimension((int) (background.getWidth() * scale), (int) (background.getHeight() * scale))); //Couldn't find another way to update the ScrollBars, besides getPreferredSize() returns a new dimension anyway
            revalidate();
            repaint();
        } else if (e.getWheelRotation() < 0 && scale != 5) {
            scale += .05;
            setPreferredSize(new Dimension((int) (background.getWidth() * scale), (int) (background.getHeight() * scale))); //Couldn't find another way to update the ScrollBars, besides getPreferredSize() returns a new dimension anyway.
            revalidate();
            repaint();
        }
    }
}
