package org.dementhium.content.activity.impl.castlewars;

import org.dementhium.action.HarvestAction;
import org.dementhium.content.activity.impl.CastleWarsActivity;
import org.dementhium.content.areas.Area;
import org.dementhium.content.skills.Mining.PickAxe;
import org.dementhium.model.Item;
import org.dementhium.model.Location;
import org.dementhium.model.map.GameObject;
import org.dementhium.model.misc.DamageManager.DamageType;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;

/**
 * Handles the mining of the rocks or the walls in the underground of Castle Wars.
 *
 * @author Emperor
 */
public class RockMiningAction extends HarvestAction {

    /**
     * The object to mine.
     */
    private GameObject object;

    /**
     * The pickaxe used.
     */
    private PickAxe pickaxe;

    /**
     * The castle object.
     */
    private CastleObject castleObject;

    /**
     * The rocks location (used when mining the wall).
     */
    private final Location location;

    /**
     * The area used.
     */
    private final Area area;

    /**
     * Represents an object to mine in the castle wars activity.
     *
     * @author Emperor
     */
    private static enum CastleObject implements HarvestObject {
        WALL(4448),
        ROCKS_1(4437),
        ROCKS_2(4438);

        private final int id;

        private CastleObject(int id) {
            this.id = id;
        }

        @Override
        public int getRequiredLevel() {
            return 0;
        }

        @Override
        public double getExperience() {
            return 0;
        }

        @Override
        public int getMaxHealth() {
            return 1;
        }

        @Override
        public int getRestoreDelay() {
            return -1;
        }

    }

    /**
     * Constructs a new {@code RockMiningAction} {@code Object}.
     */
    public RockMiningAction(GameObject object, Location location, Area area) {
        this.object = object;
        for (CastleObject co : CastleObject.values()) {
            if (co.id == object.getId()) {
                this.castleObject = co;
                break;
            }
        }
        this.area = area;
        this.location = location;
    }

    @Override
    public Item getReward() {
        if (object.getId() == 4448) {
            for (Player player : CastleWarsActivity.getSingleton().getZamorakTeam().getPlayers()) {
                if (area.contains(player.getLocation())) {
                    player.setAttribute("cantMove", true);
                    player.getDamageManager().miscDamage(player.getSkills().getHitPoints(), DamageType.RED_DAMAGE);
                }
            }
            for (Player player : CastleWarsActivity.getSingleton().getSaradominTeam().getPlayers()) {
                if (area.contains(player.getLocation())) {
                    player.setAttribute("cantMove", true);
                    player.getDamageManager().miscDamage(player.getSkills().getHitPoints(), DamageType.RED_DAMAGE);
                }
            }
            int rotation = (area.getName().equals("WestRocks") || area.getName().equals("EastRocks")) ? 1 : 0;
            CastleWarsObjects.addObject(new GameObject(4437, location, 10, rotation));
            if (area.getName().equals("NorthRocks")) {
                CastleWarsActivity.getSingleton().getZamorakTeam().setRockCleared(false);
            } else if (area.getName().equals("WestRocks")) {
                CastleWarsActivity.getSingleton().getZamorakTeam().setRockCleared2(false);
            } else if (area.getName().equals("SouthRocks")) {
                CastleWarsActivity.getSingleton().getSaradominTeam().setRockCleared(false);
            } else if (area.getName().equals("EastRocks")) {
                CastleWarsActivity.getSingleton().getSaradominTeam().setRockCleared2(false);
            }
        } else if (object.getId() == 4437) {
            CastleWarsObjects.removeObject(object.getLocation(), object.getType());
            CastleWarsObjects.addObject(new GameObject(4438, object.getLocation(), object.getType(), object.getRotation()));
        } else if (object.getId() == 4438) {
            CastleWarsObjects.removeObject(object.getLocation(), object.getType());
            if (area.getName().equals("NorthRocks")) {
                CastleWarsActivity.getSingleton().getZamorakTeam().setRockCleared(true);
            } else if (area.getName().equals("WestRocks")) {
                CastleWarsActivity.getSingleton().getZamorakTeam().setRockCleared2(true);
            } else if (area.getName().equals("SouthRocks")) {
                CastleWarsActivity.getSingleton().getSaradominTeam().setRockCleared(true);
            } else if (area.getName().equals("EastRocks")) {
                CastleWarsActivity.getSingleton().getSaradominTeam().setRockCleared2(true);
            }
        }
        return null;
    }

    @Override
    public String getStartMessage() {
        return "You swing your pick at the " + (object.getId() == 4448 ? "wall" : "rocks") + "...";
    }

    @Override
    public String getMessage(int type) {
        switch (type) {
            case NO_TOOL:
                return "You do not have a pickaxe to mine this.";
            case HARVESTED_ITEM:
                return object.getId() == 4448 ? "You've collapsed the tunnel!" : "You clear some of the rocks.";
        }
        return "";
    }

    @Override
    public int getSkill() {
        return Skills.MINING;
    }

    @Override
    public int getCycleTime() {
        int skill = mob.getPlayer().getSkills().getLevel(getSkill());
        int level = 50;
        int modifier = pickaxe.getRequiredLevel();
        double cycleCount = 1;
        cycleCount = Math.ceil((level * 50 - skill * 10) / modifier * 0.0625 * 4);
        if (cycleCount < 1) {
            cycleCount = 1;
        }
        return (int) cycleCount;
    }

    @Override
    public HarvestTool getTool() {
        Player player = mob.getPlayer();
        for (PickAxe value : PickAxe.values()) {
            if (player.getInventory().contains(value.id) || player.getEquipment().getSlot(3) == value.id) {
                pickaxe = value;
                if (mob.getPlayer().getSkills().getLevel(Skills.MINING) >= value.level) {
                    break;
                }
            }
        }
        return pickaxe;
    }

    @Override
    public HarvestObject getHarvestObject() {
        return castleObject;
    }

    @Override
    public GameObject getGameObject() {
        return object;
    }

    @Override
    public int getReplacement(GameObject gameObject) {
        return -1;
    }

}
