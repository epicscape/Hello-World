package org.dementhium.net.packethandlers;

import org.dementhium.model.player.Player;
import org.dementhium.net.PacketHandler;
import org.dementhium.net.message.Message;
import org.dementhium.util.WorldList;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class WorldRequestHandler extends PacketHandler {

	@Override
	public void handlePacket(Player player, Message packet) {
		player.write(WorldList.getData(true, true));
	}
	
}
