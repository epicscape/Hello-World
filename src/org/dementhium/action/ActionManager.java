package org.dementhium.action;

import org.dementhium.model.Mob;
import org.dementhium.model.World;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class ActionManager {

    private Action currentAction;
    private Mob mob;

    public ActionManager(Mob mob) {
        this.mob = mob;
    }

    public void appendAction(Action action) {
        if (currentAction != null) {
            stopAction();
        }
        currentAction = action;
        currentAction.setEntity(mob);
        World.getWorld().submit(action);
    }

    public void stopAction() {
        if (currentAction != null) {
            currentAction.stop();
            currentAction = null;
        }
        mob.removeTick("projectile_clip");
        mob.removeTick("skill_action_tick");
        mob.removeTick("attack_cw_door");
        mob.removeTick("following_mob");
        mob.removeTick("area_event");
        mob.removeTick("trap_action");
        mob.removeTick("agility_tick");
    }

    public void stopNonWalkableActions() {
        if (currentAction != null) {
            currentAction.stop();
            currentAction = null;
        }
        mob.removeTick("skill_action_tick");
        mob.removeTick("attack_cw_door");
        mob.removeTick("area_event");
        mob.removeTick("trap_action");
        mob.removeTick("agility_tick");
    }
}
