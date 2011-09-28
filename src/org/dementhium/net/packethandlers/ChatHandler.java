package org.dementhium.net.packethandlers;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.dementhium.content.Commands;
import org.dementhium.content.misc.ChatCensor;
import org.dementhium.model.World;
import org.dementhium.model.mask.ChatMessage;
import org.dementhium.model.player.Player;
import org.dementhium.net.PacketHandler;
import org.dementhium.net.message.Message;
import org.dementhium.util.Misc;
import org.dementhium.util.TextUtils;

/**
 * @author 'Mystic Flow
 */
public final class ChatHandler extends PacketHandler {

	@Override
	public void handlePacket(Player player, Message packet) {
		switch (packet.getOpcode()) {
		case 16:
			appendChat(player, packet);
			break;

		}
	}

	/**
	 * Handles the public chat message packet.
	 * 
	 * @param player
	 *            The player.
	 * @param packet
	 *            The packet object.
	 */
	private void appendChat(Player player, Message packet) {
		int effects = packet.readShort() & 0xffff;
		int numChars = packet.readUnsignedByte();
		String text = TextUtils.decompressHuffman(packet, numChars);
		if (text.startsWith("::")) {
			Commands.handle(player, text.substring(2).split(" "));
			return;
		}
		if (World.getWorld().getPunishHandler().isMuted(player)) {
			player.sendMessage("You cannot chat because you are muted!");
			return;
		}
		text = Misc.optimizeText(text);
		if (player.getAttribute("sendingClanMessage", false)) {
			if (player.getSettings().getCurrentClan() != null) {
				World.getWorld().getClanManager().sendClanMessage(player, text);
				player.setAttribute("sendingClanMessage", false);
				return;
			} else {
				text = "/" + text;
			}
		}
		/*
		 * if(player.getSettings().getCurrentClan() != null) {
		 * if(text.startsWith("/") && !player.getConnection().isInLobby()) {
		 * text = Misc.optimizeText(text.substring(1));
		 * World.getWorld().getClanManager().sendClanMessage(player, text);
		 * return; } else if(player.getConnection().isInLobby()) {
		 * World.getWorld().getClanManager().sendClanMessage(player, text);
		 * return; } }
		 */
		// text = ChatCensor.checkMessage(player, text);
		saveChatMessage(player, text);
		player.getMask().setLastChatMessage(
				new ChatMessage(effects, numChars, text));
	}

	/**
	 * Saves a players chat.
	 * 
	 * @param player the players' chat to save.
	 * @param text the chat.
	 */
	private void saveChatMessage(Player player, String text) {
		try {
			BufferedWriter bf = new BufferedWriter(new FileWriter(
					"data/chat/chatlog.txt", true));
			bf.write("[" + DateFormat.getDateTimeInstance().format(new Date())
					+ " " + Calendar.getInstance().getTimeZone().getDisplayName() + "] " + player.getUsername() + ": "
					+ text);
			bf.newLine();
			bf.flush();
			bf.close();
		} catch (IOException ignored) {
		}
	}
}
