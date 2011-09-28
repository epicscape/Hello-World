package org.dementhium.net.packethandlers;

import org.dementhium.model.World;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;
import org.dementhium.net.PacketHandler;
import org.dementhium.net.message.Message;
import org.dementhium.util.Misc;
import org.dementhium.util.TextUtils;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public final class FriendPacketHandler extends PacketHandler {

    public static final int ADD_FRIEND = 2, REMOVE_FRIEND = 77, ADD_IGNORE = 74, REMOVE_IGNORE = -1;

    public static final int SEND_PRIVATE_MESSAGE = 41;

    @Override
    public void handlePacket(Player player, Message packet) {
        switch (packet.getOpcode()) {
            case ADD_IGNORE:
                player.getFriendManager().addIgnore(packet.readRS2String().toLowerCase());
                break;
            case ADD_FRIEND:
                player.getFriendManager().addFriend(packet.readRS2String().toLowerCase());
                break;
            case REMOVE_FRIEND:
                player.getFriendManager().removeFriend(packet.readRS2String().toLowerCase());
                break;
            case REMOVE_IGNORE:
                player.getFriendManager().removeIgnore(packet.readRS2String().toLowerCase());
                break;
            case SEND_PRIVATE_MESSAGE:
                String otherName = Misc.formatPlayerNameForDisplay(packet.readRS2String()).toLowerCase();
                if (otherName == null || otherName.equals(player.getUsername()))
                    return;
                int numChars = packet.readUnsignedByte();
                String outMessage = TextUtils.decompressHuffman(packet, numChars);
                if (outMessage == null)
                    return;
                outMessage = Misc.optimizeText(outMessage);
                Player sendPlayer = null;
                if (World.getWorld().getPunishHandler().isMuted(player)) {
                    player.sendMessage("You cannot chat becase you are muted!");
                    return;
                }
                for (Player p2 : World.getWorld().getPlayers()) {
                    if (!p2.getUsername().equals(otherName))
                        continue;
                    sendPlayer = p2;
                    break;
                }
                if (sendPlayer == null) {
                    for (Player p2 : World.getWorld().getLobbyPlayers()) {
                        if (!p2.getUsername().equals(otherName))
                            continue;
                        sendPlayer = p2;
                        break;
                    }
                }
                if (sendPlayer != null) {
                    ActionSender.sendPrivateMessage(player, otherName, outMessage);
                    ActionSender.receivePrivateMessage(sendPlayer, Misc.formatPlayerNameForDisplay(player.getUsername()), Misc.formatPlayerNameForDisplay(player.getUsername()), player.getRights(), outMessage);
                    return;
                } else {
                    player.sendMessage("That player is not available at the moment.");//XXX Make this message like rs
                }
                break;
        }
    }

}
