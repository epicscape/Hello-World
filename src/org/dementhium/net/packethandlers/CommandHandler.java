package org.dementhium.net.packethandlers;

import org.dementhium.content.Commands;
import org.dementhium.model.player.Player;
import org.dementhium.net.PacketHandler;
import org.dementhium.net.message.Message;

/**
 * @author 'Mystic Flow
 */
public final class CommandHandler extends PacketHandler {

    @Override
    public void handlePacket(Player player, Message packet) {
        packet.readByte();//client command
        String[] command = packet.readJagString().split(" ");
        if (player.getRights() != 2) {
            //	return;
        }
        Commands.handle(player, command);
    }

}
