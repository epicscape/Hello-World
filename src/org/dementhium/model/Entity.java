package org.dementhium.model;

import org.dementhium.model.map.GameObject;
import org.dementhium.model.misc.GroundItem;
import org.dementhium.model.npc.NPC;
import org.dementhium.model.player.Player;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public abstract class Entity {

    protected Location location;

    public void setLocation(Location location) {
        if (this.location != null) {
            this.location.getRegion().removeEntity(this);
            this.location.remove(this);
        }
        if (location != null) {
            this.location = location;
            location.getRegion().addEntity(this);
            location.add(this);
        }
    }

    public Location getLocation() {
        return location;
    }

    public void destroy() {
        if (location != null) {
            location.getRegion().removeEntity(this);
        }
    }

    public boolean isPlayer() {
        return false;
    }

    public boolean isNPC() {
        return false;
    }

    public boolean isGroundItem() {
        return false;
    }

    public boolean isGameObject() {
        return false;
    }

    public GroundItem getGroundItem() {
        return null;
    }

    public Player getPlayer() {
        return null;
    }

    public NPC getNPC() {
        return null;
    }

    public GameObject getGameObject() {
        return null;
    }
}
