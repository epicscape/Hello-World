package org.dementhium.content.skills.crafting;

import org.dementhium.event.EventListener;
import org.dementhium.event.EventManager;
import org.dementhium.model.Item;
import org.dementhium.model.player.Inventory;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class LeatherTanning extends EventListener {

	@Override
	public void register(EventManager manager) {
		manager.registerInterfaceListener(324, this);
	}

	public static final int[][] INTERFACE_DATA = {
		{1739, 1741, 1},
		{1739, 1743, 3},
		{1, 1, 15},
		{1, 1, 20},
		{1753, 1745, 20},
		{1751, 2505, 20},
		{1749, 2507, 20},
		{1747, 2509, 20},
	};
	
	public static final String[][] NAMES = {
		{"cowhide", "soft leather"},
		{"cowhide", "hard leather"},
		{"snake hide", "snakeskin"},
		{"snake hide", "snakeskin"},
		{"green dragonhide", "green dragon leather"},
		{"blue dragonhide", "blue dragon leather"},
		{"red dragonhide", "red dragon leather"},
		{"black dragonhide", "black dragon leather"},
	};

	public boolean interfaceOption(Player player, int interfaceId, int buttonId, int slot, int itemId, int opcode) {
		int index = buttonId - 1;
		if (index < 0 || index >= INTERFACE_DATA.length) {
			return true;
		}
		int[] interfaceData = INTERFACE_DATA[index];
		String[] nameData = NAMES[index];
		int amount;
		int numberOf = player.getInventory().numberOf(interfaceData[0]);
		switch (opcode) {
		case 6:
			amount = 1;
			break;
		case 13:
			amount = 5;
			break;
		case 0:
			amount = 10;
			break;
		case 46:
			amount = numberOf;
			break;
		default:
			amount = -1;
		}
		if (amount == -1) {
			player.sendMessage("This option isn't available yet!");
			return true;
		} 
		if (amount > numberOf) {
			amount = numberOf;
		}
		if (amount < 1) {
			player.sendMessage("You don't have any " + nameData[0] + " to tan.");
			return true;
		}
		if (player.getInventory().numberOf(995) < interfaceData[2]) {
			player.sendMessage("You haven't got enough money to tan " + nameData[1] + ".");
			return true;
		}
		int price = interfaceData[2] * amount;
		while (player.getInventory().numberOf(995) < price) {
			if (amount < 1) {
				break;
			}
			amount--;
			price = interfaceData[2] * amount;
		}
		if (amount == 0) {
			player.sendMessage("An error has occured, please report this to an admin");
			return true;
		}
		String hide = nameData[0];
		if (amount > 1) {
			hide += "s";
		}
		int replacedAmount = 0;
		for (int i = 0; i < Inventory.SIZE; i++) {
			Item item = player.getInventory().get(i);
			if (item != null && item.getId() == interfaceData[0]) {
				player.getInventory().getContainer().set(i, new Item(interfaceData[1]));
				replacedAmount++;
			}
			if (replacedAmount >= amount) {
				break;
			}
		}
		player.getInventory().getContainer().remove(new Item(995, price));
		player.getInventory().refresh();
		player.sendMessage("The tanner tans " + amount + " " +  hide + " for you.");
		ActionSender.sendCloseInterface(player);
		return true;
	}

	public static void showInterface(Player player) {
		ActionSender.sendInterface(player, 324);
	}

}
