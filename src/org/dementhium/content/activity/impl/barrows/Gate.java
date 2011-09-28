package org.dementhium.content.activity.impl.barrows;

import org.dementhium.model.Location;
import org.dementhium.model.map.GameObject;
import org.dementhium.model.npc.NPC;

/**
 * Represents a Gate in the tunnels.
 * @author Emperor
 *
 */
public class Gate {
	
	/**
	 * One of the Barrows brothers.
	 */
	private NPC npc;
	
	/**
	 * If the Barrows NPC (if any) has spawned.
	 */
	private boolean spawned;
	
	/**
	 * The closed flag.
	 */
	private boolean closed;
	
	/**
	 * The boject to replace the current door with.
	 */
	private final GameObject toReplace;
	
	/**
	 * The gate's location.
	 */
	private final Location location;
	
	/**
	 * The object id.
	 */
	private final int id;

	/**
	 * Creates a new {@code Gate} {@code Object}.
	 * @param id The object id.
	 * @param location The location of the gate.
	 * @param toReplace The object to replace this gate with (when opened).
	 */
	public Gate(int id, Location location, GameObject toReplace) {
		this.id = id;
		this.location = location;
		this.toReplace = toReplace;
	}
	
	/**
	 * Returns a 'duplicated' Gate Object of this Gate Object.
	 * @return The duplicated Gate Object.
	 */
	public Gate duplicate() {
		return new Gate(id, location, toReplace);
	}
	
	/**
	 * @param npc the npc to set
	 */
	public Gate setNPC(NPC npc) {
		this.npc = npc;
		return this;
	}

	/**
	 * @return the npc
	 */
	public NPC getNpc() {
		return npc;
	}

	/**
	 * @param spawned the spawned to set
	 */
	public Gate setSpawned(boolean spawned) {
		this.spawned = spawned;
		return this;
	}

	/**
	 * @return the spawned
	 */
	public boolean isSpawned() {
		return spawned;
	}

	/**
	 * @param closed the closed to set
	 */
	public Gate setClosed(boolean closed) {
		this.closed = closed;
		return this;
	}

	/**
	 * @return the closed
	 */
	public boolean isClosed() {
		return closed;
	}

	/**
	 * @return the location
	 */
	public Location getLocation() {
		return location;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the toReplace
	 */
	public GameObject getToReplace() {
		return toReplace;
	}
	
}