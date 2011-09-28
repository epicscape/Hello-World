package org.dementhium.event.impl.interfaces;

import org.dementhium.event.EventListener;
import org.dementhium.event.EventManager;
import org.dementhium.model.definition.ItemDefinition;
import org.dementhium.model.player.Player;
import org.dementhium.util.InputHandler;

/**
 * The price check listener.
 *
 * @author Emperor
 */
public class PriceCheckListener extends EventListener {

    @Override
    public void register(EventManager manager) {
        manager.registerInterfaceListener(206, this);
        manager.registerInterfaceListener(207, this);
    }

    @Override
    public boolean interfaceOption(Player player, int interfaceId, int buttonId, int slot, int itemId, int opcode) {
        switch (opcode) {
            case 6:
                if (interfaceId == 206) {
                    if (buttonId == 13) {
                        return player.getPriceCheck().close();
                    }
                    return player.getPriceCheck().remove(itemId, slot / 2, 1);
                }
                return player.getPriceCheck().checkPrice(itemId, slot, 1);
            case 13:
                if (interfaceId == 206) {
                    return player.getPriceCheck().remove(itemId, slot / 2, 5);
                }
                return player.getPriceCheck().checkPrice(itemId, slot, 5);
            case 0:
                if (interfaceId == 206) {
                    return player.getPriceCheck().remove(itemId, slot / 2, 10);
                }
                return player.getPriceCheck().checkPrice(itemId, slot, 10);
            case 15:
                if (interfaceId == 206) {
                    return player.getPriceCheck().remove(itemId, slot / 2, player.getPriceCheck().getContainer().getItemCount(itemId));
                }
                return player.getPriceCheck().checkPrice(itemId, slot, player.getInventory().getContainer().getItemCount(itemId));
            case 46:
                InputHandler.requestIntegerInput(player, 7, "Please enter an amount:");
                player.setAttribute("slotId", interfaceId == 206 ? slot / 2 : slot);
                player.setAttribute("itemPriceCheckId", itemId);
                player.setAttribute("inventoryAction", interfaceId == 207);
                return true;
            case 58:
                player.sendMessage(ItemDefinition.forId(itemId).getExamine());
                return true;
        }
        return false;
    }

}
