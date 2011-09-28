package org.dementhium.model.map.region;

import org.dementhium.model.Location;
import org.dementhium.model.map.GameObject;
import org.dementhium.model.map.Region;

public class DynamicRegion {
	
	private int[][][][] regionCoords;
	
	public DynamicRegion(int regionId) {
		regionCoords = new int[4][8][8][4];
	}
	
	public int getMask(int plane, int localX, int localY) {
		int xoffset = localX/8;
		int yoffset = localY/8;
		int rotation = regionCoords[plane][xoffset][yoffset][3];
		if(rotation != 0)
			return 0;
		int realRegionX = regionCoords[plane][xoffset][yoffset][0]; 
		int realRegionY = regionCoords[plane][xoffset][yoffset][1];
		if(realRegionX == 0|| realRegionY == 0)
			return -1;
		int realRegionId = (((realRegionX / 8) << 8) + (realRegionY / 8));
		int baseX = (realRegionId >> 8) << 6, baseY = (realRegionId & 0xff) << 6;
		int coordX = baseX + localX;
		int coordY = baseY + localY;
		int regionX = coordX >> 3;
		int regionY = coordY >> 3;
		int regionOffsetX = (regionX - ((regionX/8) * 8));
		int regionOffsetY = (regionY - ((regionY/8) * 8));
		int realRegionOffsetX = (realRegionX - ((realRegionX/8) * 8));
		int realRegionOffsetY = (realRegionY - ((realRegionY/8) * 8));
		int realLocalX = (realRegionOffsetX * 8) + (localX - (regionOffsetX*8));
		int realLocalY = (realRegionOffsetY * 8) + (localY - (regionOffsetY*8));
		return Region.getClippingMask(baseX+realLocalX, baseY+realLocalY, regionCoords[plane][xoffset][yoffset][2]);
	}
	
	public GameObject getObject(int objectId, int localX, int localY, int height) {
		int xoffset = localX/8;
		int yoffset = localY/8;
		int realRegionX = regionCoords[height][xoffset][yoffset][0]; 
		int realRegionY = regionCoords[height][xoffset][yoffset][1];
		if(realRegionX == 0|| realRegionY == 0)
			return null;
		int realRegionId = (((realRegionX / 8) << 8) + (realRegionY / 8));
		int baseX = (realRegionId >> 8) << 6, baseY = (realRegionId & 0xff) << 6;
		int coordX = baseX + localX;
		int coordY = baseY + localY;
		int regionX = coordX >> 3;
		int regionY = coordY >> 3;
		int regionOffsetX = (regionX - ((regionX/8) * 8));
		int regionOffsetY = (regionY - ((regionY/8) * 8));
		int realRegionOffsetX = (realRegionX - ((realRegionX/8) * 8));
		int realRegionOffsetY = (realRegionY - ((realRegionY/8) * 8));
		int realLocalX = (realRegionOffsetX * 8) + (localX - (regionOffsetX*8));
		int realLocalY = (realRegionOffsetY * 8) + (localY - (regionOffsetY*8));
		return Location.locate(baseX+realLocalX, baseY+realLocalY, regionCoords[height][xoffset][yoffset][2]).getGameObject(objectId);
	}

	public int[][][][] getRegionCoords() {
		return regionCoords;
	}

	public void setRegionCoords(int[][][][] regionCoords) {
		this.regionCoords = regionCoords;
	}
}
