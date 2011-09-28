package org.dementhium.action;

import org.dementhium.content.skills.smithing.Smelting;
import org.dementhium.model.Item;
import org.dementhium.model.World;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.mask.Graphic;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;
import org.dementhium.net.ActionSender;
import org.dementhium.tickable.Tick;

/**
 * @author Steve <golden_32@live.com>
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public abstract class ProduceAction extends Action {

	public static final int NOT_ENOUGH_ITEMS = 0, SUCCESSFULLY_PRODUCED = 1, UNSUCCESSFULLY_PRODUCED = 2, TOO_LOW_OF_LEVEL = 3;

	public static final int[] NAME_IDS = {132, 133, 134, 135, 136, 137, 275, 316, 317, 318};
	public static final int[] CONFIG_IDS = {755, 756, 757, 758, 759, 760, 120, 185, 87, 90};
	public static final int[] PRODUCE_SKILLS = {Skills.COOKING, Skills.SMITHING, Skills.FLETCHING};

	private int productionAmount; // the amount we produce
	private int produceDelay; //the delay before producing again

	private int cycles;
	private boolean firstCycle;
	private boolean closedChatbox;

	public ProduceAction(int cycles, int productionAmount) {
		super(1);
		this.cycles = cycles;
		this.productionAmount = productionAmount;
	}


	@Override
	public void execute() {
		Player player = mob.getPlayer();
		if (!closedChatbox) {
			closedChatbox = true;
			ActionSender.sendCloseChatBox(player);
		}
		if (productionAmount <= 0) {
			stopProduction();
			return;
		}
		Item producedItem = produceItem();
		boolean ableToProduce = true;

		if (getRequiredLevel() > player.getSkills().getLevel(getSkill())) {
			ableToProduce = false;
		}
		for (Item item : getRequiredItems()) {
			if (!player.getInventory().getContainer().contains(item)) {
				ableToProduce = false;
				break;
			}
		}
		if (ableToProduce)
			doAnimation();
		if (produceDelay > 0) {
			produceDelay--;
			return;
		}
		produceDelay = cycles;
		if (productionAmount-- > 0) {
			if (ableToProduce) {
				if (isSuccessfull()) {
					for (Item item : getRequiredItems()) {
						player.getInventory().getContainer().remove(item);
					}
					//we will always have space since the items required to produce the item are removed
					//to make the produced item but we stop the event just in case......
					if (!player.getInventory().getContainer().add(producedItem)) {
						stopProduction();
					}
					player.sendMessage(getMessage(ProduceAction.SUCCESSFULLY_PRODUCED));
					player.getInventory().refresh();
					player.getSkills().addExperience(getSkill(), getExperience());
				} else {
					for (Item item : getRequiredItems()) {
						player.getInventory().getContainer().remove(item);
					}
					if (getFailItem() != null) {
						player.getInventory().getContainer().add(getFailItem());
					}
					player.getInventory().refresh();
					player.sendMessage(getMessage(ProduceAction.UNSUCCESSFULLY_PRODUCED));
				}
			} else if (getRequiredLevel() > player.getSkills().getLevel(getSkill())) {
				stopProduction();
				player.sendMessage(getMessage(ProduceAction.TOO_LOW_OF_LEVEL));
			} else {
				stopProduction();
				player.sendMessage(getMessage(ProduceAction.NOT_ENOUGH_ITEMS));
			}

		} else {
			stopProduction();
		}
	}


	private void doAnimation() {
		if (!(this instanceof Smelting)) { //Smelting is a production action that animates and then produces
			if (produceDelay > 0) {
				return;
			}
		} else {
			if (produceDelay == 3) {
				((Smelting) this).sendInitialMessage();
			}
		}
		int animationDelay = !firstCycle ? 0 : getAnimationDelay();
		if (animationDelay > 0) {
			final ProduceAction action = this;
			World.getWorld().submit(new Tick(animationDelay) {
				@Override
				public void execute() {
					if (action.isRunning()) {
						mob.getPlayer().graphics(getGraphic());
						mob.getPlayer().animate(getAnimation());
					}
					this.stop();
				}
			});
		} else {
			firstCycle = true;
			mob.getPlayer().graphics(getGraphic());
			mob.getPlayer().animate(getAnimation());
		}
	}

	public void setProduceDelay(int delay) {
		this.produceDelay = delay;
	}

	public void stopProduction() {
		mob.getPlayer().getSettings().setDialougeSkill(-1);
		stop();
	}

	public abstract int getAnimationDelay();

	public abstract Item getFailItem();

	public abstract Item produceItem();

	public abstract Item[] getRequiredItems();

	public abstract int getRequiredLevel();

	public abstract int getSkill();

	public abstract double getExperience();

	public abstract String getMessage(int type);

	public abstract Animation getAnimation();

	public abstract Graphic getGraphic();

	public abstract boolean isSuccessfull();

}
