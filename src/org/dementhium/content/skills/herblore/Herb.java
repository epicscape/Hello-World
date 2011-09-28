package org.dementhium.content.skills.herblore;

import org.dementhium.model.Item;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a herb used for {@code Herblore}.
 *
 * @author Emperor
 */
public enum Herb {

    /**
     * Guam leaf.
     */
    GUAM(199, new Item(249, 1), 1, 2.5),

    /**
     * Marrentill leaf.
     */
    MARRENTILL(201, new Item(251, 1), 5, 3.8),

    /**
     * Tarromin leaf.
     */
    TARROMIN(203, new Item(253, 1), 11, 5),

    /**
     * Harralander leaf.
     */
    HARRALANDER(205, new Item(255, 1), 20, 6.3),

    /**
     * Ranarr leaf.
     */
    RANARR(207, new Item(257, 1), 25, 7.5),

    /**
     * Toadflax leaf.
     */
    TOADFLAX(3049, new Item(2998, 1), 30, 8),

    /**
     * Spirit weed.
     */
    SPIRIT_WEED(12172, new Item(12174, 1), 35, 7.8),

    /**
     * Irit leaf.
     */
    IRIT(209, new Item(259, 1), 40, 8.8),

    /**
     * Wergali leaf.
     */
    WERGALI(14836, new Item(14854, 1), 41, 9.5),

    /**
     * Avantoe leaf.
     */
    AVANTOE(211, new Item(261, 1), 48, 11.5),

    /**
     * Kwuarm leaf.
     */
    KWUARM(213, new Item(263, 1), 54, 11.3),

    /**
     * Snapdragon leaf.
     */
    SNAPDRAGON(3051, new Item(3000, 1), 59, 11.8),

    /**
     * Cadantine leaf.
     */
    CADANTINE(215, new Item(265, 1), 65, 12.5),

    /**
     * Lantadyme leaf.
     */
    LANTADYME(2485, new Item(2481, 1), 67, 13.1),

    /**
     * Dwarf Weed leaf.
     */
    DWARF_WEED(217, new Item(267, 1), 70, 13.8),

    /**
     * Torstol leaf.
     */
    TORSTOL(219, new Item(269, 1), 75, 15);

    /**
     * The id.
     */
    private final short id;

    /**
     * The reward.
     */
    private final Item reward;

    /**
     * The level required to clean the herb.
     */
    private final byte level;

    /**
     * The experience gained upon cleaning.
     */
    private final double experience;

    /**
     * A map of object ids to primary ingredients.
     */
    private static Map<Short, Herb> herbs = new HashMap<Short, Herb>();

    /**
     * Gets a herb by an item id.
     *
     * @param item The item id.
     * @return The herbor <code>null</code> if the object is not a herb.
     */
    public static Herb forId(int item) {
        return herbs.get((short) item);
    }

    /**
     * Populates the herb mapping.
     */
    static {
        for (Herb ingredient : Herb.values()) {
            herbs.put(ingredient.id, ingredient);
        }
    }

    /**
     * Constructs a new {@code Herb} {@code Object}.
     *
     * @param id         The grimy herb id.
     * @param reward     The clean herb id.
     * @param level      The level required to clean.
     * @param experience The experience gained.
     */
    private Herb(int id, Item reward, int level, double experience) {
        this.id = (short) id;
        this.reward = reward;
        this.level = (byte) level;
        this.experience = experience;
    }

    /**
     * Gets the id.
     *
     * @return The id.
     */
    public int getId() {
        return id;
    }

    /**
     * Gets the required level.
     *
     * @return The required level.
     */
    public int getLevel() {
        return level;
    }

    /**
     * Gets the reward.
     *
     * @return The reward.
     */
    public Item getReward() {
        return reward;
    }

    /**
     * Gets the experience amount.
     *
     * @return The amount of experience upon cleaning.
     */
    public double getExperience() {
        return experience;
    }
}
