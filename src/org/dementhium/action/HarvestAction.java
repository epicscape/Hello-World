package org.dementhium.action;

import org.dementhium.model.Item;
import org.dementhium.model.map.GameObject;
import org.dementhium.model.map.ObjectManager;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.player.Player;
import org.dementhium.util.Misc;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public abstract class HarvestAction extends Action {

    public static final int TOOL_LEVEL = 0, NO_TOOL = 1, OBJECT_LEVEL = 2, HARVESTED_ITEM = 3;

    public interface HarvestTool {

        public Animation getAnimation();

        public int getRequiredLevel();
    }

    public interface HarvestObject {

        public int getRequiredLevel();

        public double getExperience();

        public int getMaxHealth();

        public int getRestoreDelay();

    }

    private int cycles;

    private int animationCycles;

    private boolean started;

    public HarvestAction() {
        super(1);
    }

    @Override
    public void execute() {
        HarvestObject object = getHarvestObject();
        GameObject gameObject = getGameObject();
        if (gameObject == null || object == null) {
            stopHarvesting();
            return;
        }
        if (gameObject.getLocation().getGameObject(gameObject.getId()) == null) {
            return;
        }
        if (gameObject.getHealth() <= 0 && gameObject.isHealthSet()) {
            stopHarvesting();
            return;
        }
        HarvestTool tool = getTool();
        Player player = mob.getPlayer();
        int level = player.getSkills().getLevel(getSkill());
        if (++animationCycles >= 3 && tool != null) {
            player.animate(tool.getAnimation());
        }
        if (cycles-- > 0) {
            return;
        }
        player.getMask().setFacePosition(gameObject.getLocation(), gameObject.getDefinition().getSizeX(), gameObject.getDefinition().getSizeY());
        if (object.getRequiredLevel() > level) {
            player.sendMessage(getMessage(OBJECT_LEVEL));
            stopHarvesting();
            return;
        }
        if (tool != null) {
            if (tool.getRequiredLevel() > level) {
                player.sendMessage(getMessage(TOOL_LEVEL));
                stopHarvesting();
                return;
            }
            cycles = getCycleTime();
            if (!started) {
                if (player.getInventory().getFreeSlots() == 0) {
                    player.sendMessage("Not enough space in your inventory.");
                    stopHarvesting();
                    return;
                }
                player.sendMessage(getStartMessage());
                player.animate(tool.getAnimation());
                started = true;
                return;
            }
            Item reward = getReward();
            if (reward != null && !player.getInventory().addItem(reward.getId(), reward.getAmount())) {
                stopHarvesting();
                return;
            }
            player.getSkills().addExperience(getSkill(), object.getExperience());
            player.sendMessage(getMessage(HARVESTED_ITEM));

            //after harvest we deduct the object's health

            int health = gameObject.getHealth();

            if (!gameObject.isHealthSet()) {
                health = object.getMaxHealth();
                gameObject.setHealthSet(true);
            }

            gameObject.setHealth(--health);
            if (health <= 0) {
                if (object.getMaxHealth() > 1) {
                    gameObject.setHealth(object.getMaxHealth() + Misc.random(object.getMaxHealth()));
                } else {
                    gameObject.setHealth(1);
                }
                int restoreDelay = object.getRestoreDelay();
                if (restoreDelay > -1) {//Remove when noticing bugs with harvest action.
                    if (getReplacement(gameObject) > -1)
                        ObjectManager.replaceObjectTemporarily(gameObject.getLocation(), getReplacement(gameObject), restoreDelay);
                    else {
                        ObjectManager.removeObjectTemporarily(gameObject.getLocation(), restoreDelay, gameObject.getType(), gameObject.getRotation());
                    }
                }
                stopHarvesting();
            }
        } else {
            player.sendMessage(getMessage(NO_TOOL));
            stopHarvesting();
        }
    }

    public void stopHarvesting() {
        stop();
        mob.getPlayer().animate(Animation.RESET);
    }

    public abstract Item getReward();

    public abstract String getStartMessage();

    public abstract String getMessage(int type);

    public abstract int getSkill();

    public abstract int getCycleTime();

    public abstract HarvestTool getTool();

    public abstract HarvestObject getHarvestObject();

    public abstract GameObject getGameObject();

    public abstract int getReplacement(GameObject gameObject);

}
