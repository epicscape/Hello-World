package org.dementhium.content.misc;

import org.dementhium.content.misc.Drinking.Drink;
import org.dementhium.model.Item;
import org.dementhium.model.player.Player;

/**
 * Handles the decanting of potions.
 * @author Steve <golden_32@live.com>, 
 * 			redone by Emperor due to a dupe.
 */
public class Decanting {

	/**
	 * Attempts to decant potions.
	 * @param player The player.
	 * @param slot The first item slot used.
	 * @param slot1 The second item slot used.
	 * @return {@code True} if the item on item action was decanting potions, {@code false} if not.
	 */
	public static boolean decantPotion(Player player, int slot, int slot1) {
		int itemId = player.getInventory().get(slot).getId(), itemId2 = player.getInventory().get(slot1).getId();
		Drink drink = Drink.forId(itemId);
		Drink otherDrink = Drink.forId(itemId2);
		if (!(drink != null && drink == otherDrink)) {
			return false;
		}
		int index = 0;
		int currentId = itemId;
		for (index = 0; index < drink.getIds().length; index++) {
			if (drink.getId(index) == itemId) {
				break;
			}
		}
		for (int i = 0; i < otherDrink.getIds().length; i++) {
			if (otherDrink.getId(i) == itemId2) {
				index += i + 1;
				if (index >= drink.getIds().length) {
					currentId = drink.getId(index - drink.getIds().length);
					index = drink.getIds().length - 1;
				} else {
					currentId = 229;
				}
				break;
			}
		}
		player.getInventory().set(slot1, new Item(currentId));
		player.getInventory().set(slot, new Item(otherDrink.getId(index)));
		player.getInventory().refresh();
		return true;
	}
}
