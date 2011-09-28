package org.dementhium.model.player;

import org.dementhium.content.quests.Quest;
import org.dementhium.content.quests.QuestRepository;

/**
 * Holds quest data to be saved.
 * @author Emperor
 *
 */
public class QuestStorage {
	
	/**
	 * The current quest states.
	 */
	private final int[] QUEST_STATES = new int[QuestRepository.AMOUNT_OF_QUESTS];
	
	/**
	 * Gets the quest state.
	 * @param questId The quest id to check.
	 * @return The quest state (0 at default).
	 */
	public int get(int questId) {
		return QUEST_STATES[questId];
	}
	
	/**
	 * Checks if the player has started a certain quest.
	 * @param questId The quest's id.
	 * @return {@code True} if the player has started the quest, {@code false} if not.
	 */
	public boolean hasStarted(int questId) {
		return QUEST_STATES[questId] > 0;
	}
	
	/**
	 * Checks if the player has finished a certain quest.
	 * @param questId The quest id.
	 * @return {@code True} if so, {@code false} if not.
	 */
	public boolean hasFinished(int questId) {
		Quest quest = QuestRepository.get(questId);
		return quest != null && quest.getFinishedState() <= QUEST_STATES[questId];
	}

	/**
	 * Sets a current quest state.
	 * @param questId The quest's id.
	 * @param state The state to set.
	 */
	public void set(int questId, int state) {
		QUEST_STATES[questId] = state;
	}
}