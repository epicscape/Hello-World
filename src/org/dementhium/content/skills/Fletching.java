package org.dementhium.content.skills;

import org.dementhium.action.ProduceAction;
import org.dementhium.content.skills.smithing.SmithingUtils;
import org.dementhium.model.Item;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.mask.Graphic;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;
import org.dementhium.net.ActionSender;
import org.dementhium.util.Misc;

/**
 * @author Steve <golden_32@live.com>
 */
public class Fletching extends ProduceAction {

    public static final Item BOWSTRING = new Item(1777);
    private static final int KNIFE = 946;
    public static final Animation CUT_ANIM = Animation.create(6702);
    public static final Item FEATHER_15 = new Item(314, 15);
    public static final Item FEATHER_10 = new Item(314, 10);
    public static final Item HEADLESS_ARROW = new Item(53, 15);
    public static final Item CROSSBOW_STRING = new Item(9438);

    public enum FletchingType {
        STRINGING("You add a string to the @item@.") {
            @Override
            public void showInterface(Player player, FletchingItem item) {
                ActionSender.sendBConfig(player, 754, 13);
                ActionSender.sendBConfig(player, ProduceAction.CONFIG_IDS[0], item.getProducedItem().getId());
                ActionSender.sendSpecialString(player, ProduceAction.NAME_IDS[0], item.getProducedItem().getDefinition().getName());
                ActionSender.sendString(player, 916, 1, "Choose how many you wish to make,<br> then click the item to begin.");
                ActionSender.sendInterface(player, 1, 752, 13, 905);
                ActionSender.sendInterface(player, 1, 905, 4, 916);
                int id = (player.getInventory().numberOf(item.getMaterials()[1].getId()) < player.getInventory().numberOf(item.getMaterials()[0].getId())) ? item.getMaterials()[1].getId() : item.getMaterials()[0].getId();
                ActionSender.sendConfig(player, 1363, (player.getInventory().numberOf(id) << 20) | (player.getInventory().numberOf(id) << 26));
                player.getSettings().setAmountToProduce(player.getInventory().numberOf(id));
                player.getSettings().setMaxToProduce(player.getInventory().numberOf(id));
                player.getSettings().setItemToProduce(item.getMaterials()[1].getId());
                player.getSettings().setItemUsed(item.getMaterials()[0].getId());
                player.getSettings().setDialougeSkill(Skills.FLETCHING);
            }
        },
        CUTTING("You carefully cut the wood into a @item@.") {
            public void showInterface(Player player, FletchingItem item) {
                FletchingGroup group = Fletching.getGroup(item);
                ActionSender.sendBConfig(player, 754, 13);
                for (int i = 0; i < group.getPossibleCreations().length; i++) {
                    ActionSender.sendBConfig(player, ProduceAction.CONFIG_IDS[i], group.getPossibleCreations()[i].getId());
                    ActionSender.sendSpecialString(player, ProduceAction.NAME_IDS[i], (group.getPossibleCreations()[i].getId() == 52 ? (group.getPossibleCreations()[i].getDefinition().getName() + "<br>(Sets of 15)") : group.getPossibleCreations()[i].getDefinition().getName()));
                }
                player.getSettings().setPossibleProductions(Misc.itemToIntArray(group.getPossibleCreations()));
                ActionSender.sendString(player, 916, 1, "Choose how many you wish to make,<br> then click the item to begin.");
                ActionSender.sendInterface(player, 1, 752, 13, 905);
                ActionSender.sendInterface(player, 1, 905, 4, 916);
                int id = group.getItemUsed().getId();
                ActionSender.sendConfig(player, 1363, (player.getInventory().numberOf(id) << 20) | (player.getInventory().numberOf(id) << 26));
                player.getSettings().setAmountToProduce(player.getInventory().numberOf(id));
                player.getSettings().setMaxToProduce(player.getInventory().numberOf(id));
                player.getSettings().setItemUsed(KNIFE);
                player.getSettings().setDialougeSkill(Skills.FLETCHING);
                player.setAttribute("isCutting", Boolean.TRUE);
            }
        },
        ATTACHING("You attach @item@ to @item2@.") {
            @Override
            public void showInterface(Player player, FletchingItem item) {
                if (item.name().contains("CBOW_UNF")) {
                    if (!player.getInventory().contains(SmithingUtils.HAMMER.getId())) {
                        player.sendMessage("You need a hammer to do that!");
                        return;
                    }
                }
                ActionSender.sendBConfig(player, 754, 13);
                ActionSender.sendBConfig(player, ProduceAction.CONFIG_IDS[0], item.getProducedItem().getId()); //+ "<br>(Sets of 15)"
                ActionSender.sendSpecialString(player, ProduceAction.NAME_IDS[0], item.getProducedItem().getDefinition().getName() + (item.getProducedItem().getAmount() > 1 ? "<br>(Sets of " + item.getProducedItem().getAmount() + ")" : ""));
                ActionSender.sendString(player, 916, 1, "Choose how many you wish to make,<br> then click the item to begin.");
                ActionSender.sendInterface(player, 1, 752, 13, 905);
                ActionSender.sendInterface(player, 1, 905, 4, 916);
                int id = (player.getInventory().numberOf(item.getMaterials()[1].getId()) < player.getInventory().numberOf(item.getMaterials()[0].getId())) ? item.getMaterials()[1].getId() : item.getMaterials()[0].getId();
                int amt = item.getProducedItem().getAmount() > 1 ? ((player.getInventory().numberOf(id) / item.getProducedItem().getAmount()) > 10 ? 10 : player.getInventory().numberOf(id) / item.getProducedItem().getAmount()) : player.getInventory().numberOf(id);
                ActionSender.sendConfig(player, 1363, (amt << 20) | (amt << 26));
                player.getSettings().setAmountToProduce(amt);
                player.getSettings().setMaxToProduce(amt);
                player.getSettings().setItemToProduce(item.getMaterials()[1].getId());
                player.getSettings().setItemUsed(item.getMaterials()[0].getId());
                player.getSettings().setDialougeSkill(Skills.FLETCHING);
            }

        };
        private String message;

        FletchingType(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }

        public abstract void showInterface(Player player, FletchingItem item);

    }

    public enum FletchingGroup {
        LOGS(new Item(1511), new Item[]{new Item(52), new Item(50), new Item(48), new Item(9440)}),
        OAK_LOGS(new Item(1521), new Item[]{new Item(54), new Item(56), new Item(9442)}),
        WILLOW_LOGS(new Item(1519), new Item[]{new Item(60), new Item(58), new Item(9444)}),
        MAPLE_LOGS(new Item(1517), new Item[]{new Item(64), new Item(62), new Item(9448)}),
        YEW_LOGS(new Item(1515), new Item[]{new Item(68), new Item(66), new Item(9452)}),
        MAGIC_LOGS(new Item(1513), new Item[]{new Item(72), new Item(70)});
        private Item itemUsed;
        private Item[] possibleCreations;

        //all these items will then show on the interface
        FletchingGroup(Item itemUsed, Item[] possibleCreations) {
            this.setItemUsed(itemUsed);
            this.setPossibleCreations(possibleCreations);
        }

        public void setPossibleCreations(Item[] possibleCreations) {
            this.possibleCreations = possibleCreations;
        }

        public Item[] getPossibleCreations() {
            return possibleCreations;
        }

        public void setItemUsed(Item itemUsed) {
            this.itemUsed = itemUsed;
        }

        public Item getItemUsed() {
            return itemUsed;
        }
    }

    public enum FletchingItem {
        /*
           * Cutting
           */
        ARROW_SHAFTS(1, 5, CUT_ANIM, new Item[]{new Item(1511)}, new Item(52, 15), FletchingType.CUTTING),
        SHORTBOW_U(5, 5, CUT_ANIM, new Item[]{new Item(1511)}, new Item(50), FletchingType.CUTTING),
        OAK_SHORTBOW_U(20, 25, CUT_ANIM, new Item[]{new Item(1521)}, new Item(54), FletchingType.CUTTING),
        WILLOW_SHORTBOW_U(35, 33.3, CUT_ANIM, new Item[]{new Item(1519)}, new Item(60), FletchingType.CUTTING),
        MAPLE_SHORTBOW_U(50, 50, CUT_ANIM, new Item[]{new Item(1517)}, new Item(64), FletchingType.CUTTING),
        YEW_SHORTBOW_U(65, 67.5, CUT_ANIM, new Item[]{new Item(1515)}, new Item(68), FletchingType.CUTTING),
        MAGIC_SHORTBOW_U(80, 83.3, CUT_ANIM, new Item[]{new Item(1513)}, new Item(72), FletchingType.CUTTING),
        LONGBOW_U(10, 10, CUT_ANIM, new Item[]{new Item(1511)}, new Item(48), FletchingType.CUTTING),
        OAK_LONGBOW_U(25, 25, CUT_ANIM, new Item[]{new Item(1521)}, new Item(56), FletchingType.CUTTING),
        WILLOW_LONGBOW_U(40, 41.5, CUT_ANIM, new Item[]{new Item(1519)}, new Item(58), FletchingType.CUTTING),
        MAPLE_LONGBOW_U(55, 58.3, CUT_ANIM, new Item[]{new Item(1517)}, new Item(62), FletchingType.CUTTING),
        YEW_LONGBOW_U(70, 75, CUT_ANIM, new Item[]{new Item(1515)}, new Item(66), FletchingType.CUTTING),
        MAGIC_LONGBOW_U(85, 91.5, CUT_ANIM, new Item[]{new Item(1513)}, new Item(70), FletchingType.CUTTING),

        WOODEN_STOCK(9, 6, CUT_ANIM, new Item[]{new Item(1511)}, new Item(9440), FletchingType.CUTTING),
        OAK_STOCK(24, 16, CUT_ANIM, new Item[]{new Item(1521)}, new Item(9442), FletchingType.CUTTING),
        WILLOW_STOCK(29, 22, CUT_ANIM, new Item[]{new Item(1519)}, new Item(9444), FletchingType.CUTTING),
        TEAK_STOCK(46, 27, CUT_ANIM, new Item[]{new Item(6333)}, new Item(9446), FletchingType.CUTTING),
        MAPLE_STOCK(54, 32, CUT_ANIM, new Item[]{new Item(1517)}, new Item(9448), FletchingType.CUTTING),
        MAHOGANY_STOCK(61, 41, CUT_ANIM, new Item[]{new Item(6332)}, new Item(9446), FletchingType.CUTTING),
        YEW_STOCK(69, 50, CUT_ANIM, new Item[]{new Item(1515)}, new Item(9448), FletchingType.CUTTING),

        /*
           * Attaching
           */
        ARROW_HEADLESS(1, 15, null, new Item[]{FEATHER_15, new Item(52, 15)}, HEADLESS_ARROW, FletchingType.ATTACHING),
        BRONZE_ARROW(1, 39.5, null, new Item[]{new Item(39, 15), HEADLESS_ARROW}, new Item(882, 15), FletchingType.ATTACHING),
        IRON_ARROW(15, 57.5, null, new Item[]{new Item(40, 15), HEADLESS_ARROW}, new Item(884, 15), FletchingType.ATTACHING),
        STEEL_ARROW(30, 95, null, new Item[]{new Item(41, 15), HEADLESS_ARROW}, new Item(886, 15), FletchingType.ATTACHING),
        MITH_ARROW(45, 132.5, null, new Item[]{new Item(42, 15), HEADLESS_ARROW}, new Item(888, 15), FletchingType.ATTACHING),
        ADAMANT_ARROW(60, 170, null, new Item[]{new Item(43, 15), HEADLESS_ARROW}, new Item(890, 15), FletchingType.ATTACHING),
        RUNE_ARROW(75, 207.4, null, new Item[]{new Item(44, 15), HEADLESS_ARROW}, new Item(892, 15), FletchingType.ATTACHING),
        DRAGON_ARROW(90, 244.5, null, new Item[]{new Item(11237, 15), HEADLESS_ARROW}, new Item(11212, 15), FletchingType.ATTACHING),

        /*
           * CBOWS attaching
           */
        BRONZE_CBOW_UNF(9, 12, Animation.create(4436), new Item[]{new Item(9420), new Item(9440)}, new Item(9454), FletchingType.ATTACHING),
        BLURITE_CBOW_UNF(24, 32, Animation.create(4437), new Item[]{new Item(9422), new Item(9442)}, new Item(9456), FletchingType.ATTACHING),
        IRON_CBOW_UNF(29, 44, Animation.create(4438), new Item[]{new Item(9423), new Item(9444)}, new Item(9457), FletchingType.ATTACHING),
        STEEL_CBOW_UNF(46, 54, Animation.create(4439), new Item[]{new Item(9425), new Item(9446)}, new Item(9459), FletchingType.ATTACHING),
        MITHRIL_CBOW_UNF(54, 64, Animation.create(4440), new Item[]{new Item(9427), new Item(9448)}, new Item(9461), FletchingType.ATTACHING),
        ADAMANT_CBOW_UNF(61, 82, Animation.create(4441), new Item[]{new Item(9429), new Item(9450)}, new Item(9463), FletchingType.ATTACHING),
        RUNITE_CBOW_UNF(69, 100, Animation.create(4442), new Item[]{new Item(9431), new Item(9452)}, new Item(9465), FletchingType.ATTACHING),

        /*
           * CBOWS stringing
           */
        BRONZE_CBOW(9, 6, Animation.create(6671), new Item[]{CROSSBOW_STRING, new Item(9454)}, new Item(9174), FletchingType.STRINGING),
        BLURITE_CBOW(24, 16, Animation.create(6672), new Item[]{CROSSBOW_STRING, new Item(9456)}, new Item(9176), FletchingType.STRINGING),
        IRON_CBOW(29, 22, Animation.create(6673), new Item[]{CROSSBOW_STRING, new Item(9457)}, new Item(9177), FletchingType.STRINGING),
        STEEL_CBOW(46, 27, Animation.create(6674), new Item[]{CROSSBOW_STRING, new Item(9459)}, new Item(9179), FletchingType.STRINGING),
        MITHRIL_CBOW(54, 32, Animation.create(6675), new Item[]{CROSSBOW_STRING, new Item(9461)}, new Item(9181), FletchingType.STRINGING),
        ADAMANT_CBOW(61, 41, Animation.create(6676), new Item[]{CROSSBOW_STRING, new Item(9463)}, new Item(9183), FletchingType.STRINGING),
        RUNITE_CBOW(69, 50, Animation.create(6677), new Item[]{CROSSBOW_STRING, new Item(9465)}, new Item(9185), FletchingType.STRINGING),

        /*
           * Bolts
           */
        BRONZE_BOLTS(9, 5, null, new Item[]{FEATHER_10, new Item(9375, 10)}, new Item(877, 10), FletchingType.ATTACHING),
        BLURITE_BOLTS(24, 10, null, new Item[]{FEATHER_10, new Item(9376, 10)}, new Item(9139, 10), FletchingType.ATTACHING),
        IRON_BOLTS(39, 15, null, new Item[]{FEATHER_10, new Item(9377, 10)}, new Item(9140, 10), FletchingType.ATTACHING),
        SILVER_BOLTS(43, 25, null, new Item[]{FEATHER_10, new Item(9378, 10)}, new Item(9141, 10), FletchingType.ATTACHING),
        STEEL_BOLTS(46, 35, null, new Item[]{FEATHER_10, new Item(9379, 10)}, new Item(9142, 10), FletchingType.ATTACHING),
        MITHRIL_BOLTS(54, 50, null, new Item[]{FEATHER_10, new Item(9380, 10)}, new Item(9143, 10), FletchingType.ATTACHING),
        ADAMANT_BOLTS(61, 70, null, new Item[]{FEATHER_10, new Item(9381, 10)}, new Item(9144, 10), FletchingType.ATTACHING),
        RUNITE_BOLTS(69, 100, null, new Item[]{FEATHER_10, new Item(9382, 10)}, new Item(9145, 10), FletchingType.ATTACHING),

        OPAL_BOLTS(11, 16, null, new Item[]{new Item(877, 10), new Item(45, 10)}, new Item(879, 10), FletchingType.ATTACHING),
        JADE_BOLTS(26, 24, null, new Item[]{new Item(9139, 10), new Item(9187, 10)}, new Item(9335, 10), FletchingType.ATTACHING),
        PEARL_BOLTS(41, 32, null, new Item[]{new Item(9140, 10), new Item(46, 10)}, new Item(880, 10), FletchingType.ATTACHING),
        RED_TOPAZ_BOLTS(48, 39, null, new Item[]{new Item(9142, 10), new Item(9188, 10)}, new Item(9336, 10), FletchingType.ATTACHING),
        SAPPHIRE_BOLTS(56, 47, null, new Item[]{new Item(9143, 10), new Item(9189, 10)}, new Item(9337, 10), FletchingType.ATTACHING),
        EMERALD_BOLTS(58, 55, null, new Item[]{new Item(9143, 10), new Item(9190, 10)}, new Item(9338, 10), FletchingType.ATTACHING),
        RUBY_BOLTS(63, 70, null, new Item[]{new Item(9144, 10), new Item(9191, 10)}, new Item(9339, 10), FletchingType.ATTACHING),
        DIAMOND_BOLTS(65, 70, null, new Item[]{new Item(9144, 10), new Item(9192, 10)}, new Item(9340, 10), FletchingType.ATTACHING),
        DRAGON_BOLTS(71, 82, null, new Item[]{new Item(9145, 10), new Item(9193, 10)}, new Item(9341, 10), FletchingType.ATTACHING),
        ONYX_BOLTS(73, 94, null, new Item[]{new Item(9145, 10), new Item(9194, 10)}, new Item(9342, 10), FletchingType.ATTACHING),


        /*
           * Darts
           */
        BRONZE_DART(1, 18, null, new Item[]{FEATHER_10, new Item(819, 10)}, new Item(806, 10), FletchingType.ATTACHING),
        IRON_DART(22, 38, null, new Item[]{FEATHER_10, new Item(820, 10)}, new Item(807, 10), FletchingType.ATTACHING),
        STEEL_DART(37, 75, null, new Item[]{FEATHER_10, new Item(821, 10)}, new Item(808, 10), FletchingType.ATTACHING),
        MITHRIL_DART(52, 112, null, new Item[]{FEATHER_10, new Item(822, 10)}, new Item(809, 10), FletchingType.ATTACHING),
        ADAMANT_DART(67, 150, null, new Item[]{FEATHER_10, new Item(823, 10)}, new Item(810, 10), FletchingType.ATTACHING),
        RUNE_DART(81, 188, null, new Item[]{FEATHER_10, new Item(824, 10)}, new Item(811, 10), FletchingType.ATTACHING),
        DRAGON_DART(95, 250, null, new Item[]{FEATHER_10, new Item(11232, 10)}, new Item(11230, 10), FletchingType.ATTACHING),


        /*
           * Stringing
           */
        SHORTBOW(5, 5, Animation.create(6678), new Item[]{BOWSTRING, new Item(50)}, new Item(841), FletchingType.STRINGING),
        OAK_SHORTBOW(20, 16.5, Animation.create(6679), new Item[]{BOWSTRING, new Item(54)}, new Item(843), FletchingType.STRINGING),
        WILLOW_SHORTBOW(35, 33.3, Animation.create(6680), new Item[]{BOWSTRING, new Item(60)}, new Item(849), FletchingType.STRINGING),
        MAPLE_SHORTBOW(50, 50, Animation.create(6681), new Item[]{BOWSTRING, new Item(64)}, new Item(853), FletchingType.STRINGING),
        YEW_SHORTBOW(65, 65, Animation.create(6682), new Item[]{BOWSTRING, new Item(68)}, new Item(857), FletchingType.STRINGING),
        MAGIC_SHORTBOW(80, 83.3, Animation.create(6683), new Item[]{BOWSTRING, new Item(72)}, new Item(861), FletchingType.STRINGING),
        LONGBOW(10, 10, Animation.create(6684), new Item[]{BOWSTRING, new Item(48)}, new Item(839), FletchingType.STRINGING),
        OAK_LONGBOW(25, 25, Animation.create(6685), new Item[]{BOWSTRING, new Item(56)}, new Item(845), FletchingType.STRINGING),
        WILLOW_LONGBOW(40, 41.5, Animation.create(6686), new Item[]{BOWSTRING, new Item(58)}, new Item(847), FletchingType.STRINGING),
        MAPLE_LONGBOW(55, 58.3, Animation.create(6687), new Item[]{BOWSTRING, new Item(62)}, new Item(851), FletchingType.STRINGING),
        YEW_LONGBOW(70, 75, Animation.create(6688), new Item[]{BOWSTRING, new Item(66)}, new Item(855), FletchingType.STRINGING),
        MAGIC_LONGBOW(85, 91.5, Animation.create(6689), new Item[]{BOWSTRING, new Item(70)}, new Item(859), FletchingType.STRINGING);

        private int level;
        private double xp;
        private Animation animation;
        private Item[] materials;
        private Item producedItem;
        private FletchingType type;

        FletchingItem(int level, double xp, Animation animation, Item[] materials, Item producedItem, FletchingType type) {
            this.level = level;
            this.xp = xp;
            this.animation = animation;
            this.materials = materials;
            this.producedItem = producedItem;
            this.type = type;
        }

        public int getLevel() {
            return level;
        }

        public double getXp() {
            return xp;
        }

        public Animation getAnimation() {
            return animation;
        }

        public Item[] getMaterials() {
            return materials;
        }

        public Item getProducedItem() {
            return producedItem;
        }

        public FletchingType getType() {
            return type;
        }


    }

    private FletchingItem item;
    private FletchingType type;

    public Fletching(int cycles, int productionAmount, FletchingItem item) {
        super(getTicksForType(item.getType()), productionAmount);
        this.item = item;
        this.type = item.getType();
    }

    private static int getTicksForType(FletchingType fletchType) {
        switch (fletchType) {
            case STRINGING:
                return 3;
            case CUTTING:
            case ATTACHING:
                return 1;
        }
        return 1;
    }

    protected static FletchingGroup getGroup(FletchingItem item) {
        for (FletchingGroup group : FletchingGroup.values()) {
            for (Item i : group.getPossibleCreations()) {
                if (i.getId() == item.getProducedItem().getId()) {
                    return group;
                }
            }
        }
        return null;
    }

    @Override
    public Item getFailItem() {
        return null;
    }

    @Override
    public Item produceItem() {
        return item.getProducedItem();
    }

    @Override
    public Item[] getRequiredItems() {
        return item.getMaterials();
    }

    @Override
    public int getRequiredLevel() {
        return item.getLevel();
    }

    @Override
    public int getSkill() {
        return Skills.FLETCHING;
    }

    @Override
    public double getExperience() {
        return item.getXp();
    }

    @Override
    public String getMessage(int outcome) {
        if (outcome == ProduceAction.TOO_LOW_OF_LEVEL) {
            return "Your fletching level is not high enough for this!";
        } else if (outcome == ProduceAction.NOT_ENOUGH_ITEMS) {
            return "You have run out of materials!";
        }
        switch (type) {
            case CUTTING:
                return type.getMessage().replace("@item@", item.getProducedItem().getDefinition().getName());
            case STRINGING:
                return type.getMessage().replace("@item@", item.getProducedItem().getDefinition().getName().contains("crossbow") ? "crossbow" : "bow");
            case ATTACHING:
                if (!item.getProducedItem().getDefinition().getName().contains("c'bow") && !item.getProducedItem().getDefinition().getName().contains("crossbow"))
                    return type.getMessage().replace("@item@", item.getMaterials()[0].getDefinition().getName().toLowerCase() + (!item.getMaterials()[0].getDefinition().getName().endsWith("s") ? "s" : "")).replace("@item2@", item.getMaterials()[1].getAmount() + " " + getItemType(item.getMaterials()[1]));
                else
                    return "You attach the limbs and the stock and make an unstrung crossbow";
        }
        return type.getMessage().replace("@item@", item.getProducedItem().getDefinition().getName());
    }

    private String getItemType(Item i) {
        String name = i.getDefinition().getName();
        if (name.contains("arrow")) {
            return "arrows";
        } else if (name.contains("bolt")) {
            return "bolts";
        }
        return name;
    }

    @Override
    public Animation getAnimation() {
        return item.getAnimation();
    }

    @Override
    public Graphic getGraphic() {
        return null;
    }

    @Override
    public boolean isSuccessfull() {
        return true;
    }

    public static FletchingItem getItemForId(int used, int usedWith, boolean usingInterface) {
        for (FletchingItem f : FletchingItem.values()) {
            if (f.getType().equals(FletchingType.STRINGING) && !usingInterface
                    && ((f.getMaterials()[0].getId() == used && f.getMaterials()[1].getId() == usedWith)
                    || (f.getMaterials()[0].getId() == usedWith && f.getMaterials()[1].getId() == used))) {
                return f;
            } else if (f.getType().equals(FletchingType.ATTACHING) && !usingInterface
                    && ((f.getMaterials()[0].getId() == used && f.getMaterials()[1].getId() == usedWith)
                    || (f.getMaterials()[0].getId() == usedWith && f.getMaterials()[1].getId() == used))) {
                return f;
            } else if (hasKnife(used, usedWith) && f.getType().equals(FletchingType.CUTTING)
                    && !usingInterface && ((usedWith == f.getMaterials()[0].getId() && used == KNIFE)
                    || (f.getMaterials()[0].getId() == used && KNIFE == usedWith))) {
                return f;
            } else if (hasKnife(used, usedWith) && f.getType().equals(FletchingType.CUTTING)
                    && usingInterface && (f.getProducedItem().getId() == used)) {
                return f;
            }
        }
        return null;
    }

    private static boolean hasKnife(int used, int usedWith) {
        return used == KNIFE || usedWith == KNIFE;
    }

    public static void sendInterfaces(Player player, FletchingItem item) {
        item.getType().showInterface(player, item);

    }

    @Override
    public int getAnimationDelay() {
        return 0;
    }

}
