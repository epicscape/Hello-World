package org.dementhium.model.misc;

import java.util.ArrayList;
import java.util.List;

import org.dementhium.model.World;
import org.dementhium.model.player.Player;

/**
 * Handles skulling in the wilderness.
 * @author Emperor
 *
 */
public class SkullManager {

	/**
	 * The player.
	 */
	private final Player player;
	
	/**
	 * The amount of ticks left for the skull to disappear (if any).
	 */
	private int skullTicks = -1;
	
	/**
	 * A list of players this player has attacked first.
	 */
	private final List<Player> victims = new ArrayList<Player>();
	
	/**
	 * A list of players who attacked this player first.
	 */
	private final List<Player> attackers = new ArrayList<Player>();
	
	/**
	 * Constructs a new {@code SkullManager} {@code Object}.
	 * @param player The player.
	 */
	public SkullManager(Player player) {
		this.player = player;
	}
	
	/**
	 * Appends a skull on this player, if required.
	 * @param other The other player.
	 */
	public void appendSkull(Player other) {
		if (other.getSkullManager().getVictims().contains(player)) {
			return;
		}
		victims.add(other);
		player.setAttribute("skulled", true);
		skullTicks = World.getTicks() + 2000;
		player.getMask().setApperanceUpdate(true);
	}
	
	/**
	 * Gets the amount of ticks left.
	 * @return The amount of ticks.
	 */
	public int getTicks() {
		return skullTicks;
	}
	
	/**
	 * Sets the amount of skull ticks left.
	 * @param ticks The ticks.
	 */
	public void setTicks(int ticks) {
		this.skullTicks = ticks;
		player.getMask().setApperanceUpdate(true);
	}
	
	/**
	 * Checks if the player is skulled.
	 * @return {@code True} if so, {@code false} if not.
	 */
	public boolean isSkulled() {
		return skullTicks > World.getTicks();
	}

	/**
	 * @return the victims
	 */
	public List<Player> getVictims() {
		return victims;
	}

	/**
	 * @return the attackers
	 */
	public List<Player> getAttackers() {
		return attackers;
	}
}