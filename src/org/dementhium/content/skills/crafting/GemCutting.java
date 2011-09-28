package org.dementhium.content.skills.crafting;

import org.dementhium.action.ProduceAction;
import org.dementhium.model.Item;
import org.dementhium.model.definition.ItemDefinition;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.mask.Graphic;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;
import org.dementhium.net.ActionSender;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class GemCutting {
	
	public static final Animation CLEAR_GEM_ANIMATION = Animation.create(886), RED_GEM_ANIMATIION = Animation.create(887), BLUE_GEM_ANIMATION = Animation.create(888), GREEN_GEM_ANIMATION = Animation.create(889);
	
	public enum Gem {
		OPAL(1625, 1609, 15.0, 1, CLEAR_GEM_ANIMATION),
		JADE(1627, 1611, 20, 13, CLEAR_GEM_ANIMATION),
		RED_TOPAZ(1629, 1613, 25, 16, RED_GEM_ANIMATIION),
		SAPPHIRE(1623, 1607, 50, 20, BLUE_GEM_ANIMATION),
		EMERALD(1621, 1605, 67, 27, GREEN_GEM_ANIMATION),
		RUBY(1619, 1603, 85, 34, RED_GEM_ANIMATIION),
		DIAMOND(1617, 1601, 107.5, 43, CLEAR_GEM_ANIMATION),
		DRAGONSTONE(1631, 1615, 137.5, 55, Animation.create(885)),
		ONYX(6571, 6573, 168, 67, Animation.create(2717));
		
		private double experience;
		private int levelRequired;
		private int uncut, cut;

		private Animation cutAnimation;
		
		private String name;
		
		private Gem(int uncut, int cut, double experience, int levelRequired, Animation cutAnimation) {
			this.uncut = uncut;
			this.cut = cut;
			this.experience = experience;
			this.levelRequired = levelRequired;
			this.cutAnimation = cutAnimation;
			this.name = toString().toLowerCase().replaceAll("_", " ");
		}

		public String getName() {
			return name;
		}

	}

	public static final Item CHISEL = new Item(1755, 1);

	public static boolean cutGem(Player player, int itemUsed, int usedWith) {
		int itemId;
		if (itemUsed == CHISEL.getId()) {
			itemId = usedWith;
		} else if (usedWith == CHISEL.getId()) {
			itemId = itemUsed;
		} else {
			return false;
		}
		Gem gem = forId(itemId);
		if (gem != null) {
			ItemDefinition def = ItemDefinition.forId(itemId);
			boolean uncut = def.getName().startsWith("Uncut");
			int numberOf = player.getInventory().numberOf(itemId);
			ActionSender.sendBConfig(player, 754, 13);
			ActionSender.sendBConfig(player, ProduceAction.CONFIG_IDS[0], uncut ? gem.cut : -1);
			ActionSender.sendSpecialString(player, ProduceAction.NAME_IDS[0], def.getName());
			ActionSender.sendString(player, 916, 1, "Choose how many you would like to cut, <br> then click on the item to begin");
			ActionSender.sendInterface(player, 1, 752, 13, 905);
			ActionSender.sendInterface(player, 1, 905, 4, 916);
			ActionSender.sendConfig(player, 1363, numberOf << 20 | numberOf << 26);
			player.getSettings().setAmountToProduce(numberOf);
			player.getSettings().setMaxToProduce(numberOf);
			player.getSettings().setItemToProduce(itemId);
			player.getSettings().setDialougeSkill(Skills.CRAFTING);
			player.setAttribute("craftingType", 1);
			return true;
		}
		return false;
	}

	private static Gem forId(int itemId) {
		for (Gem gem : Gem.values()) {
			if (gem.uncut == itemId) {
				return gem;
			}
		}
		return null;
	}
	
	/**
	 * 
	 * @author 'Mystic Flow
	 */
	public static class GemCuttingAction extends ProduceAction {
		
		private Gem gem;
		
		public GemCuttingAction(Player player, int gemId, int productionAmount) {
			super(2, productionAmount);
			this.gem = forId(gemId);
		}

		@Override
		public Animation getAnimation() {
			return gem.cutAnimation;
		}

		@Override
		public int getAnimationDelay() {
			return 0;
		}

		@Override
		public double getExperience() {
			return gem.experience;
		}

		@Override
		public Item getFailItem() {
			return null;
		}

		@Override
		public Graphic getGraphic() {
			return null;
		}

		@Override
		public String getMessage(int type) {
			switch (type) {
			case NOT_ENOUGH_ITEMS:
				return "You have no more " + gem.getName() + "'s to cut.";
			case SUCCESSFULLY_PRODUCED:
				return "You cut the " + gem.getName() + ".";
			case TOO_LOW_OF_LEVEL:
				return "You need a Crafting level of " + gem.levelRequired+ " to cut that gem.";
			}
			return null;
		}

		@Override
		public Item[] getRequiredItems() {
			return new Item[] { new Item(gem.uncut) };
		}

		@Override
		public int getRequiredLevel() {
			return gem.levelRequired;
		}

		@Override
		public int getSkill() {
			return Skills.CRAFTING;
		}

		@Override
		public boolean isSuccessfull() {
			return true;
		}

		@Override
		public Item produceItem() {
			return new Item(gem.cut, 1);
		}
		
	}
}
