package org.dementhium.content.dialogue;

import org.dementhium.cache.format.CacheNPCDefinition;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;
import org.dementhium.util.Misc;

/**
 * Holds the different kinds of dialogue types.
 * @author Emperor
 *
 */
public enum DialogueType {

	/**
	 * Represents a normal dialogue.
	 */
	NORMAL(new DialogueSender() {
		@Override
		public void send(Player player, Dialogue dialogue) {
			if (dialogue.getMessage().size() == 0 || dialogue.getMessage().size() > 4) {
				return;
			}
			int interfaceId = (dialogue.getSpeaker() == -1 ? 63 : 240) + dialogue.getMessage().size();
			int index = 4;
			ActionSender.sendString(player, interfaceId, 3, dialogue.getSpeaker() == -1 ? 
					Misc.formatPlayerNameForDisplay(player.getUsername()) : 
						CacheNPCDefinition.forID(dialogue.getSpeaker()).getName());
			for (String string : dialogue.getMessage()) {
				ActionSender.sendString(player, interfaceId, index++, getFormatted(player, string));
			}
			ActionSender.sendChatboxInterface(player, interfaceId);
			ActionSender.sendEntityOnInterface(player, dialogue.getSpeaker() == -1, dialogue.getSpeaker(), interfaceId, 2);
			ActionSender.sendInterAnimation(player, dialogue.getAnimationId(), interfaceId, 2);
		}		
	}),
	
	/**
	 * Represents an option dialogue.
	 */
	OPTION(new DialogueSender() {
		@Override
		public void send(Player player, Dialogue dialogue) {
			if (dialogue.getMessage().size() < 2 || dialogue.getMessage().size() > 5) {
				return;
			}
			int interfaceId = 224 + (dialogue.getMessage().size() * 2);
			int index = 2;
			for (String string : dialogue.getMessage()) {
				ActionSender.sendString(player, getFormatted(player, string), interfaceId, index++);
			}
			ActionSender.sendChatboxInterface(player, interfaceId);
		}		
	}),
	
	/**
	 * Represents a normal display box.
	 */
	DISPLAY_BOX(new DialogueSender() {
		@Override
		public void send(Player player, Dialogue dialogue) {
			if (dialogue.getMessage().size() < 1 || dialogue.getMessage().size() > 5) {
				return;
			}
			int interfaceId = 209 + dialogue.getMessage().size();
			int index = 1;
			for (String string : dialogue.getMessage()) {
				ActionSender.sendString(player, getFormatted(player, string), interfaceId, index++);
			}
			ActionSender.sendChatboxInterface(player, interfaceId);
		}	
	}),
	
	/**
	 * Represents a dialogue box with an item shown.
	 */
	ITEM_BOX(new DialogueSender() {
		@Override
		public void send(Player player, Dialogue dialogue) {
			int interfaceId = 131;
			ActionSender.sendString(player, getFormatted(player, dialogue.getMessage().get(0)), interfaceId, 1);
			if (dialogue.getItems().size() == 1) {
				ActionSender.sendItemOnInterface(player, interfaceId, 2, 10, dialogue.getItems().get(0));
				
			} else if (dialogue.getItems().size() == 2) {
				ActionSender.sendItemOnInterface(player, interfaceId, 0, 1, dialogue.getItems().get(0));
				ActionSender.sendItemOnInterface(player, interfaceId, 2, 1, dialogue.getItems().get(1));
			}
			ActionSender.sendChatboxInterface(player, interfaceId);
		}		
	});
	
	/**
	 * The dialogue sender used.
	 */
	private final DialogueSender sender;
	
	/**
	 * Constructs a new {@code DialogueType} {@code Object}.
	 * @param sender The dialogue sender used.
	 */
	private DialogueType(DialogueSender sender) {
		this.sender = sender;
	}
	
	/**
	 * @return the sender
	 */
	public DialogueSender getSender() {
		return sender;
	}
	
	/**
	 * Gets the formatted string.
	 * @param string The string.
	 * @return The formatted string.
	 */
	private static String getFormatted(Player p, String string) {
		/*while (string.contains("@ITEM_NAME")) {
			int index = string.indexOf("@ITEM_NAME") + 9;
			StringBuilder sb = new StringBuilder();
			for (int i = index; i < string.length(); i++) {
				char c = string.charAt(i);
				if (Character.isDigit(c)) {
					sb.append(c);
				} else if (c == '@') {
					break;
				}
			}
			string.replace(new StringBuilder("@ITEM_NAME").append(sb.toString()).append("@").toString(), ItemDefinition.forId(Integer.parseInt(sb.toString())).getName());
		}*/
		return string.replaceAll("@PLAYER_NAME@", Misc.formatPlayerNameForDisplay(p.getUsername()));
	}

	/**
	 * The class used to send the dialogue.
	 * @author Emperor
	 *
	 */
	static abstract class DialogueSender {
		
		/**
		 * Sends the dialogue.
		 * @param player The player.
		 * @param dialogue The dialogue.
		 */
		public abstract void send(Player player, Dialogue dialogue);
	}
}