/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.dementhium.tools.coordinategrabber.util;

/**
 * @author Stephen
 */
public class Formats {

    public static final String DELTA_STATIONARY_FORMAT = "spawn = \"+npcid+\"\t\"+x+\"\t\"+y+\"\t0\t0\t0\t0\t1";
    public static final String DELTA_WALKING_FORMAT = "spawn = \"+npcid+\"\t\"+x+\"\t\"+y+\"\t\"+seX+\"\t\"+seY+\"\t\"+nwX+\"\t\"+nwY+\"\t1";

    public static final String RS2HD_562_STATIONARY_FORMAT = "<npc>"
            + "\t<id>\"+npcid+\"</id>"
            + "\t<location>"
            + "\t\t<x>\"+x+\"</x>"
            + "\t\t<y>\"+y+\"</y>"
            + "\t\t<z>\"+z+\"</z>"
            + "\t</location>"
            + "</npc> ";
    public static final String RS2HD_562_WALKING_FORMAT = "<npc>"
            + "\t<id>\"+npcid+\"</id>"
            + "\t<location>"
            + "\t\t<x>\"+x+\"</x>"
            + "\t\t<y>\"+y+\"</y>"
            + "\t\t<z>\"+z+\"</z>"
            + "\t</location>"
            + "\t<walkType>RANGE</walkType>"
            + "\t<minimumCoords>"
            + "\t\t<x>\"+seX+\"</x>"
            + "\t\t<y>\"+seY+\"</y>"
            + "\t\t<z>\"+z+\"</x>"
            + "\t</minimumCoords>"
            + "\t<maximumCoords>"
            + "\t\t<x>\"+nwX+\"</x>"
            + "\t\t<y>\"+nwY+\"</y>"
            + "\t\t<z>\"+z+\"</x>"
            + "\t</maximumCoords>"
            + "</npc> ";
}
