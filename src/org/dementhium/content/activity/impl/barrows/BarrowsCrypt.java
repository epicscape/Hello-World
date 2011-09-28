package org.dementhium.content.activity.impl.barrows;

import org.dementhium.model.Entity;
import org.dementhium.model.npc.NPC;
import org.dementhium.util.Area;

/**
 * Represents a single crypt in the Barrows activity.
 * @author Emperor
 *
 */
public class BarrowsCrypt extends Entity {

	/**
	 * If this crypt provides the tunnel entrance, rather than spawning the NPC.
	 */
	private boolean tunnelsEntrance;
	
	/**
	 * The barrow NPC to spawn.
	 */
	private final NPC npc;
	
	/**
	 * The crypt area.
	 */
	private final Area area;
	
	/**
	 * Constructs a new {@code BarrowsCrypt} {@code Object}.
	 * @param npc The npc.
	 * @param area The area.
	 */
	public BarrowsCrypt(NPC npc, Area area) {
		this.npc = npc;
		this.area = area;
	}
	
	/**
	 * Returns a duplicated form of this object.
	 * @return The barrows crypt instance.
	 */
	public BarrowsCrypt duplicate() {
		return new BarrowsCrypt(new NPC(npc.getId()), area);
	}
	
	@Override
	public boolean equals(Object o) {
		if (o == null || !(o instanceof BarrowsCrypt)) {
			return false;
		}
		BarrowsCrypt b = (BarrowsCrypt) o;
		return npc.getId() == b.npc.getId();
	}

	/**
	 * @param tunnelsEntrance the tunnelsEntrance to set
	 */
	public void setTunnelsEntrance(boolean tunnelsEntrance) {
		this.tunnelsEntrance = tunnelsEntrance;
	}

	/**
	 * @return the tunnelsEntrance
	 */
	public boolean isTunnelsEntrance() {
		return tunnelsEntrance;
	}

	/**
	 * @return the npc
	 */
	public NPC getNPC() {
		return npc;
	}

	/**
	 * @return the area
	 */
	public Area getArea() {
		return area;
	}
}
