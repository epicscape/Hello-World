package org.dementhium.task.impl;

import org.dementhium.model.World;
import org.dementhium.model.map.Region;
import org.dementhium.model.map.path.PathState;
import org.dementhium.model.map.path.PrimitivePathFinder;
import org.dementhium.model.misc.GodwarsUtils.Faction;
import org.dementhium.model.npc.NPC;
import org.dementhium.model.player.Player;
import org.dementhium.task.Task;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class NPCTickTask implements Task {

    private final NPC npc;

    public NPCTickTask(NPC npc) {
        this.npc = npc;
    }

    @Override
    public void execute() {
        npc.getCombatExecutor().tick();
        npc.processTicks();
        npc.tick();
        npc.getWalkingQueue().getNextEntityMovement();
        if (npc.isDead() || npc.getCombatExecutor().getVictim() != null || !npc.getDefinition().isAggressive()) {
        	return;
        }
        int currentDistance = 20;
        Player toAttack = null;
        for (Player player : Region.getLocalPlayers(npc.getLocation(), 5)) {
        	PathState path = World.getWorld().doPath(new PrimitivePathFinder(), npc, player.getLocation().getX(), player.getLocation().getY(), false, false, true);
        	if (path == null || !path.isRouteFound() || !player.isAttackable(npc)) {
        		continue;
        	}
        	int combatLevel = player.getSkills().getCombatLevel();
        	if (combatLevel > npc.getDefinition().getCombatLevel() * 2) {
        		continue;
        	}
        	int distance = npc.getLocation().getDistance(player.getLocation());
        	if (distance < currentDistance) {
        		currentDistance = distance;
        		toAttack = player;
        	}
        }
        npc.getCombatExecutor().setVictim(toAttack);
    }

    private boolean ignoreGodItems(int id) {
        switch (id) {
            case 6222:
            case 6223:
            case 6225:
            case 6227:
            case 6203:
            case 6204:
            case 6206:
            case 6208:
            case 6247:
            case 6248:
            case 6250:
            case 6252:
            case 6260:
            case 6261:
            case 6263:
            case 6265:
                return true;
        }
        return false;
    }

    private boolean canAttackFaction(Faction faction, Faction faction2) {
        if (faction == null || faction2 == null) {
            return false;
        } else if (faction.equals(Faction.ARMADYL) && faction2.equals(Faction.SARADOMIN)) {
            return false;
        } else if (faction.equals(Faction.SARADOMIN) && faction2.equals(Faction.ARMADYL)) {
            return false;
        } else {
            return !faction.equals(faction2);
        }
    }

}
