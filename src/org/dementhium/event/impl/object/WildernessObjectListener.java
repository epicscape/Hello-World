package org.dementhium.event.impl.object;

import org.dementhium.event.EventListener;
import org.dementhium.event.EventManager;
import org.dementhium.model.Location;
import org.dementhium.model.map.GameObject;
import org.dementhium.model.player.Player;

/**
 * The wilderness objects listener.
 *
 * @author Emperor
 */
public class WildernessObjectListener extends EventListener {

    Location KBD_LADDER_LOC = Location.locate(3069, 10256, 0);

    @Override
    public void register(EventManager manager) {
        manager.registerObjectListener(1765, this);
        manager.registerObjectListener(1816, this);
        manager.registerObjectListener(1817, this);
        manager.registerObjectListener(32015, this);
    }

    @Override
    public boolean objectOption(Player player, int objectId, GameObject gameObject, Location location, ClickOption option) {
        if (objectId == 1765) {
            player.teleport(3069, 10257, 0);
            return true;
        } else if (objectId == 32015 && location.distance(KBD_LADDER_LOC) < 4) {
            player.teleport(3015, 3849, 0);
            return true;
        } else if (objectId == 1816) {
            player.teleport(2273, 4680, 0);
            return true;
        } else if (objectId == 1817) {
            player.teleport(3067, 10253, 0);
            return true;
        }
        return false;
    }
}
