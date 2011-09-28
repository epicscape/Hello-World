package org.dementhium.model.map.region;

import java.util.HashMap;
import java.util.Map;

import org.dementhium.cache.CacheManager;

/**
 * @author dragonkk
 */
public class RegionBuilder {


	public static final void init() {
		copyAllPlanesMap(getRegion(3200), getRegion(3200), getRegion(4000), getRegion(4000), 100);
		copyAllPlanesMap(getRegion(2924), getRegion(5203), getRegion(4021), getRegion(4024), 1);
	}

	private static Map<Integer, DynamicRegion> dynamicRegions = new HashMap<Integer, DynamicRegion>();

	public static final int getRegion(int c) {
		return c >> 3;
	}

	private static boolean lastSearchPositive;
	
	public static int[] findEmptyMap(int widthRegions, int heightRegions) {
		boolean lastSearchPositive = RegionBuilder.lastSearchPositive = !RegionBuilder.lastSearchPositive;
		int regionsXDistance = ((widthRegions)/8) + 1;
		int regionsYDistance = ((heightRegions)/8) + 1;
		for (int regionIdC = 0; regionIdC < 20000; regionIdC++) {
			int regionId = lastSearchPositive ? 20000-regionIdC : regionIdC;
			int regionX = (regionId >> 8) * 64;
			int regionY = (regionId & 0xff) * 64;
			if (regionX >> 3 < 336 || regionY >> 3 < 336)
				continue;
			boolean found = true;
			for (int thisRegionX = regionX - 64;	thisRegionX < (regionX + (regionsXDistance * 64)); thisRegionX += 64) {
				for (int thisRegionY = regionY - 64; thisRegionY < (regionY + (regionsYDistance * 64)); thisRegionY += 64) {
					if (thisRegionX < 0 || thisRegionY < 0)
						continue;
					if (!emptyRegion(thisRegionX, thisRegionY, !(thisRegionX < regionX || thisRegionY < regionY || thisRegionX > (regionX + ((regionsXDistance-1) * 64))) || thisRegionY > (regionY + ((regionsYDistance-1) * 64)))) {
						found = false;
						break;
					}

				}
			}
			if (found)
				return new int[] {regionX, regionY};
		}
		return null;
	}

	private static boolean emptyRegion(int regionX, int regionY, boolean checkValid) {
		if(regionX > 10000 || regionY > 16000)
			return !checkValid;
		int rx = getRegion(regionX) / 8;
		int ry = getRegion(regionY) / 8;
		if (CacheManager.getFIT(5).findName("m" + rx+ "_" + ry) != -1)
			return false;
		DynamicRegion region = dynamicRegions.get((rx << 8) + ry);
		return region == null;
	}


	public void cutRegion(int regionX, int regionY, int plane) {
		DynamicRegion toRegion = createDynamicRegion((((regionX / 8) << 8) + (regionY / 8)));
		int regionOffsetX = (regionX - ((regionX/8) * 8));
		int regionOffsetY = (regionY - ((regionY/8) * 8));
		toRegion.getRegionCoords()[plane][regionOffsetX][regionOffsetY][0] = 0;
		toRegion.getRegionCoords()[plane][regionOffsetX][regionOffsetY][1] = 0;
		toRegion.getRegionCoords()[plane][regionOffsetX][regionOffsetY][2] = 0;
		toRegion.getRegionCoords()[plane][regionOffsetX][regionOffsetY][3] = 0;
	}

	public static void copyRegion(int fromRegionX, int fromRegionY, int fromPlane, int toRegionX, int toRegionY, int toPlane, int rotation) {
		DynamicRegion toRegion = createDynamicRegion((((toRegionX / 8) << 8) + (toRegionY / 8)));
		int regionOffsetX = (toRegionX - ((toRegionX/8) * 8));
		int regionOffsetY = (toRegionY - ((toRegionY/8) * 8));
		toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][0] = fromRegionX;
		toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][1] = fromRegionY;
		toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][2] = fromPlane;
		toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][3] = rotation;
	}
	
	public static void copyAllHeights(int fromRegionX, int fromRegionY, int toRegionX, int toRegionY, int ratio) {
		fromRegionX = getRegion(fromRegionX);
		fromRegionY = getRegion(fromRegionY);
		toRegionX = getRegion(toRegionX);
		toRegionY = getRegion(toRegionY);
		int[] planes = new int[4];
		for(int plane = 1; plane < 4; plane++)
			planes[plane] = plane;
		copyMap(fromRegionX, fromRegionY, toRegionX, toRegionY, ratio, ratio, planes, planes);
	}
	
	public static void copy(int fromRegionX, int fromRegionY, int toRegionX, int toRegionY, int ratio) {
		fromRegionX = getRegion(fromRegionX);
		fromRegionY = getRegion(fromRegionY);
		toRegionX = getRegion(toRegionX);
		toRegionY = getRegion(toRegionY);
		copyMap(fromRegionX, fromRegionY, toRegionX, toRegionY, ratio, ratio, new int[4], new int[4]);
	}

	public static void copyAllPlanesMap(int fromRegionX, int fromRegionY, int toRegionX, int toRegionY, int ratio) {
		int[] planes = new int[4];
		for(int plane = 1; plane < 4; plane++)
			planes[plane] = plane;
		copyMap(fromRegionX, fromRegionY, toRegionX, toRegionY, ratio, ratio, planes, planes);
	}

	public static void copyMap(int fromRegionX, int fromRegionY, int toRegionX, int toRegionY, int ratio, int[] fromPlanes, int[] toPlanes) {
		copyMap(fromRegionX, fromRegionY, toRegionX, toRegionY, ratio, ratio, fromPlanes, toPlanes);
	}


	public static void copyMap(int fromRegionX, int fromRegionY, int toRegionX, int toRegionY, int widthRegions, int heightRegions, int[] fromPlanes, int[] toPlanes) {
		if(fromPlanes.length != toPlanes.length)
			throw new RuntimeException("PLANES LENGTH ISNT SAME OF THE NEW PLANES ORDER!");
		for(int xOffset = 0; xOffset < widthRegions; xOffset++) {
			for(int yOffset = 0; yOffset < heightRegions; yOffset++) {
				int fromThisRegionX = fromRegionX+xOffset;
				int fromThisRegionY = fromRegionY+yOffset;
				int toThisRegionX = toRegionX+xOffset;
				int toThisRegionY = toRegionY+yOffset;
				DynamicRegion toRegion = createDynamicRegion((((toThisRegionX / 8) << 8) + (toThisRegionY / 8)));
				int regionOffsetX = (toThisRegionX - ((toThisRegionX/8) * 8));
				int regionOffsetY = (toThisRegionY - ((toThisRegionY/8) * 8));
				for(int pIndex = 0; pIndex < fromPlanes.length; pIndex++) {
					int toPlane = toPlanes[pIndex];
					toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][0] = fromThisRegionX;
					toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][1] = fromThisRegionY;
					toRegion.getRegionCoords()[toPlane][regionOffsetX][regionOffsetY][2] = fromPlanes[pIndex];
				}
			}
		}
	}

	public static DynamicRegion createDynamicRegion(int regionId) {
		DynamicRegion region = dynamicRegions.get(regionId);
		if (region != null)
			if (region != null)
				return region;
		DynamicRegion newRegion = new DynamicRegion(regionId);
		dynamicRegions.put(regionId, newRegion);
		return newRegion;
	}	

	public static void destroyDynamicRegion(int regionId) {
		//TODO
	}

	public static DynamicRegion getDynamicRegion(int regionId) {
		return dynamicRegions.get(regionId);
	}

	public static DynamicRegion getDynamicRegion(int x, int y) {
		int regionId = (((x >> 3) >> 3) << 8) | ((y >> 3) >> 3);
		return getDynamicRegion(regionId);
	}

	private RegionBuilder() {

	}
}
