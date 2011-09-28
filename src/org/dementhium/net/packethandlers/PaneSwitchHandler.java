package org.dementhium.net.packethandlers;

import org.dementhium.model.player.Player;
import org.dementhium.net.PacketHandler;
import org.dementhium.net.message.Message;
import org.dementhium.tickable.Tick;
import org.dementhium.util.InterfaceSettings;

@SuppressWarnings("unused")
public class PaneSwitchHandler extends PacketHandler {

	@Override
	public void handlePacket(Player player, Message packet) {
		switch (packet.getOpcode()) {
		case 7:
			switchPanes(player, packet);
			break;
		}
	}

	private void switchPanes(final Player player, Message packet) {
		int mode = packet.readByte();
		int width = packet.readShort();
		int height = packet.readShort();
		packet.readByte();
		if (mode < 0 || mode > 3) {
			return;
		}
		if (!player.isOnline())
			return;
		if (player.getViewDistance() < 1) {
			player.incrementViewDistance();
			player.submitTick("distance_increment", new Tick(4) {
				@Override
				public void execute() {
					setTime(1);
					if (player.getViewDistance() >= 15) {
						stop();
						return;
					}
					player.incrementViewDistance();
				}
			});
		}
		if (mode == 2 && player.getConnection().getDisplayMode() != 2
				|| mode == 3 && player.getConnection().getDisplayMode() != 3) {
			InterfaceSettings.switchWindow(player, mode);
		} else if (mode == 1 && player.getConnection().getDisplayMode() != 1
				|| mode == 0 && player.getConnection().getDisplayMode() != 0) {
			InterfaceSettings.switchWindow(player, mode);
		}
	}
}
