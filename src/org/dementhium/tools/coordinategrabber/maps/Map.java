/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.dementhium.tools.coordinategrabber.maps;

import org.dementhium.tools.coordinategrabber.maps.impl.WorldMapPanel;
import org.dementhium.tools.coordinategrabber.visual.CoordinateGrabber;
import org.dementhium.tools.coordinategrabber.visual.CoordinateGrabber.GeneratedArea;
import org.dementhium.tools.coordinategrabber.visual.CoordinateGrabber.GeneratedCoord;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Stephen
 */
public abstract class Map extends JPanel {

    /**
     * Eclipse.
     */
    public static final long serialVersionUID = 1L;
    public BufferedImage background;
    public int seX = 0;
    public int seY = 0;
    public int nwX = 0;
    public int nwY = 0;
    public final Color boxColor = new Color(26, 14, 67, 136);
    public double scale;
    public int currentX;
    public int currentY;
    public final CoordinateGrabber frame;

    public Map(final CoordinateGrabber frame) {

        this.frame = frame;
        addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(MouseEvent evt) {
                try {
                    ((HTMLEditorKit) frame.outputPane.getEditorKit()).insertHTML((HTMLDocument) frame.outputPane.getDocument(), ((HTMLDocument) frame.outputPane.getDocument()).getLength(), "Possible X: " + xAxisPixelToGameTile(evt.getX()) + ", Possible Y: " + yAxisPixelToGameTile(evt.getY()) + ": Create spawn: <a href = \"coord:" + frame.generatedCoords.size() + "\">Yes</a> \n", 0, 0, null);
                    frame.generatedCoords.add(new GeneratedCoord(xAxisPixelToGameTile(evt.getX()), (int) ((638 + background.getHeight()) - (evt.getY() / (2 * scale)))));
                    frame.outputScroll.getVerticalScrollBar().setValue(frame.outputScroll.getVerticalScrollBar().getMaximum());
                    System.out.println(scale);
                } catch (BadLocationException ex) {
                    Logger.getLogger(WorldMapPanel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(WorldMapPanel.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

            @Override
            public void mouseEntered(MouseEvent arg0) {
            }

            @Override
            public void mouseExited(MouseEvent arg0) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                seX = e.getX();
                seY = e.getY();
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                try {
                    if (seX != 0 && seY != 0 && nwX != 0 && nwY != 0) {
                        ((HTMLEditorKit) frame.outputPane.getEditorKit()).insertHTML((HTMLDocument) frame.outputPane.getDocument(), ((HTMLDocument) frame.outputPane.getDocument()).getLength(), "Southeast X: " + xAxisPixelToGameTile(seX) + ", Southeast Y: " + yAxisPixelToGameTile(seY) + ", Northwest X: " + xAxisPixelToGameTile(nwX) + ", Northwest Y: " + yAxisPixelToGameTile(nwY) + " : Create spawn: <a href = \"area:" + frame.generatedAreas.size() + "\">Yes</a> \n", 0, 0, null);
                        frame.generatedAreas.add(new GeneratedArea(xAxisPixelToGameTile(seX), yAxisPixelToGameTile(seY), xAxisPixelToGameTile(nwX), yAxisPixelToGameTile(nwY)));
                        frame.outputScroll.getVerticalScrollBar().setValue(frame.outputScroll.getVerticalScrollBar().getMaximum());
                        nwX = 0;
                        nwY = 0;
                        seX = 0;
                        seY = 0;
                        repaint();
                    }
                } catch (BadLocationException ex) {
                    Logger.getLogger(WorldMapPanel.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(WorldMapPanel.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
        addMouseMotionListener(new MouseMotionListener() {

            @Override
            public void mouseDragged(MouseEvent e) {
                if (seX != 0 && seY != 0) {
                    int newX = e.getX();
                    int newY = e.getY();
                    if (newX > seX && newY < seY) {
                        nwX = e.getX();
                        nwY = e.getY();
                        repaint();
                    }
                }
            }

            @Override
            public void mouseMoved(MouseEvent e) {
                currentX = e.getX();
                currentY = e.getY();
                repaint();
            }
        });

        addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
               // if (e.isControlDown()) {
                    zoomIn(e);
               // }
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        g2.drawImage(background, AffineTransform.getScaleInstance(scale, scale), null);
        if (seX != 0 && seY != 0 && nwY != 0 && nwX != 0) {
            g2.setColor(boxColor);
            g2.fillRect(seX, nwY, nwX - seX, seY - nwY);
            g2.setColor(Color.WHITE);
            g2.drawRect(seX, nwY, nwX - seX, seY - nwY);
            g2.drawString("X: " + xAxisPixelToGameTile(nwX) + " Y: " + yAxisPixelToGameTile(nwY), nwX, nwY);
            g2.drawString("X: " + xAxisPixelToGameTile(seX) + " Y: " + yAxisPixelToGameTile(seY), seX + 1, seY + 10);
        } else {
            g2.setColor(Color.ORANGE);
            g2.drawString("X: " + xAxisPixelToGameTile(currentX) + " Y: " + yAxisPixelToGameTile(currentY), (int) (currentX + 10), (int) (currentY + 29));
        }
        g2.scale(scale, scale);
    }

    public abstract int xAxisPixelToGameTile(int n);

    public abstract int yAxisPixelToGameTile(int n);

    public abstract void zoomIn(MouseWheelEvent e);
}
