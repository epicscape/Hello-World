package org.dementhium.content.activity.impl.puropuro;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents an impling.
 * @author Emperor
 *
 */
public enum Impling {

	/**
	 * Represents a baby impling.
	 */
	BABY_IMPLING(1028, 17, 20.0, 11238, 7),

	/**
	 * Represents a young impling.
	 */
	YOUNG_IMPLING(1029, 22, 48.0, 11240, 7),
	
	/**
	 * Represents a gourmet impling.
	 */
	GOURMET_IMPLING(1030, 28, 82.0, 11242, 7),
	
	/**
	 * Represents an earth impling.
	 */
	EARTH_IMPLING(1031, 36, 126.0, 11244, 7),
	
	/**
	 * Represents an essence impling.
	 */
	ESSENCE_IMPLING(1032, 42, 160.0, 11246, 7),
	
	/**
	 * Represents an eclectic impling.
	 */
	ECLECTIC_IMPLING(1033, 50, 205.0, 11248, 7),
	
	/**
	 * Represents a spirit impling.
	 */
	//SPIRIT_IMPLING(7866, 54, 227.0, 15513, 7),
	
	/**
	 * Represents a nature impling.
	 */
	NATURE_IMPLING(1034, 58, 250.0, 11250, 7),
	
	/**
	 * Represents a magpie impling.
	 */
	MAGPIE_IMPLING(1035, 65, 289.0, 11252, 500),
	
	/**
	 * Represents a ninja impling.
	 */
	NINJA_IMPLING(6053, 74, 339.0, 11254, 2000),
	
	/**
	 * Represents a dragon impling.
	 */
	DRAGON_IMPLING(6054, 83, 390.0, 11256, 3000),
	
	/**
	 * Represents a kingly impling.
	 */
	KINGLY_IMPLING(7903, 91, 434.0, 15517, 5000);
	
	/**
	 * The mapping.
	 */
	private static final Map<Integer, Impling> IMPLINGS = new HashMap<Integer, Impling>();
	
	/**
	 * Populate the mapping.
	 */
	static {
		for (Impling impling : Impling.values()) {
			IMPLINGS.put(impling.getNpcId(), impling);
		}
	}
	
	/**
	 * Gets an impling instance from the mapping.
	 * @param id The npc id.
	 * @return The {@code Impling} instance, or {@code null} if the mapping didn't contain the npc id.
	 */
	public static Impling forId(int id) {
		return IMPLINGS.get(id);
	}
	
	/**
	 * The npc id.
	 */
	private final int npcId;
	
	/**
	 * The level required.
	 */
	private final int level;
	
	/**
	 * The experience gained when catching this impling.
	 */
	private final double experience;
	
	/**
	 * The impling jar received when catching this impling.
	 */
	private final int jar;
	
	/**
	 * The time it takes (in 600ms ticks) to respawn.
	 */
	private final int respawn;
	
	/**
	 * Constructs a new {@code Impling} {@code Object}.
	 * @param npcId The npc id.
	 * @param level The level required to catch this impling.
	 * @param experience The experience gained.
	 * @param jar The impling jar received.
	 * @param respawn The amount of delay before respawn (600 ms intervals).
	 */
	private Impling(int npcId, int level, double experience, int jar, int respawn) {
		this.npcId = npcId;
		this.level = level;
		this.experience = experience;
		this.jar = jar;
		this.respawn = respawn;
	}

	/**
	 * @return the npcId
	 */
	public int getNpcId() {
		return npcId;
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @return the experience
	 */
	public double getExperience() {
		return experience;
	}

	/**
	 * @return the jar
	 */
	public int getJar() {
		return jar;
	}

	/**
	 * @return the respawn
	 */
	public int getRespawn() {
		return respawn;
	}
}