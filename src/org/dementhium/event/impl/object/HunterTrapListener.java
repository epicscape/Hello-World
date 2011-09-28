package org.dementhium.event.impl.object;

import org.dementhium.event.EventListener;
import org.dementhium.event.EventManager;
import org.dementhium.model.Location;
import org.dementhium.model.map.GameObject;
import org.dementhium.model.player.Player;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class HunterTrapListener extends EventListener {

    @Override
    public void register(EventManager manager) {
        manager.registerObjectListener(19175, this);
        manager.registerObjectListener(19180, this);
        manager.registerObjectListener(19174, this);
        manager.registerObjectListener(19178, this);
        manager.registerObjectListener(19182, this);
        manager.registerObjectListener(19184, this);
        manager.registerObjectListener(19187, this);
        manager.registerObjectListener(19189, this);
        manager.registerObjectListener(19192, this);
        manager.registerObjectListener(19663, this);
        manager.registerObjectListener(19669, this);
        manager.registerObjectListener(19662, this);
        manager.registerObjectListener(19665, this);
        manager.registerObjectListener(19659, this);
        manager.registerObjectListener(19656, this);
    }

    public boolean objectOption(Player player, int objectId, GameObject gameObject, Location location, ClickOption option) {
        return true;
    }


}
