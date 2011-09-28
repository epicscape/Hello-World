package org.dementhium.model.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.dementhium.cache.format.CacheObjectDefinition;
import org.dementhium.model.Entity;
import org.dementhium.model.Location;
import org.dementhium.model.map.region.DynamicRegion;
import org.dementhium.model.map.region.RegionBuilder;
import org.dementhium.model.npc.NPC;
import org.dementhium.model.player.Player;

/**
 * @author Lazaro
 * @author Palidino76
 * @author 'Mystic Flow
 */
public class Region {

	public static final int PLAYERS = 0, NPCS = 1;

	public static final int REGION_SIZE = 128;

	private static Map<Integer, Region> regionCache = new HashMap<Integer, Region>();

	public static List<NPC> getLocalNPCs(Location tile) {
		return getLocalNPCs(tile, 16);
	}

	public static List<NPC> getLocalNPCs(Location tile, int depth) {
		int baseX = tile.getX();
		int baseY = tile.getY();
		int z = tile.getZ();
		int lastRegionId = hash(baseX, baseY);
		Region region = forCoords(lastRegionId);
		List<Region> regions = new ArrayList<Region>();
		regions.add(region); //initial region
		for (int x = -depth; x < depth + 1; x++) {
			for (int y = -depth; y < depth + 1; y++) {
				int currentRegionId = hash(baseX + x, baseY + y);
				if (currentRegionId != lastRegionId) {
					lastRegionId = currentRegionId;
					if (!regions.contains(region)) {
						regions.add(forCoords(lastRegionId));
					}
				}
			}
		}
		List<NPC> npcs = new ArrayList<NPC>();
		for (Region r : regions) {
			for (NPC n : r.npcs[z]) {
				npcs.add(n);
			}
		}
		return npcs;
		//return getRegionTiles(tile, depth, NPCS);
	}

	public static List<Player> getLocalPlayers(Location tile) {
		return getLocalPlayers(tile, 16);
	}

	public static List<Player> getLocalPlayers(Location tile, int depth) {
		int baseX = tile.getX();
		int baseY = tile.getY();
		int z = tile.getZ();
		int lastRegionId = hash(baseX, baseY);
		Region region = forCoords(lastRegionId);
		List<Region> regions = new ArrayList<Region>();
		regions.add(region); //initial region
		for (int x = -depth; x < depth + 1; x++) {
			for (int y = -depth; y < depth + 1; y++) {
				int currentRegionId = hash(baseX + x, baseY + y);
				if (currentRegionId != lastRegionId) {
					lastRegionId = currentRegionId;
					if (!regions.contains(region)) {
						regions.add(forCoords(lastRegionId));
					}
				}
			}
		}
		List<Player> players = new ArrayList<Player>();
		for (Region r : regions) {
			for (Player p : r.players[z]) {
				players.add(p);
			}
		}
		return players;
//		return getRegionTiles(tile, depth, PLAYERS);
	}

/*	public static List getRegionTiles(Location tile, int depth, int type) {
		int baseX = tile.getX();
		int baseY = tile.getY();
		int z = tile.getZ();

		int lastRegionId = hash(baseX, baseY);
		Region region = forCoords(lastRegionId);

		List<Region> regions = new ArrayList<Region>();

		regions.add(region); //initial region
		for (int x = -depth; x < depth + 1; x++) {
			for (int y = -depth; y < depth + 1; y++) {
				int currentRegionId = hash(baseX + x, baseY + y);
				if (currentRegionId != lastRegionId) {
					lastRegionId = currentRegionId;
					if (!regions.contains(region)) {
						regions.add(forCoords(lastRegionId));
					}
				}
			}
		}
		List list = new ArrayList();
		for (Region r : regions) {
			switch (type) {
			case PLAYERS:
				synchronized (r.players) {
					for (Player p : r.players[z]) {
						if (p.getLocation().distance(tile) <= depth) {
							list.add(p);
						}
					}
				}
				break;
			case NPCS:
				synchronized (r.npcs) {
					for (NPC n : r.npcs[z]) {
						if (n.getLocation().distance(tile) <= depth) {
							list.add(n);
						}
					}
				}
				break;
			}
		}
		return list;
	}*/

	public static int hash(int x, int y) {
		return x >> 7 << 8 | y >> 7;
	}

	public static void addClipping(int x, int y, int z, int shift) {
		Region region = forCoords(x, y);
		int localX = x - ((x >> 7) << 7);
		int localY = y - ((y >> 7) << 7);
		if (region.clippingMasks[z] == null) {
			region.clippingMasks[z] = new int[region.size][region.size];
		}
		region.clipped = true;
		region.clippingMasks[z][localX][localY] |= shift;
	}

	public static void removeClipping(int x, int y, int z, int shift) {
		Region region = forCoords(x, y);
		int localX = x - ((x >> 7) << 7);
		int localY = y - ((y >> 7) << 7);
		if (region.clippingMasks[z] == null) {
			region.clippingMasks[z] = new int[region.size][region.size];
		}
		region.clippingMasks[z][localX][localY] &= ~shift;
	}

	public static Region forCoords(int hash) {
		Region region = regionCache.get(hash);
		if (region == null) {
			region = new Region(hash >> 8, hash & 0xFF, REGION_SIZE);
			regionCache.put(hash, region);
		}
		return region;
	}

	public static Region forCoords(int x, int y) {
		x = x >> 7;
		y = y >> 7;
		int hash = x << 8 | y;
		Region region = regionCache.get(hash);
		if (region == null) {
			region = new Region(x, y, REGION_SIZE);
			regionCache.put(hash, region);
		}
		return region;
	}

	public static Region forLocation(Location other) {
		return forCoords(other.getX(), other.getY());
	}

	public static int getClippingMask(int x, int y, int z) {
		Region region = forCoords(x, y);
		if (region.clippingMasks[z] == null || !region.clipped) {
			DynamicRegion dynamicRegion = RegionBuilder.getDynamicRegion(x, y);
			if (dynamicRegion != null) {
				int baseLocalX = x - (((x >> 3) >> 3) << 6) ;
				int baseLocalY = y - (((y >> 3) >> 3) << 6);
				return dynamicRegion.getMask(z, baseLocalX, baseLocalY);
			}
			return -1;
		}
		int localX = x - ((x >> 7) << 7);
		int localY = y - ((y >> 7) << 7);
		return region.clippingMasks[z][localX][localY];
	}

	public static Region getRegion(int x, int y, int z) {
		return forCoords(x, y);
	}

	public static Location getLocation(int x, int y, int z) {
		Region region = forCoords(x, y);
		int localX = x - ((x >> 7) << 7);
		int localY = y - ((y >> 7) << 7);
		return region.getLocalLocation(localX, localY, z);
	}

	private int[][][] clippingMasks = new int[4][][];
	private Location[][][] tiles = new Location[4][][];

	private int size;
	private int x;
	private int y;

	@SuppressWarnings("unchecked")
	private Set<Player>[] players = new HashSet[4];
	@SuppressWarnings("unchecked")
	private Set<NPC>[] npcs = new HashSet[4];

	private boolean clipped;

	public Region(int x, int y, int size) {
		this.x = x;
		this.y = y;
		this.size = size;
		for (int i = 0; i < 4; i++) {
			players[i] = new HashSet<Player>();
			npcs[i] = new HashSet<NPC>();
		}
	}

	public Location getLocalLocation(int x, int y, int z) {
		z %= 4;
		if (tiles[z] == null) {
			tiles[z] = new Location[size][size];
		}
		Location tile = tiles[z][x][y];
		if (tile == null) {
			tile = new Location(this.x << 7 | x, this.y << 7 | y, z, this);
			tiles[z][x][y] = tile;
		}
		return tile;
	}

	public int getSize() {
		return size;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	@Override
	public int hashCode() {
		return x << 8 | y;
	}

	public void setClipped(boolean clipped) {
		this.clipped = clipped;
	}

	public boolean isClipped() {
		return clipped;
	}

	public static GameObject addObject(int objectId, int x, int y, int height, int type, int direction, boolean ignoreObjects) {
		if (!ignoreObjects && objectId == -1) {
			removeObject(x, y, height, type);
		}
		if (objectId == -1) {
			return null;
		}
		CacheObjectDefinition def = CacheObjectDefinition.forId(objectId);
		if (def == null) {
			return null;
		}
		int xLength;
		int yLength;
		GameObject object = new GameObject(objectId, x, y, height, type, direction);
		if (direction == 1 || direction == 3) {
			xLength = def.getSizeX();
			yLength = def.getSizeY();
		} else {
			xLength = def.getSizeY();
			yLength = def.getSizeX();
		}
		if (type == 22) {
			if (def.getActionCount() == 1) {
				addClipping(x, y, height, 0x200000);
			}
		} else if (type >= 9 && type <= 11) {
			if (def.getActionCount() != 0) {
				addClippingForSolidObject(x, y, height, xLength, yLength, def.isSolid(), !def.isClippingFlag());
			}
		} else if (type >= 0 && type <= 3) {
			if (def.getActionCount() != 0) {
				addClippingForVariableObject(x, y, height, type, direction, def.isSolid(), !def.isClippingFlag());
			}
		}
		if (!ignoreObjects) {
			removeObject(x, y, height, type);
		}
		addGameObject(object);
		return object;
	}

	public static void addGameObject(GameObject object) {
		GameObject oldObject = object.getLocation().getGameObjectType(object.getType());
		if (oldObject != null && !excluded(object.getId())) {
			return;
		}
		object.getLocation().addObject(object);
	}

	public static boolean excluded(int id) {
		switch (id) {
		case 36523:
		case 36532:
		case 36521:
		case 36495:
		case 36484:
		case 36480:
		case 36586:
		case 36540:
		case 36579:
		case 36481: //These are CW objects that are excluded because there's an object in their position already
			return true;
		}
		return false;
	}

	public static GameObject removeObject(int x, int y, int height, int type) {
		Location loc = Location.locate(x, y, height);
		GameObject oldObj = loc.getGameObjectType(type);
		loc.removeObject(oldObj);
		if (oldObj != null) {
			CacheObjectDefinition def = CacheObjectDefinition.forId(oldObj.getId());
			int xLength;
			int yLength;
			if (oldObj.getRotation() != 1 && oldObj.getRotation() != 3) {
				xLength = def.getSizeX();
				yLength = def.getSizeY();
			} else {
				xLength = def.getSizeY();
				yLength = def.getSizeX();
			}
			if (oldObj.getType() == 22) {
				if (def.getActionCount() == 1) {
					removeClipping(x, y, height, 0x200000);
				}
			} else if (oldObj.getType() >= 9 && oldObj.getType() <= 11) {
				if (def.getActionCount() != 0) {
					removeClippingForSolidObject(x, y, height, xLength, yLength, def.isSolid(), !def.isClippingFlag());
				}
			} else if (oldObj.getType() >= 0 && oldObj.getType() <= 3) {
				if (def.getActionCount() != 0) {
					removeClippingForVariableObject(x, y, height, oldObj.getType(), oldObj.getRotation(), def.isSolid(), !def.isClippingFlag());
				}
			}
		}
		return oldObj;
	}

	private static void addClippingForSolidObject(int x, int y, int height, int xLength, int yLength, boolean flag, boolean flag2) {
		int clipping = 256;
		if (flag) {
			clipping |= 0x20000;
		}
		if (flag2) {
			clipping |= 0x40000000;
		}
		for (int i = x; i < x + xLength; i++) {
			for (int i2 = y; i2 < y + yLength; i2++) {
				addClipping(i, i2, height, clipping);
			}
		}
	}

	private static void removeClippingForSolidObject(int x, int y, int height, int xLength, int yLength, boolean flag, boolean flag2) {
		int clipping = 256;
		if (flag) {
			clipping |= 0x20000;
		}
		if (flag2) {
			clipping |= 0x40000000;
		}
		for (int i = x; i < x + xLength; i++) {
			for (int i2 = y; i2 < y + yLength; i2++) {
				removeClipping(i, i2, height, clipping);
			}
		}
	}

	private static void addClippingForVariableObject(int x, int y, int z, int type, int direction, boolean flag, boolean flag2) {
		if (type == 0) {
			if (direction == 0) {
				addClipping(x, y, z, 128);
				addClipping(x - 1, y, z, 8);
			}
			if (direction == 1) {
				addClipping(x, y, z, 2);
				addClipping(x, y + 1, z, 32);
			}
			if (direction == 2) {
				addClipping(x, y, z, 8);
				addClipping(x + 1, y, z, 128);
			}
			if (direction == 3) {
				addClipping(x, y, z, 32);
				addClipping(x, y - 1, z, 2);
			}
		}
		if (type == 1 || type == 3) {
			if (direction == 0) {
				addClipping(x, y, z, 1);
				addClipping(x - 1, y + 1, z, 16);
			}
			if (direction == 1) {
				addClipping(x, y, z, 4);
				addClipping(x + 1, y + 1, z, 64);
			}
			if (direction == 2) {
				addClipping(x, y, z, 16);
				addClipping(x + 1, y - 1, z, 1);
			}
			if (direction == 3) {
				addClipping(x, y, z, 64);
				addClipping(x - 1, y - 1, z, 4);
			}
		}
		if (type == 2) {
			if (direction == 0) {
				addClipping(x, y, z, 130);
				addClipping(x - 1, y, z, 8);
				addClipping(x, y + 1, z, 32);
			}
			if (direction == 1) {
				addClipping(x, y, z, 10);
				addClipping(x, y + 1, z, 32);
				addClipping(x + 1, y, z, 128);
			}
			if (direction == 2) {
				addClipping(x, y, z, 40);
				addClipping(x + 1, y, z, 128);
				addClipping(x, y - 1, z, 2);
			}
			if (direction == 3) {
				addClipping(x, y, z, 160);
				addClipping(x, y - 1, z, 2);
				addClipping(x - 1, y, z, 8);
			}
		}
		if (flag) {
			if (type == 0) {
				if (direction == 0) {
					addClipping(x, y, z, 0x10000);
					addClipping(x - 1, y, z, 4096);
				}
				if (direction == 1) {
					addClipping(x, y, z, 1024);
					addClipping(x, y + 1, z, 16384);
				}
				if (direction == 2) {
					addClipping(x, y, z, 4096);
					addClipping(x + 1, y, z, 0x10000);
				}
				if (direction == 3) {
					addClipping(x, y, z, 16384);
					addClipping(x, y - 1, z, 1024);
				}
			}
			if (type == 1 || type == 3) {
				if (direction == 0) {
					addClipping(x, y, z, 512);
					addClipping(x - 1, y + 1, z, 8192);
				}
				if (direction == 1) {
					addClipping(x, y, z, 2048);
					addClipping(x + 1, y + 1, z, 32768);
				}
				if (direction == 2) {
					addClipping(x, y, z, 8192);
					addClipping(x + 1, y - 1, z, 512);
				}
				if (direction == 3) {
					addClipping(x, y, z, 32768);
					addClipping(x - 1, y - 1, z, 2048);
				}
			}
			if (type == 2) {
				if (direction == 0) {
					addClipping(x, y, z, 0x10400);
					addClipping(x - 1, y, z, 4096);
					addClipping(x, y + 1, z, 16384);
				}
				if (direction == 1) {
					addClipping(x, y, z, 5120);
					addClipping(x, y + 1, z, 16384);
					addClipping(x + 1, y, z, 0x10000);
				}
				if (direction == 2) {
					addClipping(x, y, z, 20480);
					addClipping(x + 1, y, z, 0x10000);
					addClipping(x, y - 1, z, 1024);
				}
				if (direction == 3) {
					addClipping(x, y, z, 0x14000);
					addClipping(x, y - 1, z, 1024);
					addClipping(x - 1, y, z, 4096);
				}
			}
		}
		if (flag2) {
			if (type == 0) {
				if (direction == 0) {
					addClipping(x, y, z, 0x20000000);
					addClipping(x - 1, y, z, 0x2000000);
				}
				if (direction == 1) {
					addClipping(x, y, z, 0x800000);
					addClipping(x, y + 1, z, 0x8000000);
				}
				if (direction == 2) {
					addClipping(x, y, z, 0x2000000);
					addClipping(x + 1, y, z, 0x20000000);
				}
				if (direction == 3) {
					addClipping(x, y, z, 0x8000000);
					addClipping(x, y - 1, z, 0x800000);
				}
			}
			if (type == 1 || type == 3) {
				if (direction == 0) {
					addClipping(x, y, z, 0x400000);
					addClipping(x - 1, y + 1, z, 0x4000000);
				}
				if (direction == 1) {
					addClipping(x, y, z, 0x1000000);
					addClipping(1 + x, 1 + y, z, 0x10000000);
				}
				if (direction == 2) {
					addClipping(x, y, z, 0x4000000);
					addClipping(x + 1, -1 + y, z, 0x400000);
				}
				if (direction == 3) {
					addClipping(x, y, z, 0x10000000);
					addClipping(-1 + x, y - 1, z, 0x1000000);
				}
			}
			if (type == 2) {
				if (direction == 0) {
					addClipping(x, y, z, 0x20800000);
					addClipping(-1 + x, y, z, 0x2000000);
					addClipping(x, 1 + y, z, 0x8000000);
				}
				if (direction == 1) {
					addClipping(x, y, z, 0x2800000);
					addClipping(x, 1 + y, z, 0x8000000);
					addClipping(x + 1, y, z, 0x20000000);
				}
				if (direction == 2) {
					addClipping(x, y, z, 0xa000000);
					addClipping(1 + x, y, z, 0x20000000);
					addClipping(x, y - 1, z, 0x800000);
				}
				if (direction == 3) {
					addClipping(x, y, z, 0x28000000);
					addClipping(x, y - 1, z, 0x800000);
					addClipping(-1 + x, y, z, 0x2000000);
				}
			}
		}
	}

	public static void removeClippingForVariableObject(int x, int y, int z, int type, int direction, boolean flag, boolean flag2) {
		if (type == 0) {
			if (direction == 0) {
				removeClipping(x, y, z, 128);
				removeClipping(x - 1, y, z, 8);
			}
			if (direction == 1) {
				removeClipping(x, y, z, 2);
				removeClipping(x, 1 + y, z, 32);
			}
			if (direction == 2) {
				removeClipping(x, y, z, 8);
				removeClipping(1 + x, y, z, 128);
			}
			if (direction == 3) {
				removeClipping(x, y, z, 32);
				removeClipping(x, y - 1, z, 2);
			}
		}
		if (type == 1 || type == 3) {
			if (direction == 0) {
				removeClipping(x, y, z, 1);
				removeClipping(x - 1, 1 + y, z, 16);
			}
			if (direction == 1) {
				removeClipping(x, y, z, 4);
				removeClipping(1 + x, y + 1, z, 64);
			}
			if (direction == 2) {
				removeClipping(x, y, z, 16);
				removeClipping(x + 1, -1 + y, z, 1);
			}
			if (direction == 3) {
				removeClipping(x, y, z, 64);
				removeClipping(-1 + x, -1 + y, z, 4);
			}
		}
		if (type == 2) {
			if (direction == 0) {
				removeClipping(x, y, z, 130);
				removeClipping(x - 1, y, z, 8);
				removeClipping(x, 1 + y, z, 32);
			}
			if (direction == 1) {
				removeClipping(x, y, z, 10);
				removeClipping(x, 1 + y, z, 32);
				removeClipping(1 + x, y, z, 128);
			}
			if (direction == 2) {
				removeClipping(x, y, z, 40);
				removeClipping(x + 1, y, z, 128);
				removeClipping(x, -1 + y, z, 2);
			}
			if (direction == 3) {
				removeClipping(x, y, z, 160);
				removeClipping(x, y - 1, z, 2);
				removeClipping(-1 + x, y, z, 8);
			}
		}
		if (flag) {
			if (type == 0) {
				if (direction == 0) {
					removeClipping(x, y, z, 0x10000);
					removeClipping(-1 + x, y, z, 4096);
				}
				if (direction == 1) {
					removeClipping(x, y, z, 1024);
					removeClipping(x, 1 + y, z, 16384);
				}
				if (direction == 2) {
					removeClipping(x, y, z, 4096);
					removeClipping(x + 1, y, z, 0x10000);
				}
				if (direction == 3) {
					removeClipping(x, y, z, 16384);
					removeClipping(x, y - 1, z, 1024);
				}
			}
			if (type == 1 || type == 3) {
				if (direction == 0) {
					removeClipping(x, y, z, 512);
					removeClipping(-1 + x, 1 + y, z, 8192);
				}
				if (direction == 1) {
					removeClipping(x, y, z, 2048);
					removeClipping(1 + x, 1 + y, z, 32768);
				}
				if (direction == 2) {
					removeClipping(x, y, z, 8192);
					removeClipping(x + 1, -1 + y, z, 512);
				}
				if (direction == 3) {
					removeClipping(x, y, z, 32768);
					removeClipping(x - 1, -1 + y, z, 2048);
				}
			}
			if (type == 2) {
				if (direction == 0) {
					removeClipping(x, y, z, 0x10400);
					removeClipping(-1 + x, y, z, 4096);
					removeClipping(x, y + 1, z, 16384);
				}
				if (direction == 1) {
					removeClipping(x, y, z, 5120);
					removeClipping(x, 1 + y, z, 16384);
					removeClipping(x + 1, y, z, 0x10000);
				}
				if (direction == 2) {
					removeClipping(x, y, z, 20480);
					removeClipping(1 + x, y, z, 0x10000);
					removeClipping(x, -1 + y, z, 1024);
				}
				if (direction == 3) {
					removeClipping(x, y, z, 0x14000);
					removeClipping(x, -1 + y, z, 1024);
					removeClipping(-1 + x, y, z, 4096);
				}
			}
		}
		if (flag2) {
			if (type == 0) {
				if (direction == 0) {
					removeClipping(x, y, z, 0x20000000);
					removeClipping(-1 + x, y, z, 0x2000000);
				}
				if (direction == 1) {
					removeClipping(x, y, z, 0x800000);
					removeClipping(x, 1 + y, z, 0x8000000);
				}
				if (direction == 2) {
					removeClipping(x, y, z, 0x2000000);
					removeClipping(x + 1, y, z, 0x20000000);
				}
				if (direction == 3) {
					removeClipping(x, y, z, 0x8000000);
					removeClipping(x, -1 + y, z, 0x800000);
				}
			}
			if (type == 1 || type == 3) {
				if (direction == 0) {
					removeClipping(x, y, z, 0x400000);
					removeClipping(x - 1, y + 1, z, 0x4000000);
				}
				if (direction == 1) {
					removeClipping(x, y, z, 0x1000000);
					removeClipping(1 + x, 1 + y, z, 0x10000000);
				}
				if (direction == 2) {
					removeClipping(x, y, z, 0x4000000);
					removeClipping(x + 1, -1 + y, z, 0x400000);
				}
				if (direction == 3) {
					removeClipping(x, y, z, 0x10000000);
					removeClipping(-1 + x, y - 1, z, 0x1000000);
				}
			}
			if (type == 2) {
				if (direction == 0) {
					removeClipping(x, y, z, 0x20800000);
					removeClipping(-1 + x, y, z, 0x2000000);
					removeClipping(x, y + 1, z, 0x8000000);
				}
				if (direction == 1) {
					removeClipping(x, y, z, 0x2800000);
					removeClipping(x, y + 1, z, 0x8000000);
					removeClipping(1 + x, y, z, 0x20000000);
				}
				if (direction == 2) {
					removeClipping(x, y, z, 0xa000000);
					removeClipping(x + 1, y, z, 0x20000000);
					removeClipping(x, y - 1, z, 0x800000);
				}
				if (direction == 3) {
					removeClipping(x, y, z, 0x28000000);
					removeClipping(x, -1 + y, z, 0x800000);
					removeClipping(x - 1, y, z, 0x2000000);
				}
			}
		}
	}

	public void addEntity(Entity entity) {
		int z = entity.getLocation().getZ();
		if (entity.isPlayer()) {
			synchronized (players) {
				players[z].add(entity.getPlayer());
			}
		} else if (entity.isNPC()) {
			synchronized (npcs) {
				npcs[z].add(entity.getNPC());
			}
		}
	}

	public void removeEntity(Entity entity) {
		int z = entity.getLocation().getZ();
		if (entity.isPlayer()) {
			synchronized (players) {
				players[z].remove(entity.getPlayer());
			}
		} else if (entity.isNPC()) {
			synchronized (npcs) {
				npcs[z].remove(entity.getNPC());
			}
		}
	}

	public int getClippingFlag(int localX, int localY, int height) {
		return clippingMasks[localX][localY][height];
	}

	public static Region getRegion(int realRegionId) {
		
		return null;
	}

}
