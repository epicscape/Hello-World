package org.dementhium.content.dialogue;

import java.util.ArrayList;
import java.util.List;

import org.dementhium.model.Item;
import org.dementhium.model.player.Player;

/**
 * Handles an option.
 * @author Emperor
 *
 */
public abstract class OptionAction {

	/**
	 * Represents several kinds of action types.
	 * @author Emperor
	 *
	 */
	public static enum ActionType {
		NEXT_DIALOGUE,
		CLOSE_DIALOGUE,
		QUEST_OPTION;
	}
	
	/**
	 * Creates an option action.
	 * @param type The action type.
	 * @param arguments The arguments.
	 * @return The option action.
	 */
	public static OptionAction create(ActionType type, final Object...arguments) {
		switch (type) {
		case NEXT_DIALOGUE:
			return new OptionAction() {
				@Override
				public boolean handle(Player player) {
					Dialogue dialogue = DialogueManager.get((Integer) arguments[0]);
					if (dialogue != null) {
						dialogue.send(player);
					}
					if (arguments.length > 1) {
						handleArguments(player, 1, arguments);
					}
					return dialogue == null;
				}
			};
		case CLOSE_DIALOGUE:
			return new OptionAction() {
				@Override
				public boolean handle(Player player) {
					if (arguments.length > 0) {
						handleArguments(player, 0, arguments);
					}
					return true;
				}
			};
		}
		return null;
	}
	
	/**
	 * Handles argument actions.
	 * @param player The player.
	 * @param start The start index.
	 * @param args The arguments.
	 */
	private static void handleArguments(Player player, int start, Object...args) {
		while (start < args.length) {
			Object object = args[start++];
			if (object instanceof String) {
				String s = (String) object;
				if (s.equals("ADD_ITEM")) {
					player.getInventory().addItem((Integer) args[start++], (Integer) args[start++]);
				} else if (s.equals("REMOVE_ITEM")) {
					int id = (Integer) args[start++];
					int amount = (Integer) args[start++];
					if (!player.getInventory().contains(id, amount)) {
						return;
					}
					player.getInventory().deleteItem(id, amount);
				} else if (s.equals("REMOVE_AND_ADD")) {
					List<Item> toRemove = new ArrayList<Item>();
					List<Item> toAdd = new ArrayList<Item>();
					while (start < args.length) {
						object = args[start++];
						if (object instanceof String) {
							s = (String) object;
							if (s.equals("REMOVE")) {
								toRemove.add(new Item((Integer) args[start++], (Integer) args[start++]));
							} else if (s.equals("ADD")) {
								toAdd.add(new Item((Integer) args[start++], (Integer) args[start++]));
							} else if (s.equals("END")){
								for (Item item : toRemove) {
									if (!player.getInventory().contains(item)) {
										return;
									}
								}
								for (Item item : toRemove) {
									player.getInventory().removeItems(item);
								}
								for (Item item : toAdd) {
									player.getInventory().addDropable(item);
								}
								break;
							}
						}
					}
				} else if (s.equals("ANIMATE")) {
					player.animate((Integer) args[start++]);
				} else if (s.equals("GRAPHIC")) {
					int gfxId = (Integer) args[start++];
					int height = 0;
					if (start < args.length - 1 && args[start + 1] instanceof Integer) {
						height = (Integer) args[start++];
					}
					player.graphics(gfxId, height << 16);
				} else if (s.equals("EXPERIENCE")) {
					player.getSkills().addExperience((Integer) args[start++], (Integer) args[start++]);
				}
			}
		}
	}
	
	/**
	 * Handles an option action.
	 * @param player The player.
	 * @return {@code True} if the dialogue should end, {@code false} if not.
	 */
	public abstract boolean handle(Player player);
	
}