package org.dementhium.model.combat;

import org.dementhium.model.Mob;
import org.dementhium.model.mask.Graphic;

/**
 * Represents a single target in a multi-damage combat cycle.
 * @author Emperor
 *
 */
public class ExtraTarget {

	/**
	 * The entity being hit.
	 */
	private final Mob victim;
	
	/**
	 * The damage dealt.
	 */
	private Damage damage;
	
	/**
	 * If the victim is deflecting.
	 */
	private boolean isDeflected;
	
	/**
	 * If the player is getting frozen (used for multi-ice attacks).
	 */
	private boolean frozen;
	
	/**
	 * The graphics to cast on hit.
	 */
	private Graphic endGraphics;
	
	/**
	 * Constructs a new {@code MultiDamageTarget} {@code Object}.
	 * @param victim The mob being hit.
	 */
	public ExtraTarget(Mob victim) {
		this.victim = victim;
	}

	/**
	 * @return the victim
	 */
	public Mob getVictim() {
		return victim;
	}

	/**
	 * @param damage the damage to set
	 */
	public void setDamage(Damage damage) {
		this.damage = damage;
	}

	/**
	 * @return the damage
	 */
	public Damage getDamage() {
		return damage;
	}

	/**
	 * @param isDeflected the isDeflected to set
	 */
	public void setDeflected(boolean isDeflected) {
		this.isDeflected = isDeflected;
	}

	/**
	 * @return the isDeflected
	 */
	public boolean isDeflected() {
		return isDeflected;
	}

	/**
	 * @param endGraphics the endGraphics to set
	 */
	public void setEndGraphics(Graphic endGraphics) {
		this.endGraphics = endGraphics;
	}

	/**
	 * @return the endGraphics
	 */
	public Graphic getEndGraphics() {
		return endGraphics;
	}

	/**
	 * @return the frozen
	 */
	public boolean isFrozen() {
		return frozen;
	}

	/**
	 * @param frozen the frozen to set
	 */
	public void setFrozen(boolean frozen) {
		this.frozen = frozen;
	}
}