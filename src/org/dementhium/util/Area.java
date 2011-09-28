package org.dementhium.util;

import java.util.LinkedList;
import java.util.List;

import org.dementhium.model.Location;

/**
 * Represents an area.
 * @author Emperor
 *
 */
public class Area {

	/**
	 * The south-western corner location.
	 */
	private Location southWest;

	/**
	 * The north-eastern corner location.
	 */
	private Location northEast;

	/**
	 * The centre point of the circle.
	 */
	private Location centre;

	/**
	 * The radius of the circle.
	 */
	private int radius;
	
	/**
	 * The area the triangle is in.
	 */
	private Area triangleArea;

	/**
	 * The area type.
	 */
	private AreaType type;

	/**
	 * The area type.
	 * 
	 * @author Emperor
	 * 
	 */
	public static enum AreaType {
		RECTANGLE, CIRCLE, TRIANGLE
	}

	/**
	 * Constructs an area.
	 * 
	 * @param areaType
	 *            The type of the area.
	 * @param firstLocation
	 *            The first location: <br>
	 *            <i>Represents the south-western location on a rectangle/square<br>
	 *            The center location on a circle</i>
	 * @param secondLocation
	 *            The second location: <br>
	 *            <i>Represents the south-western location on a rectangle/square<br>
	 *            The center location on a circle</i>
	 * @see AreaType
	 */
	public Area(AreaType areaType, Location firstLocation,
			Location secondLocation, Location thirdLocation) {
		this.type = areaType;
		if (areaType == AreaType.RECTANGLE) {
			this.southWest = firstLocation;
			this.northEast = secondLocation;
		} else if (areaType == AreaType.CIRCLE){
			int radiusX = secondLocation.getX() - firstLocation.getX();
			int radiusY = secondLocation.getY() - firstLocation.getY();
			if (radiusX < 0) {
				radiusX *= -1;
			}
			if (radiusY < 0) {
				radiusY *= -1;
			}
			if (radiusX > radiusY) {
				this.radius = radiusX;
			} else {
				this.radius = radiusY;
			}
			this.centre = firstLocation;
		} else if (areaType == AreaType.TRIANGLE) {
			if (triangleArea == null) { //TODO remove this, it's useless.
				this.centre = firstLocation;
				this.southWest = secondLocation;
				this.northEast = thirdLocation;
			}
		}
	}

	/**
	 * Creates a new rectangle-formed area.
	 * 
	 * @param southWest
	 *            The south western corner.
	 * @param northEast
	 *            The north eastern corner.
	 * @return The area instance.
	 */
	public static Area rectangle(Location southWest, Location northEast) {
		return new Area(AreaType.RECTANGLE, southWest, northEast, null);
	}

	/**
	 * Creates a circle-formed area.
	 * 
	 * @param centre
	 *            The centre point.
	 * @param otherLocation
	 *            The other location.
	 * @return The area instance.
	 */
	public static Area circle(Location centre, Location otherLocation) {
		return new Area(AreaType.CIRCLE, centre, otherLocation, null);
	}

	/**
	 * Creates a circle-formed area.
	 * 
	 * @param centre
	 *            The centre.
	 * @param radius
	 *            The radius.
	 * @return The area instance.
	 */
	public static Area circle(Location centre, int radius) {
		return new Area(AreaType.CIRCLE, centre, centre.transform(radius, 0, 0), null);
	}
	
	/**
	 * Creates a triangular area.
	 * @param centre The centre point.
	 * @param first The first location.
	 * @param second The second location.
	 * @return The constructed area.
	 */
	public static Area triangle(Location centre, Location first, Location second) {
		return new Area(AreaType.TRIANGLE, centre, first, second);
	}

	/**
	 * Gets all the tiles in this area.
	 * 
	 * @return The array of tiles.
	 */
	public List<Location> getTiles() {
		if (type == AreaType.RECTANGLE) {
			List<Location> tiles = new LinkedList<Location>();
			for (int x = southWest.getX(); x < northEast.getX() + 1; x++) {
				for (int y = southWest.getY(); y < northEast.getY() + 1; y++) {
					tiles.add(Location.locate(x, y, 0));
				}
			}
			return tiles;
		}
		List<Location> tiles = new LinkedList<Location>();
		short westX = (short) (centre.getX() - radius);
		short southY = (short) (centre.getY() - radius);
		short eastX = (short) (centre.getX() + radius);
		short northY = (short) (centre.getY() + radius);
		for (short x = westX; x < eastX + 1; x++) {
			for (short y = southY; y < northY + 1; y++) {
				Location loc = Location.locate(x, y, 0);
				if (centre.withinDistance(loc, radius)) {
					tiles.add(loc);
				}
			}
		}
		return tiles;
	}

	public List<Location> getWalkableTiles() {
		List<Location> tiles = new LinkedList<Location>();
		List<Location> area = getTiles();
		//byte[][] flags = ClippingFlags.getClippingFlags(southWest);
		for (Location l : area) {
			if (l == null) {
				continue;
			}
			/*byte f = flags[l.getLocalX()][l.getLocalY()];
			if ((f & 0x12) == 0 && (f & 0x11) == 0 && (f & 0x14) == 0 
					&& (f & 0x18) == 0 && (f & 0x16) == 0 && (f & 0x15) == 0 
					&& (f & 0x16) == 0 && (f & 0x1A) == 0 && (f & 0x19) == 0) {*/
				tiles.add(l);
			//}
		}
		return tiles;
	}
	
	/**
	 * Checks if the given location is in the constructed area.
	 * 
	 * @param location
	 *            The location.
	 * @return {@code True} if so, {@code false} if not.
	 */
	public boolean isInArea(Location location) {
		if (location == null) {
			return false;
		}
		if (type == AreaType.RECTANGLE) {
			return location.getX() >= southWest.getX()
					&& location.getY() >= southWest.getY()
					&& location.getX() <= northEast.getX()
					&& location.getY() <= northEast.getY();
		}
		return centre.withinDistance(location, radius);
	}
	
	/**
	 * Checks if the given area is in the constructed area.
	 * 
	 * @param location
	 *            The other area.
	 * @return {@code True} if so, {@code false} if not.
	 */
	public boolean isInArea(Area area) {
		for (Location l : getTiles()) {
			for (Location al : area.getTiles()) {
				if (l.getX() == al.getX() && l.getY() == al.getY() && l.getZ() == al.getZ()) {
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Checks if the location given is within distance of the area.
	 * @param location The location given.
	 * @param minimumDistance The minimum distance.
	 * @return {@code True} if the location is closer than (or has the same) minimum distance, {@code false} if the distance is larger.
	 */
	public boolean isInRange(Location location, int minimumDistance) {
		for (Location l : getTiles()) {
			if (l.getDistance(location) <= minimumDistance) {
				return true;
			}
		}
		return false;
	}
	
	public boolean isInRange(Area area, int minimumDistance) {
		for (Location l : getTiles()) {
			for (Location al : area.getTiles()) {
				if (l.getDistance(al) <= minimumDistance) {
					return true;
				}
			}
		}
		return true;
	}
	
	public int getClosestDistance(Location l) {
		int lastDistance = Integer.MAX_VALUE;
		for (Location loc : getTiles()) {
			int distance = loc.getDistance(l);
			if (distance <= lastDistance) {
				lastDistance = distance;
			}
		}
		return lastDistance;
	}
	
	public Location getClosest(Location l) {
		if (isInArea(l)) {
			return getClosestExternLocation(l);
		}
		return getClosestLocation(l);
	}
	
	public Location getClosestLocation(Location l) {
		Location lastLocation = southWest;
		int lastDistance = Integer.MAX_VALUE;
		List<Location> tiles = getTiles();
		for (Location loc : tiles) {
			int distance = loc.getDistance(l);
			if (distance <= lastDistance) {
				lastDistance = distance;
				lastLocation = loc;
			}
		}
		return lastLocation;
	}
	
	public Location getClosestExternLocation(Location l) {
		Location lastLocation = southWest;
		int lastDistance = Integer.MAX_VALUE;
		List<Location> tiles = getExternTiles();
		//byte[][] flags = ClippingFlags.getClippingFlags(Location.locate(southWest.getX() - 1, southWest.getY() - 1, southWest.getZ(), southWest.getStaticLocation()));
		for (Location loc : tiles) {
			/*byte f = flags[loc.getLocalX()][loc.getLocalY()];
			if (!((f & 0x12) == 0 && (f & 0x11) == 0 && (f & 0x14) == 0 
					&& (f & 0x18) == 0 && (f & 0x16) == 0 && (f & 0x15) == 0 
					&& (f & 0x16) == 0 && (f & 0x1A) == 0 && (f & 0x19) == 0)) {
				continue;
			}*/
			if (lastLocation == southWest) {
				lastLocation = loc;
			}
			int distance = loc.getDistance(l);
			if (distance <= lastDistance) {
				lastDistance = distance;
				lastLocation = loc;
			}
		}
		return lastLocation;
	}
	
	/**
	 * Gets all the tiles in this area.
	 * 
	 * @return The array of tiles.
	 */
	public List<Location> getExternTiles() {
		List<Location> tiles = new LinkedList<Location>();
		for (int x = southWest.getX(); x < northEast.getX() + 1; x++) {
			tiles.add(Location.locate(x, southWest.getY() - 1, 0));
			tiles.add(Location.locate(x, northEast.getY() + 1, 0));
		}
		for (int y = southWest.getY(); y < northEast.getY() + 1; y++) {
			tiles.add(Location.locate(southWest.getX() - 1, y, 0));
			tiles.add(Location.locate(northEast.getX() + 1, y, 0));
		}
		return tiles;
	}

	/**
	 * @param southWest
	 *            the southWest to set
	 */
	public void setSouthWest(Location southWest) {
		this.southWest = southWest;
	}

	/**
	 * @return the southWest
	 */
	public Location getSouthWest() {
		return southWest;
	}

	/**
	 * @param northEast
	 *            the northEast to set
	 */
	public void setNorthEast(Location northEast) {
		this.northEast = northEast;
	}

	/**
	 * @return the northEast
	 */
	public Location getNorthEast() {
		return northEast;
	}

	/**
	 * @param centre
	 *            the centre to set
	 */
	public void setCentre(Location centre) {
		this.centre = centre;
	}

	/**
	 * @return the centre
	 */
	public Location getCentre() {
		return centre;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(AreaType type) {
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public AreaType getType() {
		return type;
	}

	/**
	 * Sets the radius.
	 * 
	 * @param radius
	 *            The radius.
	 */
	public void setRadius(int radius) {
		this.radius = radius;
	}

	/**
	 * @return the radius
	 */
	public int getRadius() {
		return radius;
	}
}
