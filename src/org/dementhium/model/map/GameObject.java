package org.dementhium.model.map;

import org.dementhium.cache.format.CacheObjectDefinition;
import org.dementhium.model.Entity;
import org.dementhium.model.Location;
import org.dementhium.model.player.Player;

/**
 * @author 'Mystic Flow
 */
public class GameObject extends Entity {

    private int id;
    private int type, rotation;

    private int health;
    private boolean healthSet;

    private Player owner;

    public GameObject(int id, int x, int y, int z, int type, int rotation) {
        this(id, Location.locate(x, y, z), type, rotation);
    }

    public GameObject(int id, Location location, int type, int rotation) {
        this.id = id;
        this.type = type;
        this.rotation = rotation;
        setLocation(location);
    }

    public GameObject(Player owner, int id, Location location, int type, int rotation) {
        this.setOwner(owner);
        this.id = id;
        this.type = type;
        this.rotation = rotation;
        setLocation(location);
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }

    public int getRotation() {
        return rotation;
    }

    public void setRotation(byte rotation) {
        this.rotation = rotation;
    }

    public void setId(int id) {
        this.id = id;
    }

    public CacheObjectDefinition getDefinition() {
        return CacheObjectDefinition.forId(id);
    }

    public boolean isGameObject() {
        return true;
    }

    public GameObject getGameObject() {
        return this;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean isHealthSet() {
        return healthSet;
    }

    public void setHealthSet(boolean b) {
        this.healthSet = b;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Player getOwner() {
        return owner;
    }
}
