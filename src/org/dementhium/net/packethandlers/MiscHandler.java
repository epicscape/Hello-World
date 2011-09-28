package org.dementhium.net.packethandlers;

import org.dementhium.model.player.Player;
import org.dementhium.net.PacketHandler;
import org.dementhium.net.message.Message;

public final class MiscHandler extends PacketHandler {

    public static final int KEY_PRESSED = 26, MINIMIZED_OR_RESTORED = 62, CLICKED = 29, CAMERA_ROTATE = 30, MOUSE_MOVED = 17;

    public static final int PING = 59;

    @Override
    public void handlePacket(Player player, Message packet) {
    	switch (packet.getOpcode()) {
    	case 23:
    		
    		break;
    	case PING:
    		break;
    	}
    }
}
