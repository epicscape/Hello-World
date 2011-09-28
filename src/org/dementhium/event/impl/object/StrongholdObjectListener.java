package org.dementhium.event.impl.object;

import org.dementhium.content.DialogueManager;
import org.dementhium.event.EventListener;
import org.dementhium.event.EventManager;
import org.dementhium.model.Item;
import org.dementhium.model.Location;
import org.dementhium.model.World;
import org.dementhium.model.map.GameObject;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.player.Player;
import org.dementhium.tickable.Tick;

/**
 * Handles the walking through doors in the Stronghold of security.
 *
 * @author Emperor
 */
public class StrongholdObjectListener extends EventListener {

    /**
     * The opening a door animation.
     */
    private static final Animation OPEN_ANIM = Animation.create(4282);

    /**
     * The closing a door animation.
     */
    private static final Animation CLOSE_ANIM = Animation.create(4283);

    /**
     * The climbing a ladder animation.
     */
    private static final Animation CLIMB_LADDER = Animation.create(828);

    @Override
    public void register(EventManager manager) {
        manager.registerObjectListener(16123, this);
        manager.registerObjectListener(16124, this);
        manager.registerObjectListener(16065, this);
        manager.registerObjectListener(16066, this);
        manager.registerObjectListener(16089, this);
        manager.registerObjectListener(16090, this);
        manager.registerObjectListener(16043, this);
        manager.registerObjectListener(16044, this);
        manager.registerObjectListener(16154, this);
        manager.registerObjectListener(16148, this);
        manager.registerObjectListener(16146, this);
        manager.registerObjectListener(16149, this);
        manager.registerObjectListener(16078, this);
        manager.registerObjectListener(16080, this);
        manager.registerObjectListener(16081, this);
        manager.registerObjectListener(16114, this);
        manager.registerObjectListener(16115, this);
        manager.registerObjectListener(16112, this);
        manager.registerObjectListener(16048, this);
        manager.registerObjectListener(16049, this);
        manager.registerObjectListener(16135, this);
        manager.registerObjectListener(16077, this);
        manager.registerObjectListener(16118, this);
        manager.registerObjectListener(16047, this);
        manager.registerObjectListener(16150, this);
        manager.registerObjectListener(16082, this);
        manager.registerObjectListener(16116, this);
        manager.registerObjectListener(16050, this);
    }

    @Override
    public boolean objectOption(final Player player, int objectId, GameObject gameObject, Location location, ClickOption option) {
        if (climbLadder(player, objectId, location)) {
            return true;
        }
        if (usePortal(player, objectId, location)) {
            return true;
        }
        if (lootChest(player, objectId, gameObject, location)) {
            return true;
        }
        Location toTeleport = player.getLocation();
        switch (gameObject.getRotation()) {
            case 0:
                toTeleport = player.getLocation().getX() >= location.getX() ? location.transform(-1, 0, 0) : location.transform(0, 0, 0);
                break;
            case 1:
                toTeleport = player.getLocation().getY() > location.getY() ? location.transform(0, 0, 0) : location.transform(0, 1, 0);
                break;
            case 2:
                toTeleport = player.getLocation().getX() > location.getX() ? location.transform(0, 0, 0) : location.transform(1, 0, 0);
                break;
            default:
                toTeleport = player.getLocation().getY() >= location.getY() ? location.transform(0, -1, 0) : location.transform(0, 0, 0);
                break;
        }
        player.getMask().setFacePosition(toTeleport, 0, 0);
        player.setAttribute("cantMove", Boolean.TRUE);
        player.setAttribute("busy", Boolean.TRUE);
        final Location destination = toTeleport;
        player.animate(OPEN_ANIM);
        World.getWorld().submit(new Tick(1) {
            @Override
            public void execute() {
                player.teleport(destination);
                World.getWorld().submit(new Tick(1) {
                    @Override
                    public void execute() {
                        player.animate(CLOSE_ANIM);
                        player.removeAttribute("busy");
                        player.removeAttribute("cantMove");
                        stop();
                    }
                });
                stop();
            }
        });
        return true;
    }

    /**
     * Tries to use a portal in the stronghold.
     *
     * @param player   The player.
     * @param objectId The object id.
     * @param location The location.
     * @return {@code True} if the player used a portal, {@code false} if not.
     */
    private boolean usePortal(Player player, int objectId, Location location) {
        Location loc = player.getLocation();
        int floor = 0;
        if (objectId == 16150) {
            loc = Location.locate(1910, 5220, 0);
        } else if (objectId == 16082) {
            loc = Location.locate(2024, 5213, 0);
            floor = 1;
        } else if (objectId == 16116) {
            loc = Location.locate(2146, 5278, 0);
            floor = 2;
        } else if (objectId == 16050) {
            loc = Location.locate(2342, 5213, 0);
            floor = 3;
        } else {
            return false;
        }
        if (!player.getSettings().getStrongholdChest()[floor]) {
            player.sendMessage("You can't use this portal without looting the chest on this floor first.");
            return true;
        }
        player.sendMessage("You enter the portal. It whisks you through to the treasure room.");
        player.teleport(loc);
        return true;
    }

    /**
     * Attempts to loot a reward chest in the stronghold of security.
     *
     * @param player   The player.
     * @param objectId The object id.
     * @param location The location.
     * @return {@code True} if the object action was to loot a chest, {@code false} if not.
     */
    private boolean lootChest(final Player player, final int objectId, GameObject object, Location location) {
        Object[] reward = getReward(objectId);
        int slot = (Integer) reward[0];
        if (slot == -1) {
            return false;
        }
        final boolean hasBoots = player.getEquipment().contains(9005) || player.getEquipment().contains(9006)
                || player.getInventory().contains(9005) || player.getInventory().contains(9006)
                || player.getBank().contains(9005) || player.getBank().contains(9006);
        if ((player.getSettings().getStrongholdChest()[slot] && slot != 3) || (slot == 3 && hasBoots)) {
            DialogueManager.sendDisplayBox(player, -1, "You have already claimed your reward from this level.");
            return true;
        }
        if (reward[3] != null && !player.getInventory().addItem((Item) reward[3])) {
            return true;
        }
        player.getMask().setFacePosition(location, object.getDefinition().getSizeX(), object.getDefinition().getSizeY());
        player.animate(Animation.create(832));
        if (slot != 3) {
            player.getSettings().getStrongholdChest()[slot] = true;
        }
        player.sendMessage((String) reward[1]);
        if (objectId == 16047) {
            DialogueManager.sendDisplayBox(player, 109, "As your hand touches the cradle, you hear the voices of a million", "dead adventurers...");
        }
        if (objectId == 16118) {
            player.getSkills().heal(player.getSkills().getMaxHitpoints());
            player.getSkills().restorePray(99.0);
        }
        player.sendMessage((String) reward[2]);
        return true;
    }

    /**
     * Gets the rewards from this chest.
     *
     * @param objectId The object id.
     * @return An object array of the rewards.
     */
    private Object[] getReward(int objectId) {
        if (objectId == 16135) {
            return new Object[]{0, "You open the gift of peace...", "..and find 10000 coins.", new Item(995, 10000)};
        } else if (objectId == 16077) {
            return new Object[]{1, "You search the grain of plenty...", "..and find 25000 coins.", new Item(995, 25000)};
        } else if (objectId == 16118) {
            return new Object[]{2, "You open the box of health...", "..it restored your life and prayer points and you find 50000 coins.", new Item(995, 50000)};
        } else if (objectId == 16047) {
            return new Object[]{3, "You look in the cradle of life...", null, null};
        } else {
            return new Object[]{-1};
        }
    }

    /**
     * Climbs a ladder in the stronghold of security.
     *
     * @param player   The player.
     * @param objectId The object id.
     * @param location The location.
     * @return {@code True} if the option clicked was a ladder, {@code false} if not.
     */
    private boolean climbLadder(final Player player, final int objectId, Location location) {
        Location loc = player.getLocation();
        if (objectId == 16154) { //Barbarian village entrance.
            loc = Location.locate(1860, 5244, 0);
        } else if (objectId == 16148) { //Climb up to barbarian village.
            loc = Location.locate(3081, 3421, 0);
        } else if (objectId == 16080) {
            loc = Location.locate(1903, 5222, 0);
        } else if (objectId == 16114) {
            loc = Location.locate(2026, 5217, 0);
        } else if (objectId == 16049) {
            loc = Location.locate(2148, 5283, 0);
        } else if (objectId == 16149) {
            if (!player.getSettings().getStrongholdChest()[0]) {
                player.sendMessage("This looks too dangerous for you to go in.");
                return true;
            }
            player.sendMessage("You climb down the ladder to the next level.");
            loc = Location.locate(2042, 5245, 0);
        } else if (objectId == 16078) {
            player.sendMessage("You shin up the rope, squeeze through a passage then climb a ladder.");
            player.sendMessage("You climb up the ladder which seems to twist and wind in all directions.");
            loc = Location.locate(2042, 5245, 0);
        } else if (objectId == 16081) {
            if (!player.getSettings().getStrongholdChest()[1]) {
                player.sendMessage("This looks too dangerous for you to go in.");
                return true;
            }
            player.sendMessage("You climb down the ladder to the next level.");
            loc = Location.locate(2123, 5252, 0);
        } else if (objectId == 16112) {
            player.sendMessage("You shin up the rope, squeeze through a passage then climb a ladder.");
            player.sendMessage("You climb up the ladder which seems to twist and wind in all directions.");
            loc = Location.locate(2123, 5252, 0);
        } else if (objectId == 16115) {
            if (!player.getSettings().getStrongholdChest()[2]) {
                player.sendMessage("This looks too dangerous for you to go in.");
                return true;
            }
            player.sendMessage("You climb down the ladder to the next level.");
            loc = Location.locate(2358, 5215, 0);
        } else if (objectId == 16048) {
            player.sendMessage("You shin up the rope, squeeze through a passage then climb a ladder.");
            player.sendMessage("You climb up the ladder which seems to twist and wind in all directions.");
            loc = Location.locate(3081, 3421, 0);
        } else if (objectId == 16146) {
            player.sendMessage("You shin up the rope, squeeze through a passage then climb a ladder.");
            player.sendMessage("You climb up the ladder which seems to twist and wind in all directions.");
            loc = Location.locate(1860, 5244, 0);
        } else {
            return false;
        }
        player.getMask().setFacePosition(location, 0, 0);
        player.setAttribute("cantMove", true);
        final Location destination = loc;
        if (objectId != 16154) {
            player.animate(CLIMB_LADDER);
        }
        World.getWorld().submit(new Tick(1) {
            @Override
            public void execute() {
                player.teleport(destination);
                if (objectId == 16154) {
                    player.getMask().setFacePosition(player.getLocation().transform(-1, 0, 0), 0, 0);
                }
                World.getWorld().submit(new Tick(1) {
                    @Override
                    public void execute() {
                        player.animate(CLIMB_LADDER);
                        player.setAttribute("cantMove", false);
                        stop();
                    }
                });
                stop();
            }
        });
        return true;
    }

}
