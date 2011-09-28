package org.dementhium.net.packethandlers;

import org.dementhium.model.definition.ItemDefinition;
import org.dementhium.model.player.Player;
import org.dementhium.net.PacketHandler;
import org.dementhium.net.message.Message;


/**
 * @author Steve <golden_32@live.com>
 */
public class GESearchHandler extends PacketHandler {

	private static final int SEARCH = 19;

	@Override
	public void handlePacket(Player player, Message packet) {
		switch (packet.getOpcode()) {
		case SEARCH:
			handleSearch(player, packet);
			break;
		}

	}

	private void handleSearch(Player player, Message packet) {
		int itemId = packet.readShort();
		ItemDefinition def = ItemDefinition.forId(itemId);
		if (def != null) {

		}
	}

}
