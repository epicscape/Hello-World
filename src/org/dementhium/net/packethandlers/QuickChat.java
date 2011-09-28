package org.dementhium.net.packethandlers;

import org.dementhium.model.player.Player;
import org.dementhium.net.PacketHandler;
import org.dementhium.net.message.Message;

/**
 * @author Lumby <lumbyjr@hotmail.com>
 */
public class QuickChat extends PacketHandler {

    @Override
    public void handlePacket(Player player, Message packet) {
        switch (packet.getOpcode()) {
            case 61:
                appendChat(player, packet);
                break;

        }
    }
/**
 * boolean secondClientScript = stream.readByte() == 1;//script 5059 or 5061
			int fileId = stream.readUnsignedShort();
			byte[] data = null;
			if(length > 3) {
				data = new byte[length-3];
				stream.readBytes(data);
			}
player.setNextPublicChatMessage(new QuickChatMessage(fileId, data));
 */
    private void appendChat(Player player, Message packet) {
        //int i1 = packet.readByte();
        //int i2 = packet.readByte();
        int i1 = packet.readUnsignedShort();
        int i2 = packet.readUnsignedByte();
        System.out.println("i1: " + i1 + " i2: " + i2);//+" i3: "+i3+" i4: "+i4);
        
    }
}
