package org.dementhium.content.skills.magic;

import org.dementhium.model.Item;
import org.dementhium.model.World;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.mask.Graphic;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;
import org.dementhium.net.ActionSender;
import org.dementhium.tickable.Tick;

/**
 * @author Teemu Uusitalo
 * @author Wolfey
 */
public class Enchant {
	private static final Item COSMIC_RUNE = new Item(564, 1);

	/**
	 * 
	 * @param player
	 *            the player to handle.
	 * @param childId
	 *            the childId of the spell, this will be used to retrieve the
	 *            enchant level.
	 * @param itemId
	 *            the itemId of our item to enchant.
	 * @param slot
	 *            the slot of the item.
	 * @return
	 */
	public static boolean enchant(Player player, int childId, int itemId,
			int slot) {
		if (player.getSettings().getSpellBook() == 192) {
			EnchantLevel level = EnchantLevel.forChild(childId);
			if (level != null) {
				if (handle(player, level, new Item(itemId), slot)) {
					return true;
				}
				ActionSender
						.sendMessage(
								player,
								"This spell can only be cast in "
										+ level.name().toLowerCase()
										+ " amulets, rings, necklaces, bracelets and on");
				ActionSender.sendMessage(player,
						"shapes in the Mage Training Arena");
				return true;
			}
		}
		return false;
	}

	private static boolean handle(final Player player,
			final EnchantLevel level, final Item item, final int slot) {
		if (player.getSkills().getLevelForExperience(Skills.MAGIC) < level
				.getLevelReq()) {
			ActionSender.sendMessage(player,
					"Your Magic level is not high enough to cast this spell.");
			return true;
		}
		if (!player.getInventory().contains(COSMIC_RUNE)) {
			ActionSender.sendMessage(player,
					"You do not have enough Cosmic Runes to cast this spell.");
			return true;
		}
		for (Item rune : level.getRunes()) {
			if (!player.getInventory().contains(rune)) {
				ActionSender.sendMessage(player, "You do not have enough "
						+ rune.getDefinition().getName()
						+ "s to cast this spell.");
				return true;
			}
		}
		final int enchantedId = getEnchantedID(level, item);
		if (enchantedId == -1)
			return false;
		for (Item rune : level.getRunes()) {
			player.getInventory().deleteItem(rune);
		}
		player.setAttribute("cantMove", Boolean.TRUE);
		player.setAttribute("enchanting", Boolean.TRUE);
		player.animate(Animation.create(getAnimId(level, enchantedId)));
		player.graphics(Graphic.create(getGraphicId(level, enchantedId),
				100 << 16));
		player.getMask().setApperanceUpdate(true);
		World.getWorld().submit(new Tick(3) {
			@Override
			public void execute() {
				player.getInventory().set(slot, new Item(enchantedId));
				player.getSkills().addExperience(Skills.MAGIC,
						level.getExperience());
				player.removeAttribute("cantMove");
				player.removeAttribute("enchanting");
				player.getInventory().refresh();
				stop();
			}
		});
		return true;
	}

	private static int getEnchantedID(EnchantLevel level, Item item) {
		switch (level) {
		case SAPPHIRE:
			switch (item.getId()) {
			case 1694:// Amulet of Magic
				return 1727;
			case 1637:// Ring of Recoil
				return 2550;
			case 1658:// Games Necklace
				return 3853;
			case 11072:// Bracelet of Clay
				return 11074;
			}
		case EMERALD:
			switch (item.getId()) {
			case 1639:// Ring of Duelling
				return 2552;
			case 1658:// Farming Necklace
				return 12622;
			case 1696:// Amulet of Defence
				return 1729;
			case 11076:// Castle wars bracelet
				return 11079;
			}
		case RUBY:
			switch (item.getId()) {
			case 1641:// Ring of Forging
				return 2568;
			case 1660:// Digsite Pendant
				return 11194;
			case 1698:// Amulet of Strength
				return 1725;
			case 11086:// Inoculation Bracelet
				return 11088;
			}
		case DIAMOND:
			switch (item.getId()) {
			case 1643:// Ring of Life
				return 2570;
			case 1662:// Phoenix Necklace
				return 11090;
			case 1700:// Amulet of Power
				return 1731;
			case 11092:// Forinthry brace
				return 11095;
			}
		case DRAGONSTONE:
			switch (item.getId()) {
			case 1645:// Ring of Wealth
				return 2572;
			case 1702:// Amulet of Glory
				return 1712;
			case 11115:// Combat Bracelet
				return 11118;
			}
		case ONYX:
			switch (item.getId()) {
			case 6575:// Ring of Stone
				return 6583;
			case 6582:// Amulet of Fury
				return 6585;
			case 11130:// Regen Bracelet
				return 11133;
			}
			break;
		}
		return -1;
	}

	public static int getAnimId(EnchantLevel level, int id) {
		String name = new Item(id).getDefinition().getName();
		System.out.println(name);
		if (name.contains("Amulet")) {
			switch (level) {
			case SAPPHIRE:
				return 719;
			case EMERALD:
				return 720;
			case RUBY:
				return 721;
			case DIAMOND:
				return 719;
			case DRAGONSTONE:
				return 720;
			case ONYX:
				return 720;// TODO correct GFX.
			}
		}
		if (name.contains("Ring")) {
			return 931;
		}
		return -1;
	}

	public static int getGraphicId(EnchantLevel level, int id) {
		String name = new Item(id).getDefinition().getName();
		System.out.println(name);
		if (name.contains("Amulet")) {
			switch (level) {
			case SAPPHIRE:
				return 114;
			case EMERALD:
				return 115;
			case RUBY:
				return 115;
			case DIAMOND:
				return 153;
			case DRAGONSTONE:
				return 154;
			case ONYX:
				return 154;// TODO correct GFX.
			}
		}
		if (name.contains("Ring")) {
			return -1;
		}
		return -1;
	}
}
