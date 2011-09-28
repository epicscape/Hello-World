package org.dementhium.content.skills.smithing;

import org.dementhium.action.ProduceAction;
import org.dementhium.content.skills.smithing.SmithingUtils.SmeltingBar;
import org.dementhium.model.Item;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.mask.Graphic;
import org.dementhium.model.player.Equipment;
import org.dementhium.model.player.Skills;

import java.util.Random;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class Smelting extends ProduceAction {

	private static final Animation SMELTING_ANIMATION = Animation.create(3243);
	private static final Random RANDOM = new Random();

	private SmeltingBar bar;

	public Smelting(int productionAmount, SmeltingBar bar) {
		super(4, productionAmount);
		this.bar = bar;
		setProduceDelay(4);
	}

	@Override
	public int getAnimationDelay() {
		return 4;
	}

	@Override
	public Item getFailItem() {
		return null;
	}

	@Override
	public Item produceItem() {
		return bar.getProducedBar();
	}

	@Override
	public Item[] getRequiredItems() {
		return bar.getItemsRequired();
	}

	@Override
	public int getRequiredLevel() {
		return bar.getLevelRequired();
	}

	@Override
	public int getSkill() {
		return Skills.SMITHING;
	}

	@Override
	public double getExperience() {
		return bar.getExperience();
	}

	@Override
	public String getMessage(int type) {
		switch (type) {
		case SUCCESSFULLY_PRODUCED:
			return "You retrieve a bar of " + bar.toString().toLowerCase() + ".";
		case UNSUCCESSFULLY_PRODUCED:
			return "The ore is too impure and you fail to refine it.";
		case NOT_ENOUGH_ITEMS:
			return "You have run out of ore to smelt into " + bar.toString().toLowerCase();
		}
		return null;
	}

	@Override
	public Animation getAnimation() {
		return SMELTING_ANIMATION;
	}

	@Override
	public Graphic getGraphic() {
		return null;
	}

	@Override
	public boolean isSuccessfull() {
		//The chance of a successful forging of iron is 50% at level 15 Smithing, the minimum level for smelting iron ores, and increases incrementally as Smithing levels rise until it reaches an 80% chance of success at 45 Smithing, its highest. The chance of successfully smelting iron can also be increased to 100% by wearing a ring of forging, smelting the ore with the Superheat Item spell, or smelting the iron at the Blast Furnace activity.
		if (bar == SmeltingBar.IRON) {
			if (mob.getPlayer().getEquipment().getSlot(Equipment.SLOT_RING) == 2568) { //ring of forging
				return true;
			} else {
				return RANDOM.nextInt(100) <= (mob.getPlayer().getSkills().getLevel(Skills.SMITHING) >= 45 ? 80 : 50);
			}
		}
		return true;
	}

	public void sendInitialMessage() {
		StringBuilder sb = new StringBuilder();
		boolean usingCoal = false;
		if (bar.getItemsRequired().length > 1) {
			if (bar.getItemsRequired()[1].getDefinition().getName().equalsIgnoreCase("coal")) {
				sb.append("You place the ").append(bar.toString().toLowerCase()).append(" and ").append(SmithingUtils.getCoalAmount(bar)).append(" heaps of coal ");
				usingCoal = true;
			} else if (bar == SmeltingBar.BRONZE) {
				sb.append("You smelt the copper and tin together ");
			}
		} else {
			if (bar != SmeltingBar.SILVER) {
				sb.append("You smelt the ").append(bar.toString().toLowerCase()).append(" ");
			} else {
				sb.append("You place a lump of silver ");
			}
		}
		sb.append(usingCoal ? "into" : "in").append(" the furnace.");
		mob.getPlayer().sendMessage(sb.toString());
	}

}
