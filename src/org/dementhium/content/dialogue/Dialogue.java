package org.dementhium.content.dialogue;

import java.util.ArrayList;
import java.util.List;

import org.dementhium.model.player.Player;

/**
 * Represents a dialogue.
 * @author Emperor
 *
 */
public class Dialogue {

	/**
	 * The id of the speaker (-1 for players, NPC id for NPCs).
	 */
	private int speaker = -1;
	
	/**
	 * The animation id the mob has to use on this dialogue.
	 */
	private int animationId = 9810;
	
	/**
	 * The dialogue type.
	 */
	private DialogueType type;

	/**
	 * A list of items to be shown on an item box.
	 */
	private final List<Integer> items = new ArrayList<Integer>();
	
	/**
	 * The message to send.
	 */
	private final List<String> message = new ArrayList<String>();
	
	/**
	 * The dialogue to send if the player doesn't meet certain requirements.
	 */
	private final List<Integer> failDialogueIds = new ArrayList<Integer>();
	
	/**
	 * The requirements.
	 */
	private final List<Requirement> requirements = new ArrayList<Requirement>();
	
	/**
	 * The option actions.
	 */
	private final List<OptionAction> actions = new ArrayList<OptionAction>();
	
	/**
	 * Sends the dialogue to the player.
	 * @param player The player.
	 */
	public void send(Player player) {
		for (int i = 0; i < requirements.size(); i++) {
			if (!requirements.get(i).hasRequirements(player)) {
				DialogueManager.sendDialogue(player, failDialogueIds.get(i));
				return;
			}
		}
		player.setAttribute("dialogue", this);
		type.getSender().send(player, this);
	}

	/**
	 * @return the speaker
	 */
	public int getSpeaker() {
		return speaker;
	}

	/**
	 * @param speaker the speaker to set
	 */
	public void setSpeaker(int speaker) {
		this.speaker = speaker;
	}

	/**
	 * @return the animationId
	 */
	public int getAnimationId() {
		return animationId;
	}

	/**
	 * @param animationId the animationId to set
	 */
	public void setAnimationId(int animationId) {
		this.animationId = animationId;
	}

	/**
	 * @return the type
	 */
	public DialogueType getType() {
		return type;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(DialogueType type) {
		this.type = type;
	}

	/**
	 * @return the actions
	 */
	public List<OptionAction> getActions() {
		return actions;
	}

	/**
	 * @return the message
	 */
	public List<String> getMessage() {
		return message;
	}

	/**
	 * @return the failDialogueIds
	 */
	public List<Integer> getFailDialogueIds() {
		return failDialogueIds;
	}

	/**
	 * @return the requirements
	 */
	public List<Requirement> getRequirements() {
		return requirements;
	}

	/**
	 * @return the items
	 */
	public List<Integer> getItems() {
		return items;
	}

}