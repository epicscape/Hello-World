package org.dementhium.content.activity.impl.duel;

import org.dementhium.content.activity.impl.DuelActivity;
import org.dementhium.content.areas.Area;
import org.dementhium.model.Container;
import org.dementhium.model.Item;
import org.dementhium.model.Location;
import org.dementhium.model.World;
import org.dementhium.model.map.Region;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Random;

/**
 * Represents the dueling rules agreed by the 2 dueling players.
 *
 * @author Emperor
 */
public class DuelConfigurations {

    /**
     * The array of all fun weapon item id's.
     * TODO: Add in item id's.
     */
    public static final short[] FUN_WEAPONS = {};

    /**
     * The array of strings used on the second challenge screen.
     */
    private static final String[] DUEL_TEXT = {
            /*
            * During the duel...
            */
            "You cannot use Ranged attacks.",
            "You cannot use Melee attacks.",
            "You cannot use Magic attacks.",
            "You can only use 'fun weapons.'",
            "You cannot forfeit the duel.",
            "You cannot use drinks.",
            "You cannot use food.",
            "You cannot use Prayer.",
            "You cannot move.",
            "There will be obstacles in the arena.",
            "You cannot use special attacks.",
            "Summoning familiars can assist you in battle.",
            /*
            * Before the duel..
            */
            "Some worn items will be taken off.",
            "Some worn items will be taken off.",
            "Some worn items will be taken off.",
            "Some worn items will be taken off.",
            "Some worn items will be taken off.",
            "Some worn items will be taken off.",
            "Some worn items will be taken off.",
            "Some worn items will be taken off.",
            "Some worn items will be taken off.",
            "Some worn items will be taken off.",
            "Some worn items will be taken off.",
    };


    /**
     * The rules enum.
     *
     * @author Emperor
     */
    public static enum Rules {
        RANGE(16),
        MELEE(32),
        MAGIC(64),
        FUN_WEAPONS(4096),
        FORFEIT(1),//TODO
        DRINKS(128),
        FOOD(256),
        PRAYER(512),
        MOVEMENT(2),
        OBSTACLES(1024),
        SPECIAL_ATTACKS(8192),
        SUMMONING(268435456),
        HAT(16384),
        CAPE(32768),
        AMULET(65536),
        WEAPON(131072),
        BODIE(262144),
        SHIELD(524288),
        LEG(2097152),
        GLOVE(8388608),
        BOOT(16777216),
        RING(67108864),
        ARROW(134217728);


        /**
         * The configuration value.
         */
        private final int value;

        Rules(int configurationValue) {
            this.value = configurationValue;
        }
    }

    /**
     * The bitset of rules.
     */
    private BitSet rules = new BitSet();

    /**
     * The configurations id of summoning enabled.
     */
    public static final byte SUMMONING = 5;

    /**
     * The configurations id of obstacles arena.
     */
    public static final byte OBSTACLES = 6;

    /**
     * Sets a rule.
     *
     * @param rule The rule to set.
     * @return {@code True}.
     */
    public boolean setRule(Rules rule, boolean flag) {
        rules.set(rule.ordinal(), flag);
        return true;
    }

    /**
     * Gets the flag of the given rule.
     *
     * @param rule The rule.
     * @return {@code True} if the rule has been enabled,
     *         <br>		{@code false} if not.
     */
    public boolean getRule(Rules rule) {
        return rules.get(rule.ordinal());
    }

    /**
     * Swaps a rule.
     *
     * @param player The player.
     * @param other  The other player.
     * @param rule   The rule.
     * @return {@code True}.
     */
    public boolean swapRule(Player player, Player other, Rules rule) {
        if (canSetRule(player, rule)) {
            rules.set(rule.ordinal(), !rules.get(rule.ordinal()));
            refresh(player);
            refresh(other);
            return true;
        }
        return false;
    }

    /**
     * Checks if the rule can be changed.
     *
     * @param player The player.
     * @param rule   The rule.
     * @return {@code True} if so, {@code false} if not.
     */
    public boolean canSetRule(Player player, Rules rule) {
        if (rules.get(rule.ordinal())) {
            return true;
        }
        switch (rule) {
            case MAGIC:
                if (rules.get(Rules.MELEE.ordinal()) && rules.get(Rules.RANGE.ordinal())) {
                    player.sendMessage("You have to be able to use atleast one combat style in a duel.");
                    return false;
                }
                return true;
            case MELEE:
                if (rules.get(Rules.MAGIC.ordinal()) && rules.get(Rules.RANGE.ordinal())) {
                    player.sendMessage("You have to be able to use atleast one combat style in a duel.");
                    return false;
                }
                return true;
            case RANGE:
                if (rules.get(Rules.MELEE.ordinal()) && rules.get(Rules.MAGIC.ordinal())) {
                    player.sendMessage("You have to be able to use atleast one combat style in a duel.");
                    return false;
                }
                return true;
            case OBSTACLES:
                if (rules.get(Rules.SUMMONING.ordinal())) {
                    setRule(Rules.SUMMONING, false);
                }
                if (rules.get(Rules.MOVEMENT.ordinal())) {
                    setRule(Rules.MOVEMENT, false);
                }
                return true;
            case MOVEMENT:
            case SUMMONING:
                if (rules.get(Rules.OBSTACLES.ordinal())) {
                    setRule(Rules.OBSTACLES, false);
                }
                return true;
            case WEAPON:
                if (rules.get(Rules.FUN_WEAPONS.ordinal())) {
                    player.sendMessage("You can't have weapons disabled while the fun weapons rule is active.");
                    return false;
                }
                return true;
            case FUN_WEAPONS:
                if (rules.get(Rules.WEAPON.ordinal())) {
                    player.sendMessage("You can't have fun weapons active while weapons are disabled.");
                    return false;
                }
                return true;
        }
        return true;
    }

    /**
     * Refreshes the rules.
     *
     * @param player The player.
     * @return {@code True}.
     */
    public boolean refresh(Player player) {
        int value = 0;
        for (int i = 0; i < Rules.values().length; i++) {
            if (rules.get(i)) {
                value += Rules.values()[i].value;
            }
        }
        ActionSender.sendConfig(player, 286, value);
        return true;
    }

    /**
     * Sends the second challenge interface.
     *
     * @param player The player.
     */
    public void sendSecondInterface(Player player, Player other) {
        if (!player.getInventory().getContainer().hasSpaceFor(((Stakes) other.getAttribute("duelStakes")).getContainer())) {
            player.sendMessage("You do not have enough space in your inventory for the stake!");
            ((DuelActivity) player.getActivity()).decline(player);
            return;
        }
        int duringDuelOffset = 33;
        int beforeDuelOffset = 41;
        boolean wornItemWarning = false;
        for (int i = 28; i <= 44; i++) {
            if (i != 32) {
                ActionSender.sendString(player, "", DuelActivity.DUEL_SECOND_INTERFACE, i);
            }
        }
        ActionSender.sendString(player, "Modified stats will be restored.", DuelActivity.DUEL_SECOND_INTERFACE, beforeDuelOffset);
        beforeDuelOffset = 28;
        for (int i = 0; i < rules.size(); i++) {
            if (rules.get(i)) {
                if (i == 17) {
                    ActionSender.sendString(player, "You can't use two-handed weapons, like bows.", DuelActivity.DUEL_SECOND_INTERFACE, duringDuelOffset);
                    duringDuelOffset++;
                }
                if (i > 11) {
                    if ((i >= 11 && i <= 22) && i != 20) {
                        if (wornItemWarning) {
                            continue;
                        } else {
                            wornItemWarning = true;
                        }
                    }
                    ActionSender.sendString(player, DUEL_TEXT[i], DuelActivity.DUEL_SECOND_INTERFACE, beforeDuelOffset);
                    beforeDuelOffset++;
                    if (beforeDuelOffset == 42) {
                        beforeDuelOffset = 28;
                    }
                } else {
                    ActionSender.sendString(player, DUEL_TEXT[i], DuelActivity.DUEL_SECOND_INTERFACE, duringDuelOffset);
                    duringDuelOffset++;
                }
            }
        }
        ActionSender.sendString(other, "", DuelActivity.DUEL_SECOND_INTERFACE, 45);
        Container stake = ((Stakes) player.getAttribute("duelStakes")).getContainer();
        if (stake.freeSlots() < 28) {
            ActionSender.sendString(player, "", DuelActivity.DUEL_SECOND_INTERFACE, 25);
            ActionSender.sendString(other, "", DuelActivity.DUEL_SECOND_INTERFACE, 26);
        }
        ActionSender.sendInterface(player, DuelActivity.DUEL_SECOND_INTERFACE);
    }

    /**
     * Represents the different teleport locations.
     *
     * @author Emperor
     */
    public enum TeleportLocations {
        NORMAL_ARENA(World.getWorld().getAreaManager().getAreaByName("NormalArena")),
        OBSTACLES_ARENA(World.getWorld().getAreaManager().getAreaByName("ObstaclesArena")),
        SUMMONING_ARENA(World.getWorld().getAreaManager().getAreaByName("SummoningArena")),
        CHALLENGE_ROOM(World.getWorld().getAreaManager().getAreaByName("ChallengeRoom"));

        /**
         * The base area of the teleport.
         */
        private final Area area;

        /**
         * Constructs the teleport locations enum.
         *
         * @param area The area.
         */
        TeleportLocations(Area area) {
            this.area = area;
        }

        /**
         * @return the area
         */
        public Area getArea() {
            return area;
        }
    }

    /**
     * Teleports a player inside the duel arena.
     *
     * @param player The player.
     * @param stage  The teleport stage.
     */
    public static boolean teleport(Player player, TeleportLocations stage, boolean noMovement) {
        if (noMovement) {
            Location l = ((Player) player.getAttribute("duelingWith")).getLocation();
            Location to = getLocation(player, l);
            player.teleport(to.getX(), to.getY(), to.getZ());
            return true;
        }
        int x, y, clippingMask;
        List<Location> locations = new ArrayList<Location>();
        for (x = stage.getArea().swX; x < stage.getArea().nwX; x++) {
            for (y = stage.getArea().swY; y < stage.getArea().nwY; y++) {
                Location l = Location.locate(x, y, 0);
                clippingMask = Region.getClippingMask(l.getX(), l.getY(), l.getZ());
                if ((clippingMask & 0x1280180) == 0 && (clippingMask & 0x1280108) == 0
                        && (clippingMask & 0x1280120) == 0 && (clippingMask & 0x1280102) == 0) {
                    locations.add(l);
                }
            }
        }
        Random r = new Random();
        Location current = locations.get(r.nextInt(locations.size()));
        x = current.getX();
        y = current.getY();
        player.teleport(x, y, 0);
        return true;
    }

    /**
     * Gets the location for the player when the "No movement" rule is toggled.
     *
     * @param player The player.
     * @param l      The location of the other player.
     * @return The {@code Location} to teleport to.
     */
    private static Location getLocation(Player player, Location l) {
        List<Location> list = new ArrayList<Location>(4);
        list.add(l.transform(-1, 0, 0));
        list.add(l.transform(0, -1, 0));
        list.add(l.transform(1, 0, 0));
        list.add(l.transform(0, 1, 0));
        int clippingMask;
        for (Location i : list) {
            clippingMask = Region.getClippingMask(i.getX(), i.getY(), i.getZ());
            if ((clippingMask & 0x1280180) == 0 && (clippingMask & 0x1280108) == 0
                    && (clippingMask & 0x1280120) == 0 && (clippingMask & 0x1280102) == 0) {
                return i;
            }
        }
        return null;
    }

    /**
     * Checks if the player can accept the duel.
     *
     * @param player The player.
     * @return {@code True} if so, {@code false} if not.
     */
    public boolean canAccept(Player player) {
        int spaceNeeded = 0;
        Item item;
        /*if (player.getFamiliar().getId() > 0) {
            player.sendMessage("You can't bring summoning familiars in this arena.");
            return false;
        }*/
        for (int i = 12; i < Rules.values().length; i++) {
            int slot = i - 12;
            slot = slot == 6 ? 7 : slot > 6 ? slot + 2 : slot;
            if (rules.get(i) && (item = player.getEquipment().get(slot)) != null) {
                if (i == 12 || i == 16) {
                    if (!(item.getDefinition().isStackable() && player.getInventory().getContainer().containsOne(item))) {
                        spaceNeeded++;
                    }
                } else {
                    spaceNeeded++;
                }
            }
        }
        int freeSlots = player.getInventory().getContainer().freeSlots() - spaceNeeded;
        if (freeSlots < 0) {
            player.sendMessage("You do not have enough inventory space to remove all the equipment.");
            return false;
        }
        Container stakes = ((DuelActivity) player.getActivity()).getStakes();
        for (int i = 0; i < stakes.getSize(); i++) {
            if (stakes.get(i) != null) {
                freeSlots--;
            }
        }
        if (freeSlots < 0) {
            player.sendMessage("You do not have enough inventory space for the stakes.");
            return false;
        }
        return true;
    }

    /**
     * Checks if the player can equip an item.
     *
     * @param slot The equipment slot.
     * @return {@code True} if so, {@code false} if not.
     */
    public boolean canEquip(Player player, int slot) {
        if (rules.get(slot + 12)) {
            player.sendMessage("You can't equip " + Rules.values()[slot + 12].name().toLowerCase() + "s for this duel!");
            return false;
        }
        return true;
    }

    /**
     * Removes the equipment upon accept.
     *
     * @param player The player.
     */
    public void removeEquipment(Player player) {
        int slot;
        for (int i = 12; i < Rules.values().length; i++) {
            if (rules.get(i)) {
                slot = i - 12;
                slot = slot == 6 ? 7 : slot > 6 ? slot + 2 : slot;
                player.getEquipment().removeSlot(slot);
            }
        }
    }
}