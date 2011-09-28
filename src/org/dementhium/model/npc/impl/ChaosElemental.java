package org.dementhium.model.npc.impl;

import java.util.List;

import org.dementhium.model.combat.CombatAction;
import org.dementhium.model.combat.impl.npc.ChaosElementalAction;
import org.dementhium.model.map.Region;
import org.dementhium.model.npc.NPC;
import org.dementhium.model.player.Player;

/**
 * Represents the chaos elemental.
 * @author Emperor
 *
 */
public class ChaosElemental extends NPC {

	/**
	 * The combat action to use.
	 */
	private final CombatAction COMBAT_ACTION = new ChaosElementalAction();
	
	/**
	 * Constructs a new {@code ChaosElemental} {@code Object}.
	 * @param id The npc id.
	 */
	public ChaosElemental(int id) {
		super(id);
	}
	
    @Override
    public void tick() {
    	super.tick();
    	if (getRandom().nextInt(5) < 2) {
    		List<Player> targets = Region.getLocalPlayers(getLocation(), 15);
    		if (targets.size() > 0) {
    			getCombatExecutor().setVictim(targets.get(getRandom().nextInt(targets.size())));
    		}
    	}
    }

	@Override
	public CombatAction getCombatAction() {
		return COMBAT_ACTION;
	}
}