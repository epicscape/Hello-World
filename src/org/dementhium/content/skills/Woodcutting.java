package org.dementhium.content.skills;

import org.dementhium.action.HarvestAction;
import org.dementhium.event.EventListener;
import org.dementhium.event.EventManager;
import org.dementhium.model.Item;
import org.dementhium.model.Location;
import org.dementhium.model.definition.ItemDefinition;
import org.dementhium.model.map.GameObject;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;
import org.dementhium.util.Misc;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public final class Woodcutting extends HarvestAction {


    public static class WoodcuttingListener extends EventListener {

        @Override
        public void register(EventManager manager) {
            Tree[] trees = Tree.values();
            for (Tree tree : trees) {
                for (int treeId : tree.getTreeIds()) {
                    manager.registerObjectListener(treeId, this);
                }
            }
        }

        public boolean objectOption(Player player, int objectId, GameObject gameObject, Location location, ClickOption option) {
            if (option == ClickOption.FIRST) {
                player.registerAction(new Woodcutting(Tree.forId(objectId), gameObject));
            }
            return true;
        }

    }


    /**
     * @author Michael (Scu11)
     */
    public static enum Hatchet implements HarvestTool {

        DRAGON(6739, 61, Animation.create(2846)), // 870
        RUNE(1359, 41, Animation.create(867)), //872
        ADAMANT(1357, 31, Animation.create(869)),// 874
        MITHRIL(1355, 21, Animation.create(871)), // 876
        BLACK(1361, 6, Animation.create(873)), //878
        STEEL(1353, 6, Animation.create(875)), //880
        IRON(1349, 1, Animation.create(877)), //2847
        BRONZE(1351, 1, Animation.create(879));

        private int id;
        private int level;
        private Animation animation;

        private static final Map<Integer, Hatchet> hatchets = new HashMap<Integer, Hatchet>();

        static {
            for (Hatchet hatchet : Hatchet.values()) {
                hatchets.put(hatchet.id, hatchet);
            }
        }

        private Hatchet(int id, int level, Animation animation) {
            this.id = id;
            this.level = level;
            this.animation = animation;
        }

        @Override
        public Animation getAnimation() {
            return animation;
        }

        @Override
        public int getRequiredLevel() {
            return level;
        }
    }

    /**
     * @author Michael (Scu11)
     */
    public static enum Tree implements HarvestObject {

        NORMAL(1511, 1, 25, 5, 1, new int[]{1276, 1277, 1278, 1279, 1280, 1282,
                1283, 1284, 1285, 1286, 1289, 1290, 1291, 1315, 1316, 1318,
                1319, 1330, 1331, 1332, 1365, 1383, 1384, 3033, 3034, 3035,
                3036, 3881, 3882, 3883, 5902, 5903, 5904}),
        WILLOW(1519, 30, 67.5, 30, 4, new int[]{1308, 5551, 5552, 5553}),
        OAK(1521, 15, 37.5, 15, 2, new int[]{1281, 3037}),
        MAGIC(1513, 75, 250, 120, 9, new int[]{1306}),
        MAPLE(1517, 45, 100, 60, 5, new int[]{1307, 4674}),
        MAHOGANY(6332, 50, 125, 80, 4, new int[]{9034}),
        TEAK(6333, 35, 85, 100, 4, new int[]{9036}),
        ACHEY(2862, 1, 25, 80, 4, new int[]{2023}),
        YEW(1515, 60, 175, 160, 7, new int[]{1309}),
        DRAMEN(771, 36, 0, 22, 4, new int[]{1292});
        //VINES(-1, 20, 5, 160, 1, new int[] { 5103, 5104, 5105 });

        private int[] objects;
        private int level;
        private int log;
        private int respawnTimer;
        private int maxLogs;
        private double experience;

        private static Map<Integer, Tree> trees = new HashMap<Integer, Tree>();

        public static Tree forId(int object) {
            return trees.get(object);
        }

        static {
            for (Tree tree : Tree.values()) {
                for (int object : tree.objects) {
                    trees.put(object, tree);
                }
            }
        }

        private Tree(int log, int level, double experience, int respawnTimer, int maxLogs, int[] objects) {
            this.objects = objects;
            this.level = level;
            this.experience = experience;
            this.respawnTimer = respawnTimer;
            this.maxLogs = maxLogs;
            this.log = log;
        }

        @Override
        public int getRequiredLevel() {
            return level;
        }

        @Override
        public double getExperience() {
            return experience;
        }

        @Override
        public int getMaxHealth() {
            return maxLogs;
        }

        @Override
        public int getRestoreDelay() {
            return respawnTimer;
        }

        public int[] getTreeIds() {
            return objects;
        }
    }

    private final Tree tree;

    private Hatchet hatchet;
    private GameObject object;

    public Woodcutting(Tree tree, GameObject object) {
        this.tree = tree;
        this.object = object;
    }

    @Override
    public Item getReward() {
        if (tree.log > -1)
            return new Item(tree.log, 1);
        else
            return null;
    }

    @Override
    public String getMessage(int type) {
        switch (type) {
            case TOOL_LEVEL:
                return "You do not have the required woodcutting level to use this hatchet.";
            case NO_TOOL:
                return "You do not have a hatchet to cut this tree with.";
            case OBJECT_LEVEL:
                return "You do not have the required woodcutting level to chop this tree.";
            case HARVESTED_ITEM:
                if (tree.log > -1) {
                    return "You get some " + ItemDefinition.forId(tree.log).getName().toLowerCase() + ".";
                } else {
                    return "You chops down the " + object.getDefinition().getName() + ".";
                }
        }
        return "";
    }

    @Override
    public String getStartMessage() {
        return "You start swinging your axe at the tree...";
    }

    @Override
    public int getSkill() {
        return Skills.WOODCUTTING;
    }

    @Override
    public int getCycleTime() {
        int skill = mob.getPlayer().getSkills().getLevel(Skills.WOODCUTTING);
        int level = tree.level;
        int modifier = hatchet.level;
        int randomAmt = Misc.random(3);
        double cycleCount = Math.ceil((level * 50 - skill * 10) / modifier * 0.25 - randomAmt * 4);
        if (cycleCount < 1) {
            cycleCount = 1;
        }
        return (int) cycleCount + 1;
    }

    @Override
    public HarvestObject getHarvestObject() {
        return tree;
    }

    @Override
    public GameObject getGameObject() {
        return object;
    }

    @Override
    public int getReplacement(GameObject gameObject) {
        int id = gameObject.getId();
        String name = gameObject.getDefinition().getName().toLowerCase();
        switch (id) {
            case 3034:
            case 3033:
                return 1341;
            case 1383:
                return 1358;
            case 1282:
            case 3035:
                return 1347;
        }
        if (name.equals("vines")) {
            System.out.println("test1");
            return -1;
        }
        if (name.equals("tree") || name.equals("evergreen")) {
            return 1342;
        } else if (name.equals("oak")) {
            return 1356;
        } else if (name.equals("willow")) {
            return 5554;
        } else if (name.contains("yew")) {
            return 7402;
        } else if (name.contains("maple")) {
            return 1343;
        } else if (name.equals("teak")) {
            return 9037;
        } else if (name.equals("mahogany")) {
            return 9035;
        } else if (name.equals("achey tree")) {
            return 3371;
        } else if (name.equals("magic tree")) {
            return 7401;
        } else if (name.equals("dead tree")) {
            return 1341;
        }
        return 1342; // default tree stump
    }

    @Override
    public HarvestTool getTool() {
        if (hatchet == null) {
            Player player = mob.getPlayer();
            for (Map.Entry<Integer, Hatchet> entry : Hatchet.hatchets.entrySet()) {
                if (player.getInventory().contains(entry.getKey()) || player.getEquipment().getSlot(3) == entry.getKey()) {
                    hatchet = entry.getValue();
                    if (mob.getPlayer().getSkills().getLevel(Skills.WOODCUTTING) >= hatchet.level) {
                        break;
                    }
                }
            }
        }
        return hatchet;
    }


}
