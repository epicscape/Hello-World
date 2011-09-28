package org.dementhium.content.skills.fishing;

import org.dementhium.content.skills.SkillAction;
import org.dementhium.model.Item;
import org.dementhium.model.npc.NPC;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;
import org.dementhium.util.misc.CycleState;

/**
 * Handles the fishing skill.
 *
 * @author Emperor
 */
public class Fishing extends SkillAction {

    /**
     * The npc.
     */
    private final NPC npc;

    /**
     * The fishing spot instance.
     */
    private final FishingSpot fishingSpot;

    /**
     * The amount of ticks.
     */
    private int ticks;

    /**
     * The amount of fish caught already.
     */
    private int count;

    /**
     * The harvest id.
     */
    private int harvestId;
    
    /**
     * Constructs a new {@code Fishing} {@code Object}.
     *
     * @param player      The player.
     * @param npc         The fishing spot npc.
     * @param optionId    The option id (first option - 1, second option - 2).
     * @param fishingSpot The fishing spot.
     */
    public Fishing(Player player, NPC npc, FishingSpot fishingSpot) {
        super(player);
        this.npc = npc;
        this.fishingSpot = fishingSpot;
    }

    /**
     * Checks if the player is executing a Fishing action.
     *
     * @param player The player.
     * @param npc    The npc.
     * @param option The option clicked (first/second).
     * @return The fishing instance, or null if the action wasn't a fishing action.
     */
    public static Fishing isAction(Player player, NPC npc, int option) {
        FishingSpot spot = FishingSpot.forId(npc.getId() | (option << 24));
        if (spot != null) {
            return new Fishing(player, npc, spot);
        }
        return null;
    }

    @Override
    public boolean commence(Player player) {
        harvestId = getCalculatedHarvest();
        if (player.getSkills().getLevel(Skills.FISHING) < fishingSpot.getHarvest()[harvestId].getLevel()) {
            player.sendMessage("You need a fishing level of " + fishingSpot.getHarvest()[harvestId].getLevel() + ".");
            return false;
        }
        if (!player.getInventory().contains(fishingSpot.getItem())) {
            player.sendMessage("You need a " + new Item(fishingSpot.getItem()).getDefinition().getName().toLowerCase() + " to fish here.");
            return false;
        }
        if (!player.getInventory().contains(fishingSpot.getBait()) && fishingSpot.getBait() > 0) {
            player.sendMessage("You don't have the required bait to fish here.");
            return false;
        }
        this.ticks = (byte) getCalculatedTicks(harvestId);
        player.sendMessage("You cast out your " + new Item(fishingSpot.getItem(), 1).getDefinition().getName().toLowerCase() + "...");
        return true;
    }

    @Override
    public boolean execute(Player player) {
        if (player.getInventory().getContainer().freeSlots() < 1) {
            player.sendMessage("Not enough space in your inventory.");
            stop();
            player.animate(RESET_ANIM);
            return false;
        }
        player.animate(fishingSpot.getAnimation());
        player.turnTo(npc);
        return ticks-- < 1;
    }

    @Override
    public boolean finish(Player player) {
        Item harvest = new Item(fishingSpot.getHarvest()[harvestId].getId(), 1);
        player.sendMessage("You manage to catch some " + harvest.getDefinition().getName().toLowerCase() + ".");
        player.getInventory().addItem(harvest.getId(), 1);
        player.getSkills().addExperience(Skills.FISHING, fishingSpot.getHarvest()[harvestId].getXp());
        if (fishingSpot.getBait() > 0) {
            player.getInventory().deleteItem(fishingSpot.getBait(), 1);
            if (!player.getInventory().getContainer().contains(new Item(fishingSpot.getBait()))) {
                stop();
                player.sendMessage("You have run out of bait.");
                player.animate(RESET_ANIM);
                return true;
            }
        }
        if (RANDOM.nextInt(350) < ++count) {
            System.out.println("Fishing spot should move now. " + count + ".");
            player.animate(RESET_ANIM);
            return true;
        }
    	harvestId = getCalculatedHarvest();
        this.ticks = (byte) getCalculatedTicks(harvestId);
        setCycleState(CycleState.EXECUTE);
        return false;
    }

    /**
     * Gets the amount of ticks for this session.
     *
     * @return The amount of ticks.
     */
    private int getCalculatedTicks(int harvestId) {
        int skill = getPlayer().getSkills().getLevel(Skills.FISHING);
        int level = fishingSpot.getHarvest()[harvestId].getLevel();
        int modifier = fishingSpot.getHarvest()[harvestId].getLevel();
        int randomAmt = RANDOM.nextInt(4);
        double cycleCount = 1;
        cycleCount = Math.ceil((level * 50 - skill * 10) / modifier * 0.25 - randomAmt * 4);// 30 is modifier
        if (cycleCount < 1) {
            cycleCount = 1;
        }
        return (int) cycleCount + 1;
    }

    /**
     * Gets the calculated harvest id.
     *
     * @return The harvest id.
     */
    private int getCalculatedHarvest() {
        int randomHarvest = RANDOM.nextInt(fishingSpot.getHarvest().length);
        int difference = getPlayer().getSkills().getLevel(Skills.FISHING) - fishingSpot.getHarvest()[randomHarvest].getLevel();
        if (difference < -1) {
            return randomHarvest = 0;
        }
        if (randomHarvest < -1) {
            return randomHarvest = 0;
        }
        return randomHarvest;
    }
}