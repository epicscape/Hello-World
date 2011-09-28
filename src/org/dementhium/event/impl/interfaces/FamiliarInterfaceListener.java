package org.dementhium.event.impl.interfaces;

import org.dementhium.event.EventListener;
import org.dementhium.event.EventManager;
import org.dementhium.model.player.Player;

/**
 * Handles the summoning orb and interface buttons.
 * @author Emperor
 *
 */
public class FamiliarInterfaceListener extends EventListener {

	@Override
	public void register(EventManager manager) {
		manager.registerInterfaceListener(747, this);
		manager.registerInterfaceListener(662, this);
		manager.registerInterfaceListener(880, this);
		manager.registerInterfaceListener(665, this);
		manager.registerInterfaceListener(671, this);
	}

	@Override
    public boolean interfaceOption(final Player player, int interfaceId, int buttonId, int slot, int itemId, int opcode) {
		//Disabled so Mystic Flow doesn't leech again..
		player.sendMessage("Unhandled summoning button for interface " + interfaceId + ": " + opcode + ", " + buttonId + ", " + slot + ", " + itemId + ".");
        return false;
    }
}