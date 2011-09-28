package org.dementhium.event.impl.object;

import org.dementhium.event.EventListener;
import org.dementhium.event.EventManager;
import org.dementhium.model.Location;
import org.dementhium.model.map.GameObject;
import org.dementhium.model.player.Player;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class StairsListener extends EventListener {

	@Override
	public void register(EventManager manager) {
		manager.registerObjectListener(30943, this);
		manager.registerObjectListener(30944, this);
		manager.registerObjectListener(4495, this);
	} //2712, 9564


	public boolean objectOption(Player player, int objectId, GameObject gameObject, Location location, ClickOption option) {
		if (objectId == 30943) {
			player.teleport(3061, 3376, 0);
			return true;
		} else if (objectId == 30944) {
			player.teleport(3058, 9776, 0);
			return true;
		}
		switch (objectId) {
		case 5096:
			break;
		case 5904:
			break;
		case 18049:
			player.teleport(3637, 9695, 0);
			break;
		case 4495:
			player.teleport(3418, 3541, 0);
			break;
		}
		return false;
	}


}
