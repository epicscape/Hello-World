package org.dementhium.net;

import org.dementhium.model.player.Player;
import org.dementhium.net.message.Message;

/**
 * @author 'Mystic Flow
 */
public abstract class PacketHandler {

    public abstract void handlePacket(Player player, Message packet);

}
