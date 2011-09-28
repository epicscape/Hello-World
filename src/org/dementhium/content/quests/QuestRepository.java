package org.dementhium.content.quests;

import java.util.HashMap;
import java.util.Map;

/**
 * Works as a quest database and handles quest-related events.
 * @author Emperor
 *
 */
public class QuestRepository {

	/**
	 * The amount of quests available.
	 */
	public static final int AMOUNT_OF_QUESTS = 10;
	
	/**
	 * The mapping holding all quest data.
	 */
	private static final Map<Integer, Quest> QUEST_DATABASE = new HashMap<Integer, Quest>();
	
	/**
	 * Initializes the quests.
	 * @return {@code True} if succesful, {@code false} if not.
	 */
	public static boolean init() {
		return true;
	}
	
	/**
	 * Grabs a quest from the mapping.
	 * @param id The quest id.
	 * @return The {@code Quest} which id matches the id given, 
	 * 			or {@code null} if the quest didn't exist.
	 */
	public static Quest get(int id) {
		return QUEST_DATABASE.get(id);
	}
}