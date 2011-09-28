package org.dementhium.content.skills.thieving;

import org.dementhium.content.skills.SkillAction;
import org.dementhium.model.Entity;
import org.dementhium.model.Mob;
import org.dementhium.model.World;
import org.dementhium.model.map.GameObject;
import org.dementhium.model.player.Player;

/**
 * The Thieving skill handling class.
 *
 * @author Emperor
 */
public class Thieving {

    /**
     * The player.
     */
    private final Player player;

    /**
     * The thieving action to execute.
     */
    private final SkillAction action;

    /**
     * Constructs a new {@code Thieving} {@code Object}.
     *
     * @param player The player.
     * @param action The thieving action.
     */
    public Thieving(Player player, SkillAction action) {
        this.player = player;
        this.action = action;
    }

    /**
     * Checks if the player is executing a {@code Thieving} action, and will return the instance of the action.
     *
     * @param player The player.
     * @param entity The entity.
     * @return The {@code Thieving} instance, or null if the player isn't executing a thieving action.
     */
    public static Thieving isAction(Player player, Entity entity) {
        if (entity instanceof Mob) {
            Mob mob = (Mob) entity;
            if (mob.isNPC()) {
                PickpocketableNPC npcData = PickpocketableNPC.get(mob.getNPC().getId());
                if (npcData != null) {
                    return new Thieving(player, new NPCPickpocketAction(player, mob.getNPC(), npcData));
                }
            }
        } else if (entity instanceof GameObject) {
        	GameObject object = (GameObject) entity;
        	ThievingStalls stall = ThievingStalls.get(object.getId());
        	if (stall != null) {
        		return new Thieving(player, new StallThieving(player, object, stall));
        	}
        }
        //TODO: Finish this.
        return null;
    }

    /**
     * Commences the thieving skill 'cycle'.
     */
    public void commence() {
        if (player.getAttribute("isStealing", false)) {
            return;
        }
        action.execute();
        World.getWorld().submit(action);
    }
}
