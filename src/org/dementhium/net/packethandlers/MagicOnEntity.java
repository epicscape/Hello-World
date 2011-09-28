package org.dementhium.net.packethandlers;

import org.dementhium.model.Mob;
import org.dementhium.model.World;
import org.dementhium.model.npc.NPC;
import org.dementhium.model.player.Player;
import org.dementhium.net.PacketHandler;
import org.dementhium.net.message.Message;

/**
 * @author Stephen
 */
public class MagicOnEntity extends PacketHandler {

    public static final int MAGIC_ON_PLAYER = 57, MAGIC_ON_NPC = 28;

    @Override
    public void handlePacket(Player player, Message packet) {
        switch (packet.getOpcode()) {
            case MAGIC_ON_PLAYER:
                handleMagicOnPlayer(player, packet);
                break;
            case MAGIC_ON_NPC:
                handleMagicOnNpc(player, packet);
                break;
        }
    }

    @SuppressWarnings("unused")
	private void handleMagicOnNpc(Player player, Message in) {
        in.readShort();
        in.readByte();
        int interfaceID = in.readShort();
        int buttonId = in.readShort();
        in.readShort();
        int index = in.readLEShortA();

        NPC n = World.getWorld().getNpcs().get(index);
        if (n == null) {
            return;
        }
        if (n.getAttribute("enemyIndex", (short) -1) > -1 && n.getAttribute("enemyIndex", (short) -1) != player.getIndex()) {
            player.sendMessage("This is not your enemy!");
            return;
        }
        //MagicHandler.cast(player, n, buttonId, interfaceID);
    }

    @SuppressWarnings("unused")
	private void handleMagicOnPlayer(Player player, Message packet) {
        int iHash = packet.readLEInt();
        int bookId = iHash >> 16;
		int spellId = iHash - (bookId << 16);
        int victimsIndex = packet.readShortA();
        Mob victim = World.getWorld().getPlayers().get(victimsIndex);
        packet.readByte();
        packet.readByte();
        packet.readByte();
        //MagicHandler.cast(player, victim, bookId, spellId);
    }
}
