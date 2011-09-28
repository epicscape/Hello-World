package org.dementhium.content.areas;

import org.dementhium.model.Location;
import org.dementhium.model.Mob;
import org.dementhium.model.map.Region;
import org.dementhium.model.player.Player;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents an area in the world.
 *
 * @author Stephen
 */
public abstract class Area {

    public final String name;
    public final int swX, swY, nwX, nwY, radius, centerX, centerY, height;
    public transient int length, width;
    private final boolean isPvpZone, isMulti, canTele;

    public Area(String name, int[] coords, int radius, int centerX, int centerY, int height, boolean isPvpZone, boolean isMulti, boolean canTele) {//perhaps something like
        this.name = name;
        this.swX = coords[0];
        this.swY = coords[1];
        this.nwX = coords[2];
        this.nwY = coords[3];
        this.radius = radius;
        this.centerX = centerX;
        this.centerY = centerY;
        this.isPvpZone = isPvpZone;
        this.isMulti = isMulti;
        this.canTele = canTele;
        this.length = nwX - swX;
        this.width = nwY - swY;
        this.height = height;
    }

    public Area() {
        this.name = "";
        this.nwX = -1;
        this.nwY = -1;
        this.swX = -1;
        this.swY = -1;
        this.radius = -1;
        this.centerX = -1;
        this.centerY = -1;
        this.isPvpZone = false;
        this.isMulti = false;
        this.canTele = true;
        this.length = nwX - swX;
        this.width = nwY - swY;
        this.height = 0;
    }

    public abstract boolean contains(Location pos);

    /**
     * Teleports the mob to a random, non clipped location in this area.
     * @param mob The mob to teleport.
     * @return {@code True} if succesful, {@code false} if not.
     */
    public boolean randomTeleport(Mob mob) {
    	int x, y, clippingMask;
        List<Location> locations = new ArrayList<Location>();
        for (x = swX; x < nwX; x++) {
            for (y = swY; y < nwY; y++) {
                Location l = Location.locate(x, y, 0);
                clippingMask = Region.getClippingMask(l.getX(), l.getY(), l.getZ());
                if ((clippingMask & 0x1280180) == 0 && (clippingMask & 0x1280108) == 0
                        && (clippingMask & 0x1280120) == 0 && (clippingMask & 0x1280102) == 0) {
                    locations.add(l);
                }
            }
        }
        if (locations.size() < 1) {
        	return false;
        }
        Random r = new Random();
        Location current = locations.get(r.nextInt(locations.size()));
        x = current.getX();
        y = current.getY();
        mob.teleport(x, y, 0);
        return true;
    }
    
    public boolean isPvpZone() {
        return isPvpZone;
    }

    public boolean isMulti() {
        return isMulti;
    }

    public boolean canTele() {
        return canTele;
    }

    public String getName() {
        return name;
    }

    public void teleTo(Player player) {
        player.teleport(swX + ((nwX - swX) / 2), swY + ((nwY - swY) / 2), height);
    }

    public Point getCenterPoint() {
        return new Point(swX + ((nwX - swX) / 2), swY + ((nwY - swY) / 2));
    }
}
