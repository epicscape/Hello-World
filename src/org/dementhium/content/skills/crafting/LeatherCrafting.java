package org.dementhium.content.skills.crafting;

import java.util.HashMap;
import java.util.Map;

import org.dementhium.action.ProduceAction;
import org.dementhium.content.DialogueManager;
import org.dementhium.content.skills.SkillAction;
import org.dementhium.model.Item;
import org.dementhium.model.definition.ItemDefinition;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;
import org.dementhium.net.ActionSender;
import org.dementhium.util.Misc;
import org.dementhium.util.misc.CycleState;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class LeatherCrafting {

	public static final Animation CRAFT_ANIMATION = Animation.create(1249);
	public static final Item NEEDLE = new Item(1733);
	public static final Item THREAD = new Item(1734);

	public static final LeatherProduction[] NORMAL_LEATHER_PRODUCTIONS = {
		LeatherProduction.LEATHER_GLOVES, LeatherProduction.LEATHER_BOOTS, LeatherProduction.LEATHER_COWL, LeatherProduction.LEATHER_VAMBRACES,
		LeatherProduction.LEATHER_BODY, LeatherProduction.LEATHER_CHAPS, LeatherProduction.COIF
	};

	public static final LeatherProduction[] GREEN_DRAGONHIDE_PRODUCTIONS = {
		LeatherProduction.GREEN_D_HIDE_BODY, LeatherProduction.GREEN_D_HIDE_VAMB, LeatherProduction.GREEN_D_HIDE_CHAPS
	};

	public static final LeatherProduction[] BLUE_DRAGONHIDE_PRODUCTIONS = {
		LeatherProduction.BLUE_D_HIDE_BODY, LeatherProduction.BLUE_D_HIDE_VAMB, LeatherProduction.BLUE_D_HIDE_CHAPS
	};

	public static final LeatherProduction[] RED_DRAGONHIDE_PRODUCTIONS = {
		LeatherProduction.RED_D_HIDE_BODY, LeatherProduction.RED_D_HIDE_VAMB, LeatherProduction.RED_D_HIDE_CHAPS
	};


	public static final LeatherProduction[] BLACK_DRAGONHIDE_PRODUCTIONS = {
		LeatherProduction.BLACK_D_HIDE_BODY, LeatherProduction.BLACK_D_HIDE_VAMB, LeatherProduction.BLACK_D_HIDE_CHAPS
	};



	public static void init() {
	}

	public enum Leather {
		NORMAL_LEATHER(new Item(1741), LeatherCrafting.NORMAL_LEATHER_PRODUCTIONS, "leather"),
		GREEN_D_HIDE(new Item(1745), LeatherCrafting.GREEN_DRAGONHIDE_PRODUCTIONS, "green d'hide"),
		BLUE_D_HIDE(new Item(2505), LeatherCrafting.BLUE_DRAGONHIDE_PRODUCTIONS, "blue d'hide"),
		RED_D_HIDE(new Item(2507), LeatherCrafting.RED_DRAGONHIDE_PRODUCTIONS, "red d'hide"),
		BLACK_D_HIDE(new Item(2509), LeatherCrafting.BLACK_DRAGONHIDE_PRODUCTIONS, "black d'hide");

		private LeatherProduction[] possibleProductions;
		private Item leatherItem;
		private String type;

		private Leather(Item leatherItem, LeatherProduction[] possibleProductions, String type) {
			this.leatherItem = leatherItem;
			this.possibleProductions = possibleProductions;
			this.type = type;
			for (LeatherProduction productable : possibleProductions) {
				productable.setLeather(this);
			}
		}

		private static Map<Integer, Leather> leatherTypes = new HashMap<Integer, Leather>();

		public static Leather forId(int id) {
			return leatherTypes.get(id);
		}

		static {
			for (Leather leather : Leather.values()) {
				leatherTypes.put(leather.leatherItem.getId(), leather);
			}
		}

	}

	public enum LeatherProduction {
		LEATHER_GLOVES(1059, 13.75, 1),
		LEATHER_BOOTS(1061, 16.25, 7),
		LEATHER_COWL(1167, 18.5, 9),
		LEATHER_VAMBRACES(1063, 22.0, 11),
		LEATHER_BODY(1129, 25.0, 14),
		LEATHER_CHAPS(1095, 27.0, 18),
		COIF(1169, 37.0, 38),
		GREEN_D_HIDE_VAMB(1065, 62, 57), 
		GREEN_D_HIDE_CHAPS(1099, 124, 60), 
		GREEN_D_HIDE_BODY(1135, 186, 63), 
		BLUE_D_HIDE_VAMB(2487, 70, 66), 
		BLUE_D_HIDE_CHAPS(2493, 140, 68), 
		BLUE_D_HIDE_BODY(2499, 210, 71), 
		RED_D_HIDE_VAMB(2489, 78, 73), 
		RED_D_HIDE_CHAPS(2495, 156, 75), 
		RED_D_HIDE_BODY(2501, 234, 77), 
		BLACK_D_HIDE_VAMB(2491, 86, 79), 
		BLACK_D_HIDE_CHAPS(2497, 172, 82), 
		BLACK_D_HIDE_BODY(2503, 258, 84);

		private int itemId;
		private double experience;
		private int levelRequired;
		private boolean isPair, pairSet;
		private boolean coif;

		private ItemDefinition def;
		private String basicName;

		private Leather leather;

		private LeatherProduction(int itemId, double experience, int levelRequired) {
			this.itemId = itemId;
			this.experience = experience;
			this.levelRequired = levelRequired;
			this.coif = toString().equals("COIF");
		}

		public void setLeather(Leather leather) {
			this.leather = leather;
		}

		public int getRequiredLevel() {
			return levelRequired;
		}

		public boolean isPair() {
			if (!pairSet) {
				setDef();
				String name = def.getName().toLowerCase();
				pairSet = true;
				isPair = !name.contains("body") && !name.contains("coif") && !name.contains("cowl");
			}
			return isPair;
		}

		private void setDef() {
			if (def == null) {
				def = ItemDefinition.forId(itemId);
			}
		}

		public String getName() {
			setDef();
			return def.getName();
		}

		public String getBasicName() {
			setDef();
			if (basicName == null) {
				String newString = def.getName().toLowerCase().replace(getType(), "");
				newString = newString.substring(coif ? 0 : 1);
				StringBuilder sb = new StringBuilder("");
				if (getType().contains(" ")) {
					sb.append(getType().split(" ")[0]).append(" ").append(newString);
				} else {
					sb.append(newString);
				}
				basicName = Misc.upperFirst(sb.toString());
			}
			return basicName;
		}

		public String getType() {
			return leather.type;
		}
	}

	public static boolean handleItemOnItem(Player player, Item itemUsed, Item usedWith) {
		Item item;
		if (itemUsed.getId() == NEEDLE.getId()) {
			item = usedWith;
		} else if (usedWith.getId() == NEEDLE.getId()) {
			item = itemUsed;
		} else {
			return false;
		}
		Leather leather = Leather.forId(item.getId());
		if (leather != null) {
			int amount = player.getInventory().getContainer().getNumberOf(leather.leatherItem);
			ActionSender.sendBConfig(player, 754, 13);
			for (int index = 0; index < leather.possibleProductions.length; index++) {
				LeatherProduction leatherProduction = leather.possibleProductions[index];
				ActionSender.sendBConfig(player, ProduceAction.CONFIG_IDS[index], leatherProduction.itemId);
				StringBuilder builder = new StringBuilder();
				if (player.getSkills().getLevel(Skills.SMITHING) < leatherProduction.levelRequired) {
					builder.append("<col=FF0000>").append(leatherProduction.getBasicName()).append("<br><col=FF0000>Level ").append(leatherProduction.levelRequired);
				} else {
					builder.append(leatherProduction.getBasicName());
				}
				ActionSender.sendSpecialString(player, ProduceAction.NAME_IDS[index], builder.toString());
			}
			ActionSender.sendString(player, 916, 1, "Choose how many you wish to make, then<br>click on the chosen item to begin.");
			ActionSender.sendInterface(player, 1, 752, 13, 905);
			ActionSender.sendInterface(player, 1, 905, 4, 916);
			ActionSender.sendConfig(player, 1363, amount << 20 | amount << 26);
			player.getSettings().setAmountToProduce(amount);
			player.getSettings().setMaxToProduce(amount);
			player.getSettings().setDialougeSkill(Skills.CRAFTING);
			player.getSettings().setPossibleProductions(productionsToIntArray(leather.possibleProductions));
			player.setAttribute("craftingType", 2);
			return true;
		}
		return false;
	}

	public static int[] productionsToIntArray(LeatherProduction[] productions) {
		int[] newArray = new int[productions.length];
		for (int i = 0; i < productions.length; i++) {
			newArray[i] = productions[i].ordinal();
		}
		return newArray;
	}

	/**
	 * 
	 * @author 'Mystic Flow
	 */
	public static class LeatherProductionAction extends SkillAction {

		private LeatherProduction toProduce;
		private int amount;

		public LeatherProductionAction(Player player, LeatherProduction toProduce, int amount) {
			super(player);
			this.toProduce = toProduce;
			this.amount = amount;
		}

		@Override
		public boolean commence(Player player) {
			if (player.getSkills().getLevel(Skills.CRAFTING) < toProduce.getRequiredLevel()) {
				StringBuilder sb = new StringBuilder("You need a Crafting level of ").append(toProduce.getRequiredLevel());
				if (!toProduce.isPair()) {
					sb.append(" to make a ").append(toProduce.getName().toLowerCase()).append(".");
				} else {
					sb.append(" to make a pair of ").append(toProduce.getType());
					sb.append("<br>").append(toProduce.getName().toLowerCase().replaceAll(toProduce.getType(), ""));
				}
				DialogueManager.sendDisplayBox(player, -1, sb.toString());
				return false;
			}
			if (!player.getInventory().getContainer().contains(THREAD)) {
				player.sendMessage("You have no threads to craft with!");
				return false;
			}
			setTime(2);
			return true;
		}

		@Override
		public boolean execute(Player player) {
			if (amount > 0) {
				if (!player.getInventory().getContainer().contains(THREAD)) {
					amount = 0;
					player.sendMessage("You have run out of threads!");
				} else if (!player.getInventory().getContainer().contains(NEEDLE)) {
					amount = 0;
					player.sendMessage("You don't have a needle to craft with!");
				} else if (!player.getInventory().contains(toProduce.leather.leatherItem)) {
					amount = 0;
					player.sendMessage("You have ran out of " + toProduce.getType() + ".");
				}
				player.animate(CRAFT_ANIMATION);
			}
			return true;
		}

		@Override
		public boolean finish(Player player) {
			if (amount < 1) {
				return true;
			}
			amount--;
			if (player.getRandom().nextDouble() >= 0.80) {
				player.sendMessage("You use up one of your reels of thread.");
				player.getInventory().getContainer().remove(THREAD);
			}
			if (player.getRandom().nextDouble() >= 0.97) {
				player.sendMessage("Your needle broke.");
				player.getInventory().getContainer().remove(NEEDLE);
			}
			StringBuilder createdMessage = new StringBuilder("You make a ");
			if (!toProduce.isPair() && toProduce.getName().endsWith("s")) {
				createdMessage.append(toProduce.getName().substring(0, toProduce.getName().length() - 1));
			} else {
				if (toProduce.isPair()) {
					createdMessage.append("pair of ").append(toProduce.getName().toLowerCase());
				} else {
					createdMessage.append(toProduce.getName().toLowerCase());
				}
			}
			createdMessage.append(".");
			player.sendMessage(createdMessage.toString());
			player.getInventory().getContainer().remove(toProduce.leather.leatherItem);
			player.getInventory().getContainer().add(new Item(toProduce.itemId));
			player.getSkills().addExperience(Skills.CRAFTING, toProduce.experience);
			player.getInventory().refresh();
			setCycleState(CycleState.EXECUTE);
			return false;
		}

	}

}
