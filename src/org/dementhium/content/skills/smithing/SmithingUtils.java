package org.dementhium.content.skills.smithing;

import org.dementhium.model.Item;
import org.dementhium.model.definition.ItemDefinition;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;
import org.dementhium.net.ActionSender;
import org.dementhium.util.Misc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * http://runescape.wikia.com/wiki/Smithing
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class SmithingUtils {

    public enum SmeltingBar {
        BRONZE(1, 6.2, new Item[]{new Item(436), new Item(438)}, new Item(2349)),
        BLURITE(8, 8.0, new Item[]{new Item(668)}, new Item(9467)),
        IRON(15, 12.5, new Item[]{new Item(440)}, new Item(2351)),
        SILVER(20, 13.7, new Item[]{new Item(442)}, new Item(2355)),
        STEEL(30, 17.5, new Item[]{new Item(440), new Item(453, 2)}, new Item(2353)),
        GOLD(40, 22.5, new Item[]{new Item(444)}, new Item(2357)),
        MITHRIL(50, 30, new Item[]{new Item(447), new Item(453, 4)}, new Item(2359)),
        ADAMANT(70, 37.5, new Item[]{new Item(449), new Item(453, 6)}, new Item(2361)),
        RUNE(85, 50, new Item[]{new Item(451), new Item(453, 8)}, new Item(2363));

        private int levelRequired;
        private double experience;
        private Item[] itemsRequired;
        private Item producedBar;

        private SmeltingBar(int levelRequired, double experience, Item[] itemsRequired, Item producedBar) {
            this.levelRequired = levelRequired;
            this.experience = experience;
            this.itemsRequired = itemsRequired;
            this.producedBar = producedBar;
        }

        public Item[] getItemsRequired() {
            return itemsRequired;
        }

        public int getLevelRequired() {
            return levelRequired;
        }

        public Item getProducedBar() {
            return producedBar;
        }

        public double getExperience() {
            return experience;
        }
    }

    public enum ForgingBar {
        BRONZE(2349, 0, new int[]{1205, 1351, 1422, 1139, 877, 1277, 819, 4819, 1794, -1, -1, 39, 1321, 9420, 1291, 864, 1155, 1173, -1, -1, 1337, 1375, 1103, 1189, 3095, 1307, 1087, 1075, 1117, 1265}, new double[]{12.5, 25, 37.5, 62.5}, new int[]{66, 82, 210, 267}),
        IRON(2351, 15, new int[]{1203, 1349, 1420, 1137, 9377, 1279, 820, 4820, -1, 7225, -1, 40, 1323, 9423, 1293, 863, 1153, 1175, 4540, -1, 1335, 1363, 1101, 1191, 3096, 1309, 1081, 1067, 1115, 1267}, new double[]{25, 50, 75, 125}, new int[]{66, 90, 162, 210, 267}),
        STEEL(2353, 30, new int[]{1207, 1353, 1424, 1141, 9378, 1281, 821, 1539, -1, -1, 2370, 41, 1325, 9425, 1295, 865, 1157, 1177, 4544, -1, 1339, 1365, 1105, 1193, 3097, 1311, 1083, 1069, 1119, 1269}, new double[]{37.5, 75, 112.5, 187.5}, new int[]{66, 98, 162, 210, 267}),
        MITHRIL(2359, 50, new int[]{1209, 1355, 1428, 1143, 9379, 1285, 822, 4822, -1, -1, -1, 42, 1329, 9427, 1299, 866, 1159, 1181, -1, 9416, 1343, 1369, 1109, 1197, 3099, 1315, 1085, 1071, 1121, 1273}, new double[]{50, 100, 150, 250}, new int[]{66, 170, 210, 267}),
        ADAMANT(2361, 70, new int[]{1211, 1357, 1430, 1145, 9380, 1287, 823, 4823, -1, -1, -1, 43, 1331, 9429, 1301, 867, 1161, 1183, -1, -1, 1345, 1371, 1111, 1199, 3100, 1317, 1091, 1073, 1123, 1271}, new double[]{62.5, 125, 187.5, 312.5}, new int[]{66, 210, 267}),
        RUNE(2363, 85, new int[]{1213, 1359, 1432, 1147, 9381, 1289, 824, 4824, -1, -1, -1, 44, 1333, 9431, 1303, 868, 1163, 1185, -1, -1, 1347, 1373, 1113, 1201, 3101, 1319, 1093, 1079, 1127, 1275}, new double[]{75, 150, 225, 375}, new int[]{66, 210, 267});

        private int barId;
        private int[] activatedChildren;
        private int[] items;
        private double[] experience; //dont know what to name this lol
        private int baseLevel;

        private ForgingBar(int barId, int baseLevel, int[] items, double[] experience, int[] activatedChildren) {
            this.barId = barId;
            this.baseLevel = baseLevel;
            this.items = items;
            this.experience = experience;
            this.activatedChildren = activatedChildren;
        }

        public int[] getItems() {
            return items;
        }

        public int getBaseLevel() {
            return baseLevel;
        }

        public int getBarId() {
            return barId;
        }

        public double[] getExperience() {
            return experience;
        }

        private static Map<Integer, ForgingBar> smithingBars = new HashMap<Integer, ForgingBar>();

        static {
            for (ForgingBar bar : ForgingBar.values()) {
                smithingBars.put(bar.barId, bar);
            }
        }

        public static ForgingBar forId(int id) {
            return smithingBars.get(id);
        }
    }

    public static final int SMITHING_INTERFACE = 300;
    public static final Item HAMMER = new Item(2347);

    public static final int[] CHILD_IDS = new int[30];

    public static final int[] HIDDEN_CHILD_IDS =
            {
                    65, 81, 89, 97, 161, 169, 209, 266
            };

    public static final int[] CLICK_OPTIONS =
            {
                    28, 32767, 5, 1
            };

    private static Map<String, Integer> levelThreshold = new HashMap<String, Integer>();

    static {
        int counter = 18;
        for (int i = 0; i < CHILD_IDS.length; i++) {
            if (counter == 250) {
                counter = 267;
            }
            CHILD_IDS[i] = counter;
            counter += 8;
        }
        levelThreshold.put("dagger", 1);
        levelThreshold.put("hatchet", 2);
        levelThreshold.put("mace", 2);
        levelThreshold.put("bolts", 3);
        levelThreshold.put("med helm", 3);
        levelThreshold.put("sword", 4);
        levelThreshold.put("dart tip", 4);
        levelThreshold.put("nails", 4);
        levelThreshold.put("wire", 4);
        levelThreshold.put("arrowtips", 5);
        levelThreshold.put("pickaxe", 5);
        levelThreshold.put("scimitar", 5);
        levelThreshold.put("longsword", 6);
        levelThreshold.put("limbs", 6);
        levelThreshold.put("knife", 7);
        levelThreshold.put("full helm", 7);
        levelThreshold.put("sq shield", 8);
        levelThreshold.put("warhammer", 9);
        levelThreshold.put("battleaxe", 10);
        levelThreshold.put("chainbody", 11);
        levelThreshold.put("kiteshield", 12);
        levelThreshold.put("claws", 13);
        levelThreshold.put("2h sword", 14);
        levelThreshold.put("plateskirt", 16);
        levelThreshold.put("platelegs", 16);
        levelThreshold.put("platebody", 18);
        levelThreshold.put("iron spit", 2);
        levelThreshold.put("oil lantern", 11);
        levelThreshold.put("grapple tip", 9);
        levelThreshold.put("studs", 6);
        levelThreshold.put("bullseye lantern", 19);
    }

    public static String[] getNameForBar(Player player, ForgingBar bar, int index, int itemId) {
        if (itemId == -1 || index < 0 || index >= bar.getItems().length) {
            return null;
        }
        StringBuilder barName = new StringBuilder();
        StringBuilder levelString = new StringBuilder();
        String name = ItemDefinition.forId(itemId).getName().toLowerCase();
        String barVariableName = bar.toString().toLowerCase();
        int levelRequired = bar.baseLevel + getLevelIncrement(bar, bar.getItems()[index]);
        int barAmount = getBarAmount(levelRequired, bar, itemId);
        if (player.getInventory().getContainer().getItemCount(bar.barId) >= barAmount) {
            barName.append("<col=00FF00>");
        }
        barName.append(barAmount).append(" ").append(barAmount > 1 ? "bars" : "bar");
        if (levelRequired >= 99) {
            levelRequired = 99;
        }
        if (player.getSkills().getLevel(Skills.SMITHING) >= levelRequired) {
            levelString.append("<col=FFFFFF>");
        }
        levelString.append(Misc.upperFirst(name.replace(barVariableName + " ", "")));
        return new String[]{levelString.toString(), barName.toString()};
    }

    public static int getBarAmount(int levelRequired, ForgingBar bar, int id) {
        if (levelRequired >= 99) {
            levelRequired = 99;
        }
        int level = levelRequired - bar.baseLevel;
        String name = ItemDefinition.forId(id).getName().toLowerCase();
        if (level >= 0 && level <= 4) {
            return 1;
        } else if (level >= 4 && level <= 8) {
            if (name.contains("knife") || name.contains("arrowtips") || name.contains("limb") || name.contains("studs")) {
                return 1;
            }
            return 2;
        } else if (level >= 9 && level <= 16) {
            if (name.contains("grapple")) {
                return 1;
            } else if (name.contains("claws")) {
                return 2;
            }
            return 3;
        } else if (level >= 17) {
            if (name.contains("bullseye")) {
                return 1;
            }
            return 5;
        }
        return 1;
    }

    public static int getLevelIncrement(ForgingBar bar, int id) {
        String name = ItemDefinition.forId(id).getName().toLowerCase();
        for (Map.Entry<String, Integer> entry : levelThreshold.entrySet()) {
            if (name.contains(entry.getKey())) {
                int increment = entry.getValue();
                if (name.contains("dagger") && bar != ForgingBar.BRONZE) {
                    increment--;
                } else if (name.contains("hatchet") && bar == ForgingBar.BRONZE) {
                    increment--;
                }
                return increment;
            }
        }
        System.out.println(name + " hasn't been added to the level increment map!");
        return 1;
    }

    public static int getItemAmount(int id) {
        String name = ItemDefinition.forId(id).getName();
        if (name.contains("knife")) {
            return 5;
        } else if (name.contains("bolts") || name.contains("dart tip")) {
            return 10;
        } else if (name.contains("arrowtips") || name.contains("nails")) {
            return 15;
        }
        return 1;
    }

    public static void activateChildren(Player player, ForgingBar bar) {
        for (int i : bar.activatedChildren) {
            ActionSender.sendInterfaceConfig(player, SMITHING_INTERFACE, i - 1, true);
        }
    }

    public static int[] barToIntArray(List<SmeltingBar> bar) {
        int[] newArray = new int[bar.size()];
        for (int i = 0; i < bar.size(); i++) {
            newArray[i] = bar.get(i).ordinal();
        }
        return newArray;
    }

    public static int getMaxAmount(Player player, List<SmeltingBar> bars) {
        int amount = 0;
        for (SmeltingBar bar : bars) {
            int myAmount = player.getInventory().getContainer().getNumberOf(bar.getItemsRequired()[0]);
            if (bar.getItemsRequired().length > 1) {
                if (bar.getItemsRequired()[1].getDefinition().getName().equalsIgnoreCase("coal")) {
                    int coalAmount = player.getInventory().getContainer().getNumberOf(bar.getItemsRequired()[1]) / bar.getItemsRequired()[1].getAmount();
                    if (coalAmount > 0 && myAmount > coalAmount) {
                        myAmount = coalAmount;
                    }
                } else {
                    int thisAmount = player.getInventory().getContainer().getNumberOf(bar.getItemsRequired()[1]);
                    if (thisAmount > 0 && myAmount > thisAmount) {
                        myAmount = thisAmount;
                    }
                }
            }
            if (myAmount > amount) {
                amount = myAmount;
            }
        }
        return amount;
    }

    public static String getCoalAmount(SmeltingBar bar) {
        switch (bar) {
            case STEEL:
                return "two";
            case MITHRIL:
                return "four";
            case ADAMANT:
                return "six";
            case RUNE:
                return "eight";
        }
        return null;
    }

}
