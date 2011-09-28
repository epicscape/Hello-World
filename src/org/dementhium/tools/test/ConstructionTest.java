package org.dementhium.tools.test;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 * @author Steve <golden_32@live.com>
 * @author Lumby <lumbyjr@hotmail.com>
 * @author Emperor <black_dragon_686@hotmail.com>
 */
public class ConstructionTest {

	private static final ConstructionTile[] PLAYER_OWNED_HOUSE = new ConstructionTile[24];
	
	public static void init() {
		int centerX = 6400;
		int centerY = 6400;
		int houseIndex = 0;
		for (int y = centerY - 16; y < centerY + 16; y += 8) {
			for (int x = centerX - 16; x < centerX + 16; x += 8) {
				ConstructionTile t = new ConstructionTile();
				t.setIndex(houseIndex);
				t.setxMod(x - centerX);
				t.setyMod(y - centerY);
				PLAYER_OWNED_HOUSE[houseIndex++] = t;
			}
		}
	}
	
	public static void load() {
		int centerX = 6400; //Load this.
		int centerY = 6400; //Load this.
		List<ConstructionTile> tiles = new ArrayList<ConstructionTile>();
		/*
		 * Load tiles to the list.
		 */
		for (ConstructionTile t : tiles) {
			PLAYER_OWNED_HOUSE[t.getIndex()] = t;
		}
		//Send the dynamic region crap.
	}
}
