/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dementhium.tools.coordinategrabber;

import org.dementhium.tools.coordinategrabber.visual.CoordinateGrabber;

/**
 * @author Stephen
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                new CoordinateGrabber().setVisible(true);
            }
        });
    }
}
