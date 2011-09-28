package org.dementhium.model.misc;

import org.dementhium.model.Entity;
import org.dementhium.model.Item;
import org.dementhium.model.Location;
import org.dementhium.model.map.Position;
import org.dementhium.model.player.Player;

/**
 * Represents a single ground item.
 *
 * @author Emperor
 */
public class GroundItem extends Entity {

    /**
     * If the item is global.
     */
    private boolean isPublic;

    /**
     * If the item is removed.
     */
    private boolean removed;

    /**
     * The amount of update ticks.
     */
    private int updateTicks;

    /**
     * The item's location.
     */
    private final Position location;

    /**
     * The item's location.
     */
    private final Item item;

    /**
     * The player who dropped the item.
     */
    private final Player player;

    /**
     * The constructor.
     *
     * @param item     The item.
     * @param location The location.
     * @param isPublic The isPublic flag.
     */
    public GroundItem(Player player, Item item, Location location,
                      boolean isPublic) {
        this.item = item;
        this.location = Position.create(location.getX(), location.getY(), location.getZ());
        this.isPublic = isPublic;
        this.player = player;
    }

    public GroundItem(Player player, Item item, int x, int y, int z,
                      boolean isPublic) {
        this.item = item;
        this.location = Position.create(x, y, z);
        this.isPublic = isPublic;
        this.player = player;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }

    public Location getLocation() {
        return Location.locate(location.getX(), location.getY(), location.getZ());
    }

    public Item getItem() {
        return item;
    }

    public Player getPlayer() {
        return player;
    }

    public void setRemoved(boolean removed) {
        this.removed = removed;
    }

    public boolean isRemoved() {
        return removed;
    }

    public void setUpdateTicks(int updateTicks) {
        this.updateTicks = updateTicks;
    }

    public int getUpdateTicks() {
        return updateTicks;
    }
}