package org.dementhium.model.combat;

import java.util.List;

import org.dementhium.model.Mob;
import org.dementhium.model.SpecialAttack;
import org.dementhium.model.mask.Graphic;
import org.dementhium.util.misc.CycleState;

/**
 * Represents an interaction between 2 mobs.
 * @author Emperor
 *
 */
public class Interaction {

	/**
	 * The attacking mob.
	 */
	private final Mob source;
	
	/**
	 * The mob being attacked.
	 */
	private final Mob victim;
	
	/**
	 * The amount of ticks left.
	 */
	private int ticks = 0;
	
	/**
	 * The current cycle state.
	 */
	private CycleState state = CycleState.COMMENCE;
		
	/**
	 * The damage to deal.
	 */
	private Damage damage;
	
	/**
	 * If the victim has a deflect curse.
	 */
	private boolean deflected;

	/**
	 * The range data.
	 */
	private RangeData rangeData;
	
	/**
	 * The magic spell to execute.
	 */
	private MagicSpell spell;
	
	/**
	 * The end graphic.
	 */
	private Graphic endGraphic;
	
	/**
	 * A list of targets to hit in a multi-damage cycle.
	 */
	private List<ExtraTarget> targets;
	
	/**
	 * The special attack used.
	 */
	private SpecialAttack specialAttack;
	
	/**
	 * Constructs a new {@code Interaction} {@code Object}.
	 * @param source The source mob.
	 * @param victim The victim mob.
	 */
	public Interaction(Mob source, Mob victim) {
		this.source = source;
		this.victim = victim;
	}

	/**
	 * @return the source
	 */
	public Mob getSource() {
		return source;
	}

	/**
	 * @return the victim
	 */
	public Mob getVictim() {
		return victim;
	}

	/**
	 * @return the ticks
	 */
	public int getTicks() {
		return ticks;
	}

	/**
	 * @param ticks the ticks to set
	 */
	public void setTicks(int ticks) {
		this.ticks = ticks;
	}

	/**
	 * @return the state
	 */
	public CycleState getState() {
		return state;
	}

	/**
	 * @param state the state to set
	 */
	public void setState(CycleState state) {
		this.state = state;
	}

	/**
	 * @return the damage
	 */
	public Damage getDamage() {
		return damage;
	}

	/**
	 * @param damage the damage to set
	 */
	public void setDamage(Damage damage) {
		this.damage = damage;
	}

	/**
	 * @return the deflected
	 */
	public boolean isDeflected() {
		return deflected;
	}

	/**
	 * @param deflected the deflected to set
	 */
	public void setDeflected(boolean deflected) {
		this.deflected = deflected;
	}

	/**
	 * @return the rangeData
	 */
	public RangeData getRangeData() {
		return rangeData;
	}

	/**
	 * @param rangeData the rangeData to set
	 */
	public void setRangeData(RangeData rangeData) {
		this.rangeData = rangeData;
	}

	/**
	 * @return the spell
	 */
	public MagicSpell getSpell() {
		return spell;
	}

	/**
	 * @param spell the spell to set
	 */
	public void setSpell(MagicSpell spell) {
		this.spell = spell;
	}

	/**
	 * @return the targets
	 */
	public List<ExtraTarget> getTargets() {
		return targets;
	}

	/**
	 * @param targets the targets to set
	 */
	public void setTargets(List<ExtraTarget> targets) {
		this.targets = targets;
	}

	/**
	 * @return the endGraphic
	 */
	public Graphic getEndGraphic() {
		return endGraphic;
	}

	/**
	 * @param endGraphic the endGraphic to set
	 */
	public void setEndGraphic(Graphic endGraphic) {
		this.endGraphic = endGraphic;
	}

	/**
	 * @return the specialAttack
	 */
	public SpecialAttack getSpecialAttack() {
		return specialAttack;
	}

	/**
	 * @param specialAttack the specialAttack to set
	 */
	public void setSpecialAttack(SpecialAttack specialAttack) {
		this.specialAttack = specialAttack;
	}
	
}