package org.dementhium.event.impl.interfaces;

import org.dementhium.event.EventListener;
import org.dementhium.event.EventManager;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;
import org.dementhium.util.InputHandler;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 * @author Steve <golden_32@live.com>
 * @author Lumby <lumbyjr@hotmail.com>
 * @author Emperor <black_dragon_686@hotmail.com>
 */
public class NotesInterfaceListener extends EventListener {

    @Override
    public void register(EventManager manager) {
        manager.registerInterfaceListener(34, this);

    }

    @Override
    public boolean interfaceOption(final Player player, int interfaceId, int buttonId, int slot, int itemId, int opcode) {
        if (buttonId > 34 && buttonId < 42 && player.getAttribute("noteSlot", -1) > -1) {
            ActionSender.sendInterfaceConfig(player, 34, 16, false);
            int color = 0;
            switch (buttonId) {
                case 35:
                    color = 0;
                    break;
                case 37:
                    color = 1;
                    break;
                case 39:
                    color = 2;
                    break;
                case 41:
                    color = 3;
                    break;
            }
            player.getNotes().setColor(color, player.getAttribute("noteSlot", -1));
            player.removeAttribute("noteSlot");
        }
        if (interfaceId == 34) {
            switch (buttonId) {
                case 9:
                    switch (opcode) {
                        case 6: //select
                            if (player.getAttribute("selectedNote", -1) == slot) {
                                slot = -1;
                                player.setAttribute("selectedNote", -1);
                            } else {
                                player.setAttribute("selectedNote", slot);
                            }
                            ActionSender.sendConfig(player, 1439, slot);
                            break;
                        case 13:
                            player.setAttribute("noteSlot", slot);
                            InputHandler.requestLongStringInput(player, 1, "Please enter this note's new text");
                            break;
                        case 0:
                            player.setAttribute("noteSlot", slot);
                            ActionSender.sendInterfaceConfig(player, 34, 16, true);
                            break;
                        case 15:
                            player.getNotes().deleteNote(slot);
                            break;
                    }
                    break;
                case 8:
                    switch (opcode) {
                        case 6:
                            player.getNotes().deleteSelectedNote();
                            break;
                        case 13:
                            player.getNotes().clear();
                            break;
                    }
                    break;
                case 3:
                    InputHandler.requestLongStringInput(player, 1, "Please enter this note's text");
                    break;
            }
        }
        return false;
    }

}
