package org.dementhium.content.activity.impl.puropuro;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.dementhium.content.misc.ChanceItem;
import org.dementhium.model.Item;

/**
 * Represents an impling jar.
 * @author Emperor
 *
 */
public enum ImplingJar {

	/**
	 * Represents a baby impling jar.
	 */
	BABY_IMPlING_JAR(11238, new ChanceItem(new Item(1755), 40), 
			new ChanceItem(new Item(1734), 38), new ChanceItem(new Item(1733), 36), 
			new ChanceItem(new Item(946), 42), new ChanceItem(new Item(1985), 40), 
			new ChanceItem(new Item(2347), 37), new ChanceItem(new Item(1759), 43), 
			new ChanceItem(new Item(1927), 20), new ChanceItem(new Item(319), 18), 
			new ChanceItem(new Item(2007), 21), new ChanceItem(new Item(1779), 20), 
			new ChanceItem(new Item(7170), 18), new ChanceItem(new Item(401), 16), 
			new ChanceItem(new Item(1438), 20), new ChanceItem(new Item(2355), 10), 
			new ChanceItem(new Item(1607), 9), new ChanceItem(new Item(1743), 11), 
			new ChanceItem(new Item(379), 10)),
			
	/**
	 * Represents a young impling jar.
	 */
	YOUNG_IMPlING_JAR(11240, new ChanceItem(new Item(855), 40), 
			new ChanceItem(new Item(361), 38), new ChanceItem(new Item(1901), 36), 
			new ChanceItem(new Item(1539, 5), 42), new ChanceItem(new Item(1784, 4), 40), 
			new ChanceItem(new Item(1523), 37), new ChanceItem(new Item(7936), 43), 
			new ChanceItem(new Item(5970), 41), new ChanceItem(new Item(1353), 18), 
			new ChanceItem(new Item(2293), 21), new ChanceItem(new Item(7178), 20), 
			new ChanceItem(new Item(247), 18), new ChanceItem(new Item(453), 16), 
			new ChanceItem(new Item(1777), 20), new ChanceItem(new Item(231), 19), 
			new ChanceItem(new Item(1761), 18), new ChanceItem(new Item(1097), 11), 
			new ChanceItem(new Item(1157), 10), new ChanceItem(new Item(8778), 8), 
			new ChanceItem(new Item(133), 9)),
			
	/**
	 * Represents a gourmet impling jar.
	 */
	GOURMET_IMPlING_JAR(11242, new ChanceItem(new Item(365), 40), 
			new ChanceItem(new Item(361), 38), new ChanceItem(new Item(2011), 36), 
			new ChanceItem(new Item(2327, 5), 42), new ChanceItem(new Item(1897, 4), 40), 
			new ChanceItem(new Item(2293), 37), new ChanceItem(new Item(5004), 43), 
			new ChanceItem(new Item(2007), 41), new ChanceItem(new Item(5970), 38), 
			new ChanceItem(new Item(1883), 21), new ChanceItem(new Item(247), 20), 
			new ChanceItem(new Item(380, 4), 18), new ChanceItem(new Item(386, 3), 16), 
			new ChanceItem(new Item(7170), 20), new ChanceItem(new Item(5755), 19), 
			new ChanceItem(new Item(7178), 18), new ChanceItem(new Item(7179), 19), 
			new ChanceItem(new Item(3145, 2), 17), new ChanceItem(new Item(3143, 2), 20), 
			new ChanceItem(new Item(10137, 5), 9), new ChanceItem(new Item(7179, 6), 8), 
			new ChanceItem(new Item(374, 3), 10), new ChanceItem(new Item(10136), 9)),
			
	/**
	 * Represents an earth impling jar.
	 */
	EARTH_IMPlING_JAR(11244, new ChanceItem(new Item(557, 32), 40), 
			new ChanceItem(new Item(5535), 38), new ChanceItem(new Item(6033, 6), 36), 
			new ChanceItem(new Item(1440, 5), 42), new ChanceItem(new Item(5104, 2), 40), 
			new ChanceItem(new Item(2353), 17), new ChanceItem(new Item(444), 23), 
			new ChanceItem(new Item(237), 18), new ChanceItem(new Item(453, 6), 17), 
			new ChanceItem(new Item(1784, 4), 21), new ChanceItem(new Item(1273), 20), 
			new ChanceItem(new Item(5311, 2), 18), new ChanceItem(new Item(5294, 2), 16), 
			new ChanceItem(new Item(447), 20), new ChanceItem(new Item(448, 3), 9), 
			new ChanceItem(new Item(6035, 2), 8), new ChanceItem(new Item(1606, 2), 9), 
			new ChanceItem(new Item(1442), 7), new ChanceItem(new Item(1603), 3), 
			new ChanceItem(new Item(5303), 2)),
			
	/**
	 * Represents an essence impling jar.
	 */
	ESSENCE_IMPlING_JAR(11246, new ChanceItem(new Item(562, 4), 40), 
			new ChanceItem(new Item(555, 13), 38), new ChanceItem(new Item(555, 30), 29), 
			new ChanceItem(new Item(558, 25), 37), new ChanceItem(new Item(556, 25), 40), 
			new ChanceItem(new Item(556, 30), 37), new ChanceItem(new Item(556, 60), 27), 
			new ChanceItem(new Item(559, 28), 36), new ChanceItem(new Item(1448), 38), 
			new ChanceItem(new Item(7937, 20), 39), new ChanceItem(new Item(7937, 35), 30), 
			new ChanceItem(new Item(1437, 20), 43), new ChanceItem(new Item(564, 4), 21), 
			new ChanceItem(new Item(4695, 4), 19), new ChanceItem(new Item(4696, 4), 39), 
			new ChanceItem(new Item(4698, 4), 16), new ChanceItem(new Item(4694, 4), 29), 
			new ChanceItem(new Item(4699, 4), 30), new ChanceItem(new Item(4697, 4), 32), 
			new ChanceItem(new Item(565, 7), 12), new ChanceItem(new Item(566, 11), 5), 
			new ChanceItem(new Item(563, 13), 6), new ChanceItem(new Item(560, 13), 9), 
			new ChanceItem(new Item(561, 13), 19), new ChanceItem(new Item(1442), 8), 
			new ChanceItem(new Item(1778, 92), 2)),
			
	/**
	 * Represents an eclectic impling jar.
	 */
	ECLECTIC_IMPlING_JAR(11248, new ChanceItem(new Item(1391), 40), 
			new ChanceItem(new Item(1273), 38), new ChanceItem(new Item(5970), 36), 
			new ChanceItem(new Item(231), 42), new ChanceItem(new Item(556, 26), 40), 
			new ChanceItem(new Item(2358, 5), 17), new ChanceItem(new Item(2494), 10), 
			new ChanceItem(new Item(10083), 8), new ChanceItem(new Item(1213), 7), 
			new ChanceItem(new Item(450, 10), 10), new ChanceItem(new Item(7208), 10), 
			new ChanceItem(new Item(1601), 3), new ChanceItem(new Item(1199), 12), 
			new ChanceItem(new Item(444), 15), new ChanceItem(new Item(7937, 20), 39), 
			new ChanceItem(new Item(7937, 35), 25), new ChanceItem(new Item(237), 13), 
			new ChanceItem(new Item(5760, 2), 40), new ChanceItem(new Item(8779, 4), 23), 
			new ChanceItem(new Item(5321, 3), 5)),
			
	/**
	 * Represents a spirit impling jar.
	 */
	SPIRIT_IMPlING_JAR(15513),
	
	/**
	* Represents a nature impling jar.
	*/
	NATURE_IMPlING_JAR(11250, new ChanceItem(new Item(5100), 40), 
		new ChanceItem(new Item(5104), 38), new ChanceItem(new Item(5281), 36), 
		new ChanceItem(new Item(5294), 42), new ChanceItem(new Item(5295), 40), 
		new ChanceItem(new Item(5297), 41), new ChanceItem(new Item(5299), 39), 
		new ChanceItem(new Item(5298, 5), 37), new ChanceItem(new Item(5303), 38), 
		new ChanceItem(new Item(254, 4), 36), new ChanceItem(new Item(5313), 20), 
		new ChanceItem(new Item(5286), 18), new ChanceItem(new Item(5285), 19), 
		new ChanceItem(new Item(3000), 21), new ChanceItem(new Item(5974), 19), 
		new ChanceItem(new Item(5304), 10), new ChanceItem(new Item(270, 2), 9), 
		new ChanceItem(new Item(1513), 8)),
		
	/**
	 * Represents a magpie impling jar.
	 */
	MAGPIE_IMPlING_JAR(11252, new ChanceItem(new Item(1347), 32), 
		new ChanceItem(new Item(1682, 3), 38), new ChanceItem(new Item(1732, 3), 36), 
		new ChanceItem(new Item(2569, 3), 42), new ChanceItem(new Item(2571, 5), 17), 
		new ChanceItem(new Item(4097), 41), new ChanceItem(new Item(4095), 39), 
		new ChanceItem(new Item(1215), 32), new ChanceItem(new Item(3391), 38), 
		new ChanceItem(new Item(1333), 36), new ChanceItem(new Item(1185), 20), 
		new ChanceItem(new Item(5541), 48), new ChanceItem(new Item(1748, 6), 19), 
		new ChanceItem(new Item(2364, 2), 21), new ChanceItem(new Item(1602, 4), 19), 
		new ChanceItem(new Item(985), 20), new ChanceItem(new Item(987), 19), 
		new ChanceItem(new Item(5287), 16), new ChanceItem(new Item(5300), 12), 
		new ChanceItem(new Item(993), 2), new ChanceItem(new Item(1603), 42)),
		
	/**
	 * Represents a ninja impling jar.
	 */
	NINJA_IMPlING_JAR(11254, new ChanceItem(new Item(1113), 32), 
		new ChanceItem(new Item(6328), 38), new ChanceItem(new Item(3385), 36), 
		new ChanceItem(new Item(3391), 42), new ChanceItem(new Item(4097), 43), 
		new ChanceItem(new Item(4095), 41), new ChanceItem(new Item(3101), 39), 
		new ChanceItem(new Item(1333), 32), new ChanceItem(new Item(1347), 38), 
		new ChanceItem(new Item(1215), 36), new ChanceItem(new Item(5680), 20), 
		new ChanceItem(new Item(5698), 15), new ChanceItem(new Item(6313), 49), 
		new ChanceItem(new Item(892, 70), 32), new ChanceItem(new Item(811, 70), 27), 
		new ChanceItem(new Item(868, 40), 28), new ChanceItem(new Item(805, 50), 39), 
		new ChanceItem(new Item(9342, 2), 16), new ChanceItem(new Item(9194, 4), 12), 
		new ChanceItem(new Item(1748, 16), 3), new ChanceItem(new Item(6156, 3), 42), 
		new ChanceItem(new Item(140, 4), 43), new ChanceItem(new Item(5938, 4), 42), 
		new ChanceItem(new Item(2364, 4), 4)),
		
	/**
	 * Represents a dragon impling jar.
	 */
	DRAGON_IMPlING_JAR(11256, new ChanceItem(new Item(11212, 100 + new Random().nextInt(401)), 35), 
		new ChanceItem(new Item(9244, 3 + new Random().nextInt(38)), 40), 
		new ChanceItem(new Item(1305), 35), 
		new ChanceItem(new Item(11237, 100 + new Random().nextInt(401)), 42), 
		new ChanceItem(new Item(9193, 10 + new Random().nextInt(40)), 36), 
		new ChanceItem(new Item(535, 111 + new Random().nextInt(175)), 36), 
		new ChanceItem(new Item(4107), 43), new ChanceItem(new Item(4117), 39), 
		new ChanceItem(new Item(11230, 105 + new Random().nextInt(246)), 22), 
		new ChanceItem(new Item(11232, 105 + new Random().nextInt(246)), 21), 
		new ChanceItem(new Item(5316), 19), 
		new ChanceItem(new Item(537, 52 + new Random().nextInt(4)), 21), 
		new ChanceItem(new Item(1616, 5), 18), new ChanceItem(new Item(4099), 12), 
		new ChanceItem(new Item(4109), 11), new ChanceItem(new Item(4105), 10), 
		new ChanceItem(new Item(4115), 9), new ChanceItem(new Item(1713, 3), 10), 
		new ChanceItem(new Item(3000, 6), 12), new ChanceItem(new Item(5681, 3), 7), 
		new ChanceItem(new Item(4101), 4), new ChanceItem(new Item(4111), 3), 
		new ChanceItem(new Item(4103), 5), new ChanceItem(new Item(4113), 4), 
		new ChanceItem(new Item(5547), 5), new ChanceItem(new Item(1684, 3), 4), 
		new ChanceItem(new Item(7219, 5 + new Random().nextInt(11)), 5)), 
		
	/**
	 * Represents a kingly impling jar.
	 */
	KINGLY_IMPlING_JAR(15517, new ChanceItem(new Item(11212, 44 + new Random().nextInt(101)), 35), 
		new ChanceItem(new Item(11237, 64 + new Random().nextInt(85)), 40), 
		new ChanceItem(new Item(1306, 2), 35), 
		new ChanceItem(new Item(11230, 192 + new Random().nextInt(128)), 42), 
		new ChanceItem(new Item(11232, 213 + new Random().nextInt(132)), 36), 
		new ChanceItem(new Item(1249), 46), 
		new ChanceItem(new Item(7158), 41), new ChanceItem(new Item(2366), 39), 
		new ChanceItem(new Item(1616, 6), 49), 
		new ChanceItem(new Item(9341, 40 + new Random().nextInt(31)), 57), 
		new ChanceItem(new Item(1705, 3 + new Random().nextInt(9)), 52), 
		new ChanceItem(new Item(1703, 3), 57), 
		new ChanceItem(new Item(2364, 10), 58), new ChanceItem(new Item(990, 2), 52), 
		new ChanceItem(new Item(9342, 57), 23), new ChanceItem(new Item(15503), 10), 
		new ChanceItem(new Item(15505), 9), new ChanceItem(new Item(15507), 10), 
		new ChanceItem(new Item(15509), 12), new ChanceItem(new Item(15511), 7), 
		new ChanceItem(new Item(1632, 5), 9), new ChanceItem(new Item(9194, 1 + new Random().nextInt(73)), 13), 
		new ChanceItem(new Item(6571), 1));

	/**
	 * The mapping.
	 */
	private static final Map<Integer, ImplingJar> IMPLING_JARS = new HashMap<Integer, ImplingJar>();
	
	/**
	 * Populate the mapping.
	 */
	static {
		for (ImplingJar implingJar : ImplingJar.values()) {
			IMPLING_JARS.put(implingJar.getItemId(), implingJar);
		}
	}
	
	/**
	 * Gets an impling jar instance from the mapping.
	 * @param id The item id.
	 * @return The {@code ImplingJar} instance, or {@code null} if the mapping didn't contain the item id.
	 */
	public static ImplingJar forId(int id) {
		return IMPLING_JARS.get(id);
	}
	/**
	 * The item id.
	 */
	private final int itemId;
	
	/**
	 * The loot.
	 */
	private final ChanceItem[] loot;
	
	/**
	 * Constructs a new {@code ImplingJar} {@code Object}.
	 * @param itemId The item id.
	 * @param loot The possible loot.
	 */
	private ImplingJar(int itemId, ChanceItem...loot) {
		this.itemId = itemId;
		this.loot = loot;
	}

	/**
	 * @return the itemId
	 */
	public int getItemId() {
		return itemId;
	}

	/**
	 * @return the loot
	 */
	public ChanceItem[] getLoot() {
		return loot;
	}
}