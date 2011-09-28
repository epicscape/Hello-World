package org.dementhium.content.misc;

import org.dementhium.model.Container;
import org.dementhium.model.Item;
import org.dementhium.model.player.Inventory;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;
import org.dementhium.util.InputHandler;

/**
 * Handles the price check interface.
 *
 * @author Emperor
 */
public class PriceCheck {

	/**
	 * The player.
	 */
	private final Player player;

	/**
	 * The item container used.
	 */
	private Container container = new Container(Inventory.SIZE, false);

	/**
	 * If the price check interface is open.
	 */
	private boolean open;

	/**
	 * Constructs a new {@code PriceCheck} {@code Object}.
	 *
	 * @param player The player.
	 */
	public PriceCheck(Player player) {
		this.player = player;
	}

	/**
	 * Opens up the price check interface.
	 */
	public boolean open() {
		if(player.getCombatExecutor().getLastAttacker() != null){
			player.sendMessage("You can not open this interface while in combat.");
			close();
			return false;
		}
		player.setAttribute("cantMove", Boolean.TRUE);
		Object[] params1 = new Object[]{"", "", "", "", "Add-X", "Add-All", "Add-10", "Add-5", "Add",
				-1, 1, 7, 4, 93, 13565952};
		ActionSender.sendClientScript(player, 150, params1, "IviiiIsssssssss");
		ActionSender.sendAMask(player, 0, 54, 206, 15, 0, 1278);
		ActionSender.sendAMask(player, 0, 27, 207, 0, 36, 1086);
		ActionSender.sendInterface(player, 206);
		ActionSender.sendInventoryInterface(player, 207);
		open = true;
		return refresh(false);
	}

	/**
	 * Checks the price of an item.
	 *
	 * @param itemId The item id.
	 * @param slot   The slot clicked.
	 * @param amount The amount.
	 * @return {@code True} if succesful, {@code false} if not.
	 */
	public boolean checkPrice(int itemId, int slot, int amount) {
		Item item = player.getInventory().get(slot);
		if (item == null || item.getId() != itemId) {
			return false;
		}
		if (!item.getDefinition().isTradeable()) {
			player.sendMessage("This item is untradeable.");
			return false;
		}
		int inventoryAmount = player.getInventory().getContainer().getItemCount(itemId);
		if (amount > inventoryAmount) {
			amount = inventoryAmount;
		}
		item = new Item(item.getId(), amount);
		player.getInventory().getContainer().remove(item);
		container.add(item);
		return refresh(false);
	}

	/**
	 * Removes a price checked item.
	 *
	 * @param itemId The item id.
	 * @param slot   The item slot.
	 * @param amount The amount.
	 * @return {@code True} if succesful, {@code false} if not.
	 */
	public boolean remove(int itemId, int slot, int amount) {
		Item item = container.get(slot);
		if (item == null || item.getId() != itemId) {
			return false;
		}
		int containerAmount = container.getItemCount(itemId);
		if (amount > containerAmount) {
			amount = containerAmount;
		}
		item = new Item(item.getId(), amount);
		container.remove(item);
		player.getInventory().getContainer().add(item);
		return refresh(true);
	}

	/**
	 * Closes the price check interface.
	 *
	 * @return {@code True}.
	 */
	public boolean close() {
		player.removeAttribute("cantMove");
		InputHandler.resetInput(player);
		player.getInventory().getContainer().addAll(container);
		container.clear();
		player.getInventory().refresh();
		if (player != null && player.getConnection() != null) {
			ActionSender.sendCloseChatBox(player);
			ActionSender.closeInventoryInterface(player);
			ActionSender.sendCloseInterface(player);
		}
		return !(open = false);
	}


	/**
	 * Refreshes the price check interface & inventory interface.
	 *
	 * @param reArrange If the container should be re-arranged.
	 * @return {@code True}.
	 */
	private boolean refresh(boolean reArrange) {
		int totalValue = 0;
		int value = -1;
		int i = 0;
		if (reArrange) {
			Container cont = new Container(Inventory.SIZE, false);
			cont.addAll(container);
			container = cont;
		}
		ActionSender.sendItems(player, 90, container, false);
		for (Item item : container.toArray()) {
			if (item != null) {
				value = item.getDefinition().getStorePrice();//.getExchangePrice();
				totalValue += value * item.getAmount();
			}
			ActionSender.sendBConfig(player, 700 + i, value);
			i++;
		}
		ActionSender.sendBConfig(player, 728, totalValue);
		ActionSender.sendItems(player, 93, player.getInventory().getContainer(), false);
		return true;
	}

	/**
	 * @return the isOpen
	 */
	public boolean isOpen() {
		return open;
	}

	/**
	 * Gets the item container.
	 *
	 * @return The container.
	 */
	public Container getContainer() {
		return container;
	}

}
