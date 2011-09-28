package org.dementhium.model;

import org.dementhium.model.map.Directions;
import org.dementhium.model.map.GameObject;
import org.dementhium.model.map.Region;
import org.dementhium.model.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 'Mystic Flow
 */
public class Location {

    public final static int[] VIEWPORT_SIZES = {104, 120, 136, 168};

    public static final int SECTORS_PER_REGION = 8;
    public static final int SECTOR_LENGTH = 4;


    public static double distanceFormula(int x, int y, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x, 2) + Math.pow(y2 - y, 2));
    }

    public static double distanceFormula(double x, double y, double x2, double y2) {
        /*double cx = x - x2;
          double cy = y - y2;
          if (cx < 0)
              cx *= -1;
          if (cy < 0)
              cy *= -1;
          return cx > cy ? cx : cy;*/
        return Math.sqrt(Math.pow(x2 - x, 2) + Math.pow(y2 - y, 2));
    }

    public static Location locate(int x, int y, int z) {
        return Region.getLocation(x, y, z);
    }

    private List<GameObject> objects;
    private Region region;
    private final int x, y, z;

    private List<Player> players;

    public Location(int x, int y, int z, Region region) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.region = region;
    }

    //	public void setGameObject(GameObject object) {
    //		this.object = object;
    //	}
    //
    //	public GameObject getGameObject() {
    //		return object;
    //	}
    //
    public GameObject getGameObject(int id) {
        if (objects == null)
            return null;
        for (GameObject object : objects)
            if (object.getId() == id)
                return object;
        return null;
    }

    public GameObject getGameObjectType(int type) {
        if (objects == null)
            return null;
        for (GameObject object : objects) {
            if (object.getType() == type)
                return object;
        }
        return null;
    }

    public boolean hasObjects() {
        return objects != null && objects.size() > 0;
    }
    
    public boolean hasObjectNoDecoration() {
        if (objects == null)
            return false;
        for (GameObject object : objects)
            if (object.getType() != 22)
                return true;
        return false;
    }

    public boolean differentMap(Location other) {
        return distanceFormula(getRegionX(), getRegionY(), other.getRegionX(), other.getRegionY()) >= 4;
    }

    public double distance(Location other) {
        if (z != other.z) {
            return Integer.MAX_VALUE - 1;
        }
        return distanceFormula(x, y, other.x, other.y);
    }

    public int getWildernessLevel() {
        if (y > 3520 && y < 4000) {
            return (((int) (Math.ceil((y) - 3520D) / 8D) + 1));
        }
        return 0;
    }

    public boolean withinDistance(Location other, int dist) {
        if (other.z != z) {
            return false;
        }
        return distance(other) <= dist;
    }

    public boolean withinDistance(Location other) {
        if (other.z != z) {
            return false;
        }
        int deltaX = other.x - x, deltaY = other.y - y;
        return deltaX <= 16 && deltaX >= -15 && deltaY <= 16 && deltaY >= -15;
    }


    @Override
    public boolean equals(Object object) {
        if (object instanceof Location) {
            Location other = (Location) object;
            return x == other.x && y == other.y && other.z == z;
        }
        return false;
    }

    public int getViewportX(int depth) {
        return getViewportX(this, depth);
    }

    public int getViewportY(int depth) {
        return getViewportY(this, depth);
    }

    public int getViewportX(Location base, int depth) {
        depth = VIEWPORT_SIZES[depth];
        return x - (SECTORS_PER_REGION * (base.getRegionX() - (depth >> SECTOR_LENGTH)));
    }

    public int getViewportY(Location base, int depth) {
        depth = VIEWPORT_SIZES[depth];
        return y - (SECTORS_PER_REGION * (base.getRegionY() - (depth >> SECTOR_LENGTH)));
    }

    public int getRegionX() {
        return x >> 3;
    }

    public int getRegionY() {
        return y >> 3;
    }

    public Location getLocation(Directions.WalkingDirection direction) {
        switch (direction) {
            case SOUTH:
                return Location.locate(x, y - 1, z);
            case WEST:
                return Location.locate(x - 1, y, z);
            case NORTH:
                return Location.locate(x, y + 1, z);
            case EAST:
                return Location.locate(x + 1, y, z);
            case SOUTH_WEST:
                return Location.locate(x - 1, y - 1, z);
            case NORTH_WEST:
                return Location.locate(x - 1, y + 1, z);
            case SOUTH_EAST:
                return Location.locate(x + 1, y - 1, z);
            case NORTH_EAST:
                return Location.locate(x + 1, y + 1, z);
            default:
                return null;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    @Override
    public int hashCode() {
        return z << 30 | x << 15 | y;
    }

    @Override
    public String toString() {
        return "[" + x + "," + y + "," + z + " lx " + getViewportX(this, 0) + " ly " + getViewportY(this, 0) + " ] Clip: " + Region.getClippingMask(x, y, z);
        //return "[" + x + "," + y + "," + z + " lx " + getLocalX() + " ly " + getLocalY() + " ] players= " + getPlayerCount() + " npcs= " + getNPCCount() + " items= " + getItemCount() + " object=" + (object != null ? (object.getId() + "-" + object.getRotation() + "-" + object.getType()): -1);
    }

    public Location transform(int diffX, int diffY, int diffZ) {
        return Location.locate(x + diffX, y + diffY, z + diffZ);
    }

    public boolean withinRange(Location t) {
        return withinRange(t, 15);
    }

    public boolean withinRange(Location t, int distance) {
        return t.z == z && distance(t) <= distance;
    }

    public int getDistance(Location pos) {
        return (int) distance(pos);
    }

    public int get18BitsHash() {
        int regionId = ((getRegionX() / 8) << 8) + (getRegionY() / 8);
        return (((regionId & 0xff) << 6) >> 6) | (getZ() << 16) | ((((regionId >> 8) << 6) >> 6) << 8);
    }

    public int get30BitsHash() {
        return y | z << 28 | x << 14;
    }

    public Region getRegion() {
        return region;
    }

    public void add(Entity mob) {
        if (mob.isPlayer()) {
            getPlayers().add(mob.getPlayer());
        }
    }

    public void remove(Entity mob) {
        if (mob.isPlayer()) {
            getPlayers().remove(mob.getPlayer());
        }
    }

    public List<Player> getPlayers() {
        if (players == null) {
            players = new ArrayList<Player>();
        }
        return players;
    }

    public boolean containsPlayers() {
        return players != null && players.size() > 0;
    }

    public int distance(int x2, int y2) {
        return (int) distanceFormula(x, y, x2, y2);
    }

    public void addObject(GameObject object) {
        if (objects == null) {
            objects = new ArrayList<GameObject>();
        }
        objects.add(object);
    }

    public void removeObject(GameObject oldObj) {
        if (objects != null)
            objects.remove(oldObj);
    }
    
    public int getRegionId() {
		return (((getRegionX() / 8) << 8) + (getRegionY() / 8));
	}
	

}
