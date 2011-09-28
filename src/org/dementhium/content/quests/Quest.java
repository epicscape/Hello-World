package org.dementhium.content.quests;

/**
 * Represents a quest.
 * @author Emperor
 *
 */
public class Quest {

	/**
	 * The quest id.
	 */
	private final int id;
	
	/**
	 * The name of the quest.
	 */
	private String name;
	
	/**
	 * The finished state id.
	 */
	private int finishedState = 15;
	
	/**
	 * Constructs a new {@code Quest} {@code Object}.
	 * @param id The quest id.
	 */
	public Quest(int id) {
		this.id = id;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the finishedState
	 */
	public int getFinishedState() {
		return finishedState;
	}

	/**
	 * @param finishedState the finishedState to set
	 */
	public void setFinishedState(int finishedState) {
		this.finishedState = finishedState;
	}
}