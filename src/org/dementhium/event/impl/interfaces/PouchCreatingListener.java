package org.dementhium.event.impl.interfaces;

import org.dementhium.event.EventListener;
import org.dementhium.event.EventManager;
import org.dementhium.model.Location;
import org.dementhium.model.map.GameObject;
import org.dementhium.model.player.Player;

/**
 * The pouch creating/scroll transforming interface listener.
 * @author Emperor
 *
 */
public class PouchCreatingListener extends EventListener {

	@Override
	public void register(EventManager manager) {
		//Disabled so Mystic Flow doesn't leech again..
	}

	@Override
	public boolean interfaceOption(Player player, int interfaceId, int buttonId, int slot, int itemId, int opcode) {
		System.out.println("Unhandled summoning interface button - opcode: " + opcode + ", interfaceId: " + interfaceId + "; " + buttonId + ", " + slot + ", " + itemId + ".");
        return false;
    }
	
	@Override
	public boolean objectOption(Player player, int objectId, GameObject gameObject, Location location, ClickOption option) {
        return false;
    }
}