package org.dementhium.content.skills;

import org.dementhium.action.HarvestAction;
import org.dementhium.event.EventListener;
import org.dementhium.event.EventManager;
import org.dementhium.model.Item;
import org.dementhium.model.Location;
import org.dementhium.model.World;
import org.dementhium.model.definition.ItemDefinition;
import org.dementhium.model.map.GameObject;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;
import org.dementhium.tickable.Tick;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 'Mystic Flow
 */
public class Mining extends HarvestAction {

    public static class MiningListener extends EventListener {

        @Override
        public void register(EventManager manager) {
            Rock[] rocks = Rock.values();
            for (Rock rock : rocks) {
                for (int rockId : rock.getObjectIds()) {
                    manager.registerObjectListener(rockId, this);
                }
            }
        }

        public boolean objectOption(Player player, int objectId, GameObject gameObject, Location location, ClickOption option) {
            if (option == ClickOption.FIRST) {
            	Mining mining = new Mining(Rock.forId(objectId), gameObject);
            	mining.setEntity(player);
            	mining.execute();
                player.registerAction(mining);
            } else if (option == ClickOption.SECOND) {
                prospect(player, Rock.forId(objectId));
            }
            return true;
        }

        public void prospect(final Player player, final Rock rock) {
            if (player.getAttribute("prospecting") == null) {
                player.getActionManager().stopAction();
                player.sendMessage("You examine the rock for ores...");
                player.setAttribute("cantWalk", Boolean.TRUE);
                player.setAttribute("prospecting", Boolean.TRUE);
                World.getWorld().submit(new Tick(3) {
                    @Override
                    public void execute() {
                        String name = rock.toString();
                        String message = new StringBuilder("This rock contains ").append(name.toLowerCase()).append(".").toString();
                        player.sendMessage(message);
                        player.removeAttribute("cantWalk");
                        player.removeAttribute("prospecting");
                        stop();
                    }
                });
            }
        }

    }

    /**
     * @author 'Mystic Flow
     */
    public static enum PickAxe implements HarvestTool {
        INFERNO_ADZE(13661, 41, Animation.create(10222)),
        RUNE(1275, 41, Animation.create(10006)),
        ADAMANT(1271, 31, Animation.create(10005)),
        MITHRIL(1273, 21, Animation.create(10004)),
        STEEL(1269, 11, Animation.create(10003)),
        IRON(1267, 5, Animation.create(10002)),
        BRONZE(1265, 1, Animation.create(10001));

        public int id;
        public int level;
        private Animation animation;

        private PickAxe(int id, int level, Animation animation) {
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
     * @author 'Mystic Flow
     */
    public static enum Rock implements HarvestObject {
        CLAY(434, 1, 5, 2, new int[]{11189, 11190, 11191, 15503, 15504, 15505, 31062, 31063, 31064, 32429, 32430, 32431}),
        COPPER(436, 1, 17.5, 6, new int[]{5780, 5779, 5781, 11936, 11937, 11938, 11960, 11961, 11962, 31080, 31081, 31082}),
        TIN(438, 1, 17.5, 6, new int[]{5776, 5777, 5778, 11933, 11934, 11935, 11957, 11958, 11959, 31077, 31078, 31079}),
        IRON(440, 15, 35, 15, new int[]{5773, 5774, 5775, 11954, 11955, 11956, 14856, 14857, 14858, 31071, 31072, 31073, 32441, 32442, 32443, 37307, 37308, 37309}),
        SILVER(442, 20, 40, 100, new int[]{2311, 11165, 11186, 11187, 11188, 11948, 11949, 11950, 15579, 15580, 15581, 31074, 31075, 31076, 32444, 32445, 32446, 37304, 37305, 37306}),
        GOLD(444, 40, 65, 100, new int[]{5768, 5769, 11183, 11184, 11185, 11951, 11952, 11953, 15576, 15577, 15578, 31065, 31066, 31067, 32432, 32433, 32434, 37310, 37312}),
        COAL(453, 30, 50, 50, new int[]{5770, 5771, 5772, 11930, 11931, 11932, 14850, 14851, 14852, 31068, 31069, 31070, 32426, 32427, 32428}),
        MITHRIL(447, 55, 80, 200, new int[]{5784, 5786, 11942, 11943, 11944, 11945, 11946, 11947, 14853, 14854, 14855, 31086, 31087, 31088, 32438, 32439, 32440}),
        ADAMANTITE(449, 70, 95, 400, new int[]{5782, 5783, 11939, 11940, 11941, 11963, 11964, 11965, 14862, 14863, 14864, 31083, 31084, 31085, 32435, 32436, 32437}),
        RUNE(451, 85, 125, 1000, new int[]{14859, 14860, 14861}),
        ESSENCE(1436 << 16 | 7936, 1, 5, -1, new int[] {2491});

        private int[] objects;
        private int level;
        private int ore;
        private int respawnTimer;
        private double experience;

        private static Map<Integer, Rock> rocks = new HashMap<Integer, Rock>();

        public static Rock forId(int object) {
            return rocks.get(object);
        }

        static {
            for (Rock rock : Rock.values()) {
                for (int object : rock.objects) {
                    rocks.put(object, rock);
                }
            }
        }

        private Rock(int ore, int level, double experience, int respawnTimer, int[] objects) {
            this.objects = objects;
            this.level = level;
            this.experience = experience;
            this.respawnTimer = respawnTimer;
            this.ore = ore;
        }

        public int getOreId() {
            return ore;
        }

        public int[] getObjectIds() {
            return objects;
        }

        public int getRequiredLevel() {
            return level;
        }

        public double getExperience() {
            return experience;
        }

        public int getRespawnTimer() {
            return respawnTimer;
        }

        @Override
        public int getMaxHealth() {
            return ore > 0xFFFF ? Integer.MAX_VALUE - 1 : 1;
        }

        @Override
        public int getRestoreDelay() {
            return respawnTimer;
        }

    }

    private final Rock rock;

    private PickAxe pickAxe;
    private GameObject object;

    public Mining(Rock rock, GameObject object) {
        this.rock = rock;
        this.object = object;
    }


    @Override
    public int getCycleTime() {
        int skill = mob.getPlayer().getSkills().getLevel(getSkill());
        int level = rock.getRequiredLevel();
        int modifier = pickAxe.getRequiredLevel();
        double cycleCount = 1;
        cycleCount = Math.ceil((level * 50 - skill * 10) / modifier * 0.0625 * 4);
        if (cycleCount < 1) {
            cycleCount = 1;
        }
        return (int) cycleCount;
    }

    @Override
    public GameObject getGameObject() {
        return object;
    }

    @Override
    public HarvestObject getHarvestObject() {
        return rock;
    }

    @Override
    public String getMessage(int type) {
        switch (type) {
            case TOOL_LEVEL:
                return "You do not have the required mining level to use this pickaxe.";
            case NO_TOOL:
                return "You do not have a pickaxe to mine this rock with.";
            case OBJECT_LEVEL:
                return "You do not have the required mining level to mine this rock.";
            case HARVESTED_ITEM:
            	if (rock.ore > 0xFFFF) {
            		return null;
            	}
                return "You get some " + ItemDefinition.forId(rock.ore).getName().toLowerCase() + ".";
        }
        return "";
    }

    @Override
    public int getReplacement(GameObject gameObject) {
        int originalId = gameObject.getId();
        int emptyRock = 0;

        if (originalId == 2311) {
            return 11552;
        }

        //added
        for (int i = 37304; i <= 37312; i++) {
            if (emptyRock == 11555 || emptyRock == 0) {
                emptyRock = 11552;
            }
            if (originalId == i) {
                return emptyRock;
            }
            emptyRock++;
        }

        /*
           * Dark brown rocks
           */
        emptyRock = 0;
        for (int i = 11945; i <= 11966; i++) {
            if (emptyRock == 11558 || emptyRock == 0) {
                emptyRock = 11555;
            }
            if (originalId == i) {
                return emptyRock;
            }
            emptyRock++;
        }
        /*
           * Very dark brown rocks
           */
        for (int i = 32426; i <= 32446; i++) {
            if (emptyRock == 33403 || emptyRock == 0) {
                emptyRock = 33400;
            }
            if (originalId == i) {
                return emptyRock;
            }
            emptyRock++;
        }
        /*
           * Sand-coloured rocks
           */
        emptyRock = 0;
        for (int i = 5779; i <= 5781; i++) {
            if (emptyRock == 11555 || emptyRock == 0) {
                emptyRock = 11552;
            }
            if (originalId == i) {
                return emptyRock;
            }
            emptyRock++;
        }
        for (int i = 11183; i <= 11191; i++) {
            if (emptyRock == 11555 || emptyRock == 0) {
                emptyRock = 11552;
            }
            if (originalId == i) {
                return emptyRock;
            }
            emptyRock++;
        }
        /*
           * Beige coloured rocks
           */
        emptyRock = 0;
        for (int i = 11930; i <= 11944; i++) {
            if (emptyRock == 11555 || emptyRock == 0) {
                emptyRock = 11552;
            }
            if (originalId == i) {
                return emptyRock;
            }
            emptyRock++;
        }
        /*
           * Very light coloured sandy rocks
           */
        emptyRock = 0;
        for (int i = 31062; i <= 31088; i++) {
            if (emptyRock == 31062 || emptyRock == 0) {
                emptyRock = 31059;
            }
            if (originalId == i) {
                return emptyRock;
            }
            emptyRock++;
        }
        /*
           * Black rocks
           */
        emptyRock = 0;
        for (int i = 14850; i <= 14864; i++) {
            if (emptyRock == 14835 || emptyRock == 0) {
                emptyRock = 14832;
            }
            if (originalId == i) {
                return emptyRock;
            }
            emptyRock++;
        }
        return 11552;
    }

    @Override
    public Item getReward() {
    	if (rock.ore > 0xFFFF) {
    		if (mob.getPlayer().getSkills().getLevel(getSkill()) >= 30) {
    			return new Item(rock.ore & 0xffff);
    		} else {
    			return new Item(rock.ore >> 16);
    		}
    	}
        return new Item(rock.ore, 1);
    }

    @Override
    public int getSkill() {
        return Skills.MINING;
    }

    @Override
    public String getStartMessage() {
        return "You swing your pick at the rock...";
    }

    @Override
    public HarvestTool getTool() {
        if (pickAxe == null) {
            Player player = mob.getPlayer();
            for (PickAxe value : PickAxe.values()) {
                if (player.getInventory().contains(value.id) || player.getEquipment().getSlot(3) == value.id) {
                    pickAxe = value;
                    if (mob.getPlayer().getSkills().getLevel(Skills.MINING) >= value.level) {
                        break;
                    }
                }
            }
        }
        return pickAxe;
    }
}
