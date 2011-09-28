package org.dementhium.model.npc.impl;

import java.util.List;

import org.dementhium.model.combat.CombatAction;
import org.dementhium.model.combat.impl.npc.KingBlackDragonAction;
import org.dementhium.model.map.Region;
import org.dementhium.model.npc.NPC;
import org.dementhium.model.player.Player;

/**
 * The King Black Dragon class, represents the King Black Dragon (surprisingly).
 *
 * @author Emperor
 */
public class KingBlackDragon extends NPC {

    /**
     * The custom combat action.
     */
    private static final CombatAction COMBAT_ACTION = new KingBlackDragonAction();

    /**
     * Constructs a new {@code KingBlackDragon} {@code Object}.
     *
     * @param id The npc id.
     */
    public KingBlackDragon(int id) {
        super(id);
    }
    
    @Override
    public void tick() {
    	super.tick();
    	if (getRandom().nextInt(5) < 2) {
    		List<Player> targets = Region.getLocalPlayers(getLocation(), 30);
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