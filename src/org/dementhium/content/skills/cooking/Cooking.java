package org.dementhium.content.skills.cooking;

import org.dementhium.action.ProduceAction;
import org.dementhium.model.Item;
import org.dementhium.model.definition.ItemDefinition;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.mask.Graphic;
import org.dementhium.model.player.Equipment;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;
import org.dementhium.net.ActionSender;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 * @author Steve <golden_32@live.com>
 */
public class Cooking extends ProduceAction {

    public enum CookingMethod {

        STOVE(Animation.create(883), new int[]{114, 2728, 2729, 2730, 2731}), FIRE(
                Animation.create(899), new int[]{2732}), FIRE_PLACE(
                Animation.create(899), new int[]{2724, 2725, 2726});

        private final Animation animation;
        private final int[] objIds;

        CookingMethod(Animation animation, int[] objIds) {
            this.animation = animation;
            this.objIds = objIds;
        }

        public Animation getAnimation() {
            return animation;
        }

        public int[] getObjects() {
            return objIds;
        }
    }

    /**
     * @author Stephen Moyer <golden_32@live.com>
     * @author Cjay0091 - his enum gave me the ids for a lot of this
     */
    public enum CookingItem {

        RAW_REDBERRY_PIE(10, new Item[]{new Item(2321)}, new Item(2329), 78,
                new Item(2325), false),

        RAW_MEAT_PIE(20, new Item[]{new Item(2319)}, new Item(2329), 110,
                new Item(2327), false),

        RAW_MUD_PIE(29, new Item[]{new Item(7168)}, new Item(2329), 128,
                new Item(7170), false),

        RAW_APPLE_PIE(30, new Item[]{new Item(2317)}, new Item(2329), 130,
                new Item(2323), false),

        RAW_GARDEN_PIE(34, new Item[]{new Item(7176)}, new Item(2329), 138,
                new Item(7178), false),

        RAW_FISH_PIE(47, new Item[]{new Item(7186)}, new Item(2329), 164,
                new Item(7188), false),

        RAW_ADMIRAL_PIE(70, new Item[]{new Item(7196)}, new Item(2329), 210,
                new Item(7198), false), RAW_WILD_PIE(85, new Item[]{new Item(
                7206)}, new Item(2329), 240, new Item(7208), false),

        RAW_SUMMER_PIE(95, new Item[]{new Item(7216)}, new Item(2329), 260,
                new Item(7218), false),

        RAW_FISHCAKE(31, new Item[]{new Item(7529)}, new Item(7531), 100,
                new Item(7530), false),

        RAW_POTATO(7, new Item[]{new Item(1942)}, new Item(6699), 15,
                new Item(6701), false),

        RAW_SHRIMPS(1, new Item[]{new Item(317)}, new Item(323), 30,
                new Item(315), true),

        RAW_SARDINE(1, new Item[]{new Item(327)}, new Item(323), 40,
                new Item(325), true),

        RAW_ANCHOVIES(1, new Item[]{new Item(321)}, new Item(323), 30,
                new Item(319), true),

        POISON_KARAMBWAN(1, new Item[]{new Item(3142)}, new Item(3148), 80,
                new Item(3151), true),

        RAW_HERRING(5, new Item[]{new Item(345)}, new Item(357), 50,
                new Item(347), true),

        RAW_MACKEREL(10, new Item[]{new Item(353)}, new Item(357), 60,
                new Item(355), true),

        RAW_TROUT(15, new Item[]{new Item(335)}, new Item(343), 70,
                new Item(333), true),

        RAW_COD(18, new Item[]{new Item(341)}, new Item(343), 75, new Item(
                339), true),

        RAW_PIKE(20, new Item[]{new Item(349)}, new Item(343), 80, new Item(
                351), true),

        RAW_SALMON(25, new Item[]{new Item(331)}, new Item(343), 90,
                new Item(329), true),

        RAW_SLIMY_EEL(28, new Item[]{new Item(3379)}, new Item(3383), 95,
                new Item(3381), true),

        RAW_TUNA(30, new Item[]{new Item(359)}, new Item(367), 100,
                new Item(361), true),

        RAW_RAINBOW_FISH(35, new Item[]{new Item(10138)}, new Item(10140),
                110, new Item(10136), true),

        RAW_CAVE_EEL(38, new Item[]{new Item(5001)}, new Item(5006), 115,
                new Item(5003), true),

        RAW_LOBSTER(40, new Item[]{new Item(377)}, new Item(381), 120,
                new Item(379), true),

        RAW_BASS(43, new Item[]{new Item(363)}, new Item(367), 130,
                new Item(365), true),

        RAW_SWORDFISH(45, new Item[]{new Item(371)}, new Item(375), 140,
                new Item(373), true),

        RAW_LAVA_EEL(53, new Item[]{new Item(2148)}, new Item(-1), 140,
                new Item(2149), true),

        RAW_MONKFISH(62, new Item[]{new Item(7944)}, new Item(7948), 150,
                new Item(7946), true),

        RAW_SHARK(80, new Item[]{new Item(383)}, new Item(387), 210,
                new Item(385), true),

        RAW_SEA_TURTLE(82, new Item[]{new Item(395)}, new Item(399), 212,
                new Item(397), true),

        RAW_CAVEFISH(88, new Item[]{new Item(15264)}, new Item(15268), 214,
                new Item(15266), true),

        RAW_MANTA_RAY(91, new Item[]{new Item(389)}, new Item(393), 200,
                new Item(391), true),

        RAW_ROCKTAIL(92, new Item[]{new Item(15274)}, new Item(15270), 225,
                new Item(15272), true),
        RAW_RAT_MEAT(1, new Item[]{new Item(2134)}, new Item(2146), 30, new Item(2124), true),
        RAW_BEAR_MEAT(1, new Item[]{new Item(2134)}, new Item(2146), 30, new Item(2124), true),
        RAW_BEEF(1, new Item[]{new Item(2132)}, new Item(2146), 30, new Item(2124), true),
        RAW_CHICKEN(1, new Item[]{new Item(1)}, new Item(1), 30, new Item(1), true),
        STEW(25, new Item[]{new Item(1997)}, new Item(2005), 117, new Item(2003), true),
        SPICY_STEW(25, new Item[]{new Item(1999)}, new Item(2005), 117, new Item(7479), true),
        CURRY(60, new Item[]{new Item(2009)}, new Item(2013), 280, new Item(2011), true);

        private final Item[] ingredients;
        private final Item product;
        private final int level;
        private final int endXp;
        private final Item burntProduct;
        private boolean canCookOnFire;

        CookingItem(int level, Item[] ingredients, Item burntProduct,
                    int endXp, Item product, boolean canCookOnFire) {
            this.level = level;
            this.ingredients = ingredients;
            this.product = product;
            this.endXp = endXp;
            this.burntProduct = burntProduct;
            this.canCookOnFire = canCookOnFire;
        }

        public Item getProduct() {
            return product;
        }

        public Item[] getIngredients() {
            return ingredients;
        }

        public int getLevel() {
            return level;
        }

        public int getEndXp() {
            return endXp;
        }

        public Item getBurntProduct() {
            return burntProduct;
        }

        public boolean canCookOnFire() {
            return canCookOnFire;
        }

    }

    private static final String[] MESSAGES = new String[]{
            "You have run out of ingredients!", "You successfully cook the ",
            "You accidentally burn the ",
            "You do not have the required level to cook this!"};

    private CookingItem item;
    private CookingMethod method;

    public Cooking(int ticks, int productionAmount, CookingItem item, CookingMethod method) {
        super(ticks, productionAmount);
        this.method = method;
        this.item = item;
    }

    @Override
    public Item produceItem() {
        return item.getProduct();
    }

    @Override
    public Item[] getRequiredItems() {
        return item.getIngredients();
    }

    @Override
    public int getRequiredLevel() {
        return item.getLevel();
    }

    @Override
    public int getSkill() {
        return Skills.COOKING;
    }

    @Override
    public double getExperience() {
        return item.getEndXp();
    }

    @Override
    public String getMessage(int type) {
        String message = MESSAGES[type];
        switch (type) {
            case 1:
            case 2:
                message += item.getProduct().getDefinition().getName();
                break;
        }
        return message;
    }

    @Override
    public Animation getAnimation() {
        return method.getAnimation();
    }

    @Override
    public Graphic getGraphic() {
        return null;
    }

    @Override
    public boolean isSuccessfull() {
        final double burnBonus = 3.0;
        double burn_chance = 30.0 - burnBonus;
        final double cook_level = mob.getPlayer().getSkills()
                .getLevel(Skills.COOKING);
        final double lev_needed = item.getLevel();
        final double burn_dec = burn_chance / lev_needed;
        final double multi_b = cook_level - lev_needed;
        burn_chance -= multi_b * burn_dec;
        if (cook_level >= 99) {
            burn_chance -= 10;
        }
        final double randNum = Math.random() * 120.0;
        return burn_chance <= randNum;
    }

    public static CookingItem itemForId(Player player, int attribute, int objId) {
        CookingMethod method = getMethod(objId);
        CookingItem item = null;
        if (method != null) {
            for (CookingItem cI : CookingItem.values()) {
                for (Item i : cI.getIngredients()) {
                    if (i.getId() == attribute) {
                        item = cI;
                    }
                }
            }
            if (objId > -1 && !canCook(method, item)) {
                player.sendMessage("You cannot cook that on a fire!");
                return null;
            }
        }
        return item;
    }

    private static boolean canCook(CookingMethod method, CookingItem item) {
        if (method.equals(CookingMethod.FIRE) && !item.canCookOnFire()) {
            return false;
        } else {
            return true;
        }
    }

    private static CookingMethod getMethod(int objId) {
        for (CookingMethod method : CookingMethod.values()) {
            for (int i : method.getObjects()) {
                if (i == objId) {
                    return method;
                }
            }
        }
        return null;
    }

    public boolean stopBurning() {
        int req = item.getLevel();
        int level = mob.getPlayer().getSkills().getLevel(Skills.COOKING);
        int diff = req - level;
        if (wearingGauntlets()) {
            diff -= 2;
        }
        return diff <= -11;
    }

    public boolean wearingGauntlets() {
        return mob.getPlayer().getEquipment().getSlot(Equipment.SLOT_HANDS) == 775;
    }

    @Override
    public Item getFailItem() {
        return item.getBurntProduct();
    }

    public static void showInterface(Player player, CookingItem itemToCook,
                                     int itemUsed) {
        ActionSender.sendBConfig(player, 754, 2);
        ActionSender.sendBConfig(player, ProduceAction.CONFIG_IDS[0], itemToCook.getIngredients()[0].getId());
        ActionSender.sendSpecialString(player, ProduceAction.NAME_IDS[0], ItemDefinition.forId(itemUsed).getName());
        ActionSender
                .sendString(player, 916, 1,
                        "Choose how many you would like to cook, <br> then click on the item to begin");
        ActionSender.sendInterface(player, 1, 752, 13, 905);
        ActionSender.sendInterface(player, 1, 905, 4, 916);
        ActionSender.sendConfig(player, 1363,
                (player.getInventory().numberOf(itemUsed) << 20)
                        | (player.getInventory().numberOf(itemUsed) << 26));
        player.getSettings().setAmountToProduce(
                player.getInventory().numberOf(itemUsed));
        player.getSettings().setMaxToProduce(
                player.getInventory().numberOf(itemUsed));
        player.getSettings().setItemToProduce(itemUsed);
        player.getSettings().setDialougeSkill(Skills.COOKING);

    }

    @Override
    public int getAnimationDelay() {
        return 0;
    }

}
