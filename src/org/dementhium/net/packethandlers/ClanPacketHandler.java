package org.dementhium.net.packethandlers;

import org.dementhium.model.World;
import org.dementhium.model.player.Player;
import org.dementhium.net.PacketHandler;
import org.dementhium.net.message.Message;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public final class ClanPacketHandler extends PacketHandler {

    public static final int JOIN = 1, RANK = 51, MESSAGE = 52, KICK_BAN = 36;

    @Override
    public void handlePacket(Player player, Message packet) {
        switch (packet.getOpcode()) {
            case JOIN:
                String owner = "";
                if (packet.remaining() > 0) {
                    owner = packet.readRS2String().toLowerCase();
                }
                if (owner.length() > 0) {
                    World.getWorld().getClanManager().joinClan(player, owner);
                } else {
                    World.getWorld().getClanManager().leaveClan(player, false);
                    player.getSettings().setCurrentClan(null);
                }
                break;
            case RANK:
                handleRank(player, packet);
                break;
            case MESSAGE:
                boolean clanMessage = packet.readByte() > 0;
                player.setAttribute("sendingClanMessage", clanMessage);
                break;
            case KICK_BAN:
                handleBan(player, packet);
        }

    }

    private void handleBan(Player player, Message packet) {
        String playerName = packet.readRS2String().toLowerCase();
        World.getWorld().getClanManager().banMember(player, playerName);
    }

    private void handleRank(Player player, Message packet) {
        //something is sent before the string i think, ignore it though
        String playerName = packet.readRS2String().toLowerCase();
        int rank = packet.readByteA();
        World.getWorld().getClanManager().rankMember(player, playerName, rank);
    }

}
