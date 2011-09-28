package org.dementhium.event.impl.interfaces;

import org.dementhium.event.EventListener;
import org.dementhium.event.EventManager;
import org.dementhium.model.player.Player;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class EnchantedGemListener extends EventListener {

    @Override
    public void register(EventManager manager) {
        manager.registerInterfaceListener(149, this);
    }

    public boolean interfaceOption(Player player, int interfaceId, int buttonId, int slot, int itemId, int opcode) {
        if (itemId == 4155) {
            if (opcode == 13) {
                if (player.getSlayer().getSlayerTask() != null) {
                    player.sendMessage("Your current assignment is: " + player.getSlayer().getSlayerTask().getName().toLowerCase() + "; only " + player.getSlayer().getSlayerTask().getTaskAmount() + " more to go.");
                } else {
                    player.sendMessage("You currently have no task.");
                }
                return true;
            }
            player.getSlayer().handleDialouge(2013);
            return true;
        }
        return false;
    }

}
