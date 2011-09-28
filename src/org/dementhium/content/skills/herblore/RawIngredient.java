package org.dementhium.content.skills.herblore;

import org.dementhium.model.Item;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents raw ingredients to be crushed with a pestle and mortar.
 *
 * @author Emperor
 */
public enum RawIngredient {

    /**
     * Unicorn horn - Unicorn horn dust.
     */
    UNICORN_HORN(237, new Item(235, 1)),

    /**
     * Chocolate bar - Chocolate dust.
     */
    CHOCOLATE_BAR(1973, new Item(1975, 1)),

    /**
     * Kebbit teeth - Kebbit teeth dust.
     */
    KEBBIT_TEETH(10109, new Item(10111, 1)),

    /**
     * Gorak claw - Crushed gorak claw.
     */
    GORAK_CLAW(9016, new Item(9018, 1)),

    /**
     * Bird's nest - Crushed bird's nest.
     */
    BIRDS_NEST(5075, new Item(6693, 1)),

    /**
     * Desert goat horn - Goat horn dust.
     */
    DESERT_GOAT_HORN(9735, new Item(9736, 1)),

    /**
     * Blue dragon scales - Dragon scale dust.
     */
    BLUE_DRAGON_SCALES(243, new Item(241, 1)),

    /**
     * Spring sq'irk - Spring sq'irk juice.
     */
    SPRING_SQ_IRK(10844, new Item(10848, 1)),

    /**
     * Summer sq'irk - Summer sq'irk juice.
     */
    SUMMER_SQ_IRK(10845, new Item(10849, 1)),

    /**
     * Autumn sq'irk - Autumn sq'irk juice.
     */
    AUTUMN_SQ_IRK(10846, new Item(10850, 1)),

    /**
     * Winter sq'irk - Winter sq'irk juice.
     */
    WINTER_SQ_IRK(10847, new Item(10851, 1)),

    /**
     * Charcoal - Ground charcoal.
     */
    CHARCOAL(973, new Item(704, 1)),

    /**
     * Rune shards - Rune dust.
     */
    RUNE_SHARDS(6466, new Item(6467, 1)),

    /**
     * Ashes - Ground ashes.
     */
    ASHES(592, new Item(8865, 1)),

    /**
     * Poison karambwan - Karambwan paste.
     */
    POISON_KARAMBWAN(3146, new Item(3152, 1)),

    /**
     * Suqah tooth - Tooth dust.
     */
    SUQAH_TOOTH(9079, new Item(9082, 1)),

    /**
     * Fishing bait - Ground fishing bait.
     */
    FISHING_BAIT(313, new Item(12129, 1)),

    /**
     * Diamond root - Diamond root dust.
     */
    DIAMOND_ROOT(14703, new Item(14704, 1)),

    /**
     * Black mushroom - Black mushroom inkt.
     */
    BLACK_MUSHROOM(4620, new Item(4622, 1)),

    /**
     * Mud rune - Ground Mude rune.
     */
    MUD_RUNES(4698, new Item(9594, 1)),

    /**
     * Wyvern bones - Ground wyvern bones.
     */
    WYVERN_BONES(6812, new Item(6810, 1));

    /**
     * The mapping for the raw ingredients.
     */
    private static Map<Short, RawIngredient> rawIngredients = new HashMap<Short, RawIngredient>();

    /**
     * Gets a {@code RawIngredient} {@code Object} from the mapping.
     *
     * @param itemId The item id.
     * @return The {@code RawIngredient} {@code Object}.
     */
    public static RawIngredient forId(int itemId) {
        return rawIngredients.get((short) itemId);
    }

    /**
     * Populate the raw ingredients mapping.
     */
    static {
        for (RawIngredient rawIngredient : RawIngredient.values()) {
            rawIngredients.put(rawIngredient.rawId, rawIngredient);
        }
    }

    /**
     * The raw ingredient item id.
     */
    private final short rawId;

    /**
     * The crushed ingredient item.
     */
    private final Item crushedItem;

    /**
     * Constructs a new {@code RawIngredient} {@code Object}.
     *
     * @param rawId     The raw ingredient item id.
     * @param crushedId The crushed item id.
     */
    private RawIngredient(int rawId, Item crushedItem) {
        this.rawId = (short) rawId;
        this.crushedItem = crushedItem;
    }

    /**
     * Gets the raw ingredient item id.
     *
     * @return The item id.
     */
    public short getRawId() {
        return rawId;
    }

    /**
     * Gets the crushed ingredient item id.
     *
     * @return The item id.
     */
    public Item getCrushedItem() {
        return crushedItem;
    }
}
