package org.dementhium.model.combat;

import org.dementhium.model.combat.impl.DefaultAction;
import org.dementhium.model.combat.impl.MagicAction;
import org.dementhium.model.combat.impl.MeleeAction;
import org.dementhium.model.combat.impl.RangeAction;
import org.dementhium.model.misc.DamageManager.DamageType;

/**
 * Represents the combat types.
 * @author Emperor
 *
 */
public enum CombatType {
	
	/**
	 * Represents the melee combat type.
	 */
	MELEE(new MeleeAction(), 19, 9, 0, 1, DamageType.MELEE), 
	
	/**
	 * Represents the range combat type.
	 */
	RANGE(new RangeAction(), 18, 8, 2, 10, DamageType.RANGE),
	
	/**
	 * Represents the magic combat type.
	 */
	MAGIC(new MagicAction(), 17, 7, 1, 15, DamageType.MAGE),
	
	/**
	 * Represents the dragonfire combat type.
	 */
	DRAGONFIRE(new DefaultAction(), 17, 7, -1, 17, DamageType.RED_DAMAGE),
	
	/**
	 * Represents a default combat type, which does not use combat.
	 */
	DEFAULT(new DefaultAction(), -1, -1, -1, 0, DamageType.MELEE);
	
	/**
	 * The default combat action for this combat type.
	 */
	private final CombatAction combatAction;
	
	/**
	 * The protection prayer id.
	 */
	private final int protectionPrayer;
	
	/**
	 * The deflect curse id.
	 */
	private final int deflectCurse;
	
	/**
	 * The absorb damage id.
	 */
	private final int absorbtion;
	
	/**
	 * The maximum distance.
	 */
	private final int distance;
	
	/**
	 * The damage type for this combat type.
	 */
	private final DamageType damageType;

	/**
	 * Constructs a new {@code CombatType} {@code Object}.
	 * @param combatAction The default combat action used.
	 * @param protectionPrayer The protection prayer for this combat type.
	 * @param deflectCurse The deflect curse for this combat type.
	 * @param absorbtion The absorbtion bonus id.
	 * @param distance The maximum distance for this combat type.
	 * @param damageType The damage type.
	 */
	private CombatType(CombatAction combatAction, int protectionPrayer, int deflectCurse, int absorbtion, int distance, DamageType damageType) {
		this.combatAction = combatAction;
		this.protectionPrayer = protectionPrayer;
		this.deflectCurse = deflectCurse;
		this.absorbtion = absorbtion;
		this.distance = distance;
		this.damageType = damageType;
	}

	/**
	 * @return the protectionPrayer
	 */
	public int getProtectionPrayer() {
		return protectionPrayer;
	}

	/**
	 * @return the deflectCurse
	 */
	public int getDeflectCurse() {
		return deflectCurse;
	}

	/**
	 * @return the combatAction
	 */
	public CombatAction getCombatAction() {
		return combatAction;
	}

	/**
	 * @return the absorb damage id.
	 */
	public int getAbsorbtion() {
		return absorbtion;
	}

	/**
	 * @return the distance
	 */
	public int getDistance() {
		return distance;
	}

	/**
	 * @return the damageType
	 */
	public DamageType getDamageType() {
		return damageType;
	}

}
