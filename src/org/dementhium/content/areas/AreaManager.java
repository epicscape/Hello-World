package org.dementhium.content.areas;

import org.dementhium.io.XMLHandler;

import java.util.List;

/**
 * Holds info for all the areas.
 *
 * @author Stephen
 * @author `Discardedx2 <the_shawn@discardedx2.info>
 */

public class AreaManager {

    private static List<Area> loadedAreas;

    public AreaManager() {
        System.out.println("Loading areas...");
        try {
            loadedAreas = XMLHandler.fromXML("data/xml/areas.xml");
        } catch (Throwable e) {
            e.printStackTrace();
        }
        System.out.println("Loaded " + loadedAreas.size() + " areas.");
    }

    public Area getAreaByName(String name) {
        for (Area area : loadedAreas) {
            if (area.getName().toLowerCase().equals(name.toLowerCase())) {
                return area;
            }
        }
        return null;
        //throw new IllegalArgumentException("Area [ "+name+" ] does not exist");
    }

    public List<Area> getAreas() {
        return loadedAreas;
    }
}
