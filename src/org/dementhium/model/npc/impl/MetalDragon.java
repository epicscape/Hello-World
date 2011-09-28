package org.dementhium.model.npc.impl;

import org.dementhium.model.Mob;
import org.dementhium.model.combat.CombatAction;
import org.dementhium.model.combat.CombatType;
import org.dementhium.model.combat.Damage;
import org.dementhium.model.combat.impl.npc.MetalDragonAction;
import org.dementhium.model.npc.NPC;

/**
 * Represents a Metal Dragon.
 *
 * @author Emperor
 */
public class MetalDragon extends NPC {

	/**
	 * The combat action to use.
	 */
	private static CombatAction combatAction = new MetalDragonAction();
	
    /**
     * Constructs a new {@code MetalDragon} {@code Object}.
     *
     * @param id The NPC id.
     */
    public MetalDragon(int id) {
        super(id);
        setAttribute("dragonfireName", "fiery breath");
    }

    @Override
    public int getAttackDelay() {
    	return 4;
    }
    
	@Override
	public Damage updateHit(Mob source, int hit, CombatType type) {
		if (type == CombatType.DRAGONFIRE) {
			return new Damage(0);
		}
		return new Damage(hit);
	}
	
	@Override
	public CombatAction getCombatAction() {
		return combatAction;
	}

}