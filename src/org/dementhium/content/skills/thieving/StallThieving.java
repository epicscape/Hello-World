package org.dementhium.content.skills.thieving;

import java.util.List;

import org.dementhium.content.skills.SkillAction;
import org.dementhium.model.Item;
import org.dementhium.model.map.GameObject;
import org.dementhium.model.map.ObjectManager;
import org.dementhium.model.map.Region;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.npc.NPC;
import org.dementhium.model.player.Equipment;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;
import org.dementhium.net.ActionSender;

/**
 * Handles stall thieving.
 * @author Emperor
 *
 */
public class StallThieving extends SkillAction {

    /**
     * The stealing animation
     */
    private static final Animation STEAL_ANIMATION = Animation.create(4282);
    
    /**
     * The object.
     */
    private final GameObject object;
    
    /**
     * The thieving stall data.
     */
    private final ThievingStalls stall;

    /**
     * If this pickpocket is succesful.
     */
    private boolean succesful = true;

    /**
     * The message to send.
     */
    private String message;
    
    /**
     * The amount of ticks left.
     */
    private byte ticks = 1;
    
	/**
	 * Constructs a new {@code StallThieving} {@code Object}.
	 * @param player The player.
	 * @param stall The stall to steal from.
	 */
	public StallThieving(Player player, GameObject object, ThievingStalls stall) {
		super(player);
		this.object = object;
		this.stall = stall;
	}

	@Override
	public boolean commence(Player player) {
        if (player.getSkills().getLevel(Skills.THIEVING) < stall.getThievingLevel()) {
            ActionSender.sendMessage(player, "You need a Thieving level of " + stall.getThievingLevel() + " to steal from this stall.");
            return false;
        }
        if (player.getAttribute("stunned", false)) {
            return false;
        }
        if (player.getInventory().getFreeSlots() < 1) {
            player.sendMessage("Not enough space in your inventory.");
            return false;
        }
        player.getMask().setFacePosition(object.getLocation(), object.getDefinition().sizeX, object.getDefinition().sizeY);
        player.sendMessage("You attempt to steal some spice from the " + stall.name().toLowerCase().replaceAll("_", " ") + ".");
        int increasedChance = getIncreasedChance(player);
        int thievingLevel = player.getSkills().getLevel(Skills.THIEVING);
        int level = RANDOM.nextInt(thievingLevel + increasedChance) + 1;
        double ratio = level / (RANDOM.nextInt(stall.getThievingLevel() + 5) + 1);
        if (Math.round(ratio * thievingLevel) < stall.getThievingLevel()) {
            succesful = false;
        }
		player.animate(STEAL_ANIMATION);
        player.setAttribute("cantMove", true);
        player.setAttribute("isStealing", true);
        return true;
	}

    /**
     * Gets the increased chance for succesfully stealing a stall.
     *
     * @param player The player.
     * @return The amount of increased chance.
     */
    private int getIncreasedChance(Player player) {
        int chance = 0;
        if (player.getEquipment().getSlot(Equipment.SLOT_CAPE) == 15347 && player.getPlayerArea().isInArdougne()) {
            chance += 15;
        } else if (player.getEquipment().getSlot(Equipment.SLOT_CAPE) == 15349) {
            chance += 15;
        }
        return chance;
    }

	@Override
	public boolean execute(Player player) {
        return --ticks == 0;
	}

	@Override
	public boolean finish(Player player) {
		player.sendMessage(message);
		player.setAttribute("cantMove", false);
		player.setAttribute("isStealing", false);
		if (!succesful) {
			List<NPC> guards = Region.getLocalNPCs(player.getLocation());
			for (NPC npc : guards) {
				if (!npc.getDefinition().getName().toLowerCase().contains("guard")
						|| npc.getCombatExecutor().getVictim() != null
						|| !player.isAttackable(npc)) {
					continue;
				}
				npc.forceText("Hey! Get your hands of there!");
				npc.getCombatExecutor().setVictim(player);
				return true;
			}
		}
		player.getSkills().addExperience(Skills.THIEVING, stall.getExperience());
		Item item = stall.getLoot()[RANDOM.nextInt(stall.getLoot().length)];
		player.sendMessage("You steal a " + item.getDefinition().getName() + ".");
		player.getInventory().addItem(item.getId(), item.getAmount());
		int id = -1;
		for (int i = 0; i < stall.getObjectIds().length; i++) {
			if (stall.getObjectIds()[i] == object.getId()) {
				id = stall.getReplaceIds()[i];
				break;
			}
		}
		if (id < 0) {
			return true;
		}
        ObjectManager.replaceObjectTemporarily(object.getLocation(), id, stall.getRestoreDelay());
		return true;
	}

}
