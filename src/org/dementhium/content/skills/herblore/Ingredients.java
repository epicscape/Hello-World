package org.dementhium.content.skills.herblore;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents all possible ingredients.
 *
 * @author Emperor
 */
public enum Ingredients {

    /**
     * Guam leaf.
     */
    GUAM(249, new short[]{Herblore.VIAL, Herblore.SWAMP_TAR}, new short[]{
            91, 10142}, new byte[]{0, 19}, new double[]{0, 30}),

    /**
     * Marrentill leaf.
     */
    MARRENTILL(251, new short[]{Herblore.VIAL, Herblore.SWAMP_TAR},
            new short[]{93, 10143}, new byte[]{0, 31}, new double[]{0,
            42.5}),

    /**
     * Tarromin leaf.
     */
    TARROMIN(253, new short[]{Herblore.VIAL, Herblore.SWAMP_TAR},
            new short[]{95, 10144}, new byte[]{0, 39}, new double[]{0,
            55}),

    /**
     * Harralander leaf.
     */
    HARRALANDER(255, new short[]{Herblore.VIAL, Herblore.SWAMP_TAR},
            new short[]{97, 10145}, new byte[]{0, 44}, new double[]{0,
            72.5}),

    /**
     * Ranarr leaf.
     */
    RANARR(257, new short[]{Herblore.VIAL}, new short[]{99},
            new byte[]{0}, new double[]{0}),

    /**
     * Toadflax leaf.
     */
    TOADFLAX(2998, new short[]{Herblore.VIAL, Herblore.COCONUT_MILK},
            new short[]{3002, 5942}, new byte[]{0, 0}, new double[]{0,
            0}),

    /**
     * Spirit weed.
     */
    SPIRIT_WEED(12172, new short[]{Herblore.VIAL}, new short[]{12181},
            new byte[]{0}, new double[]{0}),

    /**
     * Wergali leaf.
     */
    WERGALI(14854, new short[]{Herblore.VIAL}, new short[]{14856},
            new byte[]{0}, new double[]{0}),

    /**
     * Irit leaf.
     */
    IRIT(259, new short[]{Herblore.VIAL, Herblore.COCONUT_MILK},
            new short[]{101, 5951}, new byte[]{0, 0},
            new double[]{0, 0}),

    /**
     * Avantoe leaf.
     */
    AVANTOE(261, new short[]{Herblore.VIAL, 2436, 145, 147, 149},
            new short[]{103, 15308, 15309, 15310, 15311}, new byte[]{0,
            88, 88, 88, 88}, new double[]{0, 220, 220, 220, 220}),

    /**
     * Kwuarm leaf.
     */
    KWUARM(263, new short[]{Herblore.VIAL}, new short[]{105},
            new byte[]{0}, new double[]{0}),

    /**
     * Starflower.
     */
    STARFLOWER(9017, new short[]{Herblore.VIAL}, new short[]{9019},
            new byte[]{0}, new double[]{0}),

    /**
     * Snapdragon leaf.
     */
    SNAPDRAGON(3000, new short[]{Herblore.VIAL}, new short[]{3004},
            new byte[]{0}, new double[]{0}),

    /**
     * Cadantine leaf.
     */
    CADANTINE(265, new short[]{Herblore.VIAL}, new short[]{107},
            new byte[]{0}, new double[]{0}),

    /**
     * Lantadyme leaf.
     */
    LANTADYME(2481, new short[]{Herblore.VIAL, 2442, 163, 165, 167},
            new short[]{2483, 15316, 15317, 15318, 15319}, new byte[]{0,
            90, 90, 90, 90}, new double[]{0, 240, 240, 240, 240}),

    /**
     * Dwarf weed.
     */
    DWARF_WEED(267, new short[]{Herblore.VIAL, 2440, 157, 159, 161},
            new short[]{109, 15312, 15313, 15314, 15315}, new byte[]{0,
            89, 89, 89, 89}, new double[]{0, 230, 230, 230, 230}),

    /**
     * Cactus spine.
     */
    CACTUS_SPINE(6016, new short[]{Herblore.COCONUT_MILK},
            new short[]{5936}, new byte[]{0}, new double[]{0}),

    /**
     * Torstol leaf.
     */
    TORSTOL(269, new short[]{Herblore.VIAL, 15309, 15313, 15317, 15321, 15325}, new short[]{111, 15333, 15333, 15333, 15333, 15333},
            new byte[]{0, 96, 96, 96, 96, 96}, new double[]{0, 1000, 1000, 1000, 1000, 1000}),

    /**
     * Cave nightshade.
     */
    CAVE_NIGHTSHADE(2398, new short[]{Herblore.COCONUT_MILK},
            new short[]{5939}, new byte[]{0}, new double[]{0}),

    /**
     * Eye of newt.
     */
    EYE_OF_NEWT(221, new short[]{91, 101}, new short[]{121, 145},
            new byte[]{0, 45}, new double[]{25, 100}),

    /**
     * Unicorn horn dust.
     */
    UNICORN_HORN_DUST(235, new short[]{93, 101}, new short[]{175, 181},
            new byte[]{5, 48}, new double[]{37.5, 106.3}),

    /**
     * Limpwurt root.
     */
    LIMPWURT_ROOT(225, new short[]{95, 105}, new short[]{115, 157},
            new byte[]{12, 55}, new double[]{50, 125}),

    /**
     * Red spider's eggs.
     */
    RED_SPIDER_EGGS(223, new short[]{97, 3004, 5936}, new short[]{127,
            3026, 5937}, new byte[]{22, 63, 73}, new double[]{62.5,
            142.5, 165}),

    /**
     * Blamish snail slime.
     */
    BLAMISH_SNAIL_SLIME(1581, new short[]{97}, new short[]{1582},
            new byte[]{25}, new double[]{80}),

    /**
     * Chocolate dust.
     */
    CHOCOLATE_DUST(1975, new short[]{97}, new short[]{3010},
            new byte[]{26}, new double[]{67.5}),

    /**
     * White berries.
     */
    WHITE_BERRIES(239, new short[]{99, 107}, new short[]{133, 163},
            new byte[]{30, 66}, new double[]{75, 150}),

    /**
     * Rubium.
     */
    RUBIUM(12630, new short[]{91}, new short[]{12633}, new byte[]{31},
            new double[]{55}),

    /**
     * Toad's legs.
     */
    TOAD_LEGS(2152, new short[]{3002}, new short[]{3034},
            new byte[]{34}, new double[]{80}),

    /**
     * Goat horn dust.
     */
    GOAT_HORN_DUST(9736, new short[]{97}, new short[]{9741},
            new byte[]{36}, new double[]{84}),

    /**
     * Pharmakos berries.
     */
    PHARMAKOS_BERRIES(11807, new short[]{3002}, new short[]{11810},
            new byte[]{37}, new double[]{85}),

    /**
     * Snape grass.
     */
    SNAPE_GRASS(231, new short[]{99, 103}, new short[]{139, 151},
            new byte[]{38, 50}, new double[]{87.5, 112.5}),

    /**
     * Cockatrice egg.
     */
    COCKATRICE_EGG(12109, new short[]{12181}, new short[]{12142},
            new byte[]{40}, new double[]{92}),

    /**
     * Frogspawn.
     */
    FROGSPAWN(10961, new short[]{14856}, new short[]{14840},
            new byte[]{40}, new double[]{92}),

    /**
     * Chopped onion.
     */
    CHOPPED_ONION(1871, new short[]{101}, new short[]{18661},
            new byte[]{46}, new double[]{0}),

    /**
     * Mort myre fungus.
     */
    MORT_MYRE_FUNGUS(2970, new short[]{103}, new short[]{3018},
            new byte[]{52}, new double[]{117.5}),

    /**
     * Shrunk ogleroot.
     */
    SHRUNK_OGLEROOT(11205, new short[]{95}, new short[]{11204},
            new byte[]{52}, new double[]{6}),

    /**
     * Kebbit teeth dust.
     */
    KEBBIT_TEETH_DUST(10111, new short[]{103}, new short[]{10000},
            new byte[]{53}, new double[]{120}),

    /**
     * Crushed gorak claw.
     */
    CRUSHED_GORAK_CLAW(9018, new short[]{9019}, new short[]{9022},
            new byte[]{57}, new double[]{130}),

    /**
     * Wimpy feather.
     */
    WIMPY_FEATHER(11525, new short[]{14856}, new short[]{14848},
            new byte[]{58}, new double[]{132}),

    /**
     * Dragon scale dust.
     */
    DRAGON_SCALE_DUST(241, new short[]{105, 2483},
            new short[]{187, 2454}, new byte[]{60, 69}, new double[]{
            137.5, 157.5}),

    /**
     * Yew roots.
     */
    YEW_ROOTS(6049, new short[]{5942}, new short[]{5945},
            new byte[]{68}, new double[]{155}),

    /**
     * Wine of Zamorak.
     */
    WINE_OF_ZAMORAK(245, new short[]{109}, new short[]{169},
            new byte[]{72}, new double[]{162.5}),

    /**
     * Potato cactus.
     */
    POTATO_CACTUS(3138, new short[]{2483}, new short[]{3042},
            new byte[]{76}, new double[]{172.5}),

    /**
     * Jangerberries.
     */
    JANGERBERRIES(247, new short[]{111}, new short[]{189},
            new byte[]{78}, new double[]{175}),

    /**
     * Magic roots.
     */
    MAGIC_ROOTS(6051, new short[]{5951}, new short[]{5954},
            new byte[]{79}, new double[]{177.5}),

    /**
     * Crushed bird nest.
     */
    CRUSHED_BIRD_NEST(6693, new short[]{3002}, new short[]{6687},
            new byte[]{81}, new double[]{180}),

    /**
     * Poison ivy berries.
     */
    POISON_IVY_BERRIES(6018, new short[]{5939}, new short[]{5940},
            new byte[]{82}, new double[]{190}),

    /**
     * Papaya fruit.
     */
    PAPAYA_FRUIT(5972, new short[]{3018}, new short[]{15301},
            new byte[]{84}, new double[]{200}),

    /**
     * Phoenix feather.
     */
    PHOENIX_FEATHER(4621, new short[]{2452, 2454, 2456, 2458}, new short[]{
            15304, 15305, 15306, 15307}, new byte[]{85, 85, 85, 85},
            new double[]{210, 210, 210, 210}),

    /**
     * Ground mud runes.
     */
    GROUND_MUD_RUNES(9594, new short[]{3040, 3042, 3044, 3046}, new short[]{
            15320, 15321, 15322, 15323}, new byte[]{91, 91, 91, 91},
            new double[]{250, 250, 250, 250}),

    /**
     * Grenwall spikes.
     */
    GRENWALL_SPIKES(12539, new short[]{2444, 169, 171, 173}, new short[]{
            15324, 15325, 15326, 15327}, new byte[]{92, 92, 92, 92},
            new double[]{260, 260, 260, 260}),

    /**
     * Bonemeal (wyvern).
     */
    BONEMEAL(6810, new short[]{2434, 139, 141, 143}, new short[]{15328,
            15329, 15330, 15331}, new byte[]{94, 94, 94, 94}, new double[]{
            270, 270, 270, 270});

    /**
     * The mappings of the {@code Ingredients}.
     */
    private static Map<Short, Ingredients> ingredients = new HashMap<Short, Ingredients>();

    /**
     * Gets an {@code Ingredients} {@code Object} from the mapping.
     *
     * @param itemId The item id.
     * @return The ingredient.
     */
    public static Ingredients forId(int itemId) {
        return ingredients.get((short) itemId);
    }

    /**
     * Populate the mapping.
     */
    static {
        for (Ingredients ingredient : Ingredients.values()) {
            ingredients.put(ingredient.itemId, ingredient);
        }
    }

    /**
     * The item id.
     */
    private final short itemId;

    /**
     * The items this ingredient can be used with.
     */
    private final short[] otherItems;

    /**
     * The rewards.
     */
    private final short[] rewards;

    /**
     * The levels required.
     */
    private final byte[] levels;

    /**
     * The experience gained upon mixing.
     */
    private final double[] experience;

    /**
     * Constructs a new {@code Ingredient} {@code Object}.
     *
     * @param itemId     The item id.
     * @param otherItems The other items.
     * @param rewards    The rewards.
     * @param levels     The levels.
     * @param experience The experience.
     */
    private Ingredients(int itemId, short[] otherItems, short[] rewards,
                        byte[] levels, double[] experience) {
        this.itemId = (short) itemId;
        this.otherItems = otherItems;
        this.rewards = rewards;
        this.levels = levels;
        this.experience = experience;
    }

    /**
     * @return the itemId
     */
    public short getItemId() {
        return itemId;
    }

    /**
     * @return the otherItems
     */
    public short[] getOtherItems() {
        return otherItems;
    }

    /**
     * @return the rewards
     */
    public short[] getRewards() {
        return rewards;
    }

    /**
     * @return the levels
     */
    public byte[] getLevels() {
        return levels;
    }

    /**
     * @return the experience
     */
    public double[] getExperience() {
        return experience;
    }

    /**
     * Gets the slot from the other items array.
     *
     * @param itemId The item id.
     * @return The slot.
     */
    public byte getSlot(int itemId) {
        for (byte i = 0; i < otherItems.length; i++) {
            if (itemId == otherItems[i]) {
                return i;
            }
        }
        return -1;
    }

}
