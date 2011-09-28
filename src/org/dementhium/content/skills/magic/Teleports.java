package org.dementhium.content.skills.magic;

import org.dementhium.model.Item;
import org.dementhium.model.Location;
import org.dementhium.model.Mob;

import java.util.HashMap;
import java.util.Map;

/**
 * Holds all possible teleports.
 *
 * @author Emperor
 */
public enum Teleports {

    /**
     * The modern Mobilising armies teleport.
     */
    MOBILISING_ARMIES(0, 37, Location.locate(2411, 2832, 0), 10, 19.0, new Item(563, 1), new Item(555, 1), new Item(556, 1)),

    /**
     * The modern Varrock teleport.
     */
    VARROCK(0, 40, Location.locate(3211, 3422, 0), 25, 35.0, new Item(563, 1), new Item(554, 1), new Item(556, 3)),

    /**
     * The modern Lumbridge teleport.
     */
    LUMBRIDGE(0, 43, Location.locate(3221, 3218, 0), 31, 41.0, new Item(563, 1), new Item(557, 1), new Item(556, 3)),

    /**
     * The modern Falador teleport.
     */
    FALADOR(0, 46, Location.locate(2966, 3379, 0), 37, 48.0, new Item(563, 1), new Item(555, 1), new Item(556, 3)),

    /**
     * The modern House teleport.
     */
    HOUSE(0, 48, Mob.DEFAULT, 40, 30.0, new Item(563, 1), new Item(557, 1), new Item(556, 1)),

    /**
     * The modern Camelot teleport.
     */
    CAMELOT(0, 51, Location.locate(2757, 3477, 0), 45, 55.5, new Item(563, 1), new Item(556, 5)),

    /**
     * The modern Ardougne teleport.
     */
    ARDOUGNE(0, 57, Location.locate(2662, 3307, 0), 51, 61.0, new Item(563, 2), new Item(555, 2)),

    /**
     * The modern Watchtower teleport.
     */
    WATCHTOWER(0, 62, Location.locate(2547, 3113, 2), 58, 68.0, new Item(563, 2), new Item(557, 2)),

    /**
     * The modern Trollheim teleport.
     */
    TROLLHEIM(0, 69, Location.locate(2891, 3678, 0), 61, 68.0, new Item(563, 2), new Item(554, 2)),

    /**
     * The modern Ape atoll teleport.
     */
    APE_ATOLL(0, 72, Location.locate(2797, 2798, 1), 64, 76.0, new Item(563, 2), new Item(554, 2), new Item(555, 2), new Item(1963)),

    /**
     * The ancient Paddewwa teleport.
     */
    PADDEWWA(1, 40, Location.locate(3096, 9880, 0), 54, 64.0, new Item(563, 2), new Item(554, 1), new Item(556, 1)),

    /**
     * The ancient Senntisten teleport.
     */
    SENNTISTEN(1, 41, Location.locate(3323, 3334, 0), 60, 70.0, new Item(563, 2), new Item(566, 1)),

    /**
     * The ancient Kharyrll teleport.
     */
    KHARYRLL(1, 42, Location.locate(3496, 3473, 0), 66, 76.0, new Item(563, 2), new Item(565, 1)),

    /**
     * The ancient Lassar teleport.
     */
    LASSAR(1, 43, Location.locate(3006, 3472, 0), 72, 82.0, new Item(563, 2), new Item(555, 4)),

    /**
     * The ancient Dareeyak teleport.
     */
    DAREEYAK(1, 44, Location.locate(2966, 3698, 0), 78, 88.0, new Item(563, 2), new Item(554, 3), new Item(556, 2)),

    /**
     * The ancient Carrallangar teleport.
     */
    CARRALLANGAR(1, 45, Location.locate(3222, 3668, 0), 84, 94.0, new Item(563, 2), new Item(566, 2)),

    /**
     * The ancient Annakarl teleport.
     */
    ANNAKARL(1, 46, Location.locate(3286, 3885, 0), 90, 100.0, new Item(563, 2), new Item(565, 2)),

    /**
     * The ancient Ghorrock teleport.
     */
    GHORROCK(1, 47, Location.locate(2976, 3874, 0), 96, 106.0, new Item(563, 2), new Item(555, 8)),

    /**
     * The lunar Moonclan teleport.
     */
    MOONCLAN(2, 42, Mob.DEFAULT /*Location.locate(2112, 3914, 0)*/, 69, 79, new Item(563), new Item(9075, 2), new Item(557, 2)),

    /**
     * The lunar Ourania teleport.
     */
    OURANIA(2, 53, Location.locate(2467, 3244, 0), 71, 81, new Item(563), new Item(9075, 2), new Item(557, 6)),

    /**
     * The lunar Waterbirth teleport.
     */
    WATERBIRTH(2, 46, Mob.DEFAULT/*Location.locate(2546, 3754, 0)*/, 72, 82, new Item(563), new Item(9075, 2), new Item(555)),

    /**
     * The lunar Barbarian teleport.
     */
    BARBARIAN(2, 22, Location.locate(2543, 3570, 0), 75, 85, new Item(563, 2), new Item(9075, 2), new Item(554, 3)),

    /**
     * The lunar Khazard teleport.
     */
    KHAZARD(2, 40, Location.locate(2638, 3167, 0), 78, 88, new Item(563, 2), new Item(9075, 2), new Item(555, 4)),

    /**
     * The lunar Fishing guild teleport.
     */
    FISHING_GUILD(2, 39, Location.locate(2612, 3382, 0), 85, 95, new Item(563, 3), new Item(9075, 3), new Item(555, 10)),

    /**
     * The lunar Catherby teleport.
     */
    CATHERBY(2, 43, Location.locate(2802, 3450, 0), 87, 97, new Item(563, 3), new Item(9075, 3), new Item(555, 10)),

    /**
     * The lunar Ice plateau teleport.
     */
    ICE_PLATEAU(2, 50, Mob.DEFAULT/*Location.locate(2976, 3937, 0)*/, 89, 99, new Item(563, 3), new Item(9075, 3), new Item(555, 8));

    /**
     * The modern spellbook teleports.
     */
    private static final Map<Integer, Teleports> MODERN = new HashMap<Integer, Teleports>();

    /**
     * The ancient spellbook teleports.
     */
    private static final Map<Integer, Teleports> ANCIENT = new HashMap<Integer, Teleports>();

    /**
     * The lunar spellbook teleports.
     */
    private static final Map<Integer, Teleports> LUNAR = new HashMap<Integer, Teleports>();

    /**
     * Populate the teleports lists.
     */
    static {
        Teleports currentTele;
        for (int i = 0; i < 10; i++) {
            currentTele = Teleports.values()[i];
            MODERN.put(currentTele.spellId, currentTele);
        }
        for (int i = 10; i < 18; i++) {
            currentTele = Teleports.values()[i];
            ANCIENT.put(currentTele.spellId, currentTele);
        }
        for (int i = 18; i < 26; i++) {
            currentTele = Teleports.values()[i];
            LUNAR.put(currentTele.spellId, currentTele);
        }
    }

    /**
     * Gets a teleport.
     *
     * @param spellbook The spellbook.
     * @param spellId   The spell id.
     * @return The Teleport, or null if the teleport wasn't added.
     */
    public static Teleports get(int spellbook, int spellId) {
        if (spellbook == 192) {
            return MODERN.get(spellId);
        } else if (spellbook == 193) {
            return ANCIENT.get(spellId);
        } else if (spellbook == 430) {
            return LUNAR.get(spellId);
        }
        return null;
    }

    /**
     * The spellbook (0: modern, 1: ancient, 2: lunar, 3: daemonheim).
     */
    private final int spellbook;

    /**
     * The spell id.
     */
    private final int spellId;

    /**
     * The destination.
     */
    private final Location destination;

    /**
     * The level required.
     */
    private final int level;

    /**
     * The experience gained.
     */
    private final double experience;

    /**
     * The runes required.
     */
    private final Item[] runes;

    /**
     * Constructs a new {@code Teleports} {@code Object}.
     *
     * @param spellbook   The spellbook (0: modern, 1: ancient, 2: lunar, 3: daemonheim).
     * @param spellId     The spell id.
     * @param destination The destination.
     * @param level       The level required.
     * @param experience  The experience gained;
     * @param runes       The runes required.
     */
    private Teleports(int spellbook, int spellId, Location destination, int level, double experience, Item... runes) {
        this.spellbook = spellbook;
        this.spellId = spellId;
        this.destination = destination;
        this.level = level;
        this.experience = experience;
        this.runes = runes;
    }

    /**
     * @return the spellbook
     */
    public int getSpellbook() {
        return spellbook;
    }

    /**
     * @return the spellId
     */
    public int getSpellId() {
        return spellId;
    }

    /**
     * @return the destination
     */
    public Location getDestination() {
        return destination;
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
     * @return the runes
     */
    public Item[] getRunes() {
        return runes;
    }
}