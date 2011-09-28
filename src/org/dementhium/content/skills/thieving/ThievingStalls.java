package org.dementhium.content.skills.thieving;

import java.util.HashMap;
import java.util.Map;

import org.dementhium.model.Item;


/**
 * Holds data of all stalls you can steal from.
 * @author Emperor
 *
 */
public enum ThievingStalls {
	/*
	 * Stall	Level	Exp.	Items	Location	Respawn Time	E.P.S
	 * Vegetable stall	Miscellania, Etceteria	2 seconds
		Baker's stall East Ardougne, Keldagrim	2.5 seconds	
		Crafting stall Ape Atoll, Keldagrim	7 seconds	2.3
		Monkey Food stall Ape Atoll	7 seconds	2.3
		Monkey general store Ape Atoll	7 seconds	2.3
		Tea stall Varrock	7 seconds	2.3
		Silk stall East Ardougne, Keldagrim	8 seconds	3
		Wine stall Draynor Village	16 seconds	1.7
		Seed stall Draynor Village	11 seconds	0.9
		Fur stall East Ardougne, Rellekka	15 seconds	2.4
		Fish stall Rellekka, Miscellenia, Etceteria	16 seconds	1.7
		Crossbow stall Keldagrim	11 seconds	4.7
		Silver stall East Ardougne, Keldagrim	30 seconds	1.8
		Customs evidence files	63	75	Rune scimitar, uncut diamond, uncut ruby, uncut emerald, uncut sapphire, sharks, mithril bar, gold bar, iron bar, Pirate boots, Stripy pirate shirt, Eye patch, steel sword, steel scimitar, iron scimitar, bronze scimitar, clue scroll, 300 coins	Rock Island Prison	30–60 seconds	1.3-2.5
		Spice stall East Ardougne	80 seconds	1
		Magic stall Ape Atoll	80 seconds	1.3
		Scimitar stall Ape Atoll	80 seconds	1.3
		Gem stall East Ardougne, Keldagrim	180 seconds	0.1

	 */
	VEGETABLE_STALL(new int[] { }, new int[] { }, 2, 10.0, 3, new Item(1957), new Item(1965), new Item(1942), new Item(1982), new Item(1550)),
	BAKER_STALL(new int[] { 34384 }, new int[] { 34381 }, 5, 16.0, 4, new Item(1891), new Item(1901), new Item(2309)),
	CRAFTING_STALL(new int[] { }, new int[] { }, 5, 16.0, 12, new Item(1755), new Item(1592), new Item(1597)),
	MONKEY_FOOD_STALL(new int[] { }, new int[] { }, 5, 16.0, 12, new Item(1963)),
	MONKEY_GENERAL_STORE(new int[] { }, new int[] { }, 5, 16.0, 12, new Item(1931), new Item(2347), new Item(590)),
	TEA_STALL(new int[] { }, new int[] { }, 5, 16.0, 12, new Item(712)),
	SILK_STALL(new int[] { 34383 }, new int[] { 34381 }, 20, 24.0, 13, new Item(950)),
	WINE_STALL(new int[] { }, new int[] { }, 22, 27.0, 27, new Item(1937), new Item(1993), new Item(1987), new Item(1935), new Item(7919)),
	SEED_STALL(new int[] { }, new int[] { }, 27, 10.0, 18, new Item(5096), new Item(5097), new Item(5098), new Item(5099), new Item(5100), new Item(5101),
            new Item(5102), new Item(5103), new Item(5104), new Item(5105), new Item(5106), new Item(5291),
            new Item(5292), new Item(5293), new Item(5294), new Item(5295), new Item(5296), new Item(5297),
            new Item(5298), new Item(5299), new Item(5300), new Item(5301), new Item(5302), new Item(5304),
            new Item(5305), new Item(5306), new Item(5307), new Item(5308), new Item(5309), new Item(5310),
            new Item(5311), new Item(5312), new Item(5318), new Item(5319), new Item(5320), new Item(5321),
            new Item(5322), new Item(5323), new Item(5324)),
    FUR_STALL(new int[] { 34387 }, new int[] { 34381 }, 35, 36.0, 25, new Item(958), new Item(6814)),
	FISH_STALL(new int[] { }, new int[] { }, 42, 42.0, 27, new Item(331), new Item(359), new Item(377)),
	CROSSBOW_STALL(new int[] { }, new int[] { }, 49, 52.0, 18, new Item(877, 3), new Item(9420), new Item(9440)),
	SILVER_STALL(new int[] { 34382}, new int[] { 34381 }, 50, 54.0, 50, new Item(442)),
	//TODO: Fuck this one for a while.. CUSTOMS_EVIDENCE_FILES(new int[] { }, new int[] { }, 63, 75, 60),
	SPICE_STALL(new int[] { 34386 }, new int[] { 34381 }, 65, 81.0, 133, new Item(2007)),
	MAGIC_STALL(new int[] { }, new int[] { }, 65, 100.0, 133, new Item(554), new Item(555), new Item(556), new Item(557), new Item(563)),
	SCIMITAR_STALL(new int[] { }, new int[] { }, 65, 100.0, 133, new Item(1323)),
	GEM_STALL(new int[] { 34385 }, new int[] { 34381 }, 75, 160.0, 300, new Item(1617), new Item(1619), new Item(1621), new Item(1623)),
	;
	  
	/**
     * A hashmap containing all the ThievingStalls data.
     */
    private static final Map<Integer, ThievingStalls> THIEVING_STALLS = new HashMap<Integer, ThievingStalls>();

    /**
     * Gets the ThievingStalls data from the mapping, depending on the NPC id.
     *
     * @param id The npc id.
     * @return The {@code ThievingStalls} {@code Object}, or {@code null} if the data was non-existant.
     */
    public static ThievingStalls get(int id) {
        return THIEVING_STALLS.get(id);
    }

    /**
     * Populate the mapping.
     */
    static {
        for (ThievingStalls data : ThievingStalls.values()) {
            for (int id : data.objectIds) {
                THIEVING_STALLS.put(id, data);
            }
        }
    }
    
    /**
     * The object ids.
     */
    private final int[] objectIds;
    
    /**
     * The object ids to replace with.
     */
    private final int[] replaceIds;
    
    /**
     * The thieving level required.
     */
	private final int thievingLevel;
	
	/**
	 * The experience gained.
	 */
	private final double experience;
	
	/**
	 * The possible loot received.
	 */
	private final Item[] loot;
	
	/**
	 * The restore delay.
	 */
	private final int restoreDelay;
	
	/**
	 * Constructs a new {@code ThievingStalls) {@code Object}.
	 * @param objectIds The object ids.
	 * @param replaceIds The object ids to replace with.
	 * @param thievingLevel The thieving level required.
	 * @param experience The experience gained.
	 * @param items The possible loot.
	 */
	private ThievingStalls(int[] objectIds, int[] replaceIds, int thievingLevel, double experience, int restoreDelay, Item...items) {
		this.objectIds = objectIds;
		this.replaceIds = replaceIds;
		this.thievingLevel = thievingLevel;
		this.experience = experience;
		this.restoreDelay = restoreDelay;
		this.loot = items;
	}

	/**
	 * @return the objectIds
	 */
	public int[] getObjectIds() {
		return objectIds;
	}

	/**
	 * @return the replaceIds
	 */
	public int[] getReplaceIds() {
		return replaceIds;
	}

	/**
	 * @return the thievingLevel
	 */
	public int getThievingLevel() {
		return thievingLevel;
	}

	/**
	 * @return the experience
	 */
	public double getExperience() {
		return experience;
	}

	/**
	 * @return the loot
	 */
	public Item[] getLoot() {
		return loot;
	}

	/**
	 * @return the restoreDelay
	 */
	public int getRestoreDelay() {
		return restoreDelay;
	}

    
}