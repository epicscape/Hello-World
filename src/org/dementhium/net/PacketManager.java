package org.dementhium.net;

import org.dementhium.io.FileUtilities;
import org.dementhium.model.player.Player;
import org.dementhium.net.message.Message;

import java.util.HashMap;

/**
 * @author 'Mystic Flow
 */
public final class PacketManager {

	private final static PacketHandler[] handlers = new PacketHandler[256];

	public void load() throws Exception {
		System.out.println("Loading packet handlers...");
		HashMap<String, PacketHandler> packetMap = new HashMap<String, PacketHandler>();
		int size = 0;
		boolean excluded = false;
		for (String string : FileUtilities.readFile("data/packetHandlers.ini")) {
			if (excluded) {
				if (string.contains("*/")) {
					excluded = false;
				}
				continue;
			}
			if (string.contains("/*")) {
				excluded = true;
				continue;
			}
			if (string.length() == 0 || string.contains("#")) {
				continue;
			}
			int index = string.indexOf("[");
			int indexOf = string.indexOf("]");
			int id = Integer.parseInt(string.substring(index + 1, indexOf));
			String handler = string.split(":")[1].substring(1);
			if (!packetMap.containsKey(handler)) {
				packetMap.put(handler, (PacketHandler) Class.forName(handler)
						.newInstance());
			}
			handlers[id] = packetMap.get(handler);
			size++;
		}
		System.out.println("Loaded " + size + " packet handlers.");
	}

	public void handlePacket(Player player, Message packet) {
		int opcode = packet.getOpcode();
		PacketHandler handler = handlers[opcode];
		player.refreshPing();
		if (handler != null) {
			handler.handlePacket(player, packet);
		} else {
			if (player.getRights() == 2)
				player.sendMessage("Unhandled message:" + opcode);
			System.out.println("Message not handled: " + opcode);
		}
	}

	public PacketManager() {
		try {
			load();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
}