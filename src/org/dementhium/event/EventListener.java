package org.dementhium.event;

import org.dementhium.model.Location;
import org.dementhium.model.map.GameObject;
import org.dementhium.model.player.Player;

/**
 * The event listener used for handling content such as action buttons, game objects, ...
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public abstract class EventListener {

    /**
     * The option clicked.
     *
     * @author 'Mystic Flow <Steven@rune-server.org>
     */
    public static enum ClickOption {
        FIRST, SECOND, THIRD, FOURTH, FIFTH
    }

    /**
     * We register the listener to the {@code EventManager}.
     *
     * @param manager The event manager object.
     */
    public abstract void register(EventManager manager);

    /**
     * Handles an action button clicked.
     *
     * @param player      The player.
     * @param interfaceId The interface id.
     * @param buttonId    The button id.
     * @param slot        The slot.
     * @param itemId      The item.
     * @param opcode      The opcode.
     * @return {@code True} if the action button got handled, {@code false} if not.
     */
    public boolean interfaceOption(Player player, int interfaceId, int buttonId, int slot, int itemId, int opcode) {
        return false;
    }

    /**
     * Handles a game object option clicked.
     *
     * @param player     The player.
     * @param objectId   The object id.
     * @param gameObject The game object instance.
     * @param location   The location of the game object.
     * @param option     The option clicked.
     * @return {@code True} if the game object action got handled, {@code false} if not.
     * @see ClickOption
     */
    public boolean objectOption(Player player, int objectId, GameObject gameObject, Location location, ClickOption option) {
        return false;
    }

}
