package org.dementhium.event.impl.object;

import org.dementhium.event.EventListener;
import org.dementhium.event.EventManager;
import org.dementhium.model.Location;
import org.dementhium.model.map.GameObject;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.player.Player;

/**
 * @author Steve <golden_32@live.com>
 */
public class TaverlyDungeonHandler extends EventListener {

    static final Animation JUMP_ANIM = Animation.create(1115);

    @Override
    public void register(EventManager manager) {
        manager.registerObjectListener(55404, this);
    }

    @Override
    public boolean objectOption(final Player player, int objectId, GameObject gameObject, Location location, ClickOption option) {
        switch (objectId) {
            case 55404:

                break;
            case 1:
                player.forceMovement(JUMP_ANIM, player.getLocation().getX() == 2880 ? 2878 : 2880, 9813, 0, 40, -1, 2, true);
                break;

        }
        return true;
    }

}
