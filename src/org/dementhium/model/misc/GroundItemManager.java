package org.dementhium.model.misc;

import java.util.ArrayList;
import java.util.List;

import org.dementhium.content.misc.GraveStone;
import org.dementhium.content.misc.GraveStoneManager;
import org.dementhium.model.Item;
import org.dementhium.model.Location;
import org.dementhium.model.World;
import org.dementhium.model.map.Region;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;
import org.dementhium.tickable.Tick;

/**
 * The ground item manager, makes sure dropped items get added/removed/made
 * global when needed.
 *
 * @author Emperor
 */
public class GroundItemManager {

    /**
     * Represents all the current ground items.
     */
    private static List<GroundItem> groundItems;

    /**
     * Prepares the ground items list.
     */
    @SuppressWarnings("serial")
	public static void load() {
        groundItems = new ArrayList<GroundItem>() {
        	@Override
        	public boolean remove(Object item) {
        		boolean ok = super.remove(item);
        		if (item instanceof RespawnableGroundItem) {
        			final RespawnableGroundItem respawnable = (RespawnableGroundItem) item;
        			World.getWorld().submit(new Tick(respawnable.getDelay()) {
        				public void execute() {
        					stop();
        					GroundItemManager.createGroundItem(respawnable, 1);
        				}
        			});
        		}
        		return ok;
        	}
        };
    }

    /**
     * Creates a new ground item.
     *
     * @param groundItem The ground item.
     */
    public static void createGroundItem(final GroundItem groundItem) {
        createGroundItem(groundItem, 100);
    }

    /**
     * Creates a new ground item.
     *
     * @param groundItem The ground item.
     */
    public static void createGroundItem(final GroundItem groundItem, int updateTicks) {
        groundItem.setUpdateTicks(updateTicks);
        groundItems.add(groundItem);
        if (groundItem.isPublic()) {
            if (groundItem.getPlayer() != null) {
                ActionSender.removeGroundItem(groundItem.getPlayer(), groundItem);
            }
            List<Player> players = Region.getLocalPlayers(groundItem.getLocation());
            for (Player player : players) {
                ActionSender.sendGroundItem(player, groundItem);
            }
        } else {
            if (groundItem.getPlayer() != null) {
                ActionSender.sendGroundItem(groundItem.getPlayer(), groundItem);
            }
        }
    }

    /**
     * Sets a ground item in a public state.
     *
     * @param groundItem The ground item.
     */
    public static void setPublic(final GroundItem groundItem) {
        groundItem.setPublic(true);
        groundItem.setUpdateTicks(100);
        List<Player> players = Region.getLocalPlayers(groundItem.getLocation());
        for (Player player : players) {
            ActionSender.removeGroundItem(player, groundItem);
            ActionSender.sendGroundItem(player, groundItem);
        }
    }

    /**
     * Removes a ground item.
     *
     * @param groundItem The ground item to remove.
     */
    public static void removeGroundItem(GroundItem groundItem) {
        groundItems.remove(groundItem);
        if (groundItem.isPublic()) {
            List<Player> players = Region.getLocalPlayers(groundItem.getLocation());
            for (Player player : players) {
                ActionSender.removeGroundItem(player, groundItem);
            }
        } else {
            if (groundItem.getPlayer() != null) {
                List<Player> players = Region.getLocalPlayers(groundItem.getLocation());
                for (Player player : players) {
                    ActionSender.removeGroundItem(player, groundItem);
                }
            }
        }
    }

    /**
     * Removes a ground item holding the public flag.
     *
     * @param item The ground item.
     */
    public static void removePublicGroundItem(GroundItem item) {
        groundItems.remove(item);
        List<Player> players = Region.getLocalPlayers(item.getLocation());
        for (Player player : players) {
            ActionSender.removeGroundItem(player, item);
        }
    }

    /**
     * Removes a ground item holding the private flag.
     *
     * @param item The ground item.
     */
    public static void removePrivateGroundItem(GroundItem item) {
        groundItems.remove(item);
        if (item.getPlayer() != null) {
            ActionSender.sendGroundItem(item.getPlayer(), item);
        }
    }

    /**
     * Replaces an existing ground item with a new one.
     *
     * @param player    The player.
     * @param id        the id of the ground item to replace.
     * @param location  The location of the ground item to replace.
     * @param toReplace The ground item to replace with.
     */
    public static void replaceGroundItem(Player player, int id,
                                         Location location, GroundItem toReplace) {
        GroundItem item = getGroundItem(id, location);
        if (item == null) {
            return;
        }
        if (item.isPublic()) {
            List<Player> players = Region.getLocalPlayers(item.getLocation());
            for (Player p : players) {
                ActionSender.sendGroundItem(p, item);
            }
        } else {
            if (item.getPlayer() != null) {
                ActionSender.sendGroundItem(item.getPlayer(), item);
            }
        }
        groundItems.remove(item);
        toReplace.setUpdateTicks(100);
        groundItems.add(toReplace);
        if (toReplace.isPublic()) {
            if (toReplace.getPlayer() != null) {
                ActionSender.removeGroundItem(toReplace.getPlayer(), toReplace);
            }
            List<Player> players = Region.getLocalPlayers(toReplace.getLocation());
            for (Player p : players) {
                ActionSender.sendGroundItem(p, toReplace);
            }
        } else {
            if (toReplace.getPlayer() != null) {
                ActionSender.sendGroundItem(toReplace.getPlayer(), toReplace);
            }
        }
    }

    /**
     * Replaces an existing ground item with a new one.
     *
     * @param player    The player.
     * @param id        the id of the ground item to replace.
     * @param location  The location of the ground item to replace.
     * @param toReplace The ground item to replace with.
     */
    public static void replacePrivateGroundItem(Player player, int id,
                                                Location location, GroundItem toReplace) {
        GroundItem item = getPrivateGroundItem(id, location);
        if (item == null) {
            return;
        }
        ActionSender.removeGroundItem(player, item);
        groundItems.remove(item);
        toReplace.setUpdateTicks(100);
        groundItems.add(toReplace);
        ActionSender.sendGroundItem(player, toReplace);
    }

    /**
     * Increases the amount of a ground item by {@code 1}, <br>
     * or creates a new ground item at the given location when there was no
     * ground item found.
     *
     * @param player   The player.
     * @param id       The id of the ground item to increase.
     * @param location The location of the ground item to increase.
     */
    public static void increaseAmount(Player player, int id, Location location) {
        increaseAmount(player, id, location, 1);
    }
    
    /**
     * Increases the amount of a ground item by {@code 1}, <br>
     * or creates a new ground item at the given location when there was no
     * ground item found.
     *
     * @param player   The player.
     * @param id       The id of the ground item to increase.
     * @param location The location of the ground item to increase.
     * @param amount The amount to increase.
     */
    public static void increaseAmount(Player player, int id, Location location, int amount) {
        GroundItem item = getPrivateGroundItem(id, location);
        if (item == null) {
            createGroundItem(new GroundItem(player, new Item(id, amount),
                    location, false));
            return;
        }
        ActionSender.removeGroundItem(player, item);
        groundItems.remove(item);
        item.getItem().setAmount(item.getItem().getAmount() + amount);
        groundItems.add(item);
        ActionSender.sendGroundItem(player, item);
    }

    /**
     * Gets a ground item with the given id and location, holding the private
     * flag.
     *
     * @param id  The id.
     * @param loc The location.
     * @return The ground item.
     */
    public static GroundItem getPrivateGroundItem(int id, Location loc) {
        GroundItem lastGroundItem = null;
        for (GroundItem item : groundItems) {
            if (item == null) {
                continue;
            }
            if (item.getItem().getId() == id && item.getLocation() == loc && !item.isPublic()) {
                if (lastGroundItem == null) {
                    lastGroundItem = item;
                    continue;
                }
                if (item.getUpdateTicks() > lastGroundItem.getUpdateTicks()) {
                    lastGroundItem = item;
                }
            }
        }
        return lastGroundItem;
    }

    /**
     * Gets a ground item with the given id and location, holding the public
     * flag.
     *
     * @param id  The id.
     * @param loc The location.
     * @return The ground item.
     */
    public static GroundItem getPublicGroundItem(int id, Location loc) {
        GroundItem lastGroundItem = null;
        for (GroundItem item : groundItems) {
            if (item == null) {
                continue;
            }
            if (item.getItem().getId() == id && item.getLocation() == loc && item.isPublic()) {
                if (lastGroundItem == null) {
                    lastGroundItem = item;
                    continue;
                }
                if (item.getUpdateTicks() > lastGroundItem.getUpdateTicks()) {
                    lastGroundItem = item;
                }
            }
        }
        return lastGroundItem;
    }

    /**
     * Gets a ground item with the given id and location.
     *
     * @param id  The id.
     * @param loc The location.
     * @return The ground item.
     */
    public static GroundItem getGroundItem(int id, Location loc) {
        for (GroundItem item : groundItems) {
            if (item == null) {
                continue;
            }
            if (item.getItem().getId() == id && item.getLocation() == loc) {
                return item;
            }
        }
        return null;
    }

    /**
     * Gets the list of ground items.
     *
     * @return The list.
     */
    public static List<GroundItem> getGroundItems() {
        return groundItems;
    }

    /**
     * Refreshes the ground items when changing regions.
     *
     * @param player
     */
    public static void refresh(Player player) {
        GraveStone grave = GraveStoneManager.forName(player.getUsername());
        for (GroundItem item : groundItems) {
            if (item != null) {
                ActionSender.removeGroundItem(player, item);
                if (item.getPlayer() == player || item.isPublic() || (grave != null && grave.getItems().contains(item))) {
                    ActionSender.sendGroundItem(player, item);
                }
            }
        }
    }
}