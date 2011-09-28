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
public class GodwarsMapPanel extends Map {

    private static final long serialVersionUID = -2571663357950773774L;

    private final File imageFile = new File("./data/tools/GodwarsMap.png");

    public GodwarsMapPanel(final CoordinateGrabber frame) {
        super(frame);
        try {
            background = ImageIO.read(imageFile);
            scale = 2.05;
        } catch (IOException ex) {
            Logger.getLogger(WorldMapPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public int xAxisPixelToGameTile(int x) {
        return (int) ((2815 + (x / (4 * scale))));
    }

    @Override
    public int yAxisPixelToGameTile(int y) {
        return (int) ((4590 + background.getHeight()) - (y / (4 * scale)));
    }

    @Override
    public void zoomIn(MouseWheelEvent e) {
        if (e.getWheelRotation() > 0 && scale != 2.05) {
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
