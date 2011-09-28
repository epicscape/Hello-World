package org.dementhium.content.dialogue;

import org.dementhium.model.Item;
import org.dementhium.model.player.Player;

/**
 * The interface implemented for certain requirements.
 * @author Emperor
 *
 */
public abstract class Requirement {

	/**
	 * Represents requirement types.
	 * @author Emperor
	 *
	 */
	public static enum Type {
		SKILL_LEVEL,
		INVENTORY_ITEM,
		EQUIPMENT_ITEM,
		BANK_ITEM,
		QUEST_STATE;
	}
	
	/**
	 * Creates a certain requirement.
	 * @param type The requirement type.
	 * @param arguments The arguments.
	 * @return The requirement instance.
	 */
	public static Requirement create(Type type, final Object...arguments) {
		switch (type) {
		case SKILL_LEVEL:
			return new Requirement() {
				@Override
				public boolean hasRequirements(Player player) {
					return player.getSkills().getLevel((Integer) arguments[0]) >= (Integer) arguments[1];
				}
			};
		case INVENTORY_ITEM:
			return new Requirement() {
				@Override
				public boolean hasRequirements(Player player) {
					return player.getInventory().contains((Integer) arguments[0], (Integer) arguments[1]);
				}
			};
		case EQUIPMENT_ITEM:
			return new Requirement() {
				@Override
				public boolean hasRequirements(Player player) {
					return player.getEquipment().getContainer().contains(new Item((Integer) arguments[0], (Integer) arguments[1]));
				}
			};
		case BANK_ITEM:
			return new Requirement() {
				@Override
				public boolean hasRequirements(Player player) {
					return player.getBank().contains((Integer) arguments[0], (Integer) arguments[1]);
				}
			};
		case QUEST_STATE:
			return new Requirement() {
				@Override
				public boolean hasRequirements(Player player) {
					return player.getQuestStorage().get((Integer) arguments[0]) == (Integer) arguments[1];
				}
			};			
		}
		return null;
	}
	
	/**
	 * Checks if a player matches the requirements for a dialogue.
	 * @param player The player.
	 * @return {@code True} if so, {@code false} if not.
	 */
	public abstract boolean hasRequirements(Player player);
	
}